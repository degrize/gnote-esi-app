import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { DemandeInspecteurDEComponent } from './list/demande-inspecteur-de.component';
import { DemandeInspecteurDEDetailComponent } from './detail/demande-inspecteur-de-detail.component';
import { DemandeInspecteurDEUpdateComponent } from './update/demande-inspecteur-de-update.component';
import { DemandeInspecteurDEDeleteDialogComponent } from './delete/demande-inspecteur-de-delete-dialog.component';
import { DemandeInspecteurDERoutingModule } from './route/demande-inspecteur-de-routing.module';

@NgModule({
  imports: [SharedModule, DemandeInspecteurDERoutingModule],
  declarations: [
    DemandeInspecteurDEComponent,
    DemandeInspecteurDEDetailComponent,
    DemandeInspecteurDEUpdateComponent,
    DemandeInspecteurDEDeleteDialogComponent,
  ],
  entryComponents: [DemandeInspecteurDEDeleteDialogComponent],
})
export class DemandeInspecteurDEModule {}
