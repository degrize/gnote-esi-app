import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { SoutenanceComponent } from './list/soutenance.component';
import { SoutenanceDetailComponent } from './detail/soutenance-detail.component';
import { SoutenanceUpdateComponent } from './update/soutenance-update.component';
import { SoutenanceDeleteDialogComponent } from './delete/soutenance-delete-dialog.component';
import { SoutenanceRoutingModule } from './route/soutenance-routing.module';

@NgModule({
  imports: [SharedModule, SoutenanceRoutingModule],
  declarations: [SoutenanceComponent, SoutenanceDetailComponent, SoutenanceUpdateComponent, SoutenanceDeleteDialogComponent],
  entryComponents: [SoutenanceDeleteDialogComponent],
})
export class SoutenanceModule {}
