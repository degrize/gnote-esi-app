import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { JuryComponent } from '../list/jury.component';
import { JuryDetailComponent } from '../detail/jury-detail.component';
import { JuryUpdateComponent } from '../update/jury-update.component';
import { JuryRoutingResolveService } from './jury-routing-resolve.service';

const juryRoute: Routes = [
  {
    path: '',
    component: JuryComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: JuryDetailComponent,
    resolve: {
      jury: JuryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: JuryUpdateComponent,
    resolve: {
      jury: JuryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: JuryUpdateComponent,
    resolve: {
      jury: JuryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(juryRoute)],
  exports: [RouterModule],
})
export class JuryRoutingModule {}
