jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IBillLineItemToOrderItem, BillLineItemToOrderItem } from '../bill-line-item-to-order-item.model';
import { BillLineItemToOrderItemService } from '../service/bill-line-item-to-order-item.service';

import { BillLineItemToOrderItemRoutingResolveService } from './bill-line-item-to-order-item-routing-resolve.service';

describe('Service Tests', () => {
  describe('BillLineItemToOrderItem routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: BillLineItemToOrderItemRoutingResolveService;
    let service: BillLineItemToOrderItemService;
    let resultBillLineItemToOrderItem: IBillLineItemToOrderItem | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(BillLineItemToOrderItemRoutingResolveService);
      service = TestBed.inject(BillLineItemToOrderItemService);
      resultBillLineItemToOrderItem = undefined;
    });

    describe('resolve', () => {
      it('should return IBillLineItemToOrderItem returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultBillLineItemToOrderItem = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultBillLineItemToOrderItem).toEqual({ id: 123 });
      });

      it('should return new IBillLineItemToOrderItem if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultBillLineItemToOrderItem = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultBillLineItemToOrderItem).toEqual(new BillLineItemToOrderItem());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as BillLineItemToOrderItem })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultBillLineItemToOrderItem = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultBillLineItemToOrderItem).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
