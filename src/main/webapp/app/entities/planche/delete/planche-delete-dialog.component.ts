import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPlanche } from '../planche.model';
import { PlancheService } from '../service/planche.service';

@Component({
  templateUrl: './planche-delete-dialog.component.html',
})
export class PlancheDeleteDialogComponent {
  planche?: IPlanche;

  constructor(protected plancheService: PlancheService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.plancheService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
