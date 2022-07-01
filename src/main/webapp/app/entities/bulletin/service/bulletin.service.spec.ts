import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IBulletin, Bulletin } from '../bulletin.model';

import { BulletinService } from './bulletin.service';

describe('Bulletin Service', () => {
  let service: BulletinService;
  let httpMock: HttpTestingController;
  let elemDefault: IBulletin;
  let expectedResult: IBulletin | IBulletin[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(BulletinService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      signatureDG: 'AAAAAAA',
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

    it('should create a Bulletin', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Bulletin()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Bulletin', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          signatureDG: 'BBBBBB',
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

    it('should partial update a Bulletin', () => {
      const patchObject = Object.assign(
        {
          signatureDG: 'BBBBBB',
        },
        new Bulletin()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Bulletin', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          signatureDG: 'BBBBBB',
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

    it('should delete a Bulletin', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addBulletinToCollectionIfMissing', () => {
      it('should add a Bulletin to an empty array', () => {
        const bulletin: IBulletin = { id: 123 };
        expectedResult = service.addBulletinToCollectionIfMissing([], bulletin);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(bulletin);
      });

      it('should not add a Bulletin to an array that contains it', () => {
        const bulletin: IBulletin = { id: 123 };
        const bulletinCollection: IBulletin[] = [
          {
            ...bulletin,
          },
          { id: 456 },
        ];
        expectedResult = service.addBulletinToCollectionIfMissing(bulletinCollection, bulletin);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Bulletin to an array that doesn't contain it", () => {
        const bulletin: IBulletin = { id: 123 };
        const bulletinCollection: IBulletin[] = [{ id: 456 }];
        expectedResult = service.addBulletinToCollectionIfMissing(bulletinCollection, bulletin);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(bulletin);
      });

      it('should add only unique Bulletin to an array', () => {
        const bulletinArray: IBulletin[] = [{ id: 123 }, { id: 456 }, { id: 66873 }];
        const bulletinCollection: IBulletin[] = [{ id: 123 }];
        expectedResult = service.addBulletinToCollectionIfMissing(bulletinCollection, ...bulletinArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const bulletin: IBulletin = { id: 123 };
        const bulletin2: IBulletin = { id: 456 };
        expectedResult = service.addBulletinToCollectionIfMissing([], bulletin, bulletin2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(bulletin);
        expect(expectedResult).toContain(bulletin2);
      });

      it('should accept null and undefined values', () => {
        const bulletin: IBulletin = { id: 123 };
        expectedResult = service.addBulletinToCollectionIfMissing([], null, bulletin, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(bulletin);
      });

      it('should return initial array if no Bulletin is added', () => {
        const bulletinCollection: IBulletin[] = [{ id: 123 }];
        expectedResult = service.addBulletinToCollectionIfMissing(bulletinCollection, undefined, null);
        expect(expectedResult).toEqual(bulletinCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
