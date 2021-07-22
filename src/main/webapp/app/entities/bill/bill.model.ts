import { ICustomer } from 'app/entities/customer/customer.model';
import { BillStatus } from 'app/entities/enumerations/bill-status.model';

export interface IBill {
  id?: number;
  code?: string;
  costBillNumber?: number | null;
  billStatus?: BillStatus;
  billTotal?: number;
  taxPercent?: number | null;
  billTotalWithTax?: number | null;
  customer?: ICustomer;
}

export class Bill implements IBill {
  constructor(
    public id?: number,
    public code?: string,
    public costBillNumber?: number | null,
    public billStatus?: BillStatus,
    public billTotal?: number,
    public taxPercent?: number | null,
    public billTotalWithTax?: number | null,
    public customer?: ICustomer
  ) {}
}

export function getBillIdentifier(bill: IBill): number | undefined {
  return bill.id;
}
