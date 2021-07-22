import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { BillLineItemComponent } from '../list/bill-line-item.component';
import { BillLineItemDetailComponent } from '../detail/bill-line-item-detail.component';
import { BillLineItemUpdateComponent } from '../update/bill-line-item-update.component';
import { BillLineItemRoutingResolveService } from './bill-line-item-routing-resolve.service';

const billLineItemRoute: Routes = [
  {
    path: '',
    component: BillLineItemComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: BillLineItemDetailComponent,
    resolve: {
      billLineItem: BillLineItemRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: BillLineItemUpdateComponent,
    resolve: {
      billLineItem: BillLineItemRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: BillLineItemUpdateComponent,
    resolve: {
      billLineItem: BillLineItemRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(billLineItemRoute)],
  exports: [RouterModule],
})
export class BillLineItemRoutingModule {}
