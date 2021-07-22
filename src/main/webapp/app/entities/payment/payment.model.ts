import * as dayjs from 'dayjs';
import { ICustomer } from 'app/entities/customer/customer.model';
import { PaymentMethod } from 'app/entities/enumerations/payment-method.model';

export interface IPayment {
  id?: number;
  paymentAmount?: number;
  paymentDate?: dayjs.Dayjs;
  paymentDetails?: string | null;
  paymentMethod?: PaymentMethod;
  customer?: ICustomer | null;
}

export class Payment implements IPayment {
  constructor(
    public id?: number,
    public paymentAmount?: number,
    public paymentDate?: dayjs.Dayjs,
    public paymentDetails?: string | null,
    public paymentMethod?: PaymentMethod,
    public customer?: ICustomer | null
  ) {}
}

export function getPaymentIdentifier(payment: IPayment): number | undefined {
  return payment.id;
}
