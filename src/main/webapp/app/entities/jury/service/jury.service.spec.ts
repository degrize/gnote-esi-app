import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IJury, Jury } from '../jury.model';

import { JuryService } from './jury.service';

describe('Jury Service', () => {
  let service: JuryService;
  let httpMock: HttpTestingController;
  let elemDefault: IJury;
  let expectedResult: IJury | IJury[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(JuryService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      presidentJury: 'AAAAAAA',
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

    it('should create a Jury', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Jury()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Jury', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          presidentJury: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Jury', () => {
      const patchObject = Object.assign(
        {
          presidentJury: 'BBBBBB',
        },
        new Jury()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Jury', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          presidentJury: 'BBBBBB',
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

    it('should delete a Jury', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addJuryToCollectionIfMissing', () => {
      it('should add a Jury to an empty array', () => {
        const jury: IJury = { id: 123 };
        expectedResult = service.addJuryToCollectionIfMissing([], jury);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(jury);
      });

      it('should not add a Jury to an array that contains it', () => {
        const jury: IJury = { id: 123 };
        const juryCollection: IJury[] = [
          {
            ...jury,
          },
          { id: 456 },
        ];
        expectedResult = service.addJuryToCollectionIfMissing(juryCollection, jury);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Jury to an array that doesn't contain it", () => {
        const jury: IJury = { id: 123 };
        const juryCollection: IJury[] = [{ id: 456 }];
        expectedResult = service.addJuryToCollectionIfMissing(juryCollection, jury);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(jury);
      });

      it('should add only unique Jury to an array', () => {
        const juryArray: IJury[] = [{ id: 123 }, { id: 456 }, { id: 40957 }];
        const juryCollection: IJury[] = [{ id: 123 }];
        expectedResult = service.addJuryToCollectionIfMissing(juryCollection, ...juryArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const jury: IJury = { id: 123 };
        const jury2: IJury = { id: 456 };
        expectedResult = service.addJuryToCollectionIfMissing([], jury, jury2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(jury);
        expect(expectedResult).toContain(jury2);
      });

      it('should accept null and undefined values', () => {
        const jury: IJury = { id: 123 };
        expectedResult = service.addJuryToCollectionIfMissing([], null, jury, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(jury);
      });

      it('should return initial array if no Jury is added', () => {
        const juryCollection: IJury[] = [{ id: 123 }];
        expectedResult = service.addJuryToCollectionIfMissing(juryCollection, undefined, null);
        expect(expectedResult).toEqual(juryCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
