import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISoutenance, Soutenance } from '../soutenance.model';
import { SoutenanceService } from '../service/soutenance.service';

@Injectable({ providedIn: 'root' })
export class SoutenanceRoutingResolveService implements Resolve<ISoutenance> {
  constructor(protected service: SoutenanceService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISoutenance> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((soutenance: HttpResponse<Soutenance>) => {
          if (soutenance.body) {
            return of(soutenance.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Soutenance());
  }
}
