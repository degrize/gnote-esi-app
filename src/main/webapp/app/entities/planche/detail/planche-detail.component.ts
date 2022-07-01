import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPlanche } from '../planche.model';

@Component({
  selector: 'jhi-planche-detail',
  templateUrl: './planche-detail.component.html',
})
export class PlancheDetailComponent implements OnInit {
  planche: IPlanche | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ planche }) => {
      this.planche = planche;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
