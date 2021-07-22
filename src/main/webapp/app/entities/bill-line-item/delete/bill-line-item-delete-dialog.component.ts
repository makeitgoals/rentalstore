import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IBillLineItem } from '../bill-line-item.model';
import { BillLineItemService } from '../service/bill-line-item.service';

@Component({
  templateUrl: './bill-line-item-delete-dialog.component.html',
})
export class BillLineItemDeleteDialogComponent {
  billLineItem?: IBillLineItem;

  constructor(protected billLineItemService: BillLineItemService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.billLineItemService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
