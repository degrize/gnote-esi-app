import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ISoutenance, Soutenance } from '../soutenance.model';
import { SoutenanceService } from '../service/soutenance.service';
import { ISalle } from 'app/entities/salle/salle.model';
import { SalleService } from 'app/entities/salle/service/salle.service';
import { IHoraire } from 'app/entities/horaire/horaire.model';
import { HoraireService } from 'app/entities/horaire/service/horaire.service';
import { IEtudiant } from 'app/entities/etudiant/etudiant.model';
import { EtudiantService } from 'app/entities/etudiant/service/etudiant.service';
import { TypeSoutenance } from 'app/entities/enumerations/type-soutenance.model';

@Component({
  selector: 'jhi-soutenance-update',
  templateUrl: './soutenance-update.component.html',
})
export class SoutenanceUpdateComponent implements OnInit {
  isSaving = false;
  typeSoutenanceValues = Object.keys(TypeSoutenance);

  sallesSharedCollection: ISalle[] = [];
  horairesSharedCollection: IHoraire[] = [];
  etudiantsSharedCollection: IEtudiant[] = [];

  editForm = this.fb.group({
    id: [],
    typeSout: [null, [Validators.required]],
    themeSout: [null, [Validators.required]],
    noteSout: [null, [Validators.required]],
    salle: [],
    horaire: [],
    etudiants: [],
  });

  constructor(
    protected soutenanceService: SoutenanceService,
    protected salleService: SalleService,
    protected horaireService: HoraireService,
    protected etudiantService: EtudiantService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ soutenance }) => {
      this.updateForm(soutenance);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const soutenance = this.createFromForm();
    if (soutenance.id !== undefined) {
      this.subscribeToSaveResponse(this.soutenanceService.update(soutenance));
    } else {
      this.subscribeToSaveResponse(this.soutenanceService.create(soutenance));
    }
  }

  trackSalleById(_index: number, item: ISalle): number {
    return item.id!;
  }

  trackHoraireById(_index: number, item: IHoraire): number {
    return item.id!;
  }

  trackEtudiantById(_index: number, item: IEtudiant): number {
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

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISoutenance>>): void {
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

  protected updateForm(soutenance: ISoutenance): void {
    this.editForm.patchValue({
      id: soutenance.id,
      typeSout: soutenance.typeSout,
      themeSout: soutenance.themeSout,
      noteSout: soutenance.noteSout,
      salle: soutenance.salle,
      horaire: soutenance.horaire,
      etudiants: soutenance.etudiants,
    });

    this.sallesSharedCollection = this.salleService.addSalleToCollectionIfMissing(this.sallesSharedCollection, soutenance.salle);
    this.horairesSharedCollection = this.horaireService.addHoraireToCollectionIfMissing(this.horairesSharedCollection, soutenance.horaire);
    this.etudiantsSharedCollection = this.etudiantService.addEtudiantToCollectionIfMissing(
      this.etudiantsSharedCollection,
      ...(soutenance.etudiants ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.salleService
      .query()
      .pipe(map((res: HttpResponse<ISalle[]>) => res.body ?? []))
      .pipe(map((salles: ISalle[]) => this.salleService.addSalleToCollectionIfMissing(salles, this.editForm.get('salle')!.value)))
      .subscribe((salles: ISalle[]) => (this.sallesSharedCollection = salles));

    this.horaireService
      .query()
      .pipe(map((res: HttpResponse<IHoraire[]>) => res.body ?? []))
      .pipe(
        map((horaires: IHoraire[]) => this.horaireService.addHoraireToCollectionIfMissing(horaires, this.editForm.get('horaire')!.value))
      )
      .subscribe((horaires: IHoraire[]) => (this.horairesSharedCollection = horaires));

    this.etudiantService
      .query()
      .pipe(map((res: HttpResponse<IEtudiant[]>) => res.body ?? []))
      .pipe(
        map((etudiants: IEtudiant[]) =>
          this.etudiantService.addEtudiantToCollectionIfMissing(etudiants, ...(this.editForm.get('etudiants')!.value ?? []))
        )
      )
      .subscribe((etudiants: IEtudiant[]) => (this.etudiantsSharedCollection = etudiants));
  }

  protected createFromForm(): ISoutenance {
    return {
      ...new Soutenance(),
      id: this.editForm.get(['id'])!.value,
      typeSout: this.editForm.get(['typeSout'])!.value,
      themeSout: this.editForm.get(['themeSout'])!.value,
      noteSout: this.editForm.get(['noteSout'])!.value,
      salle: this.editForm.get(['salle'])!.value,
      horaire: this.editForm.get(['horaire'])!.value,
      etudiants: this.editForm.get(['etudiants'])!.value,
    };
  }
}
