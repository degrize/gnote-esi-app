import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IDemandeInspecteurDE, DemandeInspecteurDE } from '../demande-inspecteur-de.model';

import { DemandeInspecteurDEService } from './demande-inspecteur-de.service';

describe('DemandeInspecteurDE Service', () => {
  let service: DemandeInspecteurDEService;
  let httpMock: HttpTestingController;
  let elemDefault: IDemandeInspecteurDE;
  let expectedResult: IDemandeInspecteurDE | IDemandeInspecteurDE[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(DemandeInspecteurDEService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      message: 'AAAAAAA',
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

    it('should create a DemandeInspecteurDE', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new DemandeInspecteurDE()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a DemandeInspecteurDE', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          message: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a DemandeInspecteurDE', () => {
      const patchObject = Object.assign(
        {
          message: 'BBBBBB',
        },
        new DemandeInspecteurDE()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of DemandeInspecteurDE', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          message: 'BBBBBB',
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

    it('should delete a DemandeInspecteurDE', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addDemandeInspecteurDEToCollectionIfMissing', () => {
      it('should add a DemandeInspecteurDE to an empty array', () => {
        const demandeInspecteurDE: IDemandeInspecteurDE = { id: 123 };
        expectedResult = service.addDemandeInspecteurDEToCollectionIfMissing([], demandeInspecteurDE);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(demandeInspecteurDE);
      });

      it('should not add a DemandeInspecteurDE to an array that contains it', () => {
        const demandeInspecteurDE: IDemandeInspecteurDE = { id: 123 };
        const demandeInspecteurDECollection: IDemandeInspecteurDE[] = [
          {
            ...demandeInspecteurDE,
          },
          { id: 456 },
        ];
        expectedResult = service.addDemandeInspecteurDEToCollectionIfMissing(demandeInspecteurDECollection, demandeInspecteurDE);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a DemandeInspecteurDE to an array that doesn't contain it", () => {
        const demandeInspecteurDE: IDemandeInspecteurDE = { id: 123 };
        const demandeInspecteurDECollection: IDemandeInspecteurDE[] = [{ id: 456 }];
        expectedResult = service.addDemandeInspecteurDEToCollectionIfMissing(demandeInspecteurDECollection, demandeInspecteurDE);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(demandeInspecteurDE);
      });

      it('should add only unique DemandeInspecteurDE to an array', () => {
        const demandeInspecteurDEArray: IDemandeInspecteurDE[] = [{ id: 123 }, { id: 456 }, { id: 88857 }];
        const demandeInspecteurDECollection: IDemandeInspecteurDE[] = [{ id: 123 }];
        expectedResult = service.addDemandeInspecteurDEToCollectionIfMissing(demandeInspecteurDECollection, ...demandeInspecteurDEArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const demandeInspecteurDE: IDemandeInspecteurDE = { id: 123 };
        const demandeInspecteurDE2: IDemandeInspecteurDE = { id: 456 };
        expectedResult = service.addDemandeInspecteurDEToCollectionIfMissing([], demandeInspecteurDE, demandeInspecteurDE2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(demandeInspecteurDE);
        expect(expectedResult).toContain(demandeInspecteurDE2);
      });

      it('should accept null and undefined values', () => {
        const demandeInspecteurDE: IDemandeInspecteurDE = { id: 123 };
        expectedResult = service.addDemandeInspecteurDEToCollectionIfMissing([], null, demandeInspecteurDE, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(demandeInspecteurDE);
      });

      it('should return initial array if no DemandeInspecteurDE is added', () => {
        const demandeInspecteurDECollection: IDemandeInspecteurDE[] = [{ id: 123 }];
        expectedResult = service.addDemandeInspecteurDEToCollectionIfMissing(demandeInspecteurDECollection, undefined, null);
        expect(expectedResult).toEqual(demandeInspecteurDECollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
