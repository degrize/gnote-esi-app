import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICycle } from '../cycle.model';
import { CycleService } from '../service/cycle.service';

@Component({
  templateUrl: './cycle-delete-dialog.component.html',
})
export class CycleDeleteDialogComponent {
  cycle?: ICycle;

  constructor(protected cycleService: CycleService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.cycleService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
