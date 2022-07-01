import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { DemandeInspecteurDEService } from '../service/demande-inspecteur-de.service';
import { IDemandeInspecteurDE, DemandeInspecteurDE } from '../demande-inspecteur-de.model';
import { IProfesseur } from 'app/entities/professeur/professeur.model';
import { ProfesseurService } from 'app/entities/professeur/service/professeur.service';
import { IInspecteur } from 'app/entities/inspecteur/inspecteur.model';
import { InspecteurService } from 'app/entities/inspecteur/service/inspecteur.service';

import { DemandeInspecteurDEUpdateComponent } from './demande-inspecteur-de-update.component';

describe('DemandeInspecteurDE Management Update Component', () => {
  let comp: DemandeInspecteurDEUpdateComponent;
  let fixture: ComponentFixture<DemandeInspecteurDEUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let demandeInspecteurDEService: DemandeInspecteurDEService;
  let professeurService: ProfesseurService;
  let inspecteurService: InspecteurService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [DemandeInspecteurDEUpdateComponent],
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
      .overrideTemplate(DemandeInspecteurDEUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DemandeInspecteurDEUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    demandeInspecteurDEService = TestBed.inject(DemandeInspecteurDEService);
    professeurService = TestBed.inject(ProfesseurService);
    inspecteurService = TestBed.inject(InspecteurService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Professeur query and add missing value', () => {
      const demandeInspecteurDE: IDemandeInspecteurDE = { id: 456 };
      const professeurs: IProfesseur[] = [{ id: 84075 }];
      demandeInspecteurDE.professeurs = professeurs;

      const professeurCollection: IProfesseur[] = [{ id: 75686 }];
      jest.spyOn(professeurService, 'query').mockReturnValue(of(new HttpResponse({ body: professeurCollection })));
      const additionalProfesseurs = [...professeurs];
      const expectedCollection: IProfesseur[] = [...additionalProfesseurs, ...professeurCollection];
      jest.spyOn(professeurService, 'addProfesseurToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ demandeInspecteurDE });
      comp.ngOnInit();

      expect(professeurService.query).toHaveBeenCalled();
      expect(professeurService.addProfesseurToCollectionIfMissing).toHaveBeenCalledWith(professeurCollection, ...additionalProfesseurs);
      expect(comp.professeursSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Inspecteur query and add missing value', () => {
      const demandeInspecteurDE: IDemandeInspecteurDE = { id: 456 };
      const inspecteurs: IInspecteur[] = [{ id: 75025 }];
      demandeInspecteurDE.inspecteurs = inspecteurs;

      const inspecteurCollection: IInspecteur[] = [{ id: 7276 }];
      jest.spyOn(inspecteurService, 'query').mockReturnValue(of(new HttpResponse({ body: inspecteurCollection })));
      const additionalInspecteurs = [...inspecteurs];
      const expectedCollection: IInspecteur[] = [...additionalInspecteurs, ...inspecteurCollection];
      jest.spyOn(inspecteurService, 'addInspecteurToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ demandeInspecteurDE });
      comp.ngOnInit();

      expect(inspecteurService.query).toHaveBeenCalled();
      expect(inspecteurService.addInspecteurToCollectionIfMissing).toHaveBeenCalledWith(inspecteurCollection, ...additionalInspecteurs);
      expect(comp.inspecteursSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const demandeInspecteurDE: IDemandeInspecteurDE = { id: 456 };
      const professeurs: IProfesseur = { id: 20451 };
      demandeInspecteurDE.professeurs = [professeurs];
      const inspecteurs: IInspecteur = { id: 8440 };
      demandeInspecteurDE.inspecteurs = [inspecteurs];

      activatedRoute.data = of({ demandeInspecteurDE });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(demandeInspecteurDE));
      expect(comp.professeursSharedCollection).toContain(professeurs);
      expect(comp.inspecteursSharedCollection).toContain(inspecteurs);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<DemandeInspecteurDE>>();
      const demandeInspecteurDE = { id: 123 };
      jest.spyOn(demandeInspecteurDEService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ demandeInspecteurDE });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: demandeInspecteurDE }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(demandeInspecteurDEService.update).toHaveBeenCalledWith(demandeInspecteurDE);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<DemandeInspecteurDE>>();
      const demandeInspecteurDE = new DemandeInspecteurDE();
      jest.spyOn(demandeInspecteurDEService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ demandeInspecteurDE });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: demandeInspecteurDE }));
      saveSubject.complete();

      // THEN
      expect(demandeInspecteurDEService.create).toHaveBeenCalledWith(demandeInspecteurDE);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<DemandeInspecteurDE>>();
      const demandeInspecteurDE = { id: 123 };
      jest.spyOn(demandeInspecteurDEService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ demandeInspecteurDE });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(demandeInspecteurDEService.update).toHaveBeenCalledWith(demandeInspecteurDE);
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

    describe('trackInspecteurById', () => {
      it('Should return tracked Inspecteur primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackInspecteurById(0, entity);
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
