import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { EncadreurComponent } from '../list/encadreur.component';
import { EncadreurDetailComponent } from '../detail/encadreur-detail.component';
import { EncadreurUpdateComponent } from '../update/encadreur-update.component';
import { EncadreurRoutingResolveService } from './encadreur-routing-resolve.service';

const encadreurRoute: Routes = [
  {
    path: '',
    component: EncadreurComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: EncadreurDetailComponent,
    resolve: {
      encadreur: EncadreurRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: EncadreurUpdateComponent,
    resolve: {
      encadreur: EncadreurRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: EncadreurUpdateComponent,
    resolve: {
      encadreur: EncadreurRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(encadreurRoute)],
  exports: [RouterModule],
})
export class EncadreurRoutingModule {}
