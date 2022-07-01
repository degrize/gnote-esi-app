import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { HoraireComponent } from '../list/horaire.component';
import { HoraireDetailComponent } from '../detail/horaire-detail.component';
import { HoraireUpdateComponent } from '../update/horaire-update.component';
import { HoraireRoutingResolveService } from './horaire-routing-resolve.service';

const horaireRoute: Routes = [
  {
    path: '',
    component: HoraireComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: HoraireDetailComponent,
    resolve: {
      horaire: HoraireRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: HoraireUpdateComponent,
    resolve: {
      horaire: HoraireRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: HoraireUpdateComponent,
    resolve: {
      horaire: HoraireRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(horaireRoute)],
  exports: [RouterModule],
})
export class HoraireRoutingModule {}
