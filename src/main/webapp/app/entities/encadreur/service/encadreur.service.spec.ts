import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IEncadreur, Encadreur } from '../encadreur.model';

import { EncadreurService } from './encadreur.service';

describe('Encadreur Service', () => {
  let service: EncadreurService;
  let httpMock: HttpTestingController;
  let elemDefault: IEncadreur;
  let expectedResult: IEncadreur | IEncadreur[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(EncadreurService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      nomEnc: 'AAAAAAA',
      prenomsEnc: 'AAAAAAA',
      emailEnc: 'AAAAAAA',
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

    it('should create a Encadreur', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Encadreur()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Encadreur', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nomEnc: 'BBBBBB',
          prenomsEnc: 'BBBBBB',
          emailEnc: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Encadreur', () => {
      const patchObject = Object.assign({}, new Encadreur());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Encadreur', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nomEnc: 'BBBBBB',
          prenomsEnc: 'BBBBBB',
          emailEnc: 'BBBBBB',
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

    it('should delete a Encadreur', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addEncadreurToCollectionIfMissing', () => {
      it('should add a Encadreur to an empty array', () => {
        const encadreur: IEncadreur = { id: 123 };
        expectedResult = service.addEncadreurToCollectionIfMissing([], encadreur);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(encadreur);
      });

      it('should not add a Encadreur to an array that contains it', () => {
        const encadreur: IEncadreur = { id: 123 };
        const encadreurCollection: IEncadreur[] = [
          {
            ...encadreur,
          },
          { id: 456 },
        ];
        expectedResult = service.addEncadreurToCollectionIfMissing(encadreurCollection, encadreur);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Encadreur to an array that doesn't contain it", () => {
        const encadreur: IEncadreur = { id: 123 };
        const encadreurCollection: IEncadreur[] = [{ id: 456 }];
        expectedResult = service.addEncadreurToCollectionIfMissing(encadreurCollection, encadreur);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(encadreur);
      });

      it('should add only unique Encadreur to an array', () => {
        const encadreurArray: IEncadreur[] = [{ id: 123 }, { id: 456 }, { id: 19806 }];
        const encadreurCollection: IEncadreur[] = [{ id: 123 }];
        expectedResult = service.addEncadreurToCollectionIfMissing(encadreurCollection, ...encadreurArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const encadreur: IEncadreur = { id: 123 };
        const encadreur2: IEncadreur = { id: 456 };
        expectedResult = service.addEncadreurToCollectionIfMissing([], encadreur, encadreur2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(encadreur);
        expect(expectedResult).toContain(encadreur2);
      });

      it('should accept null and undefined values', () => {
        const encadreur: IEncadreur = { id: 123 };
        expectedResult = service.addEncadreurToCollectionIfMissing([], null, encadreur, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(encadreur);
      });

      it('should return initial array if no Encadreur is added', () => {
        const encadreurCollection: IEncadreur[] = [{ id: 123 }];
        expectedResult = service.addEncadreurToCollectionIfMissing(encadreurCollection, undefined, null);
        expect(expectedResult).toEqual(encadreurCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
