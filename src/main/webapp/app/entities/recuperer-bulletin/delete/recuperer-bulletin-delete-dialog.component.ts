import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IRecupererBulletin } from '../recuperer-bulletin.model';
import { RecupererBulletinService } from '../service/recuperer-bulletin.service';

@Component({
  templateUrl: './recuperer-bulletin-delete-dialog.component.html',
})
export class RecupererBulletinDeleteDialogComponent {
  recupererBulletin?: IRecupererBulletin;

  constructor(protected recupererBulletinService: RecupererBulletinService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.recupererBulletinService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
