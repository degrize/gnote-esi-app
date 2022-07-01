import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IHoraire, Horaire } from '../horaire.model';
import { HoraireService } from '../service/horaire.service';

@Component({
  selector: 'jhi-horaire-update',
  templateUrl: './horaire-update.component.html',
})
export class HoraireUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    dateSout: [null, [Validators.required]],
    dateEffet: [],
  });

  constructor(protected horaireService: HoraireService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ horaire }) => {
      this.updateForm(horaire);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const horaire = this.createFromForm();
    if (horaire.id !== undefined) {
      this.subscribeToSaveResponse(this.horaireService.update(horaire));
    } else {
      this.subscribeToSaveResponse(this.horaireService.create(horaire));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IHoraire>>): void {
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

  protected updateForm(horaire: IHoraire): void {
    this.editForm.patchValue({
      id: horaire.id,
      dateSout: horaire.dateSout,
      dateEffet: horaire.dateEffet,
    });
  }

  protected createFromForm(): IHoraire {
    return {
      ...new Horaire(),
      id: this.editForm.get(['id'])!.value,
      dateSout: this.editForm.get(['dateSout'])!.value,
      dateEffet: this.editForm.get(['dateEffet'])!.value,
    };
  }
}
