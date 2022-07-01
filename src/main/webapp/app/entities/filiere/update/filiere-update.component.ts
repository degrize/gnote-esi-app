import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IFiliere, Filiere } from '../filiere.model';
import { FiliereService } from '../service/filiere.service';
import { ICycle } from 'app/entities/cycle/cycle.model';
import { CycleService } from 'app/entities/cycle/service/cycle.service';
import { IModule } from 'app/entities/module/module.model';
import { ModuleService } from 'app/entities/module/service/module.service';

@Component({
  selector: 'jhi-filiere-update',
  templateUrl: './filiere-update.component.html',
})
export class FiliereUpdateComponent implements OnInit {
  isSaving = false;

  cyclesSharedCollection: ICycle[] = [];
  modulesSharedCollection: IModule[] = [];

  editForm = this.fb.group({
    id: [],
    nomFiliere: [null, [Validators.required]],
    etudiant: [],
    modules: [],
  });

  constructor(
    protected filiereService: FiliereService,
    protected cycleService: CycleService,
    protected moduleService: ModuleService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ filiere }) => {
      this.updateForm(filiere);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const filiere = this.createFromForm();
    if (filiere.id !== undefined) {
      this.subscribeToSaveResponse(this.filiereService.update(filiere));
    } else {
      this.subscribeToSaveResponse(this.filiereService.create(filiere));
    }
  }

  trackCycleById(_index: number, item: ICycle): number {
    return item.id!;
  }

  trackModuleById(_index: number, item: IModule): number {
    return item.id!;
  }

  getSelectedModule(option: IModule, selectedVals?: IModule[]): IModule {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFiliere>>): void {
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

  protected updateForm(filiere: IFiliere): void {
    this.editForm.patchValue({
      id: filiere.id,
      nomFiliere: filiere.nomFiliere,
      etudiant: filiere.etudiant,
      modules: filiere.modules,
    });

    this.cyclesSharedCollection = this.cycleService.addCycleToCollectionIfMissing(this.cyclesSharedCollection, filiere.etudiant);
    this.modulesSharedCollection = this.moduleService.addModuleToCollectionIfMissing(
      this.modulesSharedCollection,
      ...(filiere.modules ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.cycleService
      .query()
      .pipe(map((res: HttpResponse<ICycle[]>) => res.body ?? []))
      .pipe(map((cycles: ICycle[]) => this.cycleService.addCycleToCollectionIfMissing(cycles, this.editForm.get('etudiant')!.value)))
      .subscribe((cycles: ICycle[]) => (this.cyclesSharedCollection = cycles));

    this.moduleService
      .query()
      .pipe(map((res: HttpResponse<IModule[]>) => res.body ?? []))
      .pipe(
        map((modules: IModule[]) =>
          this.moduleService.addModuleToCollectionIfMissing(modules, ...(this.editForm.get('modules')!.value ?? []))
        )
      )
      .subscribe((modules: IModule[]) => (this.modulesSharedCollection = modules));
  }

  protected createFromForm(): IFiliere {
    return {
      ...new Filiere(),
      id: this.editForm.get(['id'])!.value,
      nomFiliere: this.editForm.get(['nomFiliere'])!.value,
      etudiant: this.editForm.get(['etudiant'])!.value,
      modules: this.editForm.get(['modules'])!.value,
    };
  }
}
