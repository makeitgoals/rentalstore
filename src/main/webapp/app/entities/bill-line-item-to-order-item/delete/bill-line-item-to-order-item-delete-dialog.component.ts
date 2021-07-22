import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IBillLineItemToOrderItem } from '../bill-line-item-to-order-item.model';
import { BillLineItemToOrderItemService } from '../service/bill-line-item-to-order-item.service';

@Component({
  templateUrl: './bill-line-item-to-order-item-delete-dialog.component.html',
})
export class BillLineItemToOrderItemDeleteDialogComponent {
  billLineItemToOrderItem?: IBillLineItemToOrderItem;

  constructor(protected billLineItemToOrderItemService: BillLineItemToOrderItemService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.billLineItemToOrderItemService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
