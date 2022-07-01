import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IEncadreur, Encadreur } from '../encadreur.model';
import { EncadreurService } from '../service/encadreur.service';

@Injectable({ providedIn: 'root' })
export class EncadreurRoutingResolveService implements Resolve<IEncadreur> {
  constructor(protected service: EncadreurService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IEncadreur> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((encadreur: HttpResponse<Encadreur>) => {
          if (encadreur.body) {
            return of(encadreur.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Encadreur());
  }
}
