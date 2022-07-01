import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IHoraire, getHoraireIdentifier } from '../horaire.model';

export type EntityResponseType = HttpResponse<IHoraire>;
export type EntityArrayResponseType = HttpResponse<IHoraire[]>;

@Injectable({ providedIn: 'root' })
export class HoraireService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/horaires');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(horaire: IHoraire): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(horaire);
    return this.http
      .post<IHoraire>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(horaire: IHoraire): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(horaire);
    return this.http
      .put<IHoraire>(`${this.resourceUrl}/${getHoraireIdentifier(horaire) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(horaire: IHoraire): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(horaire);
    return this.http
      .patch<IHoraire>(`${this.resourceUrl}/${getHoraireIdentifier(horaire) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IHoraire>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IHoraire[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addHoraireToCollectionIfMissing(horaireCollection: IHoraire[], ...horairesToCheck: (IHoraire | null | undefined)[]): IHoraire[] {
    const horaires: IHoraire[] = horairesToCheck.filter(isPresent);
    if (horaires.length > 0) {
      const horaireCollectionIdentifiers = horaireCollection.map(horaireItem => getHoraireIdentifier(horaireItem)!);
      const horairesToAdd = horaires.filter(horaireItem => {
        const horaireIdentifier = getHoraireIdentifier(horaireItem);
        if (horaireIdentifier == null || horaireCollectionIdentifiers.includes(horaireIdentifier)) {
          return false;
        }
        horaireCollectionIdentifiers.push(horaireIdentifier);
        return true;
      });
      return [...horairesToAdd, ...horaireCollection];
    }
    return horaireCollection;
  }

  protected convertDateFromClient(horaire: IHoraire): IHoraire {
    return Object.assign({}, horaire, {
      dateSout: horaire.dateSout?.isValid() ? horaire.dateSout.format(DATE_FORMAT) : undefined,
      dateEffet: horaire.dateEffet?.isValid() ? horaire.dateEffet.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dateSout = res.body.dateSout ? dayjs(res.body.dateSout) : undefined;
      res.body.dateEffet = res.body.dateEffet ? dayjs(res.body.dateEffet) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((horaire: IHoraire) => {
        horaire.dateSout = horaire.dateSout ? dayjs(horaire.dateSout) : undefined;
        horaire.dateEffet = horaire.dateEffet ? dayjs(horaire.dateEffet) : undefined;
      });
    }
    return res;
  }
}
