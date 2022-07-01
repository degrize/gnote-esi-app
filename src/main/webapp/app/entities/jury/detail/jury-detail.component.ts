import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IJury } from '../jury.model';

@Component({
  selector: 'jhi-jury-detail',
  templateUrl: './jury-detail.component.html',
})
export class JuryDetailComponent implements OnInit {
  jury: IJury | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ jury }) => {
      this.jury = jury;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
