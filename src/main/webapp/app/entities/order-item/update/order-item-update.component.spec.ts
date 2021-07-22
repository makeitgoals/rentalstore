jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { OrderItemService } from '../service/order-item.service';
import { IOrderItem, OrderItem } from '../order-item.model';
import { IRentalOrder } from 'app/entities/rental-order/rental-order.model';
import { RentalOrderService } from 'app/entities/rental-order/service/rental-order.service';
import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';

import { OrderItemUpdateComponent } from './order-item-update.component';

describe('Component Tests', () => {
  describe('OrderItem Management Update Component', () => {
    let comp: OrderItemUpdateComponent;
    let fixture: ComponentFixture<OrderItemUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let orderItemService: OrderItemService;
    let rentalOrderService: RentalOrderService;
    let productService: ProductService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [OrderItemUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(OrderItemUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(OrderItemUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      orderItemService = TestBed.inject(OrderItemService);
      rentalOrderService = TestBed.inject(RentalOrderService);
      productService = TestBed.inject(ProductService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call RentalOrder query and add missing value', () => {
        const orderItem: IOrderItem = { id: 456 };
        const rentalOrder: IRentalOrder = { id: 25256 };
        orderItem.rentalOrder = rentalOrder;

        const rentalOrderCollection: IRentalOrder[] = [{ id: 42433 }];
        jest.spyOn(rentalOrderService, 'query').mockReturnValue(of(new HttpResponse({ body: rentalOrderCollection })));
        const additionalRentalOrders = [rentalOrder];
        const expectedCollection: IRentalOrder[] = [...additionalRentalOrders, ...rentalOrderCollection];
        jest.spyOn(rentalOrderService, 'addRentalOrderToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ orderItem });
        comp.ngOnInit();

        expect(rentalOrderService.query).toHaveBeenCalled();
        expect(rentalOrderService.addRentalOrderToCollectionIfMissing).toHaveBeenCalledWith(
          rentalOrderCollection,
          ...additionalRentalOrders
        );
        expect(comp.rentalOrdersSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Product query and add missing value', () => {
        const orderItem: IOrderItem = { id: 456 };
        const product: IProduct = { id: 84737 };
        orderItem.product = product;

        const productCollection: IProduct[] = [{ id: 25918 }];
        jest.spyOn(productService, 'query').mockReturnValue(of(new HttpResponse({ body: productCollection })));
        const additionalProducts = [product];
        const expectedCollection: IProduct[] = [...additionalProducts, ...productCollection];
        jest.spyOn(productService, 'addProductToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ orderItem });
        comp.ngOnInit();

        expect(productService.query).toHaveBeenCalled();
        expect(productService.addProductToCollectionIfMissing).toHaveBeenCalledWith(productCollection, ...additionalProducts);
        expect(comp.productsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const orderItem: IOrderItem = { id: 456 };
        const rentalOrder: IRentalOrder = { id: 10348 };
        orderItem.rentalOrder = rentalOrder;
        const product: IProduct = { id: 72954 };
        orderItem.product = product;

        activatedRoute.data = of({ orderItem });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(orderItem));
        expect(comp.rentalOrdersSharedCollection).toContain(rentalOrder);
        expect(comp.productsSharedCollection).toContain(product);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<OrderItem>>();
        const orderItem = { id: 123 };
        jest.spyOn(orderItemService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ orderItem });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: orderItem }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(orderItemService.update).toHaveBeenCalledWith(orderItem);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<OrderItem>>();
        const orderItem = new OrderItem();
        jest.spyOn(orderItemService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ orderItem });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: orderItem }));
        saveSubject.complete();

        // THEN
        expect(orderItemService.create).toHaveBeenCalledWith(orderItem);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<OrderItem>>();
        const orderItem = { id: 123 };
        jest.spyOn(orderItemService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ orderItem });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(orderItemService.update).toHaveBeenCalledWith(orderItem);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackRentalOrderById', () => {
        it('Should return tracked RentalOrder primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackRentalOrderById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackProductById', () => {
        it('Should return tracked Product primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackProductById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
