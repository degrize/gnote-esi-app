import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IMatiere, Matiere } from '../matiere.model';
import { MatiereService } from '../service/matiere.service';
import { IModule } from 'app/entities/module/module.model';
import { ModuleService } from 'app/entities/module/service/module.service';

@Component({
  selector: 'jhi-matiere-update',
  templateUrl: './matiere-update.component.html',
})
export class MatiereUpdateComponent implements OnInit {
  isSaving = false;

  modulesSharedCollection: IModule[] = [];

  editForm = this.fb.group({
    id: [],
    nomEC: [null, [Validators.required]],
    coeff: [null, [Validators.required]],
    module: [],
  });

  constructor(
    protected matiereService: MatiereService,
    protected moduleService: ModuleService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ matiere }) => {
      this.updateForm(matiere);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const matiere = this.createFromForm();
    if (matiere.id !== undefined) {
      this.subscribeToSaveResponse(this.matiereService.update(matiere));
    } else {
      this.subscribeToSaveResponse(this.matiereService.create(matiere));
    }
  }

  trackModuleById(_index: number, item: IModule): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMatiere>>): void {
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

  protected updateForm(matiere: IMatiere): void {
    this.editForm.patchValue({
      id: matiere.id,
      nomEC: matiere.nomEC,
      coeff: matiere.coeff,
      module: matiere.module,
    });

    this.modulesSharedCollection = this.moduleService.addModuleToCollectionIfMissing(this.modulesSharedCollection, matiere.module);
  }

  protected loadRelationshipsOptions(): void {
    this.moduleService
      .query()
      .pipe(map((res: HttpResponse<IModule[]>) => res.body ?? []))
      .pipe(map((modules: IModule[]) => this.moduleService.addModuleToCollectionIfMissing(modules, this.editForm.get('module')!.value)))
      .subscribe((modules: IModule[]) => (this.modulesSharedCollection = modules));
  }

  protected createFromForm(): IMatiere {
    return {
      ...new Matiere(),
      id: this.editForm.get(['id'])!.value,
      nomEC: this.editForm.get(['nomEC'])!.value,
      coeff: this.editForm.get(['coeff'])!.value,
      module: this.editForm.get(['module'])!.value,
    };
  }
}
