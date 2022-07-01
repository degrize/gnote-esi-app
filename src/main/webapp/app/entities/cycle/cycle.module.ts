import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CycleComponent } from './list/cycle.component';
import { CycleDetailComponent } from './detail/cycle-detail.component';
import { CycleUpdateComponent } from './update/cycle-update.component';
import { CycleDeleteDialogComponent } from './delete/cycle-delete-dialog.component';
import { CycleRoutingModule } from './route/cycle-routing.module';

@NgModule({
  imports: [SharedModule, CycleRoutingModule],
  declarations: [CycleComponent, CycleDetailComponent, CycleUpdateComponent, CycleDeleteDialogComponent],
  entryComponents: [CycleDeleteDialogComponent],
})
export class CycleModule {}
