import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IEncadreur, getEncadreurIdentifier } from '../encadreur.model';

export type EntityResponseType = HttpResponse<IEncadreur>;
export type EntityArrayResponseType = HttpResponse<IEncadreur[]>;

@Injectable({ providedIn: 'root' })
export class EncadreurService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/encadreurs');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(encadreur: IEncadreur): Observable<EntityResponseType> {
    return this.http.post<IEncadreur>(this.resourceUrl, encadreur, { observe: 'response' });
  }

  update(encadreur: IEncadreur): Observable<EntityResponseType> {
    return this.http.put<IEncadreur>(`${this.resourceUrl}/${getEncadreurIdentifier(encadreur) as number}`, encadreur, {
      observe: 'response',
    });
  }

  partialUpdate(encadreur: IEncadreur): Observable<EntityResponseType> {
    return this.http.patch<IEncadreur>(`${this.resourceUrl}/${getEncadreurIdentifier(encadreur) as number}`, encadreur, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IEncadreur>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IEncadreur[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addEncadreurToCollectionIfMissing(
    encadreurCollection: IEncadreur[],
    ...encadreursToCheck: (IEncadreur | null | undefined)[]
  ): IEncadreur[] {
    const encadreurs: IEncadreur[] = encadreursToCheck.filter(isPresent);
    if (encadreurs.length > 0) {
      const encadreurCollectionIdentifiers = encadreurCollection.map(encadreurItem => getEncadreurIdentifier(encadreurItem)!);
      const encadreursToAdd = encadreurs.filter(encadreurItem => {
        const encadreurIdentifier = getEncadreurIdentifier(encadreurItem);
        if (encadreurIdentifier == null || encadreurCollectionIdentifiers.includes(encadreurIdentifier)) {
          return false;
        }
        encadreurCollectionIdentifiers.push(encadreurIdentifier);
        return true;
      });
      return [...encadreursToAdd, ...encadreurCollection];
    }
    return encadreurCollection;
  }
}
