import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { BillLineItemService } from '../service/bill-line-item.service';

import { BillLineItemComponent } from './bill-line-item.component';

describe('Component Tests', () => {
  describe('BillLineItem Management Component', () => {
    let comp: BillLineItemComponent;
    let fixture: ComponentFixture<BillLineItemComponent>;
    let service: BillLineItemService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [BillLineItemComponent],
      })
        .overrideTemplate(BillLineItemComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(BillLineItemComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(BillLineItemService);

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
      expect(comp.billLineItems?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
