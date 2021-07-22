import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IItemBalanceByCustomer } from '../item-balance-by-customer.model';
import { ItemBalanceByCustomerService } from '../service/item-balance-by-customer.service';

@Component({
  templateUrl: './item-balance-by-customer-delete-dialog.component.html',
})
export class ItemBalanceByCustomerDeleteDialogComponent {
  itemBalanceByCustomer?: IItemBalanceByCustomer;

  constructor(protected itemBalanceByCustomerService: ItemBalanceByCustomerService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.itemBalanceByCustomerService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
