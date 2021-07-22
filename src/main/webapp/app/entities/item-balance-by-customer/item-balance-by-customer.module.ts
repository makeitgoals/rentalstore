import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ItemBalanceByCustomerComponent } from './list/item-balance-by-customer.component';
import { ItemBalanceByCustomerDetailComponent } from './detail/item-balance-by-customer-detail.component';
import { ItemBalanceByCustomerUpdateComponent } from './update/item-balance-by-customer-update.component';
import { ItemBalanceByCustomerDeleteDialogComponent } from './delete/item-balance-by-customer-delete-dialog.component';
import { ItemBalanceByCustomerRoutingModule } from './route/item-balance-by-customer-routing.module';

@NgModule({
  imports: [SharedModule, ItemBalanceByCustomerRoutingModule],
  declarations: [
    ItemBalanceByCustomerComponent,
    ItemBalanceByCustomerDetailComponent,
    ItemBalanceByCustomerUpdateComponent,
    ItemBalanceByCustomerDeleteDialogComponent,
  ],
  entryComponents: [ItemBalanceByCustomerDeleteDialogComponent],
})
export class ItemBalanceByCustomerModule {}
