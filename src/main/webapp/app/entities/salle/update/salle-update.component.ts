import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ISalle, Salle } from '../salle.model';
import { SalleService } from '../service/salle.service';
import { IHoraire } from 'app/entities/horaire/horaire.model';
import { HoraireService } from 'app/entities/horaire/service/horaire.service';

@Component({
  selector: 'jhi-salle-update',
  templateUrl: './salle-update.component.html',
})
export class SalleUpdateComponent implements OnInit {
  isSaving = false;

  horairesSharedCollection: IHoraire[] = [];

  editForm = this.fb.group({
    id: [],
    numeroSalle: [null, [Validators.required]],
    nbrePlace: [],
    etat: [],
    horaires: [],
  });

  constructor(
    protected salleService: SalleService,
    protected horaireService: HoraireService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ salle }) => {
      this.updateForm(salle);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const salle = this.createFromForm();
    if (salle.id !== undefined) {
      this.subscribeToSaveResponse(this.salleService.update(salle));
    } else {
      this.subscribeToSaveResponse(this.salleService.create(salle));
    }
  }

  trackHoraireById(_index: number, item: IHoraire): number {
    return item.id!;
  }

  getSelectedHoraire(option: IHoraire, selectedVals?: IHoraire[]): IHoraire {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISalle>>): void {
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

  protected updateForm(salle: ISalle): void {
    this.editForm.patchValue({
      id: salle.id,
      numeroSalle: salle.numeroSalle,
      nbrePlace: salle.nbrePlace,
      etat: salle.etat,
      horaires: salle.horaires,
    });

    this.horairesSharedCollection = this.horaireService.addHoraireToCollectionIfMissing(
      this.horairesSharedCollection,
      ...(salle.horaires ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.horaireService
      .query()
      .pipe(map((res: HttpResponse<IHoraire[]>) => res.body ?? []))
      .pipe(
        map((horaires: IHoraire[]) =>
          this.horaireService.addHoraireToCollectionIfMissing(horaires, ...(this.editForm.get('horaires')!.value ?? []))
        )
      )
      .subscribe((horaires: IHoraire[]) => (this.horairesSharedCollection = horaires));
  }

  protected createFromForm(): ISalle {
    return {
      ...new Salle(),
      id: this.editForm.get(['id'])!.value,
      numeroSalle: this.editForm.get(['numeroSalle'])!.value,
      nbrePlace: this.editForm.get(['nbrePlace'])!.value,
      etat: this.editForm.get(['etat'])!.value,
      horaires: this.editForm.get(['horaires'])!.value,
    };
  }
}
