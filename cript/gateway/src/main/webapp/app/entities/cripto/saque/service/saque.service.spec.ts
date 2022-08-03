import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ISaque, Saque } from '../saque.model';

import { SaqueService } from './saque.service';

describe('Saque Service', () => {
  let service: SaqueService;
  let httpMock: HttpTestingController;
  let elemDefault: ISaque;
  let expectedResult: ISaque | ISaque[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(SaqueService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      volume: 0,
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

    it('should create a Saque', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Saque()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Saque', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          volume: 1,
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

    it('should partial update a Saque', () => {
      const patchObject = Object.assign({}, new Saque());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Saque', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          volume: 1,
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

    it('should delete a Saque', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addSaqueToCollectionIfMissing', () => {
      it('should add a Saque to an empty array', () => {
        const saque: ISaque = { id: 123 };
        expectedResult = service.addSaqueToCollectionIfMissing([], saque);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(saque);
      });

      it('should not add a Saque to an array that contains it', () => {
        const saque: ISaque = { id: 123 };
        const saqueCollection: ISaque[] = [
          {
            ...saque,
          },
          { id: 456 },
        ];
        expectedResult = service.addSaqueToCollectionIfMissing(saqueCollection, saque);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Saque to an array that doesn't contain it", () => {
        const saque: ISaque = { id: 123 };
        const saqueCollection: ISaque[] = [{ id: 456 }];
        expectedResult = service.addSaqueToCollectionIfMissing(saqueCollection, saque);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(saque);
      });

      it('should add only unique Saque to an array', () => {
        const saqueArray: ISaque[] = [{ id: 123 }, { id: 456 }, { id: 84874 }];
        const saqueCollection: ISaque[] = [{ id: 123 }];
        expectedResult = service.addSaqueToCollectionIfMissing(saqueCollection, ...saqueArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const saque: ISaque = { id: 123 };
        const saque2: ISaque = { id: 456 };
        expectedResult = service.addSaqueToCollectionIfMissing([], saque, saque2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(saque);
        expect(expectedResult).toContain(saque2);
      });

      it('should accept null and undefined values', () => {
        const saque: ISaque = { id: 123 };
        expectedResult = service.addSaqueToCollectionIfMissing([], null, saque, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(saque);
      });

      it('should return initial array if no Saque is added', () => {
        const saqueCollection: ISaque[] = [{ id: 123 }];
        expectedResult = service.addSaqueToCollectionIfMissing(saqueCollection, undefined, null);
        expect(expectedResult).toEqual(saqueCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
