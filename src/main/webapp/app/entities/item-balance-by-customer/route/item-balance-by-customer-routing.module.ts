import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ItemBalanceByCustomerComponent } from '../list/item-balance-by-customer.component';
import { ItemBalanceByCustomerDetailComponent } from '../detail/item-balance-by-customer-detail.component';
import { ItemBalanceByCustomerUpdateComponent } from '../update/item-balance-by-customer-update.component';
import { ItemBalanceByCustomerRoutingResolveService } from './item-balance-by-customer-routing-resolve.service';

const itemBalanceByCustomerRoute: Routes = [
  {
    path: '',
    component: ItemBalanceByCustomerComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ItemBalanceByCustomerDetailComponent,
    resolve: {
      itemBalanceByCustomer: ItemBalanceByCustomerRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ItemBalanceByCustomerUpdateComponent,
    resolve: {
      itemBalanceByCustomer: ItemBalanceByCustomerRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ItemBalanceByCustomerUpdateComponent,
    resolve: {
      itemBalanceByCustomer: ItemBalanceByCustomerRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(itemBalanceByCustomerRoute)],
  exports: [RouterModule],
})
export class ItemBalanceByCustomerRoutingModule {}
