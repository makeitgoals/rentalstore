import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IItemBalanceByCustomer, ItemBalanceByCustomer } from '../item-balance-by-customer.model';

import { ItemBalanceByCustomerService } from './item-balance-by-customer.service';

describe('Service Tests', () => {
  describe('ItemBalanceByCustomer Service', () => {
    let service: ItemBalanceByCustomerService;
    let httpMock: HttpTestingController;
    let elemDefault: IItemBalanceByCustomer;
    let expectedResult: IItemBalanceByCustomer | IItemBalanceByCustomer[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(ItemBalanceByCustomerService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        outstandingBalance: 0,
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

      it('should create a ItemBalanceByCustomer', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new ItemBalanceByCustomer()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a ItemBalanceByCustomer', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            outstandingBalance: 1,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a ItemBalanceByCustomer', () => {
        const patchObject = Object.assign(
          {
            outstandingBalance: 1,
          },
          new ItemBalanceByCustomer()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of ItemBalanceByCustomer', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            outstandingBalance: 1,
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

      it('should delete a ItemBalanceByCustomer', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addItemBalanceByCustomerToCollectionIfMissing', () => {
        it('should add a ItemBalanceByCustomer to an empty array', () => {
          const itemBalanceByCustomer: IItemBalanceByCustomer = { id: 123 };
          expectedResult = service.addItemBalanceByCustomerToCollectionIfMissing([], itemBalanceByCustomer);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(itemBalanceByCustomer);
        });

        it('should not add a ItemBalanceByCustomer to an array that contains it', () => {
          const itemBalanceByCustomer: IItemBalanceByCustomer = { id: 123 };
          const itemBalanceByCustomerCollection: IItemBalanceByCustomer[] = [
            {
              ...itemBalanceByCustomer,
            },
            { id: 456 },
          ];
          expectedResult = service.addItemBalanceByCustomerToCollectionIfMissing(itemBalanceByCustomerCollection, itemBalanceByCustomer);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a ItemBalanceByCustomer to an array that doesn't contain it", () => {
          const itemBalanceByCustomer: IItemBalanceByCustomer = { id: 123 };
          const itemBalanceByCustomerCollection: IItemBalanceByCustomer[] = [{ id: 456 }];
          expectedResult = service.addItemBalanceByCustomerToCollectionIfMissing(itemBalanceByCustomerCollection, itemBalanceByCustomer);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(itemBalanceByCustomer);
        });

        it('should add only unique ItemBalanceByCustomer to an array', () => {
          const itemBalanceByCustomerArray: IItemBalanceByCustomer[] = [{ id: 123 }, { id: 456 }, { id: 26496 }];
          const itemBalanceByCustomerCollection: IItemBalanceByCustomer[] = [{ id: 123 }];
          expectedResult = service.addItemBalanceByCustomerToCollectionIfMissing(
            itemBalanceByCustomerCollection,
            ...itemBalanceByCustomerArray
          );
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const itemBalanceByCustomer: IItemBalanceByCustomer = { id: 123 };
          const itemBalanceByCustomer2: IItemBalanceByCustomer = { id: 456 };
          expectedResult = service.addItemBalanceByCustomerToCollectionIfMissing([], itemBalanceByCustomer, itemBalanceByCustomer2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(itemBalanceByCustomer);
          expect(expectedResult).toContain(itemBalanceByCustomer2);
        });

        it('should accept null and undefined values', () => {
          const itemBalanceByCustomer: IItemBalanceByCustomer = { id: 123 };
          expectedResult = service.addItemBalanceByCustomerToCollectionIfMissing([], null, itemBalanceByCustomer, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(itemBalanceByCustomer);
        });

        it('should return initial array if no ItemBalanceByCustomer is added', () => {
          const itemBalanceByCustomerCollection: IItemBalanceByCustomer[] = [{ id: 123 }];
          expectedResult = service.addItemBalanceByCustomerToCollectionIfMissing(itemBalanceByCustomerCollection, undefined, null);
          expect(expectedResult).toEqual(itemBalanceByCustomerCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
