import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IAbsence, Absence } from '../absence.model';
import { AbsenceService } from '../service/absence.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IProfesseur } from 'app/entities/professeur/professeur.model';
import { ProfesseurService } from 'app/entities/professeur/service/professeur.service';
import { IInspecteur } from 'app/entities/inspecteur/inspecteur.model';
import { InspecteurService } from 'app/entities/inspecteur/service/inspecteur.service';
import { IMatiere } from 'app/entities/matiere/matiere.model';
import { MatiereService } from 'app/entities/matiere/service/matiere.service';
import { IEtudiant } from 'app/entities/etudiant/etudiant.model';
import { EtudiantService } from 'app/entities/etudiant/service/etudiant.service';

@Component({
  selector: 'jhi-absence-update',
  templateUrl: './absence-update.component.html',
})
export class AbsenceUpdateComponent implements OnInit {
  isSaving = false;

  professeursSharedCollection: IProfesseur[] = [];
  inspecteursSharedCollection: IInspecteur[] = [];
  matieresSharedCollection: IMatiere[] = [];
  etudiantsSharedCollection: IEtudiant[] = [];

  editForm = this.fb.group({
    id: [],
    etat: [null, [Validators.required]],
    heureDebut: [],
    heureFin: [],
    justificationEcrit: [],
    justificationNumerique: [],
    justificationNumeriqueContentType: [],
    professeur: [],
    inspecteur: [],
    matiere: [],
    etudiant: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected absenceService: AbsenceService,
    protected professeurService: ProfesseurService,
    protected inspecteurService: InspecteurService,
    protected matiereService: MatiereService,
    protected etudiantService: EtudiantService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ absence }) => {
      this.updateForm(absence);

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
    const absence = this.createFromForm();
    if (absence.id !== undefined) {
      this.subscribeToSaveResponse(this.absenceService.update(absence));
    } else {
      this.subscribeToSaveResponse(this.absenceService.create(absence));
    }
  }

  trackProfesseurById(_index: number, item: IProfesseur): number {
    return item.id!;
  }

  trackInspecteurById(_index: number, item: IInspecteur): number {
    return item.id!;
  }

  trackMatiereById(_index: number, item: IMatiere): number {
    return item.id!;
  }

  trackEtudiantById(_index: number, item: IEtudiant): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAbsence>>): void {
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

  protected updateForm(absence: IAbsence): void {
    this.editForm.patchValue({
      id: absence.id,
      etat: absence.etat,
      heureDebut: absence.heureDebut,
      heureFin: absence.heureFin,
      justificationEcrit: absence.justificationEcrit,
      justificationNumerique: absence.justificationNumerique,
      justificationNumeriqueContentType: absence.justificationNumeriqueContentType,
      professeur: absence.professeur,
      inspecteur: absence.inspecteur,
      matiere: absence.matiere,
      etudiant: absence.etudiant,
    });

    this.professeursSharedCollection = this.professeurService.addProfesseurToCollectionIfMissing(
      this.professeursSharedCollection,
      absence.professeur
    );
    this.inspecteursSharedCollection = this.inspecteurService.addInspecteurToCollectionIfMissing(
      this.inspecteursSharedCollection,
      absence.inspecteur
    );
    this.matieresSharedCollection = this.matiereService.addMatiereToCollectionIfMissing(this.matieresSharedCollection, absence.matiere);
    this.etudiantsSharedCollection = this.etudiantService.addEtudiantToCollectionIfMissing(
      this.etudiantsSharedCollection,
      absence.etudiant
    );
  }

  protected loadRelationshipsOptions(): void {
    this.professeurService
      .query()
      .pipe(map((res: HttpResponse<IProfesseur[]>) => res.body ?? []))
      .pipe(
        map((professeurs: IProfesseur[]) =>
          this.professeurService.addProfesseurToCollectionIfMissing(professeurs, this.editForm.get('professeur')!.value)
        )
      )
      .subscribe((professeurs: IProfesseur[]) => (this.professeursSharedCollection = professeurs));

    this.inspecteurService
      .query()
      .pipe(map((res: HttpResponse<IInspecteur[]>) => res.body ?? []))
      .pipe(
        map((inspecteurs: IInspecteur[]) =>
          this.inspecteurService.addInspecteurToCollectionIfMissing(inspecteurs, this.editForm.get('inspecteur')!.value)
        )
      )
      .subscribe((inspecteurs: IInspecteur[]) => (this.inspecteursSharedCollection = inspecteurs));

    this.matiereService
      .query()
      .pipe(map((res: HttpResponse<IMatiere[]>) => res.body ?? []))
      .pipe(
        map((matieres: IMatiere[]) => this.matiereService.addMatiereToCollectionIfMissing(matieres, this.editForm.get('matiere')!.value))
      )
      .subscribe((matieres: IMatiere[]) => (this.matieresSharedCollection = matieres));

    this.etudiantService
      .query()
      .pipe(map((res: HttpResponse<IEtudiant[]>) => res.body ?? []))
      .pipe(
        map((etudiants: IEtudiant[]) =>
          this.etudiantService.addEtudiantToCollectionIfMissing(etudiants, this.editForm.get('etudiant')!.value)
        )
      )
      .subscribe((etudiants: IEtudiant[]) => (this.etudiantsSharedCollection = etudiants));
  }

  protected createFromForm(): IAbsence {
    return {
      ...new Absence(),
      id: this.editForm.get(['id'])!.value,
      etat: this.editForm.get(['etat'])!.value,
      heureDebut: this.editForm.get(['heureDebut'])!.value,
      heureFin: this.editForm.get(['heureFin'])!.value,
      justificationEcrit: this.editForm.get(['justificationEcrit'])!.value,
      justificationNumeriqueContentType: this.editForm.get(['justificationNumeriqueContentType'])!.value,
      justificationNumerique: this.editForm.get(['justificationNumerique'])!.value,
      professeur: this.editForm.get(['professeur'])!.value,
      inspecteur: this.editForm.get(['inspecteur'])!.value,
      matiere: this.editForm.get(['matiere'])!.value,
      etudiant: this.editForm.get(['etudiant'])!.value,
    };
  }
}
