import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IHoraire } from '../horaire.model';

@Component({
  selector: 'jhi-horaire-detail',
  templateUrl: './horaire-detail.component.html',
})
export class HoraireDetailComponent implements OnInit {
  horaire: IHoraire | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ horaire }) => {
      this.horaire = horaire;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
