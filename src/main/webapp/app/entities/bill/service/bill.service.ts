import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IBill, getBillIdentifier } from '../bill.model';

export type EntityResponseType = HttpResponse<IBill>;
export type EntityArrayResponseType = HttpResponse<IBill[]>;

@Injectable({ providedIn: 'root' })
export class BillService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/bills');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(bill: IBill): Observable<EntityResponseType> {
    return this.http.post<IBill>(this.resourceUrl, bill, { observe: 'response' });
  }

  update(bill: IBill): Observable<EntityResponseType> {
    return this.http.put<IBill>(`${this.resourceUrl}/${getBillIdentifier(bill) as number}`, bill, { observe: 'response' });
  }

  partialUpdate(bill: IBill): Observable<EntityResponseType> {
    return this.http.patch<IBill>(`${this.resourceUrl}/${getBillIdentifier(bill) as number}`, bill, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IBill>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IBill[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addBillToCollectionIfMissing(billCollection: IBill[], ...billsToCheck: (IBill | null | undefined)[]): IBill[] {
    const bills: IBill[] = billsToCheck.filter(isPresent);
    if (bills.length > 0) {
      const billCollectionIdentifiers = billCollection.map(billItem => getBillIdentifier(billItem)!);
      const billsToAdd = bills.filter(billItem => {
        const billIdentifier = getBillIdentifier(billItem);
        if (billIdentifier == null || billCollectionIdentifiers.includes(billIdentifier)) {
          return false;
        }
        billCollectionIdentifiers.push(billIdentifier);
        return true;
      });
      return [...billsToAdd, ...billCollection];
    }
    return billCollection;
  }
}
