import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISemestre, getSemestreIdentifier } from '../semestre.model';

export type EntityResponseType = HttpResponse<ISemestre>;
export type EntityArrayResponseType = HttpResponse<ISemestre[]>;

@Injectable({ providedIn: 'root' })
export class SemestreService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/semestres');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(semestre: ISemestre): Observable<EntityResponseType> {
    return this.http.post<ISemestre>(this.resourceUrl, semestre, { observe: 'response' });
  }

  update(semestre: ISemestre): Observable<EntityResponseType> {
    return this.http.put<ISemestre>(`${this.resourceUrl}/${getSemestreIdentifier(semestre) as number}`, semestre, { observe: 'response' });
  }

  partialUpdate(semestre: ISemestre): Observable<EntityResponseType> {
    return this.http.patch<ISemestre>(`${this.resourceUrl}/${getSemestreIdentifier(semestre) as number}`, semestre, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ISemestre>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISemestre[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addSemestreToCollectionIfMissing(semestreCollection: ISemestre[], ...semestresToCheck: (ISemestre | null | undefined)[]): ISemestre[] {
    const semestres: ISemestre[] = semestresToCheck.filter(isPresent);
    if (semestres.length > 0) {
      const semestreCollectionIdentifiers = semestreCollection.map(semestreItem => getSemestreIdentifier(semestreItem)!);
      const semestresToAdd = semestres.filter(semestreItem => {
        const semestreIdentifier = getSemestreIdentifier(semestreItem);
        if (semestreIdentifier == null || semestreCollectionIdentifiers.includes(semestreIdentifier)) {
          return false;
        }
        semestreCollectionIdentifiers.push(semestreIdentifier);
        return true;
      });
      return [...semestresToAdd, ...semestreCollection];
    }
    return semestreCollection;
  }
}
