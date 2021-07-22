import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { BillLineItemToOrderItemComponent } from './list/bill-line-item-to-order-item.component';
import { BillLineItemToOrderItemDetailComponent } from './detail/bill-line-item-to-order-item-detail.component';
import { BillLineItemToOrderItemUpdateComponent } from './update/bill-line-item-to-order-item-update.component';
import { BillLineItemToOrderItemDeleteDialogComponent } from './delete/bill-line-item-to-order-item-delete-dialog.component';
import { BillLineItemToOrderItemRoutingModule } from './route/bill-line-item-to-order-item-routing.module';

@NgModule({
  imports: [SharedModule, BillLineItemToOrderItemRoutingModule],
  declarations: [
    BillLineItemToOrderItemComponent,
    BillLineItemToOrderItemDetailComponent,
    BillLineItemToOrderItemUpdateComponent,
    BillLineItemToOrderItemDeleteDialogComponent,
  ],
  entryComponents: [BillLineItemToOrderItemDeleteDialogComponent],
})
export class BillLineItemToOrderItemModule {}
