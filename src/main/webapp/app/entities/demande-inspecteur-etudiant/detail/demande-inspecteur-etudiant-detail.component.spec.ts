import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DemandeInspecteurEtudiantDetailComponent } from './demande-inspecteur-etudiant-detail.component';

describe('DemandeInspecteurEtudiant Management Detail Component', () => {
  let comp: DemandeInspecteurEtudiantDetailComponent;
  let fixture: ComponentFixture<DemandeInspecteurEtudiantDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DemandeInspecteurEtudiantDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ demandeInspecteurEtudiant: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(DemandeInspecteurEtudiantDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(DemandeInspecteurEtudiantDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load demandeInspecteurEtudiant on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.demandeInspecteurEtudiant).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
