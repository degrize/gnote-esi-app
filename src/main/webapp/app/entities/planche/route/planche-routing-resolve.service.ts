import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPlanche, Planche } from '../planche.model';
import { PlancheService } from '../service/planche.service';

@Injectable({ providedIn: 'root' })
export class PlancheRoutingResolveService implements Resolve<IPlanche> {
  constructor(protected service: PlancheService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPlanche> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((planche: HttpResponse<Planche>) => {
          if (planche.body) {
            return of(planche.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Planche());
  }
}
