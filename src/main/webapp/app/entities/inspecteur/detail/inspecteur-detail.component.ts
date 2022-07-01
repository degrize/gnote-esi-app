import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IInspecteur } from '../inspecteur.model';

@Component({
  selector: 'jhi-inspecteur-detail',
  templateUrl: './inspecteur-detail.component.html',
})
export class InspecteurDetailComponent implements OnInit {
  inspecteur: IInspecteur | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ inspecteur }) => {
      this.inspecteur = inspecteur;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
