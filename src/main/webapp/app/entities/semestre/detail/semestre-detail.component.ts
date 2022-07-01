import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISemestre } from '../semestre.model';

@Component({
  selector: 'jhi-semestre-detail',
  templateUrl: './semestre-detail.component.html',
})
export class SemestreDetailComponent implements OnInit {
  semestre: ISemestre | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ semestre }) => {
      this.semestre = semestre;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
