import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { InspecteurService } from '../service/inspecteur.service';
import { IInspecteur, Inspecteur } from '../inspecteur.model';
import { IProfesseur } from 'app/entities/professeur/professeur.model';
import { ProfesseurService } from 'app/entities/professeur/service/professeur.service';
import { IEtudiant } from 'app/entities/etudiant/etudiant.model';
import { EtudiantService } from 'app/entities/etudiant/service/etudiant.service';

import { InspecteurUpdateComponent } from './inspecteur-update.component';

describe('Inspecteur Management Update Component', () => {
  let comp: InspecteurUpdateComponent;
  let fixture: ComponentFixture<InspecteurUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let inspecteurService: InspecteurService;
  let professeurService: ProfesseurService;
  let etudiantService: EtudiantService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [InspecteurUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(InspecteurUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(InspecteurUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    inspecteurService = TestBed.inject(InspecteurService);
    professeurService = TestBed.inject(ProfesseurService);
    etudiantService = TestBed.inject(EtudiantService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Professeur query and add missing value', () => {
      const inspecteur: IInspecteur = { id: 456 };
      const professeurs: IProfesseur[] = [{ id: 95353 }];
      inspecteur.professeurs = professeurs;

      const professeurCollection: IProfesseur[] = [{ id: 76487 }];
      jest.spyOn(professeurService, 'query').mockReturnValue(of(new HttpResponse({ body: professeurCollection })));
      const additionalProfesseurs = [...professeurs];
      const expectedCollection: IProfesseur[] = [...additionalProfesseurs, ...professeurCollection];
      jest.spyOn(professeurService, 'addProfesseurToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ inspecteur });
      comp.ngOnInit();

      expect(professeurService.query).toHaveBeenCalled();
      expect(professeurService.addProfesseurToCollectionIfMissing).toHaveBeenCalledWith(professeurCollection, ...additionalProfesseurs);
      expect(comp.professeursSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Etudiant query and add missing value', () => {
      const inspecteur: IInspecteur = { id: 456 };
      const etudiants: IEtudiant[] = [{ id: 92093 }];
      inspecteur.etudiants = etudiants;

      const etudiantCollection: IEtudiant[] = [{ id: 6996 }];
      jest.spyOn(etudiantService, 'query').mockReturnValue(of(new HttpResponse({ body: etudiantCollection })));
      const additionalEtudiants = [...etudiants];
      const expectedCollection: IEtudiant[] = [...additionalEtudiants, ...etudiantCollection];
      jest.spyOn(etudiantService, 'addEtudiantToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ inspecteur });
      comp.ngOnInit();

      expect(etudiantService.query).toHaveBeenCalled();
      expect(etudiantService.addEtudiantToCollectionIfMissing).toHaveBeenCalledWith(etudiantCollection, ...additionalEtudiants);
      expect(comp.etudiantsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const inspecteur: IInspecteur = { id: 456 };
      const professeurs: IProfesseur = { id: 2910 };
      inspecteur.professeurs = [professeurs];
      const etudiants: IEtudiant = { id: 66676 };
      inspecteur.etudiants = [etudiants];

      activatedRoute.data = of({ inspecteur });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(inspecteur));
      expect(comp.professeursSharedCollection).toContain(professeurs);
      expect(comp.etudiantsSharedCollection).toContain(etudiants);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Inspecteur>>();
      const inspecteur = { id: 123 };
      jest.spyOn(inspecteurService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ inspecteur });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: inspecteur }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(inspecteurService.update).toHaveBeenCalledWith(inspecteur);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Inspecteur>>();
      const inspecteur = new Inspecteur();
      jest.spyOn(inspecteurService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ inspecteur });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: inspecteur }));
      saveSubject.complete();

      // THEN
      expect(inspecteurService.create).toHaveBeenCalledWith(inspecteur);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Inspecteur>>();
      const inspecteur = { id: 123 };
      jest.spyOn(inspecteurService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ inspecteur });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(inspecteurService.update).toHaveBeenCalledWith(inspecteur);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackProfesseurById', () => {
      it('Should return tracked Professeur primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackProfesseurById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackEtudiantById', () => {
      it('Should return tracked Etudiant primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackEtudiantById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });

  describe('Getting selected relationships', () => {
    describe('getSelectedProfesseur', () => {
      it('Should return option if no Professeur is selected', () => {
        const option = { id: 123 };
        const result = comp.getSelectedProfesseur(option);
        expect(result === option).toEqual(true);
      });

      it('Should return selected Professeur for according option', () => {
        const option = { id: 123 };
        const selected = { id: 123 };
        const selected2 = { id: 456 };
        const result = comp.getSelectedProfesseur(option, [selected2, selected]);
        expect(result === selected).toEqual(true);
        expect(result === selected2).toEqual(false);
        expect(result === option).toEqual(false);
      });

      it('Should return option if this Professeur is not selected', () => {
        const option = { id: 123 };
        const selected = { id: 456 };
        const result = comp.getSelectedProfesseur(option, [selected]);
        expect(result === option).toEqual(true);
        expect(result === selected).toEqual(false);
      });
    });

    describe('getSelectedEtudiant', () => {
      it('Should return option if no Etudiant is selected', () => {
        const option = { id: 123 };
        const result = comp.getSelectedEtudiant(option);
        expect(result === option).toEqual(true);
      });

      it('Should return selected Etudiant for according option', () => {
        const option = { id: 123 };
        const selected = { id: 123 };
        const selected2 = { id: 456 };
        const result = comp.getSelectedEtudiant(option, [selected2, selected]);
        expect(result === selected).toEqual(true);
        expect(result === selected2).toEqual(false);
        expect(result === option).toEqual(false);
      });

      it('Should return option if this Etudiant is not selected', () => {
        const option = { id: 123 };
        const selected = { id: 456 };
        const result = comp.getSelectedEtudiant(option, [selected]);
        expect(result === option).toEqual(true);
        expect(result === selected).toEqual(false);
      });
    });
  });
});
