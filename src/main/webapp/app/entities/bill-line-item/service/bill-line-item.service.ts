import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IBillLineItem, getBillLineItemIdentifier } from '../bill-line-item.model';

export type EntityResponseType = HttpResponse<IBillLineItem>;
export type EntityArrayResponseType = HttpResponse<IBillLineItem[]>;

@Injectable({ providedIn: 'root' })
export class BillLineItemService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/bill-line-items');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(billLineItem: IBillLineItem): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(billLineItem);
    return this.http
      .post<IBillLineItem>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(billLineItem: IBillLineItem): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(billLineItem);
    return this.http
      .put<IBillLineItem>(`${this.resourceUrl}/${getBillLineItemIdentifier(billLineItem) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(billLineItem: IBillLineItem): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(billLineItem);
    return this.http
      .patch<IBillLineItem>(`${this.resourceUrl}/${getBillLineItemIdentifier(billLineItem) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IBillLineItem>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IBillLineItem[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addBillLineItemToCollectionIfMissing(
    billLineItemCollection: IBillLineItem[],
    ...billLineItemsToCheck: (IBillLineItem | null | undefined)[]
  ): IBillLineItem[] {
    const billLineItems: IBillLineItem[] = billLineItemsToCheck.filter(isPresent);
    if (billLineItems.length > 0) {
      const billLineItemCollectionIdentifiers = billLineItemCollection.map(
        billLineItemItem => getBillLineItemIdentifier(billLineItemItem)!
      );
      const billLineItemsToAdd = billLineItems.filter(billLineItemItem => {
        const billLineItemIdentifier = getBillLineItemIdentifier(billLineItemItem);
        if (billLineItemIdentifier == null || billLineItemCollectionIdentifiers.includes(billLineItemIdentifier)) {
          return false;
        }
        billLineItemCollectionIdentifiers.push(billLineItemIdentifier);
        return true;
      });
      return [...billLineItemsToAdd, ...billLineItemCollection];
    }
    return billLineItemCollection;
  }

  protected convertDateFromClient(billLineItem: IBillLineItem): IBillLineItem {
    return Object.assign({}, billLineItem, {
      fromDate: billLineItem.fromDate?.isValid() ? billLineItem.fromDate.toJSON() : undefined,
      toDate: billLineItem.toDate?.isValid() ? billLineItem.toDate.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.fromDate = res.body.fromDate ? dayjs(res.body.fromDate) : undefined;
      res.body.toDate = res.body.toDate ? dayjs(res.body.toDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((billLineItem: IBillLineItem) => {
        billLineItem.fromDate = billLineItem.fromDate ? dayjs(billLineItem.fromDate) : undefined;
        billLineItem.toDate = billLineItem.toDate ? dayjs(billLineItem.toDate) : undefined;
      });
    }
    return res;
  }
}
