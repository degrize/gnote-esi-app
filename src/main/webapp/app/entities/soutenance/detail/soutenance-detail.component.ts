import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISoutenance } from '../soutenance.model';

@Component({
  selector: 'jhi-soutenance-detail',
  templateUrl: './soutenance-detail.component.html',
})
export class SoutenanceDetailComponent implements OnInit {
  soutenance: ISoutenance | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ soutenance }) => {
      this.soutenance = soutenance;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
