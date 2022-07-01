import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IRecupererBulletin } from '../recuperer-bulletin.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-recuperer-bulletin-detail',
  templateUrl: './recuperer-bulletin-detail.component.html',
})
export class RecupererBulletinDetailComponent implements OnInit {
  recupererBulletin: IRecupererBulletin | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ recupererBulletin }) => {
      this.recupererBulletin = recupererBulletin;
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  previousState(): void {
    window.history.back();
  }
}
