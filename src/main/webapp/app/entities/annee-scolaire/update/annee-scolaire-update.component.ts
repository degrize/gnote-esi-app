import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IAnneeScolaire, AnneeScolaire } from '../annee-scolaire.model';
import { AnneeScolaireService } from '../service/annee-scolaire.service';

@Component({
  selector: 'jhi-annee-scolaire-update',
  templateUrl: './annee-scolaire-update.component.html',
})
export class AnneeScolaireUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    periode: [null, [Validators.required]],
    dateDebut: [null, [Validators.required]],
    dateFin: [],
  });

  constructor(protected anneeScolaireService: AnneeScolaireService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ anneeScolaire }) => {
      this.updateForm(anneeScolaire);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const anneeScolaire = this.createFromForm();
    if (anneeScolaire.id !== undefined) {
      this.subscribeToSaveResponse(this.anneeScolaireService.update(anneeScolaire));
    } else {
      this.subscribeToSaveResponse(this.anneeScolaireService.create(anneeScolaire));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAnneeScolaire>>): void {
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

  protected updateForm(anneeScolaire: IAnneeScolaire): void {
    this.editForm.patchValue({
      id: anneeScolaire.id,
      periode: anneeScolaire.periode,
      dateDebut: anneeScolaire.dateDebut,
      dateFin: anneeScolaire.dateFin,
    });
  }

  protected createFromForm(): IAnneeScolaire {
    return {
      ...new AnneeScolaire(),
      id: this.editForm.get(['id'])!.value,
      periode: this.editForm.get(['periode'])!.value,
      dateDebut: this.editForm.get(['dateDebut'])!.value,
      dateFin: this.editForm.get(['dateFin'])!.value,
    };
  }
}
