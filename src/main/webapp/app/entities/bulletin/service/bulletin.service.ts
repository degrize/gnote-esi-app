import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IBulletin, getBulletinIdentifier } from '../bulletin.model';

export type EntityResponseType = HttpResponse<IBulletin>;
export type EntityArrayResponseType = HttpResponse<IBulletin[]>;

@Injectable({ providedIn: 'root' })
export class BulletinService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/bulletins');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(bulletin: IBulletin): Observable<EntityResponseType> {
    return this.http.post<IBulletin>(this.resourceUrl, bulletin, { observe: 'response' });
  }

  update(bulletin: IBulletin): Observable<EntityResponseType> {
    return this.http.put<IBulletin>(`${this.resourceUrl}/${getBulletinIdentifier(bulletin) as number}`, bulletin, { observe: 'response' });
  }

  partialUpdate(bulletin: IBulletin): Observable<EntityResponseType> {
    return this.http.patch<IBulletin>(`${this.resourceUrl}/${getBulletinIdentifier(bulletin) as number}`, bulletin, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IBulletin>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IBulletin[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addBulletinToCollectionIfMissing(bulletinCollection: IBulletin[], ...bulletinsToCheck: (IBulletin | null | undefined)[]): IBulletin[] {
    const bulletins: IBulletin[] = bulletinsToCheck.filter(isPresent);
    if (bulletins.length > 0) {
      const bulletinCollectionIdentifiers = bulletinCollection.map(bulletinItem => getBulletinIdentifier(bulletinItem)!);
      const bulletinsToAdd = bulletins.filter(bulletinItem => {
        const bulletinIdentifier = getBulletinIdentifier(bulletinItem);
        if (bulletinIdentifier == null || bulletinCollectionIdentifiers.includes(bulletinIdentifier)) {
          return false;
        }
        bulletinCollectionIdentifiers.push(bulletinIdentifier);
        return true;
      });
      return [...bulletinsToAdd, ...bulletinCollection];
    }
    return bulletinCollection;
  }
}
