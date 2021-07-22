import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IBillLineItemToOrderItem } from '../bill-line-item-to-order-item.model';

@Component({
  selector: 'jhi-bill-line-item-to-order-item-detail',
  templateUrl: './bill-line-item-to-order-item-detail.component.html',
})
export class BillLineItemToOrderItemDetailComponent implements OnInit {
  billLineItemToOrderItem: IBillLineItemToOrderItem | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ billLineItemToOrderItem }) => {
      this.billLineItemToOrderItem = billLineItemToOrderItem;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
