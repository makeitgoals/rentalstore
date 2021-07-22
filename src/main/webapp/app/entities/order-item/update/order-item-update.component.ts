import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IOrderItem, OrderItem } from '../order-item.model';
import { OrderItemService } from '../service/order-item.service';
import { IRentalOrder } from 'app/entities/rental-order/rental-order.model';
import { RentalOrderService } from 'app/entities/rental-order/service/rental-order.service';
import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';

@Component({
  selector: 'jhi-order-item-update',
  templateUrl: './order-item-update.component.html',
})
export class OrderItemUpdateComponent implements OnInit {
  isSaving = false;

  rentalOrdersSharedCollection: IRentalOrder[] = [];
  productsSharedCollection: IProduct[] = [];

  editForm = this.fb.group({
    id: [],
    quantity: [null, [Validators.required, Validators.min(0)]],
    rentalOrder: [null, Validators.required],
    product: [null, Validators.required],
  });

  constructor(
    protected orderItemService: OrderItemService,
    protected rentalOrderService: RentalOrderService,
    protected productService: ProductService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ orderItem }) => {
      this.updateForm(orderItem);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const orderItem = this.createFromForm();
    if (orderItem.id !== undefined) {
      this.subscribeToSaveResponse(this.orderItemService.update(orderItem));
    } else {
      this.subscribeToSaveResponse(this.orderItemService.create(orderItem));
    }
  }

  trackRentalOrderById(index: number, item: IRentalOrder): number {
    return item.id!;
  }

  trackProductById(index: number, item: IProduct): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IOrderItem>>): void {
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

  protected updateForm(orderItem: IOrderItem): void {
    this.editForm.patchValue({
      id: orderItem.id,
      quantity: orderItem.quantity,
      rentalOrder: orderItem.rentalOrder,
      product: orderItem.product,
    });

    this.rentalOrdersSharedCollection = this.rentalOrderService.addRentalOrderToCollectionIfMissing(
      this.rentalOrdersSharedCollection,
      orderItem.rentalOrder
    );
    this.productsSharedCollection = this.productService.addProductToCollectionIfMissing(this.productsSharedCollection, orderItem.product);
  }

  protected loadRelationshipsOptions(): void {
    this.rentalOrderService
      .query()
      .pipe(map((res: HttpResponse<IRentalOrder[]>) => res.body ?? []))
      .pipe(
        map((rentalOrders: IRentalOrder[]) =>
          this.rentalOrderService.addRentalOrderToCollectionIfMissing(rentalOrders, this.editForm.get('rentalOrder')!.value)
        )
      )
      .subscribe((rentalOrders: IRentalOrder[]) => (this.rentalOrdersSharedCollection = rentalOrders));

    this.productService
      .query()
      .pipe(map((res: HttpResponse<IProduct[]>) => res.body ?? []))
      .pipe(
        map((products: IProduct[]) => this.productService.addProductToCollectionIfMissing(products, this.editForm.get('product')!.value))
      )
      .subscribe((products: IProduct[]) => (this.productsSharedCollection = products));
  }

  protected createFromForm(): IOrderItem {
    return {
      ...new OrderItem(),
      id: this.editForm.get(['id'])!.value,
      quantity: this.editForm.get(['quantity'])!.value,
      rentalOrder: this.editForm.get(['rentalOrder'])!.value,
      product: this.editForm.get(['product'])!.value,
    };
  }
}
