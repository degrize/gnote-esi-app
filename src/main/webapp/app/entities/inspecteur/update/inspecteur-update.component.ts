import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IInspecteur, Inspecteur } from '../inspecteur.model';
import { InspecteurService } from '../service/inspecteur.service';
import { IProfesseur } from 'app/entities/professeur/professeur.model';
import { ProfesseurService } from 'app/entities/professeur/service/professeur.service';
import { IEtudiant } from 'app/entities/etudiant/etudiant.model';
import { EtudiantService } from 'app/entities/etudiant/service/etudiant.service';

@Component({
  selector: 'jhi-inspecteur-update',
  templateUrl: './inspecteur-update.component.html',
})
export class InspecteurUpdateComponent implements OnInit {
  isSaving = false;

  professeursSharedCollection: IProfesseur[] = [];
  etudiantsSharedCollection: IEtudiant[] = [];

  editForm = this.fb.group({
    id: [],
    nomInspecteur: [null, [Validators.required]],
    prenomInspecteur: [],
    contactInspecteur: [],
    professeurs: [],
    etudiants: [],
  });

  constructor(
    protected inspecteurService: InspecteurService,
    protected professeurService: ProfesseurService,
    protected etudiantService: EtudiantService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ inspecteur }) => {
      this.updateForm(inspecteur);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const inspecteur = this.createFromForm();
    if (inspecteur.id !== undefined) {
      this.subscribeToSaveResponse(this.inspecteurService.update(inspecteur));
    } else {
      this.subscribeToSaveResponse(this.inspecteurService.create(inspecteur));
    }
  }

  trackProfesseurById(_index: number, item: IProfesseur): number {
    return item.id!;
  }

  trackEtudiantById(_index: number, item: IEtudiant): number {
    return item.id!;
  }

  getSelectedProfesseur(option: IProfesseur, selectedVals?: IProfesseur[]): IProfesseur {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  getSelectedEtudiant(option: IEtudiant, selectedVals?: IEtudiant[]): IEtudiant {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IInspecteur>>): void {
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

  protected updateForm(inspecteur: IInspecteur): void {
    this.editForm.patchValue({
      id: inspecteur.id,
      nomInspecteur: inspecteur.nomInspecteur,
      prenomInspecteur: inspecteur.prenomInspecteur,
      contactInspecteur: inspecteur.contactInspecteur,
      professeurs: inspecteur.professeurs,
      etudiants: inspecteur.etudiants,
    });

    this.professeursSharedCollection = this.professeurService.addProfesseurToCollectionIfMissing(
      this.professeursSharedCollection,
      ...(inspecteur.professeurs ?? [])
    );
    this.etudiantsSharedCollection = this.etudiantService.addEtudiantToCollectionIfMissing(
      this.etudiantsSharedCollection,
      ...(inspecteur.etudiants ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.professeurService
      .query()
      .pipe(map((res: HttpResponse<IProfesseur[]>) => res.body ?? []))
      .pipe(
        map((professeurs: IProfesseur[]) =>
          this.professeurService.addProfesseurToCollectionIfMissing(professeurs, ...(this.editForm.get('professeurs')!.value ?? []))
        )
      )
      .subscribe((professeurs: IProfesseur[]) => (this.professeursSharedCollection = professeurs));

    this.etudiantService
      .query()
      .pipe(map((res: HttpResponse<IEtudiant[]>) => res.body ?? []))
      .pipe(
        map((etudiants: IEtudiant[]) =>
          this.etudiantService.addEtudiantToCollectionIfMissing(etudiants, ...(this.editForm.get('etudiants')!.value ?? []))
        )
      )
      .subscribe((etudiants: IEtudiant[]) => (this.etudiantsSharedCollection = etudiants));
  }

  protected createFromForm(): IInspecteur {
    return {
      ...new Inspecteur(),
      id: this.editForm.get(['id'])!.value,
      nomInspecteur: this.editForm.get(['nomInspecteur'])!.value,
      prenomInspecteur: this.editForm.get(['prenomInspecteur'])!.value,
      contactInspecteur: this.editForm.get(['contactInspecteur'])!.value,
      professeurs: this.editForm.get(['professeurs'])!.value,
      etudiants: this.editForm.get(['etudiants'])!.value,
    };
  }
}
