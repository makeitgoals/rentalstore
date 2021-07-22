import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IRentalOrder, RentalOrder } from '../rental-order.model';
import { RentalOrderService } from '../service/rental-order.service';
import { ICustomer } from 'app/entities/customer/customer.model';
import { CustomerService } from 'app/entities/customer/service/customer.service';

@Component({
  selector: 'jhi-rental-order-update',
  templateUrl: './rental-order-update.component.html',
})
export class RentalOrderUpdateComponent implements OnInit {
  isSaving = false;

  customersSharedCollection: ICustomer[] = [];

  editForm = this.fb.group({
    id: [],
    rentalIssueDate: [],
    rentalReturnDate: [],
    rentalOrderStatus: [null, [Validators.required]],
    code: [null, [Validators.required]],
    customer: [null, Validators.required],
  });

  constructor(
    protected rentalOrderService: RentalOrderService,
    protected customerService: CustomerService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ rentalOrder }) => {
      if (rentalOrder.id === undefined) {
        const today = dayjs().startOf('day');
        rentalOrder.rentalIssueDate = today;
        rentalOrder.rentalReturnDate = today;
      }

      this.updateForm(rentalOrder);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const rentalOrder = this.createFromForm();
    if (rentalOrder.id !== undefined) {
      this.subscribeToSaveResponse(this.rentalOrderService.update(rentalOrder));
    } else {
      this.subscribeToSaveResponse(this.rentalOrderService.create(rentalOrder));
    }
  }

  trackCustomerById(index: number, item: ICustomer): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRentalOrder>>): void {
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

  protected updateForm(rentalOrder: IRentalOrder): void {
    this.editForm.patchValue({
      id: rentalOrder.id,
      rentalIssueDate: rentalOrder.rentalIssueDate ? rentalOrder.rentalIssueDate.format(DATE_TIME_FORMAT) : null,
      rentalReturnDate: rentalOrder.rentalReturnDate ? rentalOrder.rentalReturnDate.format(DATE_TIME_FORMAT) : null,
      rentalOrderStatus: rentalOrder.rentalOrderStatus,
      code: rentalOrder.code,
      customer: rentalOrder.customer,
    });

    this.customersSharedCollection = this.customerService.addCustomerToCollectionIfMissing(
      this.customersSharedCollection,
      rentalOrder.customer
    );
  }

  protected loadRelationshipsOptions(): void {
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

  protected createFromForm(): IRentalOrder {
    return {
      ...new RentalOrder(),
      id: this.editForm.get(['id'])!.value,
      rentalIssueDate: this.editForm.get(['rentalIssueDate'])!.value
        ? dayjs(this.editForm.get(['rentalIssueDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      rentalReturnDate: this.editForm.get(['rentalReturnDate'])!.value
        ? dayjs(this.editForm.get(['rentalReturnDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      rentalOrderStatus: this.editForm.get(['rentalOrderStatus'])!.value,
      code: this.editForm.get(['code'])!.value,
      customer: this.editForm.get(['customer'])!.value,
    };
  }
}
