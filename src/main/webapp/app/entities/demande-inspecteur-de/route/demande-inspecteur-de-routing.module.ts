import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { DemandeInspecteurDEComponent } from '../list/demande-inspecteur-de.component';
import { DemandeInspecteurDEDetailComponent } from '../detail/demande-inspecteur-de-detail.component';
import { DemandeInspecteurDEUpdateComponent } from '../update/demande-inspecteur-de-update.component';
import { DemandeInspecteurDERoutingResolveService } from './demande-inspecteur-de-routing-resolve.service';

const demandeInspecteurDERoute: Routes = [
  {
    path: '',
    component: DemandeInspecteurDEComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DemandeInspecteurDEDetailComponent,
    resolve: {
      demandeInspecteurDE: DemandeInspecteurDERoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DemandeInspecteurDEUpdateComponent,
    resolve: {
      demandeInspecteurDE: DemandeInspecteurDERoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DemandeInspecteurDEUpdateComponent,
    resolve: {
      demandeInspecteurDE: DemandeInspecteurDERoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(demandeInspecteurDERoute)],
  exports: [RouterModule],
})
export class DemandeInspecteurDERoutingModule {}
