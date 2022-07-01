import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IHoraire } from '../horaire.model';
import { HoraireService } from '../service/horaire.service';

@Component({
  templateUrl: './horaire-delete-dialog.component.html',
})
export class HoraireDeleteDialogComponent {
  horaire?: IHoraire;

  constructor(protected horaireService: HoraireService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.horaireService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
