import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDemandeInspecteurDE, getDemandeInspecteurDEIdentifier } from '../demande-inspecteur-de.model';

export type EntityResponseType = HttpResponse<IDemandeInspecteurDE>;
export type EntityArrayResponseType = HttpResponse<IDemandeInspecteurDE[]>;

@Injectable({ providedIn: 'root' })
export class DemandeInspecteurDEService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/demande-inspecteur-des');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(demandeInspecteurDE: IDemandeInspecteurDE): Observable<EntityResponseType> {
    return this.http.post<IDemandeInspecteurDE>(this.resourceUrl, demandeInspecteurDE, { observe: 'response' });
  }

  update(demandeInspecteurDE: IDemandeInspecteurDE): Observable<EntityResponseType> {
    return this.http.put<IDemandeInspecteurDE>(
      `${this.resourceUrl}/${getDemandeInspecteurDEIdentifier(demandeInspecteurDE) as number}`,
      demandeInspecteurDE,
      { observe: 'response' }
    );
  }

  partialUpdate(demandeInspecteurDE: IDemandeInspecteurDE): Observable<EntityResponseType> {
    return this.http.patch<IDemandeInspecteurDE>(
      `${this.resourceUrl}/${getDemandeInspecteurDEIdentifier(demandeInspecteurDE) as number}`,
      demandeInspecteurDE,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IDemandeInspecteurDE>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDemandeInspecteurDE[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addDemandeInspecteurDEToCollectionIfMissing(
    demandeInspecteurDECollection: IDemandeInspecteurDE[],
    ...demandeInspecteurDESToCheck: (IDemandeInspecteurDE | null | undefined)[]
  ): IDemandeInspecteurDE[] {
    const demandeInspecteurDES: IDemandeInspecteurDE[] = demandeInspecteurDESToCheck.filter(isPresent);
    if (demandeInspecteurDES.length > 0) {
      const demandeInspecteurDECollectionIdentifiers = demandeInspecteurDECollection.map(
        demandeInspecteurDEItem => getDemandeInspecteurDEIdentifier(demandeInspecteurDEItem)!
      );
      const demandeInspecteurDESToAdd = demandeInspecteurDES.filter(demandeInspecteurDEItem => {
        const demandeInspecteurDEIdentifier = getDemandeInspecteurDEIdentifier(demandeInspecteurDEItem);
        if (demandeInspecteurDEIdentifier == null || demandeInspecteurDECollectionIdentifiers.includes(demandeInspecteurDEIdentifier)) {
          return false;
        }
        demandeInspecteurDECollectionIdentifiers.push(demandeInspecteurDEIdentifier);
        return true;
      });
      return [...demandeInspecteurDESToAdd, ...demandeInspecteurDECollection];
    }
    return demandeInspecteurDECollection;
  }
}
