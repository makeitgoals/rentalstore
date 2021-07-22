import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IBillLineItem, BillLineItem } from '../bill-line-item.model';
import { BillLineItemService } from '../service/bill-line-item.service';
import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';
import { IBill } from 'app/entities/bill/bill.model';
import { BillService } from 'app/entities/bill/service/bill.service';
import { IRentalOrder } from 'app/entities/rental-order/rental-order.model';
import { RentalOrderService } from 'app/entities/rental-order/service/rental-order.service';

@Component({
  selector: 'jhi-bill-line-item-update',
  templateUrl: './bill-line-item-update.component.html',
})
export class BillLineItemUpdateComponent implements OnInit {
  isSaving = false;

  productsSharedCollection: IProduct[] = [];
  billsSharedCollection: IBill[] = [];
  rentalOrdersSharedCollection: IRentalOrder[] = [];

  editForm = this.fb.group({
    id: [],
    details: [],
    fromDate: [null, [Validators.required]],
    toDate: [null, [Validators.required]],
    outstandingQuantity: [null, [Validators.required, Validators.min(0)]],
    lineAmount: [null, [Validators.required, Validators.min(0)]],
    product: [],
    bill: [],
    rentalOrder: [],
  });

  constructor(
    protected billLineItemService: BillLineItemService,
    protected productService: ProductService,
    protected billService: BillService,
    protected rentalOrderService: RentalOrderService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ billLineItem }) => {
      if (billLineItem.id === undefined) {
        const today = dayjs().startOf('day');
        billLineItem.fromDate = today;
        billLineItem.toDate = today;
      }

      this.updateForm(billLineItem);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const billLineItem = this.createFromForm();
    if (billLineItem.id !== undefined) {
      this.subscribeToSaveResponse(this.billLineItemService.update(billLineItem));
    } else {
      this.subscribeToSaveResponse(this.billLineItemService.create(billLineItem));
    }
  }

  trackProductById(index: number, item: IProduct): number {
    return item.id!;
  }

  trackBillById(index: number, item: IBill): number {
    return item.id!;
  }

  trackRentalOrderById(index: number, item: IRentalOrder): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBillLineItem>>): void {
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

  protected updateForm(billLineItem: IBillLineItem): void {
    this.editForm.patchValue({
      id: billLineItem.id,
      details: billLineItem.details,
      fromDate: billLineItem.fromDate ? billLineItem.fromDate.format(DATE_TIME_FORMAT) : null,
      toDate: billLineItem.toDate ? billLineItem.toDate.format(DATE_TIME_FORMAT) : null,
      outstandingQuantity: billLineItem.outstandingQuantity,
      lineAmount: billLineItem.lineAmount,
      product: billLineItem.product,
      bill: billLineItem.bill,
      rentalOrder: billLineItem.rentalOrder,
    });

    this.productsSharedCollection = this.productService.addProductToCollectionIfMissing(
      this.productsSharedCollection,
      billLineItem.product
    );
    this.billsSharedCollection = this.billService.addBillToCollectionIfMissing(this.billsSharedCollection, billLineItem.bill);
    this.rentalOrdersSharedCollection = this.rentalOrderService.addRentalOrderToCollectionIfMissing(
      this.rentalOrdersSharedCollection,
      billLineItem.rentalOrder
    );
  }

  protected loadRelationshipsOptions(): void {
    this.productService
      .query()
      .pipe(map((res: HttpResponse<IProduct[]>) => res.body ?? []))
      .pipe(
        map((products: IProduct[]) => this.productService.addProductToCollectionIfMissing(products, this.editForm.get('product')!.value))
      )
      .subscribe((products: IProduct[]) => (this.productsSharedCollection = products));

    this.billService
      .query()
      .pipe(map((res: HttpResponse<IBill[]>) => res.body ?? []))
      .pipe(map((bills: IBill[]) => this.billService.addBillToCollectionIfMissing(bills, this.editForm.get('bill')!.value)))
      .subscribe((bills: IBill[]) => (this.billsSharedCollection = bills));

    this.rentalOrderService
      .query()
      .pipe(map((res: HttpResponse<IRentalOrder[]>) => res.body ?? []))
      .pipe(
        map((rentalOrders: IRentalOrder[]) =>
          this.rentalOrderService.addRentalOrderToCollectionIfMissing(rentalOrders, this.editForm.get('rentalOrder')!.value)
        )
      )
      .subscribe((rentalOrders: IRentalOrder[]) => (this.rentalOrdersSharedCollection = rentalOrders));
  }

  protected createFromForm(): IBillLineItem {
    return {
      ...new BillLineItem(),
      id: this.editForm.get(['id'])!.value,
      details: this.editForm.get(['details'])!.value,
      fromDate: this.editForm.get(['fromDate'])!.value ? dayjs(this.editForm.get(['fromDate'])!.value, DATE_TIME_FORMAT) : undefined,
      toDate: this.editForm.get(['toDate'])!.value ? dayjs(this.editForm.get(['toDate'])!.value, DATE_TIME_FORMAT) : undefined,
      outstandingQuantity: this.editForm.get(['outstandingQuantity'])!.value,
      lineAmount: this.editForm.get(['lineAmount'])!.value,
      product: this.editForm.get(['product'])!.value,
      bill: this.editForm.get(['bill'])!.value,
      rentalOrder: this.editForm.get(['rentalOrder'])!.value,
    };
  }
}
