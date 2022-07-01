import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IRecupererBulletin, RecupererBulletin } from '../recuperer-bulletin.model';
import { RecupererBulletinService } from '../service/recuperer-bulletin.service';

@Injectable({ providedIn: 'root' })
export class RecupererBulletinRoutingResolveService implements Resolve<IRecupererBulletin> {
  constructor(protected service: RecupererBulletinService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IRecupererBulletin> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((recupererBulletin: HttpResponse<RecupererBulletin>) => {
          if (recupererBulletin.body) {
            return of(recupererBulletin.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new RecupererBulletin());
  }
}
