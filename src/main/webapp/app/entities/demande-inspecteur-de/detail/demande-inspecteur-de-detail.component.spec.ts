import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DemandeInspecteurDEDetailComponent } from './demande-inspecteur-de-detail.component';

describe('DemandeInspecteurDE Management Detail Component', () => {
  let comp: DemandeInspecteurDEDetailComponent;
  let fixture: ComponentFixture<DemandeInspecteurDEDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DemandeInspecteurDEDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ demandeInspecteurDE: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(DemandeInspecteurDEDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(DemandeInspecteurDEDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load demandeInspecteurDE on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.demandeInspecteurDE).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
