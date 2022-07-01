import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDemandeInspecteurDE } from '../demande-inspecteur-de.model';

@Component({
  selector: 'jhi-demande-inspecteur-de-detail',
  templateUrl: './demande-inspecteur-de-detail.component.html',
})
export class DemandeInspecteurDEDetailComponent implements OnInit {
  demandeInspecteurDE: IDemandeInspecteurDE | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ demandeInspecteurDE }) => {
      this.demandeInspecteurDE = demandeInspecteurDE;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
