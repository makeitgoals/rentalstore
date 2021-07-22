import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IBillLineItem, BillLineItem } from '../bill-line-item.model';
import { BillLineItemService } from '../service/bill-line-item.service';

@Injectable({ providedIn: 'root' })
export class BillLineItemRoutingResolveService implements Resolve<IBillLineItem> {
  constructor(protected service: BillLineItemService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IBillLineItem> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((billLineItem: HttpResponse<BillLineItem>) => {
          if (billLineItem.body) {
            return of(billLineItem.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new BillLineItem());
  }
}
