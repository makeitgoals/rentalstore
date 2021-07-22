jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { BillService } from '../service/bill.service';
import { IBill, Bill } from '../bill.model';
import { ICustomer } from 'app/entities/customer/customer.model';
import { CustomerService } from 'app/entities/customer/service/customer.service';

import { BillUpdateComponent } from './bill-update.component';

describe('Component Tests', () => {
  describe('Bill Management Update Component', () => {
    let comp: BillUpdateComponent;
    let fixture: ComponentFixture<BillUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let billService: BillService;
    let customerService: CustomerService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [BillUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(BillUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(BillUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      billService = TestBed.inject(BillService);
      customerService = TestBed.inject(CustomerService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Customer query and add missing value', () => {
        const bill: IBill = { id: 456 };
        const customer: ICustomer = { id: 67985 };
        bill.customer = customer;

        const customerCollection: ICustomer[] = [{ id: 57426 }];
        jest.spyOn(customerService, 'query').mockReturnValue(of(new HttpResponse({ body: customerCollection })));
        const additionalCustomers = [customer];
        const expectedCollection: ICustomer[] = [...additionalCustomers, ...customerCollection];
        jest.spyOn(customerService, 'addCustomerToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ bill });
        comp.ngOnInit();

        expect(customerService.query).toHaveBeenCalled();
        expect(customerService.addCustomerToCollectionIfMissing).toHaveBeenCalledWith(customerCollection, ...additionalCustomers);
        expect(comp.customersSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const bill: IBill = { id: 456 };
        const customer: ICustomer = { id: 38529 };
        bill.customer = customer;

        activatedRoute.data = of({ bill });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(bill));
        expect(comp.customersSharedCollection).toContain(customer);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Bill>>();
        const bill = { id: 123 };
        jest.spyOn(billService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ bill });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: bill }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(billService.update).toHaveBeenCalledWith(bill);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Bill>>();
        const bill = new Bill();
        jest.spyOn(billService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ bill });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: bill }));
        saveSubject.complete();

        // THEN
        expect(billService.create).toHaveBeenCalledWith(bill);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Bill>>();
        const bill = { id: 123 };
        jest.spyOn(billService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ bill });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(billService.update).toHaveBeenCalledWith(bill);
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
