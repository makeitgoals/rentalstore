import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IBillLineItemToOrderItem, BillLineItemToOrderItem } from '../bill-line-item-to-order-item.model';
import { BillLineItemToOrderItemService } from '../service/bill-line-item-to-order-item.service';
import { IOrderItem } from 'app/entities/order-item/order-item.model';
import { OrderItemService } from 'app/entities/order-item/service/order-item.service';
import { IRentalOrder } from 'app/entities/rental-order/rental-order.model';
import { RentalOrderService } from 'app/entities/rental-order/service/rental-order.service';
import { IBillLineItem } from 'app/entities/bill-line-item/bill-line-item.model';
import { BillLineItemService } from 'app/entities/bill-line-item/service/bill-line-item.service';

@Component({
  selector: 'jhi-bill-line-item-to-order-item-update',
  templateUrl: './bill-line-item-to-order-item-update.component.html',
})
export class BillLineItemToOrderItemUpdateComponent implements OnInit {
  isSaving = false;

  orderItemsSharedCollection: IOrderItem[] = [];
  rentalOrdersSharedCollection: IRentalOrder[] = [];
  billLineItemsSharedCollection: IBillLineItem[] = [];

  editForm = this.fb.group({
    id: [],
    details: [],
    orderItem: [],
    rentalOrder: [],
    billLineItem: [],
  });

  constructor(
    protected billLineItemToOrderItemService: BillLineItemToOrderItemService,
    protected orderItemService: OrderItemService,
    protected rentalOrderService: RentalOrderService,
    protected billLineItemService: BillLineItemService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ billLineItemToOrderItem }) => {
      this.updateForm(billLineItemToOrderItem);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const billLineItemToOrderItem = this.createFromForm();
    if (billLineItemToOrderItem.id !== undefined) {
      this.subscribeToSaveResponse(this.billLineItemToOrderItemService.update(billLineItemToOrderItem));
    } else {
      this.subscribeToSaveResponse(this.billLineItemToOrderItemService.create(billLineItemToOrderItem));
    }
  }

  trackOrderItemById(index: number, item: IOrderItem): number {
    return item.id!;
  }

  trackRentalOrderById(index: number, item: IRentalOrder): number {
    return item.id!;
  }

  trackBillLineItemById(index: number, item: IBillLineItem): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBillLineItemToOrderItem>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(billLineItemToOrderItem: IBillLineItemToOrderItem): void {
    this.editForm.patchValue({
      id: billLineItemToOrderItem.id,
      details: billLineItemToOrderItem.details,
      orderItem: billLineItemToOrderItem.orderItem,
      rentalOrder: billLineItemToOrderItem.rentalOrder,
      billLineItem: billLineItemToOrderItem.billLineItem,
    });

    this.orderItemsSharedCollection = this.orderItemService.addOrderItemToCollectionIfMissing(
      this.orderItemsSharedCollection,
      billLineItemToOrderItem.orderItem
    );
    this.rentalOrdersSharedCollection = this.rentalOrderService.addRentalOrderToCollectionIfMissing(
      this.rentalOrdersSharedCollection,
      billLineItemToOrderItem.rentalOrder
    );
    this.billLineItemsSharedCollection = this.billLineItemService.addBillLineItemToCollectionIfMissing(
      this.billLineItemsSharedCollection,
      billLineItemToOrderItem.billLineItem
    );
  }

  protected loadRelationshipsOptions(): void {
    this.orderItemService
      .query()
      .pipe(map((res: HttpResponse<IOrderItem[]>) => res.body ?? []))
      .pipe(
        map((orderItems: IOrderItem[]) =>
          this.orderItemService.addOrderItemToCollectionIfMissing(orderItems, this.editForm.get('orderItem')!.value)
        )
      )
      .subscribe((orderItems: IOrderItem[]) => (this.orderItemsSharedCollection = orderItems));

    this.rentalOrderService
      .query()
      .pipe(map((res: HttpResponse<IRentalOrder[]>) => res.body ?? []))
      .pipe(
        map((rentalOrders: IRentalOrder[]) =>
          this.rentalOrderService.addRentalOrderToCollectionIfMissing(rentalOrders, this.editForm.get('rentalOrder')!.value)
        )
      )
      .subscribe((rentalOrders: IRentalOrder[]) => (this.rentalOrdersSharedCollection = rentalOrders));

    this.billLineItemService
      .query()
      .pipe(map((res: HttpResponse<IBillLineItem[]>) => res.body ?? []))
      .pipe(
        map((billLineItems: IBillLineItem[]) =>
          this.billLineItemService.addBillLineItemToCollectionIfMissing(billLineItems, this.editForm.get('billLineItem')!.value)
        )
      )
      .subscribe((billLineItems: IBillLineItem[]) => (this.billLineItemsSharedCollection = billLineItems));
  }

  protected createFromForm(): IBillLineItemToOrderItem {
    return {
      ...new BillLineItemToOrderItem(),
      id: this.editForm.get(['id'])!.value,
      details: this.editForm.get(['details'])!.value,
      orderItem: this.editForm.get(['orderItem'])!.value,
      rentalOrder: this.editForm.get(['rentalOrder'])!.value,
      billLineItem: this.editForm.get(['billLineItem'])!.value,
    };
  }
}
