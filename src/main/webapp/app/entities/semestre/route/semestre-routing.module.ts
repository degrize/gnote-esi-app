import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SemestreComponent } from '../list/semestre.component';
import { SemestreDetailComponent } from '../detail/semestre-detail.component';
import { SemestreUpdateComponent } from '../update/semestre-update.component';
import { SemestreRoutingResolveService } from './semestre-routing-resolve.service';

const semestreRoute: Routes = [
  {
    path: '',
    component: SemestreComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SemestreDetailComponent,
    resolve: {
      semestre: SemestreRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SemestreUpdateComponent,
    resolve: {
      semestre: SemestreRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SemestreUpdateComponent,
    resolve: {
      semestre: SemestreRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(semestreRoute)],
  exports: [RouterModule],
})
export class SemestreRoutingModule {}
