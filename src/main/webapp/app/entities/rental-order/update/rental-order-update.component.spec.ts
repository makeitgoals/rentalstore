jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { RentalOrderService } from '../service/rental-order.service';
import { IRentalOrder, RentalOrder } from '../rental-order.model';
import { ICustomer } from 'app/entities/customer/customer.model';
import { CustomerService } from 'app/entities/customer/service/customer.service';

import { RentalOrderUpdateComponent } from './rental-order-update.component';

describe('Component Tests', () => {
  describe('RentalOrder Management Update Component', () => {
    let comp: RentalOrderUpdateComponent;
    let fixture: ComponentFixture<RentalOrderUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let rentalOrderService: RentalOrderService;
    let customerService: CustomerService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [RentalOrderUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(RentalOrderUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(RentalOrderUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      rentalOrderService = TestBed.inject(RentalOrderService);
      customerService = TestBed.inject(CustomerService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Customer query and add missing value', () => {
        const rentalOrder: IRentalOrder = { id: 456 };
        const customer: ICustomer = { id: 99279 };
        rentalOrder.customer = customer;

        const customerCollection: ICustomer[] = [{ id: 86658 }];
        jest.spyOn(customerService, 'query').mockReturnValue(of(new HttpResponse({ body: customerCollection })));
        const additionalCustomers = [customer];
        const expectedCollection: ICustomer[] = [...additionalCustomers, ...customerCollection];
        jest.spyOn(customerService, 'addCustomerToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ rentalOrder });
        comp.ngOnInit();

        expect(customerService.query).toHaveBeenCalled();
        expect(customerService.addCustomerToCollectionIfMissing).toHaveBeenCalledWith(customerCollection, ...additionalCustomers);
        expect(comp.customersSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const rentalOrder: IRentalOrder = { id: 456 };
        const customer: ICustomer = { id: 5773 };
        rentalOrder.customer = customer;

        activatedRoute.data = of({ rentalOrder });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(rentalOrder));
        expect(comp.customersSharedCollection).toContain(customer);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<RentalOrder>>();
        const rentalOrder = { id: 123 };
        jest.spyOn(rentalOrderService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ rentalOrder });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: rentalOrder }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(rentalOrderService.update).toHaveBeenCalledWith(rentalOrder);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<RentalOrder>>();
        const rentalOrder = new RentalOrder();
        jest.spyOn(rentalOrderService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ rentalOrder });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: rentalOrder }));
        saveSubject.complete();

        // THEN
        expect(rentalOrderService.create).toHaveBeenCalledWith(rentalOrder);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<RentalOrder>>();
        const rentalOrder = { id: 123 };
        jest.spyOn(rentalOrderService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ rentalOrder });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(rentalOrderService.update).toHaveBeenCalledWith(rentalOrder);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
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
