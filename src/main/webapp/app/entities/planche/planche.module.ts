import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PlancheComponent } from './list/planche.component';
import { PlancheDetailComponent } from './detail/planche-detail.component';
import { PlancheUpdateComponent } from './update/planche-update.component';
import { PlancheDeleteDialogComponent } from './delete/planche-delete-dialog.component';
import { PlancheRoutingModule } from './route/planche-routing.module';

@NgModule({
  imports: [SharedModule, PlancheRoutingModule],
  declarations: [PlancheComponent, PlancheDetailComponent, PlancheUpdateComponent, PlancheDeleteDialogComponent],
  entryComponents: [PlancheDeleteDialogComponent],
})
export class PlancheModule {}
