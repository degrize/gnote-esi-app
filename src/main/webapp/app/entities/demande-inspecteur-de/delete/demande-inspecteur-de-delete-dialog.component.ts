import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IDemandeInspecteurDE } from '../demande-inspecteur-de.model';
import { DemandeInspecteurDEService } from '../service/demande-inspecteur-de.service';

@Component({
  templateUrl: './demande-inspecteur-de-delete-dialog.component.html',
})
export class DemandeInspecteurDEDeleteDialogComponent {
  demandeInspecteurDE?: IDemandeInspecteurDE;

  constructor(protected demandeInspecteurDEService: DemandeInspecteurDEService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.demandeInspecteurDEService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
