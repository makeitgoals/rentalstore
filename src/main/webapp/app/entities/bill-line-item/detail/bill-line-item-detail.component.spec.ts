import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { BillLineItemDetailComponent } from './bill-line-item-detail.component';

describe('Component Tests', () => {
  describe('BillLineItem Management Detail Component', () => {
    let comp: BillLineItemDetailComponent;
    let fixture: ComponentFixture<BillLineItemDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [BillLineItemDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ billLineItem: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(BillLineItemDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(BillLineItemDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load billLineItem on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.billLineItem).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
