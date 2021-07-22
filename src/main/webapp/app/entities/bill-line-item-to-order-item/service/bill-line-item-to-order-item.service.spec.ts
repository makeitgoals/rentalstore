import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IBillLineItemToOrderItem, BillLineItemToOrderItem } from '../bill-line-item-to-order-item.model';

import { BillLineItemToOrderItemService } from './bill-line-item-to-order-item.service';

describe('Service Tests', () => {
  describe('BillLineItemToOrderItem Service', () => {
    let service: BillLineItemToOrderItemService;
    let httpMock: HttpTestingController;
    let elemDefault: IBillLineItemToOrderItem;
    let expectedResult: IBillLineItemToOrderItem | IBillLineItemToOrderItem[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(BillLineItemToOrderItemService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        details: 'AAAAAAA',
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a BillLineItemToOrderItem', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new BillLineItemToOrderItem()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a BillLineItemToOrderItem', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            details: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a BillLineItemToOrderItem', () => {
        const patchObject = Object.assign(
          {
            details: 'BBBBBB',
          },
          new BillLineItemToOrderItem()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of BillLineItemToOrderItem', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            details: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a BillLineItemToOrderItem', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addBillLineItemToOrderItemToCollectionIfMissing', () => {
        it('should add a BillLineItemToOrderItem to an empty array', () => {
          const billLineItemToOrderItem: IBillLineItemToOrderItem = { id: 123 };
          expectedResult = service.addBillLineItemToOrderItemToCollectionIfMissing([], billLineItemToOrderItem);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(billLineItemToOrderItem);
        });

        it('should not add a BillLineItemToOrderItem to an array that contains it', () => {
          const billLineItemToOrderItem: IBillLineItemToOrderItem = { id: 123 };
          const billLineItemToOrderItemCollection: IBillLineItemToOrderItem[] = [
            {
              ...billLineItemToOrderItem,
            },
            { id: 456 },
          ];
          expectedResult = service.addBillLineItemToOrderItemToCollectionIfMissing(
            billLineItemToOrderItemCollection,
            billLineItemToOrderItem
          );
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a BillLineItemToOrderItem to an array that doesn't contain it", () => {
          const billLineItemToOrderItem: IBillLineItemToOrderItem = { id: 123 };
          const billLineItemToOrderItemCollection: IBillLineItemToOrderItem[] = [{ id: 456 }];
          expectedResult = service.addBillLineItemToOrderItemToCollectionIfMissing(
            billLineItemToOrderItemCollection,
            billLineItemToOrderItem
          );
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(billLineItemToOrderItem);
        });

        it('should add only unique BillLineItemToOrderItem to an array', () => {
          const billLineItemToOrderItemArray: IBillLineItemToOrderItem[] = [{ id: 123 }, { id: 456 }, { id: 99017 }];
          const billLineItemToOrderItemCollection: IBillLineItemToOrderItem[] = [{ id: 123 }];
          expectedResult = service.addBillLineItemToOrderItemToCollectionIfMissing(
            billLineItemToOrderItemCollection,
            ...billLineItemToOrderItemArray
          );
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const billLineItemToOrderItem: IBillLineItemToOrderItem = { id: 123 };
          const billLineItemToOrderItem2: IBillLineItemToOrderItem = { id: 456 };
          expectedResult = service.addBillLineItemToOrderItemToCollectionIfMissing([], billLineItemToOrderItem, billLineItemToOrderItem2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(billLineItemToOrderItem);
          expect(expectedResult).toContain(billLineItemToOrderItem2);
        });

        it('should accept null and undefined values', () => {
          const billLineItemToOrderItem: IBillLineItemToOrderItem = { id: 123 };
          expectedResult = service.addBillLineItemToOrderItemToCollectionIfMissing([], null, billLineItemToOrderItem, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(billLineItemToOrderItem);
        });

        it('should return initial array if no BillLineItemToOrderItem is added', () => {
          const billLineItemToOrderItemCollection: IBillLineItemToOrderItem[] = [{ id: 123 }];
          expectedResult = service.addBillLineItemToOrderItemToCollectionIfMissing(billLineItemToOrderItemCollection, undefined, null);
          expect(expectedResult).toEqual(billLineItemToOrderItemCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
