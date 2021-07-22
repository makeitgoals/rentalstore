import * as dayjs from 'dayjs';
import { ICustomer } from 'app/entities/customer/customer.model';
import { IBillLineItem } from 'app/entities/bill-line-item/bill-line-item.model';
import { IOrderItem } from 'app/entities/order-item/order-item.model';
import { OrderStatus } from 'app/entities/enumerations/order-status.model';

export interface IRentalOrder {
  id?: number;
  rentalIssueDate?: dayjs.Dayjs | null;
  rentalReturnDate?: dayjs.Dayjs | null;
  rentalOrderStatus?: OrderStatus;
  code?: string;
  customer?: ICustomer;
  billLineItems?: IBillLineItem[] | null;
  orderItems?: IOrderItem[] | null;
}

export class RentalOrder implements IRentalOrder {
  constructor(
    public id?: number,
    public rentalIssueDate?: dayjs.Dayjs | null,
    public rentalReturnDate?: dayjs.Dayjs | null,
    public rentalOrderStatus?: OrderStatus,
    public code?: string,
    public customer?: ICustomer,
    public billLineItems?: IBillLineItem[] | null,
    public orderItems?: IOrderItem[] | null
  ) {}
}

export function getRentalOrderIdentifier(rentalOrder: IRentalOrder): number | undefined {
  return rentalOrder.id;
}
