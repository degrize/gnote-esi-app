import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPlanche, getPlancheIdentifier } from '../planche.model';

export type EntityResponseType = HttpResponse<IPlanche>;
export type EntityArrayResponseType = HttpResponse<IPlanche[]>;

@Injectable({ providedIn: 'root' })
export class PlancheService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/planches');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(planche: IPlanche): Observable<EntityResponseType> {
    return this.http.post<IPlanche>(this.resourceUrl, planche, { observe: 'response' });
  }

  update(planche: IPlanche): Observable<EntityResponseType> {
    return this.http.put<IPlanche>(`${this.resourceUrl}/${getPlancheIdentifier(planche) as number}`, planche, { observe: 'response' });
  }

  partialUpdate(planche: IPlanche): Observable<EntityResponseType> {
    return this.http.patch<IPlanche>(`${this.resourceUrl}/${getPlancheIdentifier(planche) as number}`, planche, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPlanche>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPlanche[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addPlancheToCollectionIfMissing(plancheCollection: IPlanche[], ...planchesToCheck: (IPlanche | null | undefined)[]): IPlanche[] {
    const planches: IPlanche[] = planchesToCheck.filter(isPresent);
    if (planches.length > 0) {
      const plancheCollectionIdentifiers = plancheCollection.map(plancheItem => getPlancheIdentifier(plancheItem)!);
      const planchesToAdd = planches.filter(plancheItem => {
        const plancheIdentifier = getPlancheIdentifier(plancheItem);
        if (plancheIdentifier == null || plancheCollectionIdentifiers.includes(plancheIdentifier)) {
          return false;
        }
        plancheCollectionIdentifiers.push(plancheIdentifier);
        return true;
      });
      return [...planchesToAdd, ...plancheCollection];
    }
    return plancheCollection;
  }
}
