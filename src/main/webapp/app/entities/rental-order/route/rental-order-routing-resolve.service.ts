import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IRentalOrder, RentalOrder } from '../rental-order.model';
import { RentalOrderService } from '../service/rental-order.service';

@Injectable({ providedIn: 'root' })
export class RentalOrderRoutingResolveService implements Resolve<IRentalOrder> {
  constructor(protected service: RentalOrderService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IRentalOrder> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((rentalOrder: HttpResponse<RentalOrder>) => {
          if (rentalOrder.body) {
            return of(rentalOrder.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new RentalOrder());
  }
}
