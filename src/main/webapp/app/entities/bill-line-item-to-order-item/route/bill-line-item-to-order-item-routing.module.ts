import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { BillLineItemToOrderItemComponent } from '../list/bill-line-item-to-order-item.component';
import { BillLineItemToOrderItemDetailComponent } from '../detail/bill-line-item-to-order-item-detail.component';
import { BillLineItemToOrderItemUpdateComponent } from '../update/bill-line-item-to-order-item-update.component';
import { BillLineItemToOrderItemRoutingResolveService } from './bill-line-item-to-order-item-routing-resolve.service';

const billLineItemToOrderItemRoute: Routes = [
  {
    path: '',
    component: BillLineItemToOrderItemComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: BillLineItemToOrderItemDetailComponent,
    resolve: {
      billLineItemToOrderItem: BillLineItemToOrderItemRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: BillLineItemToOrderItemUpdateComponent,
    resolve: {
      billLineItemToOrderItem: BillLineItemToOrderItemRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: BillLineItemToOrderItemUpdateComponent,
    resolve: {
      billLineItemToOrderItem: BillLineItemToOrderItemRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(billLineItemToOrderItemRoute)],
  exports: [RouterModule],
})
export class BillLineItemToOrderItemRoutingModule {}
