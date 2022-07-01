import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { TypeSoutenance } from 'app/entities/enumerations/type-soutenance.model';
import { ISoutenance, Soutenance } from '../soutenance.model';

import { SoutenanceService } from './soutenance.service';

describe('Soutenance Service', () => {
  let service: SoutenanceService;
  let httpMock: HttpTestingController;
  let elemDefault: ISoutenance;
  let expectedResult: ISoutenance | ISoutenance[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(SoutenanceService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      typeSout: TypeSoutenance.PFE,
      themeSout: 'AAAAAAA',
      noteSout: 0,
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

    it('should create a Soutenance', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Soutenance()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Soutenance', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          typeSout: 'BBBBBB',
          themeSout: 'BBBBBB',
          noteSout: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Soutenance', () => {
      const patchObject = Object.assign(
        {
          typeSout: 'BBBBBB',
        },
        new Soutenance()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Soutenance', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          typeSout: 'BBBBBB',
          themeSout: 'BBBBBB',
          noteSout: 1,
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

    it('should delete a Soutenance', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addSoutenanceToCollectionIfMissing', () => {
      it('should add a Soutenance to an empty array', () => {
        const soutenance: ISoutenance = { id: 123 };
        expectedResult = service.addSoutenanceToCollectionIfMissing([], soutenance);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(soutenance);
      });

      it('should not add a Soutenance to an array that contains it', () => {
        const soutenance: ISoutenance = { id: 123 };
        const soutenanceCollection: ISoutenance[] = [
          {
            ...soutenance,
          },
          { id: 456 },
        ];
        expectedResult = service.addSoutenanceToCollectionIfMissing(soutenanceCollection, soutenance);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Soutenance to an array that doesn't contain it", () => {
        const soutenance: ISoutenance = { id: 123 };
        const soutenanceCollection: ISoutenance[] = [{ id: 456 }];
        expectedResult = service.addSoutenanceToCollectionIfMissing(soutenanceCollection, soutenance);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(soutenance);
      });

      it('should add only unique Soutenance to an array', () => {
        const soutenanceArray: ISoutenance[] = [{ id: 123 }, { id: 456 }, { id: 66855 }];
        const soutenanceCollection: ISoutenance[] = [{ id: 123 }];
        expectedResult = service.addSoutenanceToCollectionIfMissing(soutenanceCollection, ...soutenanceArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const soutenance: ISoutenance = { id: 123 };
        const soutenance2: ISoutenance = { id: 456 };
        expectedResult = service.addSoutenanceToCollectionIfMissing([], soutenance, soutenance2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(soutenance);
        expect(expectedResult).toContain(soutenance2);
      });

      it('should accept null and undefined values', () => {
        const soutenance: ISoutenance = { id: 123 };
        expectedResult = service.addSoutenanceToCollectionIfMissing([], null, soutenance, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(soutenance);
      });

      it('should return initial array if no Soutenance is added', () => {
        const soutenanceCollection: ISoutenance[] = [{ id: 123 }];
        expectedResult = service.addSoutenanceToCollectionIfMissing(soutenanceCollection, undefined, null);
        expect(expectedResult).toEqual(soutenanceCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
