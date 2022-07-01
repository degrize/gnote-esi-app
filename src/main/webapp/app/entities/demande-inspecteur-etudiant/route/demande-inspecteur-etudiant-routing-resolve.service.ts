import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDemandeInspecteurEtudiant, DemandeInspecteurEtudiant } from '../demande-inspecteur-etudiant.model';
import { DemandeInspecteurEtudiantService } from '../service/demande-inspecteur-etudiant.service';

@Injectable({ providedIn: 'root' })
export class DemandeInspecteurEtudiantRoutingResolveService implements Resolve<IDemandeInspecteurEtudiant> {
  constructor(protected service: DemandeInspecteurEtudiantService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDemandeInspecteurEtudiant> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((demandeInspecteurEtudiant: HttpResponse<DemandeInspecteurEtudiant>) => {
          if (demandeInspecteurEtudiant.body) {
            return of(demandeInspecteurEtudiant.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new DemandeInspecteurEtudiant());
  }
}
