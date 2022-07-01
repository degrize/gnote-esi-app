import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SoutenanceDetailComponent } from './soutenance-detail.component';

describe('Soutenance Management Detail Component', () => {
  let comp: SoutenanceDetailComponent;
  let fixture: ComponentFixture<SoutenanceDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [SoutenanceDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ soutenance: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(SoutenanceDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(SoutenanceDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load soutenance on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.soutenance).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
