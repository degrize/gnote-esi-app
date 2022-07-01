import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ISoutenance } from '../soutenance.model';
import { SoutenanceService } from '../service/soutenance.service';

@Component({
  templateUrl: './soutenance-delete-dialog.component.html',
})
export class SoutenanceDeleteDialogComponent {
  soutenance?: ISoutenance;

  constructor(protected soutenanceService: SoutenanceService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.soutenanceService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
