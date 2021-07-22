import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ItemBalanceByCustomerDetailComponent } from './item-balance-by-customer-detail.component';

describe('Component Tests', () => {
  describe('ItemBalanceByCustomer Management Detail Component', () => {
    let comp: ItemBalanceByCustomerDetailComponent;
    let fixture: ComponentFixture<ItemBalanceByCustomerDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [ItemBalanceByCustomerDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ itemBalanceByCustomer: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(ItemBalanceByCustomerDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ItemBalanceByCustomerDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load itemBalanceByCustomer on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.itemBalanceByCustomer).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
