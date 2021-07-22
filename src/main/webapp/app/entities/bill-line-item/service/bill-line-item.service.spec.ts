import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IBillLineItem, BillLineItem } from '../bill-line-item.model';

import { BillLineItemService } from './bill-line-item.service';

describe('Service Tests', () => {
  describe('BillLineItem Service', () => {
    let service: BillLineItemService;
    let httpMock: HttpTestingController;
    let elemDefault: IBillLineItem;
    let expectedResult: IBillLineItem | IBillLineItem[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(BillLineItemService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        details: 'AAAAAAA',
        fromDate: currentDate,
        toDate: currentDate,
        outstandingQuantity: 0,
        lineAmount: 0,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            fromDate: currentDate.format(DATE_TIME_FORMAT),
            toDate: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a BillLineItem', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            fromDate: currentDate.format(DATE_TIME_FORMAT),
            toDate: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            fromDate: currentDate,
            toDate: currentDate,
          },
          returnedFromService
        );

        service.create(new BillLineItem()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a BillLineItem', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            details: 'BBBBBB',
            fromDate: currentDate.format(DATE_TIME_FORMAT),
            toDate: currentDate.format(DATE_TIME_FORMAT),
            outstandingQuantity: 1,
            lineAmount: 1,
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            fromDate: currentDate,
            toDate: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a BillLineItem', () => {
        const patchObject = Object.assign(
          {
            details: 'BBBBBB',
            fromDate: currentDate.format(DATE_TIME_FORMAT),
          },
          new BillLineItem()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            fromDate: currentDate,
            toDate: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of BillLineItem', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            details: 'BBBBBB',
            fromDate: currentDate.format(DATE_TIME_FORMAT),
            toDate: currentDate.format(DATE_TIME_FORMAT),
            outstandingQuantity: 1,
            lineAmount: 1,
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            fromDate: currentDate,
            toDate: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a BillLineItem', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addBillLineItemToCollectionIfMissing', () => {
        it('should add a BillLineItem to an empty array', () => {
          const billLineItem: IBillLineItem = { id: 123 };
          expectedResult = service.addBillLineItemToCollectionIfMissing([], billLineItem);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(billLineItem);
        });

        it('should not add a BillLineItem to an array that contains it', () => {
          const billLineItem: IBillLineItem = { id: 123 };
          const billLineItemCollection: IBillLineItem[] = [
            {
              ...billLineItem,
            },
            { id: 456 },
          ];
          expectedResult = service.addBillLineItemToCollectionIfMissing(billLineItemCollection, billLineItem);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a BillLineItem to an array that doesn't contain it", () => {
          const billLineItem: IBillLineItem = { id: 123 };
          const billLineItemCollection: IBillLineItem[] = [{ id: 456 }];
          expectedResult = service.addBillLineItemToCollectionIfMissing(billLineItemCollection, billLineItem);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(billLineItem);
        });

        it('should add only unique BillLineItem to an array', () => {
          const billLineItemArray: IBillLineItem[] = [{ id: 123 }, { id: 456 }, { id: 88568 }];
          const billLineItemCollection: IBillLineItem[] = [{ id: 123 }];
          expectedResult = service.addBillLineItemToCollectionIfMissing(billLineItemCollection, ...billLineItemArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const billLineItem: IBillLineItem = { id: 123 };
          const billLineItem2: IBillLineItem = { id: 456 };
          expectedResult = service.addBillLineItemToCollectionIfMissing([], billLineItem, billLineItem2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(billLineItem);
          expect(expectedResult).toContain(billLineItem2);
        });

        it('should accept null and undefined values', () => {
          const billLineItem: IBillLineItem = { id: 123 };
          expectedResult = service.addBillLineItemToCollectionIfMissing([], null, billLineItem, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(billLineItem);
        });

        it('should return initial array if no BillLineItem is added', () => {
          const billLineItemCollection: IBillLineItem[] = [{ id: 123 }];
          expectedResult = service.addBillLineItemToCollectionIfMissing(billLineItemCollection, undefined, null);
          expect(expectedResult).toEqual(billLineItemCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
