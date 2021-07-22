import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IBillLineItemToOrderItem } from '../bill-line-item-to-order-item.model';
import { BillLineItemToOrderItemService } from '../service/bill-line-item-to-order-item.service';
import { BillLineItemToOrderItemDeleteDialogComponent } from '../delete/bill-line-item-to-order-item-delete-dialog.component';

@Component({
  selector: 'jhi-bill-line-item-to-order-item',
  templateUrl: './bill-line-item-to-order-item.component.html',
})
export class BillLineItemToOrderItemComponent implements OnInit {
  billLineItemToOrderItems?: IBillLineItemToOrderItem[];
  isLoading = false;

  constructor(protected billLineItemToOrderItemService: BillLineItemToOrderItemService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.billLineItemToOrderItemService.query().subscribe(
      (res: HttpResponse<IBillLineItemToOrderItem[]>) => {
        this.isLoading = false;
        this.billLineItemToOrderItems = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IBillLineItemToOrderItem): number {
    return item.id!;
  }

  delete(billLineItemToOrderItem: IBillLineItemToOrderItem): void {
    const modalRef = this.modalService.open(BillLineItemToOrderItemDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.billLineItemToOrderItem = billLineItemToOrderItem;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
