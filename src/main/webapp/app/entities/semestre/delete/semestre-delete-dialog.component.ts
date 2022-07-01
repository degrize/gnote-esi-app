import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ISemestre } from '../semestre.model';
import { SemestreService } from '../service/semestre.service';

@Component({
  templateUrl: './semestre-delete-dialog.component.html',
})
export class SemestreDeleteDialogComponent {
  semestre?: ISemestre;

  constructor(protected semestreService: SemestreService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.semestreService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
