import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IDemandeInspecteurDE, DemandeInspecteurDE } from '../demande-inspecteur-de.model';
import { DemandeInspecteurDEService } from '../service/demande-inspecteur-de.service';
import { IProfesseur } from 'app/entities/professeur/professeur.model';
import { ProfesseurService } from 'app/entities/professeur/service/professeur.service';
import { IInspecteur } from 'app/entities/inspecteur/inspecteur.model';
import { InspecteurService } from 'app/entities/inspecteur/service/inspecteur.service';

@Component({
  selector: 'jhi-demande-inspecteur-de-update',
  templateUrl: './demande-inspecteur-de-update.component.html',
})
export class DemandeInspecteurDEUpdateComponent implements OnInit {
  isSaving = false;

  professeursSharedCollection: IProfesseur[] = [];
  inspecteursSharedCollection: IInspecteur[] = [];

  editForm = this.fb.group({
    id: [],
    message: [null, [Validators.required]],
    professeurs: [],
    inspecteurs: [],
  });

  constructor(
    protected demandeInspecteurDEService: DemandeInspecteurDEService,
    protected professeurService: ProfesseurService,
    protected inspecteurService: InspecteurService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ demandeInspecteurDE }) => {
      this.updateForm(demandeInspecteurDE);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const demandeInspecteurDE = this.createFromForm();
    if (demandeInspecteurDE.id !== undefined) {
      this.subscribeToSaveResponse(this.demandeInspecteurDEService.update(demandeInspecteurDE));
    } else {
      this.subscribeToSaveResponse(this.demandeInspecteurDEService.create(demandeInspecteurDE));
    }
  }

  trackProfesseurById(_index: number, item: IProfesseur): number {
    return item.id!;
  }

  trackInspecteurById(_index: number, item: IInspecteur): number {
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

  getSelectedInspecteur(option: IInspecteur, selectedVals?: IInspecteur[]): IInspecteur {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDemandeInspecteurDE>>): void {
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

  protected updateForm(demandeInspecteurDE: IDemandeInspecteurDE): void {
    this.editForm.patchValue({
      id: demandeInspecteurDE.id,
      message: demandeInspecteurDE.message,
      professeurs: demandeInspecteurDE.professeurs,
      inspecteurs: demandeInspecteurDE.inspecteurs,
    });

    this.professeursSharedCollection = this.professeurService.addProfesseurToCollectionIfMissing(
      this.professeursSharedCollection,
      ...(demandeInspecteurDE.professeurs ?? [])
    );
    this.inspecteursSharedCollection = this.inspecteurService.addInspecteurToCollectionIfMissing(
      this.inspecteursSharedCollection,
      ...(demandeInspecteurDE.inspecteurs ?? [])
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

    this.inspecteurService
      .query()
      .pipe(map((res: HttpResponse<IInspecteur[]>) => res.body ?? []))
      .pipe(
        map((inspecteurs: IInspecteur[]) =>
          this.inspecteurService.addInspecteurToCollectionIfMissing(inspecteurs, ...(this.editForm.get('inspecteurs')!.value ?? []))
        )
      )
      .subscribe((inspecteurs: IInspecteur[]) => (this.inspecteursSharedCollection = inspecteurs));
  }

  protected createFromForm(): IDemandeInspecteurDE {
    return {
      ...new DemandeInspecteurDE(),
      id: this.editForm.get(['id'])!.value,
      message: this.editForm.get(['message'])!.value,
      professeurs: this.editForm.get(['professeurs'])!.value,
      inspecteurs: this.editForm.get(['inspecteurs'])!.value,
    };
  }
}
