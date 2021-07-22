jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IBillLineItem, BillLineItem } from '../bill-line-item.model';
import { BillLineItemService } from '../service/bill-line-item.service';

import { BillLineItemRoutingResolveService } from './bill-line-item-routing-resolve.service';

describe('Service Tests', () => {
  describe('BillLineItem routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: BillLineItemRoutingResolveService;
    let service: BillLineItemService;
    let resultBillLineItem: IBillLineItem | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(BillLineItemRoutingResolveService);
      service = TestBed.inject(BillLineItemService);
      resultBillLineItem = undefined;
    });

    describe('resolve', () => {
      it('should return IBillLineItem returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultBillLineItem = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultBillLineItem).toEqual({ id: 123 });
      });

      it('should return new IBillLineItem if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultBillLineItem = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultBillLineItem).toEqual(new BillLineItem());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as BillLineItem })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultBillLineItem = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultBillLineItem).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
