import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICycle, getCycleIdentifier } from '../cycle.model';

export type EntityResponseType = HttpResponse<ICycle>;
export type EntityArrayResponseType = HttpResponse<ICycle[]>;

@Injectable({ providedIn: 'root' })
export class CycleService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/cycles');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(cycle: ICycle): Observable<EntityResponseType> {
    return this.http.post<ICycle>(this.resourceUrl, cycle, { observe: 'response' });
  }

  update(cycle: ICycle): Observable<EntityResponseType> {
    return this.http.put<ICycle>(`${this.resourceUrl}/${getCycleIdentifier(cycle) as number}`, cycle, { observe: 'response' });
  }

  partialUpdate(cycle: ICycle): Observable<EntityResponseType> {
    return this.http.patch<ICycle>(`${this.resourceUrl}/${getCycleIdentifier(cycle) as number}`, cycle, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICycle>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICycle[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCycleToCollectionIfMissing(cycleCollection: ICycle[], ...cyclesToCheck: (ICycle | null | undefined)[]): ICycle[] {
    const cycles: ICycle[] = cyclesToCheck.filter(isPresent);
    if (cycles.length > 0) {
      const cycleCollectionIdentifiers = cycleCollection.map(cycleItem => getCycleIdentifier(cycleItem)!);
      const cyclesToAdd = cycles.filter(cycleItem => {
        const cycleIdentifier = getCycleIdentifier(cycleItem);
        if (cycleIdentifier == null || cycleCollectionIdentifiers.includes(cycleIdentifier)) {
          return false;
        }
        cycleCollectionIdentifiers.push(cycleIdentifier);
        return true;
      });
      return [...cyclesToAdd, ...cycleCollection];
    }
    return cycleCollection;
  }
}
