import * as dayjs from 'dayjs';
import { IProduct } from 'app/entities/product/product.model';
import { IBill } from 'app/entities/bill/bill.model';
import { IRentalOrder } from 'app/entities/rental-order/rental-order.model';

export interface IBillLineItem {
  id?: number;
  details?: string | null;
  fromDate?: dayjs.Dayjs;
  toDate?: dayjs.Dayjs;
  outstandingQuantity?: number;
  lineAmount?: number;
  product?: IProduct | null;
  bill?: IBill | null;
  rentalOrder?: IRentalOrder | null;
}

export class BillLineItem implements IBillLineItem {
  constructor(
    public id?: number,
    public details?: string | null,
    public fromDate?: dayjs.Dayjs,
    public toDate?: dayjs.Dayjs,
    public outstandingQuantity?: number,
    public lineAmount?: number,
    public product?: IProduct | null,
    public bill?: IBill | null,
    public rentalOrder?: IRentalOrder | null
  ) {}
}

export function getBillLineItemIdentifier(billLineItem: IBillLineItem): number | undefined {
  return billLineItem.id;
}
