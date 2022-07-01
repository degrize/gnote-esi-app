import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ProfesseurDetailComponent } from './professeur-detail.component';

describe('Professeur Management Detail Component', () => {
  let comp: ProfesseurDetailComponent;
  let fixture: ComponentFixture<ProfesseurDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ProfesseurDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ professeur: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(ProfesseurDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ProfesseurDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load professeur on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.professeur).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
