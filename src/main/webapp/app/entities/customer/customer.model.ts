import { IItemBalanceByCustomer } from 'app/entities/item-balance-by-customer/item-balance-by-customer.model';
import { IRentalOrder } from 'app/entities/rental-order/rental-order.model';
import { IBill } from 'app/entities/bill/bill.model';
import { IPayment } from 'app/entities/payment/payment.model';

export interface ICustomer {
  id?: number;
  customerName?: string;
  contactName?: string | null;
  fatherName?: string | null;
  ownerName?: string | null;
  siteAddress?: string | null;
  phoneNumber?: string | null;
  email?: string | null;
  itemBalanceByCustomers?: IItemBalanceByCustomer[] | null;
  rentalOrders?: IRentalOrder[] | null;
  bills?: IBill[] | null;
  payments?: IPayment[] | null;
}

export class Customer implements ICustomer {
  constructor(
    public id?: number,
    public customerName?: string,
    public contactName?: string | null,
    public fatherName?: string | null,
    public ownerName?: string | null,
    public siteAddress?: string | null,
    public phoneNumber?: string | null,
    public email?: string | null,
    public itemBalanceByCustomers?: IItemBalanceByCustomer[] | null,
    public rentalOrders?: IRentalOrder[] | null,
    public bills?: IBill[] | null,
    public payments?: IPayment[] | null
  ) {}
}

export function getCustomerIdentifier(customer: ICustomer): number | undefined {
  return customer.id;
}
