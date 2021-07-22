import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ICustomer, Customer } from '../customer.model';
import { CustomerService } from '../service/customer.service';

@Component({
  selector: 'jhi-customer-update',
  templateUrl: './customer-update.component.html',
})
export class CustomerUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    customerName: [null, [Validators.required]],
    contactName: [],
    fatherName: [],
    ownerName: [],
    siteAddress: [],
    phoneNumber: [],
    email: [null, [Validators.pattern('^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$')]],
  });

  constructor(protected customerService: CustomerService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ customer }) => {
      this.updateForm(customer);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const customer = this.createFromForm();
    if (customer.id !== undefined) {
      this.subscribeToSaveResponse(this.customerService.update(customer));
    } else {
      this.subscribeToSaveResponse(this.customerService.create(customer));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICustomer>>): void {
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

  protected updateForm(customer: ICustomer): void {
    this.editForm.patchValue({
      id: customer.id,
      customerName: customer.customerName,
      contactName: customer.contactName,
      fatherName: customer.fatherName,
      ownerName: customer.ownerName,
      siteAddress: customer.siteAddress,
      phoneNumber: customer.phoneNumber,
      email: customer.email,
    });
  }

  protected createFromForm(): ICustomer {
    return {
      ...new Customer(),
      id: this.editForm.get(['id'])!.value,
      customerName: this.editForm.get(['customerName'])!.value,
      contactName: this.editForm.get(['contactName'])!.value,
      fatherName: this.editForm.get(['fatherName'])!.value,
      ownerName: this.editForm.get(['ownerName'])!.value,
      siteAddress: this.editForm.get(['siteAddress'])!.value,
      phoneNumber: this.editForm.get(['phoneNumber'])!.value,
      email: this.editForm.get(['email'])!.value,
    };
  }
}
