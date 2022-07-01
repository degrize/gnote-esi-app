import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { TypeCycle } from 'app/entities/enumerations/type-cycle.model';
import { ICycle, Cycle } from '../cycle.model';

import { CycleService } from './cycle.service';

describe('Cycle Service', () => {
  let service: CycleService;
  let httpMock: HttpTestingController;
  let elemDefault: ICycle;
  let expectedResult: ICycle | ICycle[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CycleService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      nomCycle: TypeCycle.TS,
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

    it('should create a Cycle', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Cycle()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Cycle', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nomCycle: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Cycle', () => {
      const patchObject = Object.assign({}, new Cycle());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Cycle', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nomCycle: 'BBBBBB',
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

    it('should delete a Cycle', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addCycleToCollectionIfMissing', () => {
      it('should add a Cycle to an empty array', () => {
        const cycle: ICycle = { id: 123 };
        expectedResult = service.addCycleToCollectionIfMissing([], cycle);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(cycle);
      });

      it('should not add a Cycle to an array that contains it', () => {
        const cycle: ICycle = { id: 123 };
        const cycleCollection: ICycle[] = [
          {
            ...cycle,
          },
          { id: 456 },
        ];
        expectedResult = service.addCycleToCollectionIfMissing(cycleCollection, cycle);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Cycle to an array that doesn't contain it", () => {
        const cycle: ICycle = { id: 123 };
        const cycleCollection: ICycle[] = [{ id: 456 }];
        expectedResult = service.addCycleToCollectionIfMissing(cycleCollection, cycle);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(cycle);
      });

      it('should add only unique Cycle to an array', () => {
        const cycleArray: ICycle[] = [{ id: 123 }, { id: 456 }, { id: 99007 }];
        const cycleCollection: ICycle[] = [{ id: 123 }];
        expectedResult = service.addCycleToCollectionIfMissing(cycleCollection, ...cycleArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const cycle: ICycle = { id: 123 };
        const cycle2: ICycle = { id: 456 };
        expectedResult = service.addCycleToCollectionIfMissing([], cycle, cycle2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(cycle);
        expect(expectedResult).toContain(cycle2);
      });

      it('should accept null and undefined values', () => {
        const cycle: ICycle = { id: 123 };
        expectedResult = service.addCycleToCollectionIfMissing([], null, cycle, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(cycle);
      });

      it('should return initial array if no Cycle is added', () => {
        const cycleCollection: ICycle[] = [{ id: 123 }];
        expectedResult = service.addCycleToCollectionIfMissing(cycleCollection, undefined, null);
        expect(expectedResult).toEqual(cycleCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
