import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IRecupererBulletin, RecupererBulletin } from '../recuperer-bulletin.model';
import { RecupererBulletinService } from '../service/recuperer-bulletin.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IEtudiant } from 'app/entities/etudiant/etudiant.model';
import { EtudiantService } from 'app/entities/etudiant/service/etudiant.service';
import { IBulletin } from 'app/entities/bulletin/bulletin.model';
import { BulletinService } from 'app/entities/bulletin/service/bulletin.service';

@Component({
  selector: 'jhi-recuperer-bulletin-update',
  templateUrl: './recuperer-bulletin-update.component.html',
})
export class RecupererBulletinUpdateComponent implements OnInit {
  isSaving = false;

  etudiantsSharedCollection: IEtudiant[] = [];
  bulletinsSharedCollection: IBulletin[] = [];

  editForm = this.fb.group({
    id: [],
    signatureEleve: [null, [Validators.required]],
    bulletinScanne: [null, [Validators.required]],
    bulletinScanneContentType: [],
    etudiant: [],
    bulletin: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected recupererBulletinService: RecupererBulletinService,
    protected etudiantService: EtudiantService,
    protected bulletinService: BulletinService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ recupererBulletin }) => {
      this.updateForm(recupererBulletin);

      this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('gnoteEsiApp.error', { ...err, key: 'error.file.' + err.key })),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const recupererBulletin = this.createFromForm();
    if (recupererBulletin.id !== undefined) {
      this.subscribeToSaveResponse(this.recupererBulletinService.update(recupererBulletin));
    } else {
      this.subscribeToSaveResponse(this.recupererBulletinService.create(recupererBulletin));
    }
  }

  trackEtudiantById(_index: number, item: IEtudiant): number {
    return item.id!;
  }

  trackBulletinById(_index: number, item: IBulletin): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRecupererBulletin>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(recupererBulletin: IRecupererBulletin): void {
    this.editForm.patchValue({
      id: recupererBulletin.id,
      signatureEleve: recupererBulletin.signatureEleve,
      bulletinScanne: recupererBulletin.bulletinScanne,
      bulletinScanneContentType: recupererBulletin.bulletinScanneContentType,
      etudiant: recupererBulletin.etudiant,
      bulletin: recupererBulletin.bulletin,
    });

    this.etudiantsSharedCollection = this.etudiantService.addEtudiantToCollectionIfMissing(
      this.etudiantsSharedCollection,
      recupererBulletin.etudiant
    );
    this.bulletinsSharedCollection = this.bulletinService.addBulletinToCollectionIfMissing(
      this.bulletinsSharedCollection,
      recupererBulletin.bulletin
    );
  }

  protected loadRelationshipsOptions(): void {
    this.etudiantService
      .query()
      .pipe(map((res: HttpResponse<IEtudiant[]>) => res.body ?? []))
      .pipe(
        map((etudiants: IEtudiant[]) =>
          this.etudiantService.addEtudiantToCollectionIfMissing(etudiants, this.editForm.get('etudiant')!.value)
        )
      )
      .subscribe((etudiants: IEtudiant[]) => (this.etudiantsSharedCollection = etudiants));

    this.bulletinService
      .query()
      .pipe(map((res: HttpResponse<IBulletin[]>) => res.body ?? []))
      .pipe(
        map((bulletins: IBulletin[]) =>
          this.bulletinService.addBulletinToCollectionIfMissing(bulletins, this.editForm.get('bulletin')!.value)
        )
      )
      .subscribe((bulletins: IBulletin[]) => (this.bulletinsSharedCollection = bulletins));
  }

  protected createFromForm(): IRecupererBulletin {
    return {
      ...new RecupererBulletin(),
      id: this.editForm.get(['id'])!.value,
      signatureEleve: this.editForm.get(['signatureEleve'])!.value,
      bulletinScanneContentType: this.editForm.get(['bulletinScanneContentType'])!.value,
      bulletinScanne: this.editForm.get(['bulletinScanne'])!.value,
      etudiant: this.editForm.get(['etudiant'])!.value,
      bulletin: this.editForm.get(['bulletin'])!.value,
    };
  }
}
