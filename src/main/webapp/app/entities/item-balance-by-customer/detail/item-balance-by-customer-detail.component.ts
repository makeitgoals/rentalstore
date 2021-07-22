import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IItemBalanceByCustomer } from '../item-balance-by-customer.model';

@Component({
  selector: 'jhi-item-balance-by-customer-detail',
  templateUrl: './item-balance-by-customer-detail.component.html',
})
export class ItemBalanceByCustomerDetailComponent implements OnInit {
  itemBalanceByCustomer: IItemBalanceByCustomer | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ itemBalanceByCustomer }) => {
      this.itemBalanceByCustomer = itemBalanceByCustomer;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
