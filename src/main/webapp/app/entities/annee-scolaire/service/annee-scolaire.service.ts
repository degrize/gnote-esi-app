import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAnneeScolaire, getAnneeScolaireIdentifier } from '../annee-scolaire.model';

export type EntityResponseType = HttpResponse<IAnneeScolaire>;
export type EntityArrayResponseType = HttpResponse<IAnneeScolaire[]>;

@Injectable({ providedIn: 'root' })
export class AnneeScolaireService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/annee-scolaires');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(anneeScolaire: IAnneeScolaire): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(anneeScolaire);
    return this.http
      .post<IAnneeScolaire>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(anneeScolaire: IAnneeScolaire): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(anneeScolaire);
    return this.http
      .put<IAnneeScolaire>(`${this.resourceUrl}/${getAnneeScolaireIdentifier(anneeScolaire) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(anneeScolaire: IAnneeScolaire): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(anneeScolaire);
    return this.http
      .patch<IAnneeScolaire>(`${this.resourceUrl}/${getAnneeScolaireIdentifier(anneeScolaire) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IAnneeScolaire>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IAnneeScolaire[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addAnneeScolaireToCollectionIfMissing(
    anneeScolaireCollection: IAnneeScolaire[],
    ...anneeScolairesToCheck: (IAnneeScolaire | null | undefined)[]
  ): IAnneeScolaire[] {
    const anneeScolaires: IAnneeScolaire[] = anneeScolairesToCheck.filter(isPresent);
    if (anneeScolaires.length > 0) {
      const anneeScolaireCollectionIdentifiers = anneeScolaireCollection.map(
        anneeScolaireItem => getAnneeScolaireIdentifier(anneeScolaireItem)!
      );
      const anneeScolairesToAdd = anneeScolaires.filter(anneeScolaireItem => {
        const anneeScolaireIdentifier = getAnneeScolaireIdentifier(anneeScolaireItem);
        if (anneeScolaireIdentifier == null || anneeScolaireCollectionIdentifiers.includes(anneeScolaireIdentifier)) {
          return false;
        }
        anneeScolaireCollectionIdentifiers.push(anneeScolaireIdentifier);
        return true;
      });
      return [...anneeScolairesToAdd, ...anneeScolaireCollection];
    }
    return anneeScolaireCollection;
  }

  protected convertDateFromClient(anneeScolaire: IAnneeScolaire): IAnneeScolaire {
    return Object.assign({}, anneeScolaire, {
      dateDebut: anneeScolaire.dateDebut?.isValid() ? anneeScolaire.dateDebut.format(DATE_FORMAT) : undefined,
      dateFin: anneeScolaire.dateFin?.isValid() ? anneeScolaire.dateFin.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dateDebut = res.body.dateDebut ? dayjs(res.body.dateDebut) : undefined;
      res.body.dateFin = res.body.dateFin ? dayjs(res.body.dateFin) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((anneeScolaire: IAnneeScolaire) => {
        anneeScolaire.dateDebut = anneeScolaire.dateDebut ? dayjs(anneeScolaire.dateDebut) : undefined;
        anneeScolaire.dateFin = anneeScolaire.dateFin ? dayjs(anneeScolaire.dateFin) : undefined;
      });
    }
    return res;
  }
}
