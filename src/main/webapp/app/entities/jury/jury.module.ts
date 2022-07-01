import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { JuryComponent } from './list/jury.component';
import { JuryDetailComponent } from './detail/jury-detail.component';
import { JuryUpdateComponent } from './update/jury-update.component';
import { JuryDeleteDialogComponent } from './delete/jury-delete-dialog.component';
import { JuryRoutingModule } from './route/jury-routing.module';

@NgModule({
  imports: [SharedModule, JuryRoutingModule],
  declarations: [JuryComponent, JuryDetailComponent, JuryUpdateComponent, JuryDeleteDialogComponent],
  entryComponents: [JuryDeleteDialogComponent],
})
export class JuryModule {}
