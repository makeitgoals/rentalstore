jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ItemBalanceByCustomerService } from '../service/item-balance-by-customer.service';

import { ItemBalanceByCustomerDeleteDialogComponent } from './item-balance-by-customer-delete-dialog.component';

describe('Component Tests', () => {
  describe('ItemBalanceByCustomer Management Delete Component', () => {
    let comp: ItemBalanceByCustomerDeleteDialogComponent;
    let fixture: ComponentFixture<ItemBalanceByCustomerDeleteDialogComponent>;
    let service: ItemBalanceByCustomerService;
    let mockActiveModal: NgbActiveModal;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ItemBalanceByCustomerDeleteDialogComponent],
        providers: [NgbActiveModal],
      })
        .overrideTemplate(ItemBalanceByCustomerDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ItemBalanceByCustomerDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(ItemBalanceByCustomerService);
      mockActiveModal = TestBed.inject(NgbActiveModal);
    });

    describe('confirmDelete', () => {
      it('Should call delete service on confirmDelete', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          jest.spyOn(service, 'delete').mockReturnValue(of(new HttpResponse({})));

          // WHEN
          comp.confirmDelete(123);
          tick();

          // THEN
          expect(service.delete).toHaveBeenCalledWith(123);
          expect(mockActiveModal.close).toHaveBeenCalledWith('deleted');
        })
      ));

      it('Should not call delete service on clear', () => {
        // GIVEN
        jest.spyOn(service, 'delete');

        // WHEN
        comp.cancel();

        // THEN
        expect(service.delete).not.toHaveBeenCalled();
        expect(mockActiveModal.close).not.toHaveBeenCalled();
        expect(mockActiveModal.dismiss).toHaveBeenCalled();
      });
    });
  });
});
