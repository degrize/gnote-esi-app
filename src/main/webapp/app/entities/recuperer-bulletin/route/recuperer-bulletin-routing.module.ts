import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { RecupererBulletinComponent } from '../list/recuperer-bulletin.component';
import { RecupererBulletinDetailComponent } from '../detail/recuperer-bulletin-detail.component';
import { RecupererBulletinUpdateComponent } from '../update/recuperer-bulletin-update.component';
import { RecupererBulletinRoutingResolveService } from './recuperer-bulletin-routing-resolve.service';

const recupererBulletinRoute: Routes = [
  {
    path: '',
    component: RecupererBulletinComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: RecupererBulletinDetailComponent,
    resolve: {
      recupererBulletin: RecupererBulletinRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: RecupererBulletinUpdateComponent,
    resolve: {
      recupererBulletin: RecupererBulletinRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: RecupererBulletinUpdateComponent,
    resolve: {
      recupererBulletin: RecupererBulletinRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(recupererBulletinRoute)],
  exports: [RouterModule],
})
export class RecupererBulletinRoutingModule {}
