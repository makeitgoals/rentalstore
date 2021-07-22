import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IRentalOrder, getRentalOrderIdentifier } from '../rental-order.model';

export type EntityResponseType = HttpResponse<IRentalOrder>;
export type EntityArrayResponseType = HttpResponse<IRentalOrder[]>;

@Injectable({ providedIn: 'root' })
export class RentalOrderService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/rental-orders');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(rentalOrder: IRentalOrder): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(rentalOrder);
    return this.http
      .post<IRentalOrder>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(rentalOrder: IRentalOrder): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(rentalOrder);
    return this.http
      .put<IRentalOrder>(`${this.resourceUrl}/${getRentalOrderIdentifier(rentalOrder) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(rentalOrder: IRentalOrder): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(rentalOrder);
    return this.http
      .patch<IRentalOrder>(`${this.resourceUrl}/${getRentalOrderIdentifier(rentalOrder) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IRentalOrder>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IRentalOrder[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addRentalOrderToCollectionIfMissing(
    rentalOrderCollection: IRentalOrder[],
    ...rentalOrdersToCheck: (IRentalOrder | null | undefined)[]
  ): IRentalOrder[] {
    const rentalOrders: IRentalOrder[] = rentalOrdersToCheck.filter(isPresent);
    if (rentalOrders.length > 0) {
      const rentalOrderCollectionIdentifiers = rentalOrderCollection.map(rentalOrderItem => getRentalOrderIdentifier(rentalOrderItem)!);
      const rentalOrdersToAdd = rentalOrders.filter(rentalOrderItem => {
        const rentalOrderIdentifier = getRentalOrderIdentifier(rentalOrderItem);
        if (rentalOrderIdentifier == null || rentalOrderCollectionIdentifiers.includes(rentalOrderIdentifier)) {
          return false;
        }
        rentalOrderCollectionIdentifiers.push(rentalOrderIdentifier);
        return true;
      });
      return [...rentalOrdersToAdd, ...rentalOrderCollection];
    }
    return rentalOrderCollection;
  }

  protected convertDateFromClient(rentalOrder: IRentalOrder): IRentalOrder {
    return Object.assign({}, rentalOrder, {
      rentalIssueDate: rentalOrder.rentalIssueDate?.isValid() ? rentalOrder.rentalIssueDate.toJSON() : undefined,
      rentalReturnDate: rentalOrder.rentalReturnDate?.isValid() ? rentalOrder.rentalReturnDate.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.rentalIssueDate = res.body.rentalIssueDate ? dayjs(res.body.rentalIssueDate) : undefined;
      res.body.rentalReturnDate = res.body.rentalReturnDate ? dayjs(res.body.rentalReturnDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((rentalOrder: IRentalOrder) => {
        rentalOrder.rentalIssueDate = rentalOrder.rentalIssueDate ? dayjs(rentalOrder.rentalIssueDate) : undefined;
        rentalOrder.rentalReturnDate = rentalOrder.rentalReturnDate ? dayjs(rentalOrder.rentalReturnDate) : undefined;
      });
    }
    return res;
  }
}
