import { IOrderItem } from 'app/entities/order-item/order-item.model';
import { IRentalOrder } from 'app/entities/rental-order/rental-order.model';
import { IBillLineItem } from 'app/entities/bill-line-item/bill-line-item.model';

export interface IBillLineItemToOrderItem {
  id?: number;
  details?: string | null;
  orderItem?: IOrderItem | null;
  rentalOrder?: IRentalOrder | null;
  billLineItem?: IBillLineItem | null;
}

export class BillLineItemToOrderItem implements IBillLineItemToOrderItem {
  constructor(
    public id?: number,
    public details?: string | null,
    public orderItem?: IOrderItem | null,
    public rentalOrder?: IRentalOrder | null,
    public billLineItem?: IBillLineItem | null
  ) {}
}

export function getBillLineItemToOrderItemIdentifier(billLineItemToOrderItem: IBillLineItemToOrderItem): number | undefined {
  return billLineItemToOrderItem.id;
}
