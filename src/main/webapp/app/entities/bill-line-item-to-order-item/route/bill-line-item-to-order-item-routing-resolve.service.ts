import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IBillLineItemToOrderItem, BillLineItemToOrderItem } from '../bill-line-item-to-order-item.model';
import { BillLineItemToOrderItemService } from '../service/bill-line-item-to-order-item.service';

@Injectable({ providedIn: 'root' })
export class BillLineItemToOrderItemRoutingResolveService implements Resolve<IBillLineItemToOrderItem> {
  constructor(protected service: BillLineItemToOrderItemService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IBillLineItemToOrderItem> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((billLineItemToOrderItem: HttpResponse<BillLineItemToOrderItem>) => {
          if (billLineItemToOrderItem.body) {
            return of(billLineItemToOrderItem.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new BillLineItemToOrderItem());
  }
}
