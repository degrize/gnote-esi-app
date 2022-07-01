import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ISemestre, Semestre } from '../semestre.model';

import { SemestreService } from './semestre.service';

describe('Semestre Service', () => {
  let service: SemestreService;
  let httpMock: HttpTestingController;
  let elemDefault: ISemestre;
  let expectedResult: ISemestre | ISemestre[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(SemestreService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      nomSemestre: 'AAAAAAA',
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

    it('should create a Semestre', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Semestre()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Semestre', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nomSemestre: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Semestre', () => {
      const patchObject = Object.assign(
        {
          nomSemestre: 'BBBBBB',
        },
        new Semestre()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Semestre', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nomSemestre: 'BBBBBB',
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

    it('should delete a Semestre', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addSemestreToCollectionIfMissing', () => {
      it('should add a Semestre to an empty array', () => {
        const semestre: ISemestre = { id: 123 };
        expectedResult = service.addSemestreToCollectionIfMissing([], semestre);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(semestre);
      });

      it('should not add a Semestre to an array that contains it', () => {
        const semestre: ISemestre = { id: 123 };
        const semestreCollection: ISemestre[] = [
          {
            ...semestre,
          },
          { id: 456 },
        ];
        expectedResult = service.addSemestreToCollectionIfMissing(semestreCollection, semestre);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Semestre to an array that doesn't contain it", () => {
        const semestre: ISemestre = { id: 123 };
        const semestreCollection: ISemestre[] = [{ id: 456 }];
        expectedResult = service.addSemestreToCollectionIfMissing(semestreCollection, semestre);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(semestre);
      });

      it('should add only unique Semestre to an array', () => {
        const semestreArray: ISemestre[] = [{ id: 123 }, { id: 456 }, { id: 3482 }];
        const semestreCollection: ISemestre[] = [{ id: 123 }];
        expectedResult = service.addSemestreToCollectionIfMissing(semestreCollection, ...semestreArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const semestre: ISemestre = { id: 123 };
        const semestre2: ISemestre = { id: 456 };
        expectedResult = service.addSemestreToCollectionIfMissing([], semestre, semestre2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(semestre);
        expect(expectedResult).toContain(semestre2);
      });

      it('should accept null and undefined values', () => {
        const semestre: ISemestre = { id: 123 };
        expectedResult = service.addSemestreToCollectionIfMissing([], null, semestre, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(semestre);
      });

      it('should return initial array if no Semestre is added', () => {
        const semestreCollection: ISemestre[] = [{ id: 123 }];
        expectedResult = service.addSemestreToCollectionIfMissing(semestreCollection, undefined, null);
        expect(expectedResult).toEqual(semestreCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
