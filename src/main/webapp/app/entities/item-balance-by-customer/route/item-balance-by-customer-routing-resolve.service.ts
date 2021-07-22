import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IItemBalanceByCustomer, ItemBalanceByCustomer } from '../item-balance-by-customer.model';
import { ItemBalanceByCustomerService } from '../service/item-balance-by-customer.service';

@Injectable({ providedIn: 'root' })
export class ItemBalanceByCustomerRoutingResolveService implements Resolve<IItemBalanceByCustomer> {
  constructor(protected service: ItemBalanceByCustomerService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IItemBalanceByCustomer> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((itemBalanceByCustomer: HttpResponse<ItemBalanceByCustomer>) => {
          if (itemBalanceByCustomer.body) {
            return of(itemBalanceByCustomer.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ItemBalanceByCustomer());
  }
}
