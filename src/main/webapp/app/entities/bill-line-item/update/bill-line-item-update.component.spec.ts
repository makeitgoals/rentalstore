jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { BillLineItemService } from '../service/bill-line-item.service';
import { IBillLineItem, BillLineItem } from '../bill-line-item.model';
import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';
import { IBill } from 'app/entities/bill/bill.model';
import { BillService } from 'app/entities/bill/service/bill.service';
import { IRentalOrder } from 'app/entities/rental-order/rental-order.model';
import { RentalOrderService } from 'app/entities/rental-order/service/rental-order.service';

import { BillLineItemUpdateComponent } from './bill-line-item-update.component';

describe('Component Tests', () => {
  describe('BillLineItem Management Update Component', () => {
    let comp: BillLineItemUpdateComponent;
    let fixture: ComponentFixture<BillLineItemUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let billLineItemService: BillLineItemService;
    let productService: ProductService;
    let billService: BillService;
    let rentalOrderService: RentalOrderService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [BillLineItemUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(BillLineItemUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(BillLineItemUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      billLineItemService = TestBed.inject(BillLineItemService);
      productService = TestBed.inject(ProductService);
      billService = TestBed.inject(BillService);
      rentalOrderService = TestBed.inject(RentalOrderService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Product query and add missing value', () => {
        const billLineItem: IBillLineItem = { id: 456 };
        const product: IProduct = { id: 26410 };
        billLineItem.product = product;

        const productCollection: IProduct[] = [{ id: 91961 }];
        jest.spyOn(productService, 'query').mockReturnValue(of(new HttpResponse({ body: productCollection })));
        const additionalProducts = [product];
        const expectedCollection: IProduct[] = [...additionalProducts, ...productCollection];
        jest.spyOn(productService, 'addProductToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ billLineItem });
        comp.ngOnInit();

        expect(productService.query).toHaveBeenCalled();
        expect(productService.addProductToCollectionIfMissing).toHaveBeenCalledWith(productCollection, ...additionalProducts);
        expect(comp.productsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Bill query and add missing value', () => {
        const billLineItem: IBillLineItem = { id: 456 };
        const bill: IBill = { id: 97735 };
        billLineItem.bill = bill;

        const billCollection: IBill[] = [{ id: 5453 }];
        jest.spyOn(billService, 'query').mockReturnValue(of(new HttpResponse({ body: billCollection })));
        const additionalBills = [bill];
        const expectedCollection: IBill[] = [...additionalBills, ...billCollection];
        jest.spyOn(billService, 'addBillToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ billLineItem });
        comp.ngOnInit();

        expect(billService.query).toHaveBeenCalled();
        expect(billService.addBillToCollectionIfMissing).toHaveBeenCalledWith(billCollection, ...additionalBills);
        expect(comp.billsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call RentalOrder query and add missing value', () => {
        const billLineItem: IBillLineItem = { id: 456 };
        const rentalOrder: IRentalOrder = { id: 35771 };
        billLineItem.rentalOrder = rentalOrder;

        const rentalOrderCollection: IRentalOrder[] = [{ id: 43610 }];
        jest.spyOn(rentalOrderService, 'query').mockReturnValue(of(new HttpResponse({ body: rentalOrderCollection })));
        const additionalRentalOrders = [rentalOrder];
        const expectedCollection: IRentalOrder[] = [...additionalRentalOrders, ...rentalOrderCollection];
        jest.spyOn(rentalOrderService, 'addRentalOrderToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ billLineItem });
        comp.ngOnInit();

        expect(rentalOrderService.query).toHaveBeenCalled();
        expect(rentalOrderService.addRentalOrderToCollectionIfMissing).toHaveBeenCalledWith(
          rentalOrderCollection,
          ...additionalRentalOrders
        );
        expect(comp.rentalOrdersSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const billLineItem: IBillLineItem = { id: 456 };
        const product: IProduct = { id: 12316 };
        billLineItem.product = product;
        const bill: IBill = { id: 75101 };
        billLineItem.bill = bill;
        const rentalOrder: IRentalOrder = { id: 68412 };
        billLineItem.rentalOrder = rentalOrder;

        activatedRoute.data = of({ billLineItem });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(billLineItem));
        expect(comp.productsSharedCollection).toContain(product);
        expect(comp.billsSharedCollection).toContain(bill);
        expect(comp.rentalOrdersSharedCollection).toContain(rentalOrder);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<BillLineItem>>();
        const billLineItem = { id: 123 };
        jest.spyOn(billLineItemService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ billLineItem });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: billLineItem }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(billLineItemService.update).toHaveBeenCalledWith(billLineItem);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<BillLineItem>>();
        const billLineItem = new BillLineItem();
        jest.spyOn(billLineItemService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ billLineItem });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: billLineItem }));
        saveSubject.complete();

        // THEN
        expect(billLineItemService.create).toHaveBeenCalledWith(billLineItem);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<BillLineItem>>();
        const billLineItem = { id: 123 };
        jest.spyOn(billLineItemService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ billLineItem });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(billLineItemService.update).toHaveBeenCalledWith(billLineItem);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackProductById', () => {
        it('Should return tracked Product primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackProductById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackBillById', () => {
        it('Should return tracked Bill primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackBillById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackRentalOrderById', () => {
        it('Should return tracked RentalOrder primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackRentalOrderById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
