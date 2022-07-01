import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICycle, Cycle } from '../cycle.model';
import { CycleService } from '../service/cycle.service';

@Injectable({ providedIn: 'root' })
export class CycleRoutingResolveService implements Resolve<ICycle> {
  constructor(protected service: CycleService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICycle> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((cycle: HttpResponse<Cycle>) => {
          if (cycle.body) {
            return of(cycle.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Cycle());
  }
}
