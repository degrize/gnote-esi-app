import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ICycle, Cycle } from '../cycle.model';
import { CycleService } from '../service/cycle.service';
import { TypeCycle } from 'app/entities/enumerations/type-cycle.model';

@Component({
  selector: 'jhi-cycle-update',
  templateUrl: './cycle-update.component.html',
})
export class CycleUpdateComponent implements OnInit {
  isSaving = false;
  typeCycleValues = Object.keys(TypeCycle);

  editForm = this.fb.group({
    id: [],
    nomCycle: [null, [Validators.required]],
  });

  constructor(protected cycleService: CycleService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cycle }) => {
      this.updateForm(cycle);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const cycle = this.createFromForm();
    if (cycle.id !== undefined) {
      this.subscribeToSaveResponse(this.cycleService.update(cycle));
    } else {
      this.subscribeToSaveResponse(this.cycleService.create(cycle));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICycle>>): void {
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

  protected updateForm(cycle: ICycle): void {
    this.editForm.patchValue({
      id: cycle.id,
      nomCycle: cycle.nomCycle,
    });
  }

  protected createFromForm(): ICycle {
    return {
      ...new Cycle(),
      id: this.editForm.get(['id'])!.value,
      nomCycle: this.editForm.get(['nomCycle'])!.value,
    };
  }
}
