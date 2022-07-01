import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CycleComponent } from '../list/cycle.component';
import { CycleDetailComponent } from '../detail/cycle-detail.component';
import { CycleUpdateComponent } from '../update/cycle-update.component';
import { CycleRoutingResolveService } from './cycle-routing-resolve.service';

const cycleRoute: Routes = [
  {
    path: '',
    component: CycleComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CycleDetailComponent,
    resolve: {
      cycle: CycleRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CycleUpdateComponent,
    resolve: {
      cycle: CycleRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CycleUpdateComponent,
    resolve: {
      cycle: CycleRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(cycleRoute)],
  exports: [RouterModule],
})
export class CycleRoutingModule {}
