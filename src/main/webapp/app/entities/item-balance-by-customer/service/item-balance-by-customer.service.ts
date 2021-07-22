import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IItemBalanceByCustomer, getItemBalanceByCustomerIdentifier } from '../item-balance-by-customer.model';

export type EntityResponseType = HttpResponse<IItemBalanceByCustomer>;
export type EntityArrayResponseType = HttpResponse<IItemBalanceByCustomer[]>;

@Injectable({ providedIn: 'root' })
export class ItemBalanceByCustomerService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/item-balance-by-customers');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(itemBalanceByCustomer: IItemBalanceByCustomer): Observable<EntityResponseType> {
    return this.http.post<IItemBalanceByCustomer>(this.resourceUrl, itemBalanceByCustomer, { observe: 'response' });
  }

  update(itemBalanceByCustomer: IItemBalanceByCustomer): Observable<EntityResponseType> {
    return this.http.put<IItemBalanceByCustomer>(
      `${this.resourceUrl}/${getItemBalanceByCustomerIdentifier(itemBalanceByCustomer) as number}`,
      itemBalanceByCustomer,
      { observe: 'response' }
    );
  }

  partialUpdate(itemBalanceByCustomer: IItemBalanceByCustomer): Observable<EntityResponseType> {
    return this.http.patch<IItemBalanceByCustomer>(
      `${this.resourceUrl}/${getItemBalanceByCustomerIdentifier(itemBalanceByCustomer) as number}`,
      itemBalanceByCustomer,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IItemBalanceByCustomer>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IItemBalanceByCustomer[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addItemBalanceByCustomerToCollectionIfMissing(
    itemBalanceByCustomerCollection: IItemBalanceByCustomer[],
    ...itemBalanceByCustomersToCheck: (IItemBalanceByCustomer | null | undefined)[]
  ): IItemBalanceByCustomer[] {
    const itemBalanceByCustomers: IItemBalanceByCustomer[] = itemBalanceByCustomersToCheck.filter(isPresent);
    if (itemBalanceByCustomers.length > 0) {
      const itemBalanceByCustomerCollectionIdentifiers = itemBalanceByCustomerCollection.map(
        itemBalanceByCustomerItem => getItemBalanceByCustomerIdentifier(itemBalanceByCustomerItem)!
      );
      const itemBalanceByCustomersToAdd = itemBalanceByCustomers.filter(itemBalanceByCustomerItem => {
        const itemBalanceByCustomerIdentifier = getItemBalanceByCustomerIdentifier(itemBalanceByCustomerItem);
        if (
          itemBalanceByCustomerIdentifier == null ||
          itemBalanceByCustomerCollectionIdentifiers.includes(itemBalanceByCustomerIdentifier)
        ) {
          return false;
        }
        itemBalanceByCustomerCollectionIdentifiers.push(itemBalanceByCustomerIdentifier);
        return true;
      });
      return [...itemBalanceByCustomersToAdd, ...itemBalanceByCustomerCollection];
    }
    return itemBalanceByCustomerCollection;
  }
}
