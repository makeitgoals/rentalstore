import { IProduct } from 'app/entities/product/product.model';
import { IOrderItem } from 'app/entities/order-item/order-item.model';
import { ICustomer } from 'app/entities/customer/customer.model';

export interface IItemBalanceByCustomer {
  id?: number;
  outstandingBalance?: number;
  product?: IProduct | null;
  orderItem?: IOrderItem | null;
  customer?: ICustomer | null;
}

export class ItemBalanceByCustomer implements IItemBalanceByCustomer {
  constructor(
    public id?: number,
    public outstandingBalance?: number,
    public product?: IProduct | null,
    public orderItem?: IOrderItem | null,
    public customer?: ICustomer | null
  ) {}
}

export function getItemBalanceByCustomerIdentifier(itemBalanceByCustomer: IItemBalanceByCustomer): number | undefined {
  return itemBalanceByCustomer.id;
}
