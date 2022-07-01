import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IJury, getJuryIdentifier } from '../jury.model';

export type EntityResponseType = HttpResponse<IJury>;
export type EntityArrayResponseType = HttpResponse<IJury[]>;

@Injectable({ providedIn: 'root' })
export class JuryService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/juries');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(jury: IJury): Observable<EntityResponseType> {
    return this.http.post<IJury>(this.resourceUrl, jury, { observe: 'response' });
  }

  update(jury: IJury): Observable<EntityResponseType> {
    return this.http.put<IJury>(`${this.resourceUrl}/${getJuryIdentifier(jury) as number}`, jury, { observe: 'response' });
  }

  partialUpdate(jury: IJury): Observable<EntityResponseType> {
    return this.http.patch<IJury>(`${this.resourceUrl}/${getJuryIdentifier(jury) as number}`, jury, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IJury>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IJury[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addJuryToCollectionIfMissing(juryCollection: IJury[], ...juriesToCheck: (IJury | null | undefined)[]): IJury[] {
    const juries: IJury[] = juriesToCheck.filter(isPresent);
    if (juries.length > 0) {
      const juryCollectionIdentifiers = juryCollection.map(juryItem => getJuryIdentifier(juryItem)!);
      const juriesToAdd = juries.filter(juryItem => {
        const juryIdentifier = getJuryIdentifier(juryItem);
        if (juryIdentifier == null || juryCollectionIdentifiers.includes(juryIdentifier)) {
          return false;
        }
        juryCollectionIdentifiers.push(juryIdentifier);
        return true;
      });
      return [...juriesToAdd, ...juryCollection];
    }
    return juryCollection;
  }
}
