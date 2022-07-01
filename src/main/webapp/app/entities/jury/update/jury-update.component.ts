import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IJury, Jury } from '../jury.model';
import { JuryService } from '../service/jury.service';
import { IProfesseur } from 'app/entities/professeur/professeur.model';
import { ProfesseurService } from 'app/entities/professeur/service/professeur.service';
import { ISoutenance } from 'app/entities/soutenance/soutenance.model';
import { SoutenanceService } from 'app/entities/soutenance/service/soutenance.service';

@Component({
  selector: 'jhi-jury-update',
  templateUrl: './jury-update.component.html',
})
export class JuryUpdateComponent implements OnInit {
  isSaving = false;

  professeursSharedCollection: IProfesseur[] = [];
  soutenancesSharedCollection: ISoutenance[] = [];

  editForm = this.fb.group({
    id: [],
    presidentJury: [null, [Validators.required]],
    professeurs: [],
    soutenance: [],
  });

  constructor(
    protected juryService: JuryService,
    protected professeurService: ProfesseurService,
    protected soutenanceService: SoutenanceService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ jury }) => {
      this.updateForm(jury);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const jury = this.createFromForm();
    if (jury.id !== undefined) {
      this.subscribeToSaveResponse(this.juryService.update(jury));
    } else {
      this.subscribeToSaveResponse(this.juryService.create(jury));
    }
  }

  trackProfesseurById(_index: number, item: IProfesseur): number {
    return item.id!;
  }

  trackSoutenanceById(_index: number, item: ISoutenance): number {
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

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IJury>>): void {
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

  protected updateForm(jury: IJury): void {
    this.editForm.patchValue({
      id: jury.id,
      presidentJury: jury.presidentJury,
      professeurs: jury.professeurs,
      soutenance: jury.soutenance,
    });

    this.professeursSharedCollection = this.professeurService.addProfesseurToCollectionIfMissing(
      this.professeursSharedCollection,
      ...(jury.professeurs ?? [])
    );
    this.soutenancesSharedCollection = this.soutenanceService.addSoutenanceToCollectionIfMissing(
      this.soutenancesSharedCollection,
      jury.soutenance
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

    this.soutenanceService
      .query()
      .pipe(map((res: HttpResponse<ISoutenance[]>) => res.body ?? []))
      .pipe(
        map((soutenances: ISoutenance[]) =>
          this.soutenanceService.addSoutenanceToCollectionIfMissing(soutenances, this.editForm.get('soutenance')!.value)
        )
      )
      .subscribe((soutenances: ISoutenance[]) => (this.soutenancesSharedCollection = soutenances));
  }

  protected createFromForm(): IJury {
    return {
      ...new Jury(),
      id: this.editForm.get(['id'])!.value,
      presidentJury: this.editForm.get(['presidentJury'])!.value,
      professeurs: this.editForm.get(['professeurs'])!.value,
      soutenance: this.editForm.get(['soutenance'])!.value,
    };
  }
}
