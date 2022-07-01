import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SemestreDetailComponent } from './semestre-detail.component';

describe('Semestre Management Detail Component', () => {
  let comp: SemestreDetailComponent;
  let fixture: ComponentFixture<SemestreDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [SemestreDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ semestre: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(SemestreDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(SemestreDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load semestre on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.semestre).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
