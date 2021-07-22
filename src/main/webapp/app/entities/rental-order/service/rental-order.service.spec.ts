import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { OrderStatus } from 'app/entities/enumerations/order-status.model';
import { IRentalOrder, RentalOrder } from '../rental-order.model';

import { RentalOrderService } from './rental-order.service';

describe('Service Tests', () => {
  describe('RentalOrder Service', () => {
    let service: RentalOrderService;
    let httpMock: HttpTestingController;
    let elemDefault: IRentalOrder;
    let expectedResult: IRentalOrder | IRentalOrder[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(RentalOrderService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        rentalIssueDate: currentDate,
        rentalReturnDate: currentDate,
        rentalOrderStatus: OrderStatus.COMPLETED,
        code: 'AAAAAAA',
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            rentalIssueDate: currentDate.format(DATE_TIME_FORMAT),
            rentalReturnDate: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a RentalOrder', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            rentalIssueDate: currentDate.format(DATE_TIME_FORMAT),
            rentalReturnDate: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            rentalIssueDate: currentDate,
            rentalReturnDate: currentDate,
          },
          returnedFromService
        );

        service.create(new RentalOrder()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a RentalOrder', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            rentalIssueDate: currentDate.format(DATE_TIME_FORMAT),
            rentalReturnDate: currentDate.format(DATE_TIME_FORMAT),
            rentalOrderStatus: 'BBBBBB',
            code: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            rentalIssueDate: currentDate,
            rentalReturnDate: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a RentalOrder', () => {
        const patchObject = Object.assign({}, new RentalOrder());

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            rentalIssueDate: currentDate,
            rentalReturnDate: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of RentalOrder', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            rentalIssueDate: currentDate.format(DATE_TIME_FORMAT),
            rentalReturnDate: currentDate.format(DATE_TIME_FORMAT),
            rentalOrderStatus: 'BBBBBB',
            code: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            rentalIssueDate: currentDate,
            rentalReturnDate: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a RentalOrder', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addRentalOrderToCollectionIfMissing', () => {
        it('should add a RentalOrder to an empty array', () => {
          const rentalOrder: IRentalOrder = { id: 123 };
          expectedResult = service.addRentalOrderToCollectionIfMissing([], rentalOrder);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(rentalOrder);
        });

        it('should not add a RentalOrder to an array that contains it', () => {
          const rentalOrder: IRentalOrder = { id: 123 };
          const rentalOrderCollection: IRentalOrder[] = [
            {
              ...rentalOrder,
            },
            { id: 456 },
          ];
          expectedResult = service.addRentalOrderToCollectionIfMissing(rentalOrderCollection, rentalOrder);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a RentalOrder to an array that doesn't contain it", () => {
          const rentalOrder: IRentalOrder = { id: 123 };
          const rentalOrderCollection: IRentalOrder[] = [{ id: 456 }];
          expectedResult = service.addRentalOrderToCollectionIfMissing(rentalOrderCollection, rentalOrder);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(rentalOrder);
        });

        it('should add only unique RentalOrder to an array', () => {
          const rentalOrderArray: IRentalOrder[] = [{ id: 123 }, { id: 456 }, { id: 94796 }];
          const rentalOrderCollection: IRentalOrder[] = [{ id: 123 }];
          expectedResult = service.addRentalOrderToCollectionIfMissing(rentalOrderCollection, ...rentalOrderArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const rentalOrder: IRentalOrder = { id: 123 };
          const rentalOrder2: IRentalOrder = { id: 456 };
          expectedResult = service.addRentalOrderToCollectionIfMissing([], rentalOrder, rentalOrder2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(rentalOrder);
          expect(expectedResult).toContain(rentalOrder2);
        });

        it('should accept null and undefined values', () => {
          const rentalOrder: IRentalOrder = { id: 123 };
          expectedResult = service.addRentalOrderToCollectionIfMissing([], null, rentalOrder, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(rentalOrder);
        });

        it('should return initial array if no RentalOrder is added', () => {
          const rentalOrderCollection: IRentalOrder[] = [{ id: 123 }];
          expectedResult = service.addRentalOrderToCollectionIfMissing(rentalOrderCollection, undefined, null);
          expect(expectedResult).toEqual(rentalOrderCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
