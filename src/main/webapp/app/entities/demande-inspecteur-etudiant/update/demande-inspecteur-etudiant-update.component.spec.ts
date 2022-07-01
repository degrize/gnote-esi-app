import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { DemandeInspecteurEtudiantService } from '../service/demande-inspecteur-etudiant.service';
import { IDemandeInspecteurEtudiant, DemandeInspecteurEtudiant } from '../demande-inspecteur-etudiant.model';
import { IEtudiant } from 'app/entities/etudiant/etudiant.model';
import { EtudiantService } from 'app/entities/etudiant/service/etudiant.service';
import { IInspecteur } from 'app/entities/inspecteur/inspecteur.model';
import { InspecteurService } from 'app/entities/inspecteur/service/inspecteur.service';

import { DemandeInspecteurEtudiantUpdateComponent } from './demande-inspecteur-etudiant-update.component';

describe('DemandeInspecteurEtudiant Management Update Component', () => {
  let comp: DemandeInspecteurEtudiantUpdateComponent;
  let fixture: ComponentFixture<DemandeInspecteurEtudiantUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let demandeInspecteurEtudiantService: DemandeInspecteurEtudiantService;
  let etudiantService: EtudiantService;
  let inspecteurService: InspecteurService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [DemandeInspecteurEtudiantUpdateComponent],
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
      .overrideTemplate(DemandeInspecteurEtudiantUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DemandeInspecteurEtudiantUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    demandeInspecteurEtudiantService = TestBed.inject(DemandeInspecteurEtudiantService);
    etudiantService = TestBed.inject(EtudiantService);
    inspecteurService = TestBed.inject(InspecteurService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Etudiant query and add missing value', () => {
      const demandeInspecteurEtudiant: IDemandeInspecteurEtudiant = { id: 456 };
      const etudiants: IEtudiant[] = [{ id: 64493 }];
      demandeInspecteurEtudiant.etudiants = etudiants;

      const etudiantCollection: IEtudiant[] = [{ id: 2457 }];
      jest.spyOn(etudiantService, 'query').mockReturnValue(of(new HttpResponse({ body: etudiantCollection })));
      const additionalEtudiants = [...etudiants];
      const expectedCollection: IEtudiant[] = [...additionalEtudiants, ...etudiantCollection];
      jest.spyOn(etudiantService, 'addEtudiantToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ demandeInspecteurEtudiant });
      comp.ngOnInit();

      expect(etudiantService.query).toHaveBeenCalled();
      expect(etudiantService.addEtudiantToCollectionIfMissing).toHaveBeenCalledWith(etudiantCollection, ...additionalEtudiants);
      expect(comp.etudiantsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Inspecteur query and add missing value', () => {
      const demandeInspecteurEtudiant: IDemandeInspecteurEtudiant = { id: 456 };
      const inspecteurs: IInspecteur[] = [{ id: 86065 }];
      demandeInspecteurEtudiant.inspecteurs = inspecteurs;

      const inspecteurCollection: IInspecteur[] = [{ id: 38749 }];
      jest.spyOn(inspecteurService, 'query').mockReturnValue(of(new HttpResponse({ body: inspecteurCollection })));
      const additionalInspecteurs = [...inspecteurs];
      const expectedCollection: IInspecteur[] = [...additionalInspecteurs, ...inspecteurCollection];
      jest.spyOn(inspecteurService, 'addInspecteurToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ demandeInspecteurEtudiant });
      comp.ngOnInit();

      expect(inspecteurService.query).toHaveBeenCalled();
      expect(inspecteurService.addInspecteurToCollectionIfMissing).toHaveBeenCalledWith(inspecteurCollection, ...additionalInspecteurs);
      expect(comp.inspecteursSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const demandeInspecteurEtudiant: IDemandeInspecteurEtudiant = { id: 456 };
      const etudiants: IEtudiant = { id: 93794 };
      demandeInspecteurEtudiant.etudiants = [etudiants];
      const inspecteurs: IInspecteur = { id: 4806 };
      demandeInspecteurEtudiant.inspecteurs = [inspecteurs];

      activatedRoute.data = of({ demandeInspecteurEtudiant });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(demandeInspecteurEtudiant));
      expect(comp.etudiantsSharedCollection).toContain(etudiants);
      expect(comp.inspecteursSharedCollection).toContain(inspecteurs);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<DemandeInspecteurEtudiant>>();
      const demandeInspecteurEtudiant = { id: 123 };
      jest.spyOn(demandeInspecteurEtudiantService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ demandeInspecteurEtudiant });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: demandeInspecteurEtudiant }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(demandeInspecteurEtudiantService.update).toHaveBeenCalledWith(demandeInspecteurEtudiant);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<DemandeInspecteurEtudiant>>();
      const demandeInspecteurEtudiant = new DemandeInspecteurEtudiant();
      jest.spyOn(demandeInspecteurEtudiantService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ demandeInspecteurEtudiant });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: demandeInspecteurEtudiant }));
      saveSubject.complete();

      // THEN
      expect(demandeInspecteurEtudiantService.create).toHaveBeenCalledWith(demandeInspecteurEtudiant);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<DemandeInspecteurEtudiant>>();
      const demandeInspecteurEtudiant = { id: 123 };
      jest.spyOn(demandeInspecteurEtudiantService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ demandeInspecteurEtudiant });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(demandeInspecteurEtudiantService.update).toHaveBeenCalledWith(demandeInspecteurEtudiant);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackEtudiantById', () => {
      it('Should return tracked Etudiant primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackEtudiantById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackInspecteurById', () => {
      it('Should return tracked Inspecteur primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackInspecteurById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });

  describe('Getting selected relationships', () => {
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

    describe('getSelectedInspecteur', () => {
      it('Should return option if no Inspecteur is selected', () => {
        const option = { id: 123 };
        const result = comp.getSelectedInspecteur(option);
        expect(result === option).toEqual(true);
      });

      it('Should return selected Inspecteur for according option', () => {
        const option = { id: 123 };
        const selected = { id: 123 };
        const selected2 = { id: 456 };
        const result = comp.getSelectedInspecteur(option, [selected2, selected]);
        expect(result === selected).toEqual(true);
        expect(result === selected2).toEqual(false);
        expect(result === option).toEqual(false);
      });

      it('Should return option if this Inspecteur is not selected', () => {
        const option = { id: 123 };
        const selected = { id: 456 };
        const result = comp.getSelectedInspecteur(option, [selected]);
        expect(result === option).toEqual(true);
        expect(result === selected).toEqual(false);
      });
    });
  });
});
