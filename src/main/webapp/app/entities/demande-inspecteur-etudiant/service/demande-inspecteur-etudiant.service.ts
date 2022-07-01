import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDemandeInspecteurEtudiant, getDemandeInspecteurEtudiantIdentifier } from '../demande-inspecteur-etudiant.model';

export type EntityResponseType = HttpResponse<IDemandeInspecteurEtudiant>;
export type EntityArrayResponseType = HttpResponse<IDemandeInspecteurEtudiant[]>;

@Injectable({ providedIn: 'root' })
export class DemandeInspecteurEtudiantService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/demande-inspecteur-etudiants');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(demandeInspecteurEtudiant: IDemandeInspecteurEtudiant): Observable<EntityResponseType> {
    return this.http.post<IDemandeInspecteurEtudiant>(this.resourceUrl, demandeInspecteurEtudiant, { observe: 'response' });
  }

  update(demandeInspecteurEtudiant: IDemandeInspecteurEtudiant): Observable<EntityResponseType> {
    return this.http.put<IDemandeInspecteurEtudiant>(
      `${this.resourceUrl}/${getDemandeInspecteurEtudiantIdentifier(demandeInspecteurEtudiant) as number}`,
      demandeInspecteurEtudiant,
      { observe: 'response' }
    );
  }

  partialUpdate(demandeInspecteurEtudiant: IDemandeInspecteurEtudiant): Observable<EntityResponseType> {
    return this.http.patch<IDemandeInspecteurEtudiant>(
      `${this.resourceUrl}/${getDemandeInspecteurEtudiantIdentifier(demandeInspecteurEtudiant) as number}`,
      demandeInspecteurEtudiant,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IDemandeInspecteurEtudiant>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDemandeInspecteurEtudiant[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addDemandeInspecteurEtudiantToCollectionIfMissing(
    demandeInspecteurEtudiantCollection: IDemandeInspecteurEtudiant[],
    ...demandeInspecteurEtudiantsToCheck: (IDemandeInspecteurEtudiant | null | undefined)[]
  ): IDemandeInspecteurEtudiant[] {
    const demandeInspecteurEtudiants: IDemandeInspecteurEtudiant[] = demandeInspecteurEtudiantsToCheck.filter(isPresent);
    if (demandeInspecteurEtudiants.length > 0) {
      const demandeInspecteurEtudiantCollectionIdentifiers = demandeInspecteurEtudiantCollection.map(
        demandeInspecteurEtudiantItem => getDemandeInspecteurEtudiantIdentifier(demandeInspecteurEtudiantItem)!
      );
      const demandeInspecteurEtudiantsToAdd = demandeInspecteurEtudiants.filter(demandeInspecteurEtudiantItem => {
        const demandeInspecteurEtudiantIdentifier = getDemandeInspecteurEtudiantIdentifier(demandeInspecteurEtudiantItem);
        if (
          demandeInspecteurEtudiantIdentifier == null ||
          demandeInspecteurEtudiantCollectionIdentifiers.includes(demandeInspecteurEtudiantIdentifier)
        ) {
          return false;
        }
        demandeInspecteurEtudiantCollectionIdentifiers.push(demandeInspecteurEtudiantIdentifier);
        return true;
      });
      return [...demandeInspecteurEtudiantsToAdd, ...demandeInspecteurEtudiantCollection];
    }
    return demandeInspecteurEtudiantCollection;
  }
}
