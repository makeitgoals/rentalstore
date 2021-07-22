jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { BillLineItemToOrderItemService } from '../service/bill-line-item-to-order-item.service';
import { IBillLineItemToOrderItem, BillLineItemToOrderItem } from '../bill-line-item-to-order-item.model';
import { IOrderItem } from 'app/entities/order-item/order-item.model';
import { OrderItemService } from 'app/entities/order-item/service/order-item.service';
import { IRentalOrder } from 'app/entities/rental-order/rental-order.model';
import { RentalOrderService } from 'app/entities/rental-order/service/rental-order.service';
import { IBillLineItem } from 'app/entities/bill-line-item/bill-line-item.model';
import { BillLineItemService } from 'app/entities/bill-line-item/service/bill-line-item.service';

import { BillLineItemToOrderItemUpdateComponent } from './bill-line-item-to-order-item-update.component';

describe('Component Tests', () => {
  describe('BillLineItemToOrderItem Management Update Component', () => {
    let comp: BillLineItemToOrderItemUpdateComponent;
    let fixture: ComponentFixture<BillLineItemToOrderItemUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let billLineItemToOrderItemService: BillLineItemToOrderItemService;
    let orderItemService: OrderItemService;
    let rentalOrderService: RentalOrderService;
    let billLineItemService: BillLineItemService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [BillLineItemToOrderItemUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(BillLineItemToOrderItemUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(BillLineItemToOrderItemUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      billLineItemToOrderItemService = TestBed.inject(BillLineItemToOrderItemService);
      orderItemService = TestBed.inject(OrderItemService);
      rentalOrderService = TestBed.inject(RentalOrderService);
      billLineItemService = TestBed.inject(BillLineItemService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call OrderItem query and add missing value', () => {
        const billLineItemToOrderItem: IBillLineItemToOrderItem = { id: 456 };
        const orderItem: IOrderItem = { id: 14661 };
        billLineItemToOrderItem.orderItem = orderItem;

        const orderItemCollection: IOrderItem[] = [{ id: 31536 }];
        jest.spyOn(orderItemService, 'query').mockReturnValue(of(new HttpResponse({ body: orderItemCollection })));
        const additionalOrderItems = [orderItem];
        const expectedCollection: IOrderItem[] = [...additionalOrderItems, ...orderItemCollection];
        jest.spyOn(orderItemService, 'addOrderItemToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ billLineItemToOrderItem });
        comp.ngOnInit();

        expect(orderItemService.query).toHaveBeenCalled();
        expect(orderItemService.addOrderItemToCollectionIfMissing).toHaveBeenCalledWith(orderItemCollection, ...additionalOrderItems);
        expect(comp.orderItemsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call RentalOrder query and add missing value', () => {
        const billLineItemToOrderItem: IBillLineItemToOrderItem = { id: 456 };
        const rentalOrder: IRentalOrder = { id: 20848 };
        billLineItemToOrderItem.rentalOrder = rentalOrder;

        const rentalOrderCollection: IRentalOrder[] = [{ id: 45306 }];
        jest.spyOn(rentalOrderService, 'query').mockReturnValue(of(new HttpResponse({ body: rentalOrderCollection })));
        const additionalRentalOrders = [rentalOrder];
        const expectedCollection: IRentalOrder[] = [...additionalRentalOrders, ...rentalOrderCollection];
        jest.spyOn(rentalOrderService, 'addRentalOrderToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ billLineItemToOrderItem });
        comp.ngOnInit();

        expect(rentalOrderService.query).toHaveBeenCalled();
        expect(rentalOrderService.addRentalOrderToCollectionIfMissing).toHaveBeenCalledWith(
          rentalOrderCollection,
          ...additionalRentalOrders
        );
        expect(comp.rentalOrdersSharedCollection).toEqual(expectedCollection);
      });

      it('Should call BillLineItem query and add missing value', () => {
        const billLineItemToOrderItem: IBillLineItemToOrderItem = { id: 456 };
        const billLineItem: IBillLineItem = { id: 51358 };
        billLineItemToOrderItem.billLineItem = billLineItem;

        const billLineItemCollection: IBillLineItem[] = [{ id: 77454 }];
        jest.spyOn(billLineItemService, 'query').mockReturnValue(of(new HttpResponse({ body: billLineItemCollection })));
        const additionalBillLineItems = [billLineItem];
        const expectedCollection: IBillLineItem[] = [...additionalBillLineItems, ...billLineItemCollection];
        jest.spyOn(billLineItemService, 'addBillLineItemToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ billLineItemToOrderItem });
        comp.ngOnInit();

        expect(billLineItemService.query).toHaveBeenCalled();
        expect(billLineItemService.addBillLineItemToCollectionIfMissing).toHaveBeenCalledWith(
          billLineItemCollection,
          ...additionalBillLineItems
        );
        expect(comp.billLineItemsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const billLineItemToOrderItem: IBillLineItemToOrderItem = { id: 456 };
        const orderItem: IOrderItem = { id: 1774 };
        billLineItemToOrderItem.orderItem = orderItem;
        const rentalOrder: IRentalOrder = { id: 17579 };
        billLineItemToOrderItem.rentalOrder = rentalOrder;
        const billLineItem: IBillLineItem = { id: 24328 };
        billLineItemToOrderItem.billLineItem = billLineItem;

        activatedRoute.data = of({ billLineItemToOrderItem });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(billLineItemToOrderItem));
        expect(comp.orderItemsSharedCollection).toContain(orderItem);
        expect(comp.rentalOrdersSharedCollection).toContain(rentalOrder);
        expect(comp.billLineItemsSharedCollection).toContain(billLineItem);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<BillLineItemToOrderItem>>();
        const billLineItemToOrderItem = { id: 123 };
        jest.spyOn(billLineItemToOrderItemService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ billLineItemToOrderItem });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: billLineItemToOrderItem }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(billLineItemToOrderItemService.update).toHaveBeenCalledWith(billLineItemToOrderItem);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<BillLineItemToOrderItem>>();
        const billLineItemToOrderItem = new BillLineItemToOrderItem();
        jest.spyOn(billLineItemToOrderItemService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ billLineItemToOrderItem });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: billLineItemToOrderItem }));
        saveSubject.complete();

        // THEN
        expect(billLineItemToOrderItemService.create).toHaveBeenCalledWith(billLineItemToOrderItem);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<BillLineItemToOrderItem>>();
        const billLineItemToOrderItem = { id: 123 };
        jest.spyOn(billLineItemToOrderItemService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ billLineItemToOrderItem });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(billLineItemToOrderItemService.update).toHaveBeenCalledWith(billLineItemToOrderItem);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackOrderItemById', () => {
        it('Should return tracked OrderItem primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackOrderItemById(0, entity);
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

      describe('trackBillLineItemById', () => {
        it('Should return tracked BillLineItem primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackBillLineItemById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
