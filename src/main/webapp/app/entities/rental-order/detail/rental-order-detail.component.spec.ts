import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { RentalOrderDetailComponent } from './rental-order-detail.component';

describe('Component Tests', () => {
  describe('RentalOrder Management Detail Component', () => {
    let comp: RentalOrderDetailComponent;
    let fixture: ComponentFixture<RentalOrderDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [RentalOrderDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ rentalOrder: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(RentalOrderDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(RentalOrderDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load rentalOrder on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.rentalOrder).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
