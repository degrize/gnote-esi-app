import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IHoraire, Horaire } from '../horaire.model';

import { HoraireService } from './horaire.service';

describe('Horaire Service', () => {
  let service: HoraireService;
  let httpMock: HttpTestingController;
  let elemDefault: IHoraire;
  let expectedResult: IHoraire | IHoraire[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(HoraireService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      dateSout: currentDate,
      dateEffet: currentDate,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          dateSout: currentDate.format(DATE_FORMAT),
          dateEffet: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Horaire', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          dateSout: currentDate.format(DATE_FORMAT),
          dateEffet: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          dateSout: currentDate,
          dateEffet: currentDate,
        },
        returnedFromService
      );

      service.create(new Horaire()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Horaire', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          dateSout: currentDate.format(DATE_FORMAT),
          dateEffet: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          dateSout: currentDate,
          dateEffet: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Horaire', () => {
      const patchObject = Object.assign(
        {
          dateSout: currentDate.format(DATE_FORMAT),
        },
        new Horaire()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          dateSout: currentDate,
          dateEffet: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Horaire', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          dateSout: currentDate.format(DATE_FORMAT),
          dateEffet: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          dateSout: currentDate,
          dateEffet: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Horaire', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addHoraireToCollectionIfMissing', () => {
      it('should add a Horaire to an empty array', () => {
        const horaire: IHoraire = { id: 123 };
        expectedResult = service.addHoraireToCollectionIfMissing([], horaire);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(horaire);
      });

      it('should not add a Horaire to an array that contains it', () => {
        const horaire: IHoraire = { id: 123 };
        const horaireCollection: IHoraire[] = [
          {
            ...horaire,
          },
          { id: 456 },
        ];
        expectedResult = service.addHoraireToCollectionIfMissing(horaireCollection, horaire);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Horaire to an array that doesn't contain it", () => {
        const horaire: IHoraire = { id: 123 };
        const horaireCollection: IHoraire[] = [{ id: 456 }];
        expectedResult = service.addHoraireToCollectionIfMissing(horaireCollection, horaire);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(horaire);
      });

      it('should add only unique Horaire to an array', () => {
        const horaireArray: IHoraire[] = [{ id: 123 }, { id: 456 }, { id: 99546 }];
        const horaireCollection: IHoraire[] = [{ id: 123 }];
        expectedResult = service.addHoraireToCollectionIfMissing(horaireCollection, ...horaireArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const horaire: IHoraire = { id: 123 };
        const horaire2: IHoraire = { id: 456 };
        expectedResult = service.addHoraireToCollectionIfMissing([], horaire, horaire2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(horaire);
        expect(expectedResult).toContain(horaire2);
      });

      it('should accept null and undefined values', () => {
        const horaire: IHoraire = { id: 123 };
        expectedResult = service.addHoraireToCollectionIfMissing([], null, horaire, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(horaire);
      });

      it('should return initial array if no Horaire is added', () => {
        const horaireCollection: IHoraire[] = [{ id: 123 }];
        expectedResult = service.addHoraireToCollectionIfMissing(horaireCollection, undefined, null);
        expect(expectedResult).toEqual(horaireCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
