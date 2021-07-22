import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IBill, Bill } from '../bill.model';
import { BillService } from '../service/bill.service';
import { ICustomer } from 'app/entities/customer/customer.model';
import { CustomerService } from 'app/entities/customer/service/customer.service';

@Component({
  selector: 'jhi-bill-update',
  templateUrl: './bill-update.component.html',
})
export class BillUpdateComponent implements OnInit {
  isSaving = false;

  customersSharedCollection: ICustomer[] = [];

  editForm = this.fb.group({
    id: [],
    code: [null, [Validators.required]],
    costBillNumber: [],
    billStatus: [null, [Validators.required]],
    billTotal: [null, [Validators.required, Validators.min(0)]],
    taxPercent: [null, [Validators.min(0)]],
    billTotalWithTax: [null, [Validators.min(0)]],
    customer: [],
  });

  constructor(
    protected billService: BillService,
    protected customerService: CustomerService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ bill }) => {
      this.updateForm(bill);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const bill = this.createFromForm();
    if (bill.id !== undefined) {
      this.subscribeToSaveResponse(this.billService.update(bill));
    } else {
      this.subscribeToSaveResponse(this.billService.create(bill));
    }
  }

  trackCustomerById(index: number, item: ICustomer): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBill>>): void {
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

  protected updateForm(bill: IBill): void {
    this.editForm.patchValue({
      id: bill.id,
      code: bill.code,
      costBillNumber: bill.costBillNumber,
      billStatus: bill.billStatus,
      billTotal: bill.billTotal,
      taxPercent: bill.taxPercent,
      billTotalWithTax: bill.billTotalWithTax,
      customer: bill.customer,
    });

    this.customersSharedCollection = this.customerService.addCustomerToCollectionIfMissing(this.customersSharedCollection, bill.customer);
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

  protected createFromForm(): IBill {
    return {
      ...new Bill(),
      id: this.editForm.get(['id'])!.value,
      code: this.editForm.get(['code'])!.value,
      costBillNumber: this.editForm.get(['costBillNumber'])!.value,
      billStatus: this.editForm.get(['billStatus'])!.value,
      billTotal: this.editForm.get(['billTotal'])!.value,
      taxPercent: this.editForm.get(['taxPercent'])!.value,
      billTotalWithTax: this.editForm.get(['billTotalWithTax'])!.value,
      customer: this.editForm.get(['customer'])!.value,
    };
  }
}
