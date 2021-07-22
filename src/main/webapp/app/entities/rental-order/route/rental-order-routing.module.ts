import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { RentalOrderComponent } from '../list/rental-order.component';
import { RentalOrderDetailComponent } from '../detail/rental-order-detail.component';
import { RentalOrderUpdateComponent } from '../update/rental-order-update.component';
import { RentalOrderRoutingResolveService } from './rental-order-routing-resolve.service';

const rentalOrderRoute: Routes = [
  {
    path: '',
    component: RentalOrderComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: RentalOrderDetailComponent,
    resolve: {
      rentalOrder: RentalOrderRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: RentalOrderUpdateComponent,
    resolve: {
      rentalOrder: RentalOrderRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: RentalOrderUpdateComponent,
    resolve: {
      rentalOrder: RentalOrderRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(rentalOrderRoute)],
  exports: [RouterModule],
})
export class RentalOrderRoutingModule {}
