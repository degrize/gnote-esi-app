import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IEncadreur } from '../encadreur.model';
import { EncadreurService } from '../service/encadreur.service';

@Component({
  templateUrl: './encadreur-delete-dialog.component.html',
})
export class EncadreurDeleteDialogComponent {
  encadreur?: IEncadreur;

  constructor(protected encadreurService: EncadreurService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.encadreurService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
