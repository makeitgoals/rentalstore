import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IRentalOrder } from '../rental-order.model';

@Component({
  selector: 'jhi-rental-order-detail',
  templateUrl: './rental-order-detail.component.html',
})
export class RentalOrderDetailComponent implements OnInit {
  rentalOrder: IRentalOrder | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ rentalOrder }) => {
      this.rentalOrder = rentalOrder;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
