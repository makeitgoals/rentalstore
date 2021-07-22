import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IRentalOrder } from '../rental-order.model';
import { RentalOrderService } from '../service/rental-order.service';

@Component({
  templateUrl: './rental-order-delete-dialog.component.html',
})
export class RentalOrderDeleteDialogComponent {
  rentalOrder?: IRentalOrder;

  constructor(protected rentalOrderService: RentalOrderService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.rentalOrderService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
