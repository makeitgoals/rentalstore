jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ItemBalanceByCustomerService } from '../service/item-balance-by-customer.service';
import { IItemBalanceByCustomer, ItemBalanceByCustomer } from '../item-balance-by-customer.model';
import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';
import { IOrderItem } from 'app/entities/order-item/order-item.model';
import { OrderItemService } from 'app/entities/order-item/service/order-item.service';
import { ICustomer } from 'app/entities/customer/customer.model';
import { CustomerService } from 'app/entities/customer/service/customer.service';

import { ItemBalanceByCustomerUpdateComponent } from './item-balance-by-customer-update.component';

describe('Component Tests', () => {
  describe('ItemBalanceByCustomer Management Update Component', () => {
    let comp: ItemBalanceByCustomerUpdateComponent;
    let fixture: ComponentFixture<ItemBalanceByCustomerUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let itemBalanceByCustomerService: ItemBalanceByCustomerService;
    let productService: ProductService;
    let orderItemService: OrderItemService;
    let customerService: CustomerService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ItemBalanceByCustomerUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ItemBalanceByCustomerUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ItemBalanceByCustomerUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      itemBalanceByCustomerService = TestBed.inject(ItemBalanceByCustomerService);
      productService = TestBed.inject(ProductService);
      orderItemService = TestBed.inject(OrderItemService);
      customerService = TestBed.inject(CustomerService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Product query and add missing value', () => {
        const itemBalanceByCustomer: IItemBalanceByCustomer = { id: 456 };
        const product: IProduct = { id: 80921 };
        itemBalanceByCustomer.product = product;

        const productCollection: IProduct[] = [{ id: 42299 }];
        jest.spyOn(productService, 'query').mockReturnValue(of(new HttpResponse({ body: productCollection })));
        const additionalProducts = [product];
        const expectedCollection: IProduct[] = [...additionalProducts, ...productCollection];
        jest.spyOn(productService, 'addProductToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ itemBalanceByCustomer });
        comp.ngOnInit();

        expect(productService.query).toHaveBeenCalled();
        expect(productService.addProductToCollectionIfMissing).toHaveBeenCalledWith(productCollection, ...additionalProducts);
        expect(comp.productsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call OrderItem query and add missing value', () => {
        const itemBalanceByCustomer: IItemBalanceByCustomer = { id: 456 };
        const orderItem: IOrderItem = { id: 72524 };
        itemBalanceByCustomer.orderItem = orderItem;

        const orderItemCollection: IOrderItem[] = [{ id: 38108 }];
        jest.spyOn(orderItemService, 'query').mockReturnValue(of(new HttpResponse({ body: orderItemCollection })));
        const additionalOrderItems = [orderItem];
        const expectedCollection: IOrderItem[] = [...additionalOrderItems, ...orderItemCollection];
        jest.spyOn(orderItemService, 'addOrderItemToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ itemBalanceByCustomer });
        comp.ngOnInit();

        expect(orderItemService.query).toHaveBeenCalled();
        expect(orderItemService.addOrderItemToCollectionIfMissing).toHaveBeenCalledWith(orderItemCollection, ...additionalOrderItems);
        expect(comp.orderItemsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Customer query and add missing value', () => {
        const itemBalanceByCustomer: IItemBalanceByCustomer = { id: 456 };
        const customer: ICustomer = { id: 9616 };
        itemBalanceByCustomer.customer = customer;

        const customerCollection: ICustomer[] = [{ id: 71441 }];
        jest.spyOn(customerService, 'query').mockReturnValue(of(new HttpResponse({ body: customerCollection })));
        const additionalCustomers = [customer];
        const expectedCollection: ICustomer[] = [...additionalCustomers, ...customerCollection];
        jest.spyOn(customerService, 'addCustomerToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ itemBalanceByCustomer });
        comp.ngOnInit();

        expect(customerService.query).toHaveBeenCalled();
        expect(customerService.addCustomerToCollectionIfMissing).toHaveBeenCalledWith(customerCollection, ...additionalCustomers);
        expect(comp.customersSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const itemBalanceByCustomer: IItemBalanceByCustomer = { id: 456 };
        const product: IProduct = { id: 68112 };
        itemBalanceByCustomer.product = product;
        const orderItem: IOrderItem = { id: 60830 };
        itemBalanceByCustomer.orderItem = orderItem;
        const customer: ICustomer = { id: 78996 };
        itemBalanceByCustomer.customer = customer;

        activatedRoute.data = of({ itemBalanceByCustomer });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(itemBalanceByCustomer));
        expect(comp.productsSharedCollection).toContain(product);
        expect(comp.orderItemsSharedCollection).toContain(orderItem);
        expect(comp.customersSharedCollection).toContain(customer);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<ItemBalanceByCustomer>>();
        const itemBalanceByCustomer = { id: 123 };
        jest.spyOn(itemBalanceByCustomerService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ itemBalanceByCustomer });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: itemBalanceByCustomer }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(itemBalanceByCustomerService.update).toHaveBeenCalledWith(itemBalanceByCustomer);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<ItemBalanceByCustomer>>();
        const itemBalanceByCustomer = new ItemBalanceByCustomer();
        jest.spyOn(itemBalanceByCustomerService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ itemBalanceByCustomer });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: itemBalanceByCustomer }));
        saveSubject.complete();

        // THEN
        expect(itemBalanceByCustomerService.create).toHaveBeenCalledWith(itemBalanceByCustomer);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<ItemBalanceByCustomer>>();
        const itemBalanceByCustomer = { id: 123 };
        jest.spyOn(itemBalanceByCustomerService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ itemBalanceByCustomer });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(itemBalanceByCustomerService.update).toHaveBeenCalledWith(itemBalanceByCustomer);
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

      describe('trackOrderItemById', () => {
        it('Should return tracked OrderItem primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackOrderItemById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackCustomerById', () => {
        it('Should return tracked Customer primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackCustomerById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
