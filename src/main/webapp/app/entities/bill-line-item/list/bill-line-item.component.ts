import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IBillLineItem } from '../bill-line-item.model';
import { BillLineItemService } from '../service/bill-line-item.service';
import { BillLineItemDeleteDialogComponent } from '../delete/bill-line-item-delete-dialog.component';

@Component({
  selector: 'jhi-bill-line-item',
  templateUrl: './bill-line-item.component.html',
})
export class BillLineItemComponent implements OnInit {
  billLineItems?: IBillLineItem[];
  isLoading = false;

  constructor(protected billLineItemService: BillLineItemService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.billLineItemService.query().subscribe(
      (res: HttpResponse<IBillLineItem[]>) => {
        this.isLoading = false;
        this.billLineItems = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IBillLineItem): number {
    return item.id!;
  }

  delete(billLineItem: IBillLineItem): void {
    const modalRef = this.modalService.open(BillLineItemDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.billLineItem = billLineItem;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
