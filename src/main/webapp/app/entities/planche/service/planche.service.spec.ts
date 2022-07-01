import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IPlanche, Planche } from '../planche.model';

import { PlancheService } from './planche.service';

describe('Planche Service', () => {
  let service: PlancheService;
  let httpMock: HttpTestingController;
  let elemDefault: IPlanche;
  let expectedResult: IPlanche | IPlanche[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PlancheService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      observation: 'AAAAAAA',
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

    it('should create a Planche', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Planche()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Planche', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          observation: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Planche', () => {
      const patchObject = Object.assign(
        {
          observation: 'BBBBBB',
        },
        new Planche()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Planche', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          observation: 'BBBBBB',
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

    it('should delete a Planche', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addPlancheToCollectionIfMissing', () => {
      it('should add a Planche to an empty array', () => {
        const planche: IPlanche = { id: 123 };
        expectedResult = service.addPlancheToCollectionIfMissing([], planche);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(planche);
      });

      it('should not add a Planche to an array that contains it', () => {
        const planche: IPlanche = { id: 123 };
        const plancheCollection: IPlanche[] = [
          {
            ...planche,
          },
          { id: 456 },
        ];
        expectedResult = service.addPlancheToCollectionIfMissing(plancheCollection, planche);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Planche to an array that doesn't contain it", () => {
        const planche: IPlanche = { id: 123 };
        const plancheCollection: IPlanche[] = [{ id: 456 }];
        expectedResult = service.addPlancheToCollectionIfMissing(plancheCollection, planche);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(planche);
      });

      it('should add only unique Planche to an array', () => {
        const plancheArray: IPlanche[] = [{ id: 123 }, { id: 456 }, { id: 91471 }];
        const plancheCollection: IPlanche[] = [{ id: 123 }];
        expectedResult = service.addPlancheToCollectionIfMissing(plancheCollection, ...plancheArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const planche: IPlanche = { id: 123 };
        const planche2: IPlanche = { id: 456 };
        expectedResult = service.addPlancheToCollectionIfMissing([], planche, planche2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(planche);
        expect(expectedResult).toContain(planche2);
      });

      it('should accept null and undefined values', () => {
        const planche: IPlanche = { id: 123 };
        expectedResult = service.addPlancheToCollectionIfMissing([], null, planche, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(planche);
      });

      it('should return initial array if no Planche is added', () => {
        const plancheCollection: IPlanche[] = [{ id: 123 }];
        expectedResult = service.addPlancheToCollectionIfMissing(plancheCollection, undefined, null);
        expect(expectedResult).toEqual(plancheCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
