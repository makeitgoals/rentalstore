import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IItemBalanceByCustomer, ItemBalanceByCustomer } from '../item-balance-by-customer.model';
import { ItemBalanceByCustomerService } from '../service/item-balance-by-customer.service';
import { IProduct } from 'app/entities/product/product.model';
import { ProductService } from 'app/entities/product/service/product.service';
import { IOrderItem } from 'app/entities/order-item/order-item.model';
import { OrderItemService } from 'app/entities/order-item/service/order-item.service';
import { ICustomer } from 'app/entities/customer/customer.model';
import { CustomerService } from 'app/entities/customer/service/customer.service';

@Component({
  selector: 'jhi-item-balance-by-customer-update',
  templateUrl: './item-balance-by-customer-update.component.html',
})
export class ItemBalanceByCustomerUpdateComponent implements OnInit {
  isSaving = false;

  productsSharedCollection: IProduct[] = [];
  orderItemsSharedCollection: IOrderItem[] = [];
  customersSharedCollection: ICustomer[] = [];

  editForm = this.fb.group({
    id: [],
    outstandingBalance: [null, [Validators.required, Validators.min(0)]],
    product: [null, Validators.required],
    orderItem: [null, Validators.required],
    customer: [null, Validators.required],
  });

  constructor(
    protected itemBalanceByCustomerService: ItemBalanceByCustomerService,
    protected productService: ProductService,
    protected orderItemService: OrderItemService,
    protected customerService: CustomerService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ itemBalanceByCustomer }) => {
      this.updateForm(itemBalanceByCustomer);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const itemBalanceByCustomer = this.createFromForm();
    if (itemBalanceByCustomer.id !== undefined) {
      this.subscribeToSaveResponse(this.itemBalanceByCustomerService.update(itemBalanceByCustomer));
    } else {
      this.subscribeToSaveResponse(this.itemBalanceByCustomerService.create(itemBalanceByCustomer));
    }
  }

  trackProductById(index: number, item: IProduct): number {
    return item.id!;
  }

  trackOrderItemById(index: number, item: IOrderItem): number {
    return item.id!;
  }

  trackCustomerById(index: number, item: ICustomer): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IItemBalanceByCustomer>>): void {
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

  protected updateForm(itemBalanceByCustomer: IItemBalanceByCustomer): void {
    this.editForm.patchValue({
      id: itemBalanceByCustomer.id,
      outstandingBalance: itemBalanceByCustomer.outstandingBalance,
      product: itemBalanceByCustomer.product,
      orderItem: itemBalanceByCustomer.orderItem,
      customer: itemBalanceByCustomer.customer,
    });

    this.productsSharedCollection = this.productService.addProductToCollectionIfMissing(
      this.productsSharedCollection,
      itemBalanceByCustomer.product
    );
    this.orderItemsSharedCollection = this.orderItemService.addOrderItemToCollectionIfMissing(
      this.orderItemsSharedCollection,
      itemBalanceByCustomer.orderItem
    );
    this.customersSharedCollection = this.customerService.addCustomerToCollectionIfMissing(
      this.customersSharedCollection,
      itemBalanceByCustomer.customer
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

    this.orderItemService
      .query()
      .pipe(map((res: HttpResponse<IOrderItem[]>) => res.body ?? []))
      .pipe(
        map((orderItems: IOrderItem[]) =>
          this.orderItemService.addOrderItemToCollectionIfMissing(orderItems, this.editForm.get('orderItem')!.value)
        )
      )
      .subscribe((orderItems: IOrderItem[]) => (this.orderItemsSharedCollection = orderItems));

    this.customerService
      .query()
      .pipe(map((res: HttpResponse<ICustomer[]>) => res.body ?? []))
      .pipe(
        map((customers: ICustomer[]) =>
          this.customerService.addCustomerToCollectionIfMissing(customers, this.editForm.get('customer')!.value)
        )
      )
      .subscribe((customers: ICustomer[]) => (this.customersSharedCollection = customers));
  }

  protected createFromForm(): IItemBalanceByCustomer {
    return {
      ...new ItemBalanceByCustomer(),
      id: this.editForm.get(['id'])!.value,
      outstandingBalance: this.editForm.get(['outstandingBalance'])!.value,
      product: this.editForm.get(['product'])!.value,
      orderItem: this.editForm.get(['orderItem'])!.value,
      customer: this.editForm.get(['customer'])!.value,
    };
  }
}
