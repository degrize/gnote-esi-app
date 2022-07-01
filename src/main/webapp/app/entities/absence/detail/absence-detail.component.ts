import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAbsence } from '../absence.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-absence-detail',
  templateUrl: './absence-detail.component.html',
})
export class AbsenceDetailComponent implements OnInit {
  absence: IAbsence | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ absence }) => {
      this.absence = absence;
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
