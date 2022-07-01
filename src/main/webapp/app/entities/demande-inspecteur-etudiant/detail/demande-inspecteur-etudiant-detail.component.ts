import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDemandeInspecteurEtudiant } from '../demande-inspecteur-etudiant.model';

@Component({
  selector: 'jhi-demande-inspecteur-etudiant-detail',
  templateUrl: './demande-inspecteur-etudiant-detail.component.html',
})
export class DemandeInspecteurEtudiantDetailComponent implements OnInit {
  demandeInspecteurEtudiant: IDemandeInspecteurEtudiant | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ demandeInspecteurEtudiant }) => {
      this.demandeInspecteurEtudiant = demandeInspecteurEtudiant;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
