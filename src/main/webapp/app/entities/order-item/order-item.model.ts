import { IRentalOrder } from 'app/entities/rental-order/rental-order.model';
import { IProduct } from 'app/entities/product/product.model';
import { IItemBalanceByCustomer } from 'app/entities/item-balance-by-customer/item-balance-by-customer.model';

export interface IOrderItem {
  id?: number;
  quantity?: number;
  rentalOrder?: IRentalOrder;
  product?: IProduct;
  itemBalanceByCustomers?: IItemBalanceByCustomer[] | null;
}

export class OrderItem implements IOrderItem {
  constructor(
    public id?: number,
    public quantity?: number,
    public rentalOrder?: IRentalOrder,
    public product?: IProduct,
    public itemBalanceByCustomers?: IItemBalanceByCustomer[] | null
  ) {}
}

export function getOrderItemIdentifier(orderItem: IOrderItem): number | undefined {
  return orderItem.id;
}
