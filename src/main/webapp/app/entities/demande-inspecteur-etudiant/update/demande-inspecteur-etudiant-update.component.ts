import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IDemandeInspecteurEtudiant, DemandeInspecteurEtudiant } from '../demande-inspecteur-etudiant.model';
import { DemandeInspecteurEtudiantService } from '../service/demande-inspecteur-etudiant.service';
import { IEtudiant } from 'app/entities/etudiant/etudiant.model';
import { EtudiantService } from 'app/entities/etudiant/service/etudiant.service';
import { IInspecteur } from 'app/entities/inspecteur/inspecteur.model';
import { InspecteurService } from 'app/entities/inspecteur/service/inspecteur.service';

@Component({
  selector: 'jhi-demande-inspecteur-etudiant-update',
  templateUrl: './demande-inspecteur-etudiant-update.component.html',
})
export class DemandeInspecteurEtudiantUpdateComponent implements OnInit {
  isSaving = false;

  etudiantsSharedCollection: IEtudiant[] = [];
  inspecteursSharedCollection: IInspecteur[] = [];

  editForm = this.fb.group({
    id: [],
    message: [null, [Validators.required]],
    etudiants: [],
    inspecteurs: [],
  });

  constructor(
    protected demandeInspecteurEtudiantService: DemandeInspecteurEtudiantService,
    protected etudiantService: EtudiantService,
    protected inspecteurService: InspecteurService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ demandeInspecteurEtudiant }) => {
      this.updateForm(demandeInspecteurEtudiant);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const demandeInspecteurEtudiant = this.createFromForm();
    if (demandeInspecteurEtudiant.id !== undefined) {
      this.subscribeToSaveResponse(this.demandeInspecteurEtudiantService.update(demandeInspecteurEtudiant));
    } else {
      this.subscribeToSaveResponse(this.demandeInspecteurEtudiantService.create(demandeInspecteurEtudiant));
    }
  }

  trackEtudiantById(_index: number, item: IEtudiant): number {
    return item.id!;
  }

  trackInspecteurById(_index: number, item: IInspecteur): number {
    return item.id!;
  }

  getSelectedEtudiant(option: IEtudiant, selectedVals?: IEtudiant[]): IEtudiant {
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

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDemandeInspecteurEtudiant>>): void {
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

  protected updateForm(demandeInspecteurEtudiant: IDemandeInspecteurEtudiant): void {
    this.editForm.patchValue({
      id: demandeInspecteurEtudiant.id,
      message: demandeInspecteurEtudiant.message,
      etudiants: demandeInspecteurEtudiant.etudiants,
      inspecteurs: demandeInspecteurEtudiant.inspecteurs,
    });

    this.etudiantsSharedCollection = this.etudiantService.addEtudiantToCollectionIfMissing(
      this.etudiantsSharedCollection,
      ...(demandeInspecteurEtudiant.etudiants ?? [])
    );
    this.inspecteursSharedCollection = this.inspecteurService.addInspecteurToCollectionIfMissing(
      this.inspecteursSharedCollection,
      ...(demandeInspecteurEtudiant.inspecteurs ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.etudiantService
      .query()
      .pipe(map((res: HttpResponse<IEtudiant[]>) => res.body ?? []))
      .pipe(
        map((etudiants: IEtudiant[]) =>
          this.etudiantService.addEtudiantToCollectionIfMissing(etudiants, ...(this.editForm.get('etudiants')!.value ?? []))
        )
      )
      .subscribe((etudiants: IEtudiant[]) => (this.etudiantsSharedCollection = etudiants));

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

  protected createFromForm(): IDemandeInspecteurEtudiant {
    return {
      ...new DemandeInspecteurEtudiant(),
      id: this.editForm.get(['id'])!.value,
      message: this.editForm.get(['message'])!.value,
      etudiants: this.editForm.get(['etudiants'])!.value,
      inspecteurs: this.editForm.get(['inspecteurs'])!.value,
    };
  }
}
