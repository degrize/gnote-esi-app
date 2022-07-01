import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ISemestre, Semestre } from '../semestre.model';
import { SemestreService } from '../service/semestre.service';
import { IAnneeScolaire } from 'app/entities/annee-scolaire/annee-scolaire.model';
import { AnneeScolaireService } from 'app/entities/annee-scolaire/service/annee-scolaire.service';

@Component({
  selector: 'jhi-semestre-update',
  templateUrl: './semestre-update.component.html',
})
export class SemestreUpdateComponent implements OnInit {
  isSaving = false;

  anneeScolairesSharedCollection: IAnneeScolaire[] = [];

  editForm = this.fb.group({
    id: [],
    nomSemestre: [null, [Validators.required]],
    anneeScolaire: [],
  });

  constructor(
    protected semestreService: SemestreService,
    protected anneeScolaireService: AnneeScolaireService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ semestre }) => {
      this.updateForm(semestre);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const semestre = this.createFromForm();
    if (semestre.id !== undefined) {
      this.subscribeToSaveResponse(this.semestreService.update(semestre));
    } else {
      this.subscribeToSaveResponse(this.semestreService.create(semestre));
    }
  }

  trackAnneeScolaireById(_index: number, item: IAnneeScolaire): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISemestre>>): void {
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

  protected updateForm(semestre: ISemestre): void {
    this.editForm.patchValue({
      id: semestre.id,
      nomSemestre: semestre.nomSemestre,
      anneeScolaire: semestre.anneeScolaire,
    });

    this.anneeScolairesSharedCollection = this.anneeScolaireService.addAnneeScolaireToCollectionIfMissing(
      this.anneeScolairesSharedCollection,
      semestre.anneeScolaire
    );
  }

  protected loadRelationshipsOptions(): void {
    this.anneeScolaireService
      .query()
      .pipe(map((res: HttpResponse<IAnneeScolaire[]>) => res.body ?? []))
      .pipe(
        map((anneeScolaires: IAnneeScolaire[]) =>
          this.anneeScolaireService.addAnneeScolaireToCollectionIfMissing(anneeScolaires, this.editForm.get('anneeScolaire')!.value)
        )
      )
      .subscribe((anneeScolaires: IAnneeScolaire[]) => (this.anneeScolairesSharedCollection = anneeScolaires));
  }

  protected createFromForm(): ISemestre {
    return {
      ...new Semestre(),
      id: this.editForm.get(['id'])!.value,
      nomSemestre: this.editForm.get(['nomSemestre'])!.value,
      anneeScolaire: this.editForm.get(['anneeScolaire'])!.value,
    };
  }
}
