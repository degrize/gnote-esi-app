import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IJury, Jury } from '../jury.model';
import { JuryService } from '../service/jury.service';

@Injectable({ providedIn: 'root' })
export class JuryRoutingResolveService implements Resolve<IJury> {
  constructor(protected service: JuryService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IJury> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((jury: HttpResponse<Jury>) => {
          if (jury.body) {
            return of(jury.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Jury());
  }
}
