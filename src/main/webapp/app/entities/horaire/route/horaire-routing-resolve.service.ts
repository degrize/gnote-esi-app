import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IHoraire, Horaire } from '../horaire.model';
import { HoraireService } from '../service/horaire.service';

@Injectable({ providedIn: 'root' })
export class HoraireRoutingResolveService implements Resolve<IHoraire> {
  constructor(protected service: HoraireService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IHoraire> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((horaire: HttpResponse<Horaire>) => {
          if (horaire.body) {
            return of(horaire.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Horaire());
  }
}
