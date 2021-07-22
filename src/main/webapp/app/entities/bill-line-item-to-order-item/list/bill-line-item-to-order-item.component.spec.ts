import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { BillLineItemToOrderItemService } from '../service/bill-line-item-to-order-item.service';

import { BillLineItemToOrderItemComponent } from './bill-line-item-to-order-item.component';

describe('Component Tests', () => {
  describe('BillLineItemToOrderItem Management Component', () => {
    let comp: BillLineItemToOrderItemComponent;
    let fixture: ComponentFixture<BillLineItemToOrderItemComponent>;
    let service: BillLineItemToOrderItemService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [BillLineItemToOrderItemComponent],
      })
        .overrideTemplate(BillLineItemToOrderItemComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(BillLineItemToOrderItemComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(BillLineItemToOrderItemService);

      const headers = new HttpHeaders().append('link', 'link;link');
      jest.spyOn(service, 'query').mockReturnValue(
        of(
          new HttpResponse({
            body: [{ id: 123 }],
            headers,
          })
        )
      );
    });

    it('Should call load all on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.billLineItemToOrderItems?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
