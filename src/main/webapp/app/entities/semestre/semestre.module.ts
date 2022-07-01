import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { SemestreComponent } from './list/semestre.component';
import { SemestreDetailComponent } from './detail/semestre-detail.component';
import { SemestreUpdateComponent } from './update/semestre-update.component';
import { SemestreDeleteDialogComponent } from './delete/semestre-delete-dialog.component';
import { SemestreRoutingModule } from './route/semestre-routing.module';

@NgModule({
  imports: [SharedModule, SemestreRoutingModule],
  declarations: [SemestreComponent, SemestreDetailComponent, SemestreUpdateComponent, SemestreDeleteDialogComponent],
  entryComponents: [SemestreDeleteDialogComponent],
})
export class SemestreModule {}
