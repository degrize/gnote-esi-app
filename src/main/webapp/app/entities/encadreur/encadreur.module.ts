import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { EncadreurComponent } from './list/encadreur.component';
import { EncadreurDetailComponent } from './detail/encadreur-detail.component';
import { EncadreurUpdateComponent } from './update/encadreur-update.component';
import { EncadreurDeleteDialogComponent } from './delete/encadreur-delete-dialog.component';
import { EncadreurRoutingModule } from './route/encadreur-routing.module';

@NgModule({
  imports: [SharedModule, EncadreurRoutingModule],
  declarations: [EncadreurComponent, EncadreurDetailComponent, EncadreurUpdateComponent, EncadreurDeleteDialogComponent],
  entryComponents: [EncadreurDeleteDialogComponent],
})
export class EncadreurModule {}
