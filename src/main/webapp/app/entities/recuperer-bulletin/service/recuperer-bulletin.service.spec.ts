import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IRecupererBulletin, RecupererBulletin } from '../recuperer-bulletin.model';

import { RecupererBulletinService } from './recuperer-bulletin.service';

describe('RecupererBulletin Service', () => {
  let service: RecupererBulletinService;
  let httpMock: HttpTestingController;
  let elemDefault: IRecupererBulletin;
  let expectedResult: IRecupererBulletin | IRecupererBulletin[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(RecupererBulletinService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      signatureEleve: 'AAAAAAA',
      bulletinScanneContentType: 'image/png',
      bulletinScanne: 'AAAAAAA',
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

    it('should create a RecupererBulletin', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new RecupererBulletin()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a RecupererBulletin', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          signatureEleve: 'BBBBBB',
          bulletinScanne: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a RecupererBulletin', () => {
      const patchObject = Object.assign(
        {
          signatureEleve: 'BBBBBB',
        },
        new RecupererBulletin()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of RecupererBulletin', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          signatureEleve: 'BBBBBB',
          bulletinScanne: 'BBBBBB',
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

    it('should delete a RecupererBulletin', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addRecupererBulletinToCollectionIfMissing', () => {
      it('should add a RecupererBulletin to an empty array', () => {
        const recupererBulletin: IRecupererBulletin = { id: 123 };
        expectedResult = service.addRecupererBulletinToCollectionIfMissing([], recupererBulletin);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(recupererBulletin);
      });

      it('should not add a RecupererBulletin to an array that contains it', () => {
        const recupererBulletin: IRecupererBulletin = { id: 123 };
        const recupererBulletinCollection: IRecupererBulletin[] = [
          {
            ...recupererBulletin,
          },
          { id: 456 },
        ];
        expectedResult = service.addRecupererBulletinToCollectionIfMissing(recupererBulletinCollection, recupererBulletin);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a RecupererBulletin to an array that doesn't contain it", () => {
        const recupererBulletin: IRecupererBulletin = { id: 123 };
        const recupererBulletinCollection: IRecupererBulletin[] = [{ id: 456 }];
        expectedResult = service.addRecupererBulletinToCollectionIfMissing(recupererBulletinCollection, recupererBulletin);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(recupererBulletin);
      });

      it('should add only unique RecupererBulletin to an array', () => {
        const recupererBulletinArray: IRecupererBulletin[] = [{ id: 123 }, { id: 456 }, { id: 8619 }];
        const recupererBulletinCollection: IRecupererBulletin[] = [{ id: 123 }];
        expectedResult = service.addRecupererBulletinToCollectionIfMissing(recupererBulletinCollection, ...recupererBulletinArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const recupererBulletin: IRecupererBulletin = { id: 123 };
        const recupererBulletin2: IRecupererBulletin = { id: 456 };
        expectedResult = service.addRecupererBulletinToCollectionIfMissing([], recupererBulletin, recupererBulletin2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(recupererBulletin);
        expect(expectedResult).toContain(recupererBulletin2);
      });

      it('should accept null and undefined values', () => {
        const recupererBulletin: IRecupererBulletin = { id: 123 };
        expectedResult = service.addRecupererBulletinToCollectionIfMissing([], null, recupererBulletin, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(recupererBulletin);
      });

      it('should return initial array if no RecupererBulletin is added', () => {
        const recupererBulletinCollection: IRecupererBulletin[] = [{ id: 123 }];
        expectedResult = service.addRecupererBulletinToCollectionIfMissing(recupererBulletinCollection, undefined, null);
        expect(expectedResult).toEqual(recupererBulletinCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
