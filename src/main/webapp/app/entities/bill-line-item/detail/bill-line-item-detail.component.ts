import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IBillLineItem } from '../bill-line-item.model';

@Component({
  selector: 'jhi-bill-line-item-detail',
  templateUrl: './bill-line-item-detail.component.html',
})
export class BillLineItemDetailComponent implements OnInit {
  billLineItem: IBillLineItem | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ billLineItem }) => {
      this.billLineItem = billLineItem;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
