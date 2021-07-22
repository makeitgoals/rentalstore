import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { RentalOrderComponent } from './list/rental-order.component';
import { RentalOrderDetailComponent } from './detail/rental-order-detail.component';
import { RentalOrderUpdateComponent } from './update/rental-order-update.component';
import { RentalOrderDeleteDialogComponent } from './delete/rental-order-delete-dialog.component';
import { RentalOrderRoutingModule } from './route/rental-order-routing.module';

@NgModule({
  imports: [SharedModule, RentalOrderRoutingModule],
  declarations: [RentalOrderComponent, RentalOrderDetailComponent, RentalOrderUpdateComponent, RentalOrderDeleteDialogComponent],
  entryComponents: [RentalOrderDeleteDialogComponent],
})
export class RentalOrderModule {}
