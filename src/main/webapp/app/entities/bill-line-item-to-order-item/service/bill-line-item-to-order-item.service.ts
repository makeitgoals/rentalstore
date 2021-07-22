import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IBillLineItemToOrderItem, getBillLineItemToOrderItemIdentifier } from '../bill-line-item-to-order-item.model';

export type EntityResponseType = HttpResponse<IBillLineItemToOrderItem>;
export type EntityArrayResponseType = HttpResponse<IBillLineItemToOrderItem[]>;

@Injectable({ providedIn: 'root' })
export class BillLineItemToOrderItemService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/bill-line-item-to-order-items');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(billLineItemToOrderItem: IBillLineItemToOrderItem): Observable<EntityResponseType> {
    return this.http.post<IBillLineItemToOrderItem>(this.resourceUrl, billLineItemToOrderItem, { observe: 'response' });
  }

  update(billLineItemToOrderItem: IBillLineItemToOrderItem): Observable<EntityResponseType> {
    return this.http.put<IBillLineItemToOrderItem>(
      `${this.resourceUrl}/${getBillLineItemToOrderItemIdentifier(billLineItemToOrderItem) as number}`,
      billLineItemToOrderItem,
      { observe: 'response' }
    );
  }

  partialUpdate(billLineItemToOrderItem: IBillLineItemToOrderItem): Observable<EntityResponseType> {
    return this.http.patch<IBillLineItemToOrderItem>(
      `${this.resourceUrl}/${getBillLineItemToOrderItemIdentifier(billLineItemToOrderItem) as number}`,
      billLineItemToOrderItem,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IBillLineItemToOrderItem>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IBillLineItemToOrderItem[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addBillLineItemToOrderItemToCollectionIfMissing(
    billLineItemToOrderItemCollection: IBillLineItemToOrderItem[],
    ...billLineItemToOrderItemsToCheck: (IBillLineItemToOrderItem | null | undefined)[]
  ): IBillLineItemToOrderItem[] {
    const billLineItemToOrderItems: IBillLineItemToOrderItem[] = billLineItemToOrderItemsToCheck.filter(isPresent);
    if (billLineItemToOrderItems.length > 0) {
      const billLineItemToOrderItemCollectionIdentifiers = billLineItemToOrderItemCollection.map(
        billLineItemToOrderItemItem => getBillLineItemToOrderItemIdentifier(billLineItemToOrderItemItem)!
      );
      const billLineItemToOrderItemsToAdd = billLineItemToOrderItems.filter(billLineItemToOrderItemItem => {
        const billLineItemToOrderItemIdentifier = getBillLineItemToOrderItemIdentifier(billLineItemToOrderItemItem);
        if (
          billLineItemToOrderItemIdentifier == null ||
          billLineItemToOrderItemCollectionIdentifiers.includes(billLineItemToOrderItemIdentifier)
        ) {
          return false;
        }
        billLineItemToOrderItemCollectionIdentifiers.push(billLineItemToOrderItemIdentifier);
        return true;
      });
      return [...billLineItemToOrderItemsToAdd, ...billLineItemToOrderItemCollection];
    }
    return billLineItemToOrderItemCollection;
  }
}
