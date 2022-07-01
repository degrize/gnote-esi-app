import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IJury } from '../jury.model';
import { JuryService } from '../service/jury.service';

@Component({
  templateUrl: './jury-delete-dialog.component.html',
})
export class JuryDeleteDialogComponent {
  jury?: IJury;

  constructor(protected juryService: JuryService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.juryService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
