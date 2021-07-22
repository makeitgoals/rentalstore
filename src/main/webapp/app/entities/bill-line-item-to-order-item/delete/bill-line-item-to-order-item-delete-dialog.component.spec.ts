jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { BillLineItemToOrderItemService } from '../service/bill-line-item-to-order-item.service';

import { BillLineItemToOrderItemDeleteDialogComponent } from './bill-line-item-to-order-item-delete-dialog.component';

describe('Component Tests', () => {
  describe('BillLineItemToOrderItem Management Delete Component', () => {
    let comp: BillLineItemToOrderItemDeleteDialogComponent;
    let fixture: ComponentFixture<BillLineItemToOrderItemDeleteDialogComponent>;
    let service: BillLineItemToOrderItemService;
    let mockActiveModal: NgbActiveModal;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [BillLineItemToOrderItemDeleteDialogComponent],
        providers: [NgbActiveModal],
      })
        .overrideTemplate(BillLineItemToOrderItemDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(BillLineItemToOrderItemDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(BillLineItemToOrderItemService);
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
