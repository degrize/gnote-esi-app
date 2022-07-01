import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IRecupererBulletin, getRecupererBulletinIdentifier } from '../recuperer-bulletin.model';

export type EntityResponseType = HttpResponse<IRecupererBulletin>;
export type EntityArrayResponseType = HttpResponse<IRecupererBulletin[]>;

@Injectable({ providedIn: 'root' })
export class RecupererBulletinService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/recuperer-bulletins');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(recupererBulletin: IRecupererBulletin): Observable<EntityResponseType> {
    return this.http.post<IRecupererBulletin>(this.resourceUrl, recupererBulletin, { observe: 'response' });
  }

  update(recupererBulletin: IRecupererBulletin): Observable<EntityResponseType> {
    return this.http.put<IRecupererBulletin>(
      `${this.resourceUrl}/${getRecupererBulletinIdentifier(recupererBulletin) as number}`,
      recupererBulletin,
      { observe: 'response' }
    );
  }

  partialUpdate(recupererBulletin: IRecupererBulletin): Observable<EntityResponseType> {
    return this.http.patch<IRecupererBulletin>(
      `${this.resourceUrl}/${getRecupererBulletinIdentifier(recupererBulletin) as number}`,
      recupererBulletin,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IRecupererBulletin>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IRecupererBulletin[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addRecupererBulletinToCollectionIfMissing(
    recupererBulletinCollection: IRecupererBulletin[],
    ...recupererBulletinsToCheck: (IRecupererBulletin | null | undefined)[]
  ): IRecupererBulletin[] {
    const recupererBulletins: IRecupererBulletin[] = recupererBulletinsToCheck.filter(isPresent);
    if (recupererBulletins.length > 0) {
      const recupererBulletinCollectionIdentifiers = recupererBulletinCollection.map(
        recupererBulletinItem => getRecupererBulletinIdentifier(recupererBulletinItem)!
      );
      const recupererBulletinsToAdd = recupererBulletins.filter(recupererBulletinItem => {
        const recupererBulletinIdentifier = getRecupererBulletinIdentifier(recupererBulletinItem);
        if (recupererBulletinIdentifier == null || recupererBulletinCollectionIdentifiers.includes(recupererBulletinIdentifier)) {
          return false;
        }
        recupererBulletinCollectionIdentifiers.push(recupererBulletinIdentifier);
        return true;
      });
      return [...recupererBulletinsToAdd, ...recupererBulletinCollection];
    }
    return recupererBulletinCollection;
  }
}
