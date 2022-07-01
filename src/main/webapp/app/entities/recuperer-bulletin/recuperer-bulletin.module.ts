import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { RecupererBulletinComponent } from './list/recuperer-bulletin.component';
import { RecupererBulletinDetailComponent } from './detail/recuperer-bulletin-detail.component';
import { RecupererBulletinUpdateComponent } from './update/recuperer-bulletin-update.component';
import { RecupererBulletinDeleteDialogComponent } from './delete/recuperer-bulletin-delete-dialog.component';
import { RecupererBulletinRoutingModule } from './route/recuperer-bulletin-routing.module';

@NgModule({
  imports: [SharedModule, RecupererBulletinRoutingModule],
  declarations: [
    RecupererBulletinComponent,
    RecupererBulletinDetailComponent,
    RecupererBulletinUpdateComponent,
    RecupererBulletinDeleteDialogComponent,
  ],
  entryComponents: [RecupererBulletinDeleteDialogComponent],
})
export class RecupererBulletinModule {}
