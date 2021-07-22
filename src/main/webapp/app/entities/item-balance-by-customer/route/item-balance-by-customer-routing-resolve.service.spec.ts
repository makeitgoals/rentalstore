jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IItemBalanceByCustomer, ItemBalanceByCustomer } from '../item-balance-by-customer.model';
import { ItemBalanceByCustomerService } from '../service/item-balance-by-customer.service';

import { ItemBalanceByCustomerRoutingResolveService } from './item-balance-by-customer-routing-resolve.service';

describe('Service Tests', () => {
  describe('ItemBalanceByCustomer routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: ItemBalanceByCustomerRoutingResolveService;
    let service: ItemBalanceByCustomerService;
    let resultItemBalanceByCustomer: IItemBalanceByCustomer | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(ItemBalanceByCustomerRoutingResolveService);
      service = TestBed.inject(ItemBalanceByCustomerService);
      resultItemBalanceByCustomer = undefined;
    });

    describe('resolve', () => {
      it('should return IItemBalanceByCustomer returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultItemBalanceByCustomer = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultItemBalanceByCustomer).toEqual({ id: 123 });
      });

      it('should return new IItemBalanceByCustomer if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultItemBalanceByCustomer = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultItemBalanceByCustomer).toEqual(new ItemBalanceByCustomer());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as ItemBalanceByCustomer })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultItemBalanceByCustomer = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultItemBalanceByCustomer).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
