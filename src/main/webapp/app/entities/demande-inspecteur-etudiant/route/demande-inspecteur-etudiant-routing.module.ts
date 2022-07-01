import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { DemandeInspecteurEtudiantComponent } from '../list/demande-inspecteur-etudiant.component';
import { DemandeInspecteurEtudiantDetailComponent } from '../detail/demande-inspecteur-etudiant-detail.component';
import { DemandeInspecteurEtudiantUpdateComponent } from '../update/demande-inspecteur-etudiant-update.component';
import { DemandeInspecteurEtudiantRoutingResolveService } from './demande-inspecteur-etudiant-routing-resolve.service';

const demandeInspecteurEtudiantRoute: Routes = [
  {
    path: '',
    component: DemandeInspecteurEtudiantComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DemandeInspecteurEtudiantDetailComponent,
    resolve: {
      demandeInspecteurEtudiant: DemandeInspecteurEtudiantRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DemandeInspecteurEtudiantUpdateComponent,
    resolve: {
      demandeInspecteurEtudiant: DemandeInspecteurEtudiantRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DemandeInspecteurEtudiantUpdateComponent,
    resolve: {
      demandeInspecteurEtudiant: DemandeInspecteurEtudiantRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(demandeInspecteurEtudiantRoute)],
  exports: [RouterModule],
})
export class DemandeInspecteurEtudiantRoutingModule {}
