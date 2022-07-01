import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SoutenanceComponent } from '../list/soutenance.component';
import { SoutenanceDetailComponent } from '../detail/soutenance-detail.component';
import { SoutenanceUpdateComponent } from '../update/soutenance-update.component';
import { SoutenanceRoutingResolveService } from './soutenance-routing-resolve.service';

const soutenanceRoute: Routes = [
  {
    path: '',
    component: SoutenanceComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SoutenanceDetailComponent,
    resolve: {
      soutenance: SoutenanceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SoutenanceUpdateComponent,
    resolve: {
      soutenance: SoutenanceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SoutenanceUpdateComponent,
    resolve: {
      soutenance: SoutenanceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(soutenanceRoute)],
  exports: [RouterModule],
})
export class SoutenanceRoutingModule {}
