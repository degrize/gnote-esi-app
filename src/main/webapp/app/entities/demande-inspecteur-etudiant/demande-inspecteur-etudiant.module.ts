import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { DemandeInspecteurEtudiantComponent } from './list/demande-inspecteur-etudiant.component';
import { DemandeInspecteurEtudiantDetailComponent } from './detail/demande-inspecteur-etudiant-detail.component';
import { DemandeInspecteurEtudiantUpdateComponent } from './update/demande-inspecteur-etudiant-update.component';
import { DemandeInspecteurEtudiantDeleteDialogComponent } from './delete/demande-inspecteur-etudiant-delete-dialog.component';
import { DemandeInspecteurEtudiantRoutingModule } from './route/demande-inspecteur-etudiant-routing.module';

@NgModule({
  imports: [SharedModule, DemandeInspecteurEtudiantRoutingModule],
  declarations: [
    DemandeInspecteurEtudiantComponent,
    DemandeInspecteurEtudiantDetailComponent,
    DemandeInspecteurEtudiantUpdateComponent,
    DemandeInspecteurEtudiantDeleteDialogComponent,
  ],
  entryComponents: [DemandeInspecteurEtudiantDeleteDialogComponent],
})
export class DemandeInspecteurEtudiantModule {}
