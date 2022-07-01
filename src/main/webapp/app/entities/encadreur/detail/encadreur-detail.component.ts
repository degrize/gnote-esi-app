import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IEncadreur } from '../encadreur.model';

@Component({
  selector: 'jhi-encadreur-detail',
  templateUrl: './encadreur-detail.component.html',
})
export class EncadreurDetailComponent implements OnInit {
  encadreur: IEncadreur | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ encadreur }) => {
      this.encadreur = encadreur;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
