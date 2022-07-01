import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PlancheComponent } from '../list/planche.component';
import { PlancheDetailComponent } from '../detail/planche-detail.component';
import { PlancheUpdateComponent } from '../update/planche-update.component';
import { PlancheRoutingResolveService } from './planche-routing-resolve.service';

const plancheRoute: Routes = [
  {
    path: '',
    component: PlancheComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PlancheDetailComponent,
    resolve: {
      planche: PlancheRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PlancheUpdateComponent,
    resolve: {
      planche: PlancheRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PlancheUpdateComponent,
    resolve: {
      planche: PlancheRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(plancheRoute)],
  exports: [RouterModule],
})
export class PlancheRoutingModule {}
