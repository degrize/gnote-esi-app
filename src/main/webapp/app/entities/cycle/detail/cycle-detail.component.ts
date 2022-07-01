import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICycle } from '../cycle.model';

@Component({
  selector: 'jhi-cycle-detail',
  templateUrl: './cycle-detail.component.html',
})
export class CycleDetailComponent implements OnInit {
  cycle: ICycle | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cycle }) => {
      this.cycle = cycle;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
