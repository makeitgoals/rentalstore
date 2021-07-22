import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { BillLineItemToOrderItemDetailComponent } from './bill-line-item-to-order-item-detail.component';

describe('Component Tests', () => {
  describe('BillLineItemToOrderItem Management Detail Component', () => {
    let comp: BillLineItemToOrderItemDetailComponent;
    let fixture: ComponentFixture<BillLineItemToOrderItemDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [BillLineItemToOrderItemDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ billLineItemToOrderItem: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(BillLineItemToOrderItemDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(BillLineItemToOrderItemDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load billLineItemToOrderItem on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.billLineItemToOrderItem).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
