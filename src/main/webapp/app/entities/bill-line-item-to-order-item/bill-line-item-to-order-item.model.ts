import { IOrderItem } from 'app/entities/order-item/order-item.model';
import { IRentalOrder } from 'app/entities/rental-order/rental-order.model';
import { IBillLineItem } from 'app/entities/bill-line-item/bill-line-item.model';

export interface IBillLineItemToOrderItem {
  id?: number;
  details?: string | null;
  orderItem?: IOrderItem;
  rentalOrder?: IRentalOrder;
  billLineItem?: IBillLineItem;
}

export class BillLineItemToOrderItem implements IBillLineItemToOrderItem {
  constructor(
    public id?: number,
    public details?: string | null,
    public orderItem?: IOrderItem,
    public rentalOrder?: IRentalOrder,
    public billLineItem?: IBillLineItem
  ) {}
}

export function getBillLineItemToOrderItemIdentifier(billLineItemToOrderItem: IBillLineItemToOrderItem): number | undefined {
  return billLineItemToOrderItem.id;
}
