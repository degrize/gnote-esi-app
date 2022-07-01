import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IClasse, Classe } from '../classe.model';
import { ClasseService } from '../service/classe.service';
import { IFiliere } from 'app/entities/filiere/filiere.model';
import { FiliereService } from 'app/entities/filiere/service/filiere.service';
import { IMatiere } from 'app/entities/matiere/matiere.model';
import { MatiereService } from 'app/entities/matiere/service/matiere.service';

@Component({
  selector: 'jhi-classe-update',
  templateUrl: './classe-update.component.html',
})
export class ClasseUpdateComponent implements OnInit {
  isSaving = false;

  filieresSharedCollection: IFiliere[] = [];
  matieresSharedCollection: IMatiere[] = [];

  editForm = this.fb.group({
    id: [],
    nomClasse: [null, [Validators.required]],
    filiere: [],
    matieres: [],
  });

  constructor(
    protected classeService: ClasseService,
    protected filiereService: FiliereService,
    protected matiereService: MatiereService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ classe }) => {
      this.updateForm(classe);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const classe = this.createFromForm();
    if (classe.id !== undefined) {
      this.subscribeToSaveResponse(this.classeService.update(classe));
    } else {
      this.subscribeToSaveResponse(this.classeService.create(classe));
    }
  }

  trackFiliereById(_index: number, item: IFiliere): number {
    return item.id!;
  }

  trackMatiereById(_index: number, item: IMatiere): number {
    return item.id!;
  }

  getSelectedMatiere(option: IMatiere, selectedVals?: IMatiere[]): IMatiere {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IClasse>>): void {
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

  protected updateForm(classe: IClasse): void {
    this.editForm.patchValue({
      id: classe.id,
      nomClasse: classe.nomClasse,
      filiere: classe.filiere,
      matieres: classe.matieres,
    });

    this.filieresSharedCollection = this.filiereService.addFiliereToCollectionIfMissing(this.filieresSharedCollection, classe.filiere);
    this.matieresSharedCollection = this.matiereService.addMatiereToCollectionIfMissing(
      this.matieresSharedCollection,
      ...(classe.matieres ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.filiereService
      .query()
      .pipe(map((res: HttpResponse<IFiliere[]>) => res.body ?? []))
      .pipe(
        map((filieres: IFiliere[]) => this.filiereService.addFiliereToCollectionIfMissing(filieres, this.editForm.get('filiere')!.value))
      )
      .subscribe((filieres: IFiliere[]) => (this.filieresSharedCollection = filieres));

    this.matiereService
      .query()
      .pipe(map((res: HttpResponse<IMatiere[]>) => res.body ?? []))
      .pipe(
        map((matieres: IMatiere[]) =>
          this.matiereService.addMatiereToCollectionIfMissing(matieres, ...(this.editForm.get('matieres')!.value ?? []))
        )
      )
      .subscribe((matieres: IMatiere[]) => (this.matieresSharedCollection = matieres));
  }

  protected createFromForm(): IClasse {
    return {
      ...new Classe(),
      id: this.editForm.get(['id'])!.value,
      nomClasse: this.editForm.get(['nomClasse'])!.value,
      filiere: this.editForm.get(['filiere'])!.value,
      matieres: this.editForm.get(['matieres'])!.value,
    };
  }
}
