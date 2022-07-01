import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IDemandeInspecteurEtudiant } from '../demande-inspecteur-etudiant.model';
import { DemandeInspecteurEtudiantService } from '../service/demande-inspecteur-etudiant.service';

@Component({
  templateUrl: './demande-inspecteur-etudiant-delete-dialog.component.html',
})
export class DemandeInspecteurEtudiantDeleteDialogComponent {
  demandeInspecteurEtudiant?: IDemandeInspecteurEtudiant;

  constructor(protected demandeInspecteurEtudiantService: DemandeInspecteurEtudiantService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.demandeInspecteurEtudiantService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
