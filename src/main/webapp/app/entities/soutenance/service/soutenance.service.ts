import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISoutenance, getSoutenanceIdentifier } from '../soutenance.model';

export type EntityResponseType = HttpResponse<ISoutenance>;
export type EntityArrayResponseType = HttpResponse<ISoutenance[]>;

@Injectable({ providedIn: 'root' })
export class SoutenanceService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/soutenances');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(soutenance: ISoutenance): Observable<EntityResponseType> {
    return this.http.post<ISoutenance>(this.resourceUrl, soutenance, { observe: 'response' });
  }

  update(soutenance: ISoutenance): Observable<EntityResponseType> {
    return this.http.put<ISoutenance>(`${this.resourceUrl}/${getSoutenanceIdentifier(soutenance) as number}`, soutenance, {
      observe: 'response',
    });
  }

  partialUpdate(soutenance: ISoutenance): Observable<EntityResponseType> {
    return this.http.patch<ISoutenance>(`${this.resourceUrl}/${getSoutenanceIdentifier(soutenance) as number}`, soutenance, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ISoutenance>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISoutenance[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addSoutenanceToCollectionIfMissing(
    soutenanceCollection: ISoutenance[],
    ...soutenancesToCheck: (ISoutenance | null | undefined)[]
  ): ISoutenance[] {
    const soutenances: ISoutenance[] = soutenancesToCheck.filter(isPresent);
    if (soutenances.length > 0) {
      const soutenanceCollectionIdentifiers = soutenanceCollection.map(soutenanceItem => getSoutenanceIdentifier(soutenanceItem)!);
      const soutenancesToAdd = soutenances.filter(soutenanceItem => {
        const soutenanceIdentifier = getSoutenanceIdentifier(soutenanceItem);
        if (soutenanceIdentifier == null || soutenanceCollectionIdentifiers.includes(soutenanceIdentifier)) {
          return false;
        }
        soutenanceCollectionIdentifiers.push(soutenanceIdentifier);
        return true;
      });
      return [...soutenancesToAdd, ...soutenanceCollection];
    }
    return soutenanceCollection;
  }
}
