import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { BillLineItemComponent } from './list/bill-line-item.component';
import { BillLineItemDetailComponent } from './detail/bill-line-item-detail.component';
import { BillLineItemUpdateComponent } from './update/bill-line-item-update.component';
import { BillLineItemDeleteDialogComponent } from './delete/bill-line-item-delete-dialog.component';
import { BillLineItemRoutingModule } from './route/bill-line-item-routing.module';

@NgModule({
  imports: [SharedModule, BillLineItemRoutingModule],
  declarations: [BillLineItemComponent, BillLineItemDetailComponent, BillLineItemUpdateComponent, BillLineItemDeleteDialogComponent],
  entryComponents: [BillLineItemDeleteDialogComponent],
})
export class BillLineItemModule {}
