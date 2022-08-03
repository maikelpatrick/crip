import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IDeposito, Deposito } from '../deposito.model';

import { DepositoService } from './deposito.service';

describe('Deposito Service', () => {
  let service: DepositoService;
  let httpMock: HttpTestingController;
  let elemDefault: IDeposito;
  let expectedResult: IDeposito | IDeposito[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(DepositoService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      volume: 0,
      id_deposito: 'AAAAAAA',
      client_account: 'AAAAAAA',
      entidade_account: 'AAAAAAA',
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

    it('should create a Deposito', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Deposito()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Deposito', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          volume: 1,
          id_deposito: 'BBBBBB',
          client_account: 'BBBBBB',
          entidade_account: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Deposito', () => {
      const patchObject = Object.assign(
        {
          volume: 1,
          id_deposito: 'BBBBBB',
          entidade_account: 'BBBBBB',
        },
        new Deposito()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Deposito', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          volume: 1,
          id_deposito: 'BBBBBB',
          client_account: 'BBBBBB',
          entidade_account: 'BBBBBB',
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

    it('should delete a Deposito', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addDepositoToCollectionIfMissing', () => {
      it('should add a Deposito to an empty array', () => {
        const deposito: IDeposito = { id: 123 };
        expectedResult = service.addDepositoToCollectionIfMissing([], deposito);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(deposito);
      });

      it('should not add a Deposito to an array that contains it', () => {
        const deposito: IDeposito = { id: 123 };
        const depositoCollection: IDeposito[] = [
          {
            ...deposito,
          },
          { id: 456 },
        ];
        expectedResult = service.addDepositoToCollectionIfMissing(depositoCollection, deposito);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Deposito to an array that doesn't contain it", () => {
        const deposito: IDeposito = { id: 123 };
        const depositoCollection: IDeposito[] = [{ id: 456 }];
        expectedResult = service.addDepositoToCollectionIfMissing(depositoCollection, deposito);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(deposito);
      });

      it('should add only unique Deposito to an array', () => {
        const depositoArray: IDeposito[] = [{ id: 123 }, { id: 456 }, { id: 14365 }];
        const depositoCollection: IDeposito[] = [{ id: 123 }];
        expectedResult = service.addDepositoToCollectionIfMissing(depositoCollection, ...depositoArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const deposito: IDeposito = { id: 123 };
        const deposito2: IDeposito = { id: 456 };
        expectedResult = service.addDepositoToCollectionIfMissing([], deposito, deposito2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(deposito);
        expect(expectedResult).toContain(deposito2);
      });

      it('should accept null and undefined values', () => {
        const deposito: IDeposito = { id: 123 };
        expectedResult = service.addDepositoToCollectionIfMissing([], null, deposito, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(deposito);
      });

      it('should return initial array if no Deposito is added', () => {
        const depositoCollection: IDeposito[] = [{ id: 123 }];
        expectedResult = service.addDepositoToCollectionIfMissing(depositoCollection, undefined, null);
        expect(expectedResult).toEqual(depositoCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
