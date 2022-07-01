import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { HoraireComponent } from './list/horaire.component';
import { HoraireDetailComponent } from './detail/horaire-detail.component';
import { HoraireUpdateComponent } from './update/horaire-update.component';
import { HoraireDeleteDialogComponent } from './delete/horaire-delete-dialog.component';
import { HoraireRoutingModule } from './route/horaire-routing.module';

@NgModule({
  imports: [SharedModule, HoraireRoutingModule],
  declarations: [HoraireComponent, HoraireDetailComponent, HoraireUpdateComponent, HoraireDeleteDialogComponent],
  entryComponents: [HoraireDeleteDialogComponent],
})
export class HoraireModule {}
