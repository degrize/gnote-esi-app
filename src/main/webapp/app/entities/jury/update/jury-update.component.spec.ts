import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { JuryService } from '../service/jury.service';
import { IJury, Jury } from '../jury.model';
import { IProfesseur } from 'app/entities/professeur/professeur.model';
import { ProfesseurService } from 'app/entities/professeur/service/professeur.service';
import { ISoutenance } from 'app/entities/soutenance/soutenance.model';
import { SoutenanceService } from 'app/entities/soutenance/service/soutenance.service';

import { JuryUpdateComponent } from './jury-update.component';

describe('Jury Management Update Component', () => {
  let comp: JuryUpdateComponent;
  let fixture: ComponentFixture<JuryUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let juryService: JuryService;
  let professeurService: ProfesseurService;
  let soutenanceService: SoutenanceService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [JuryUpdateComponent],
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
      .overrideTemplate(JuryUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(JuryUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    juryService = TestBed.inject(JuryService);
    professeurService = TestBed.inject(ProfesseurService);
    soutenanceService = TestBed.inject(SoutenanceService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Professeur query and add missing value', () => {
      const jury: IJury = { id: 456 };
      const professeurs: IProfesseur[] = [{ id: 20759 }];
      jury.professeurs = professeurs;

      const professeurCollection: IProfesseur[] = [{ id: 95583 }];
      jest.spyOn(professeurService, 'query').mockReturnValue(of(new HttpResponse({ body: professeurCollection })));
      const additionalProfesseurs = [...professeurs];
      const expectedCollection: IProfesseur[] = [...additionalProfesseurs, ...professeurCollection];
      jest.spyOn(professeurService, 'addProfesseurToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ jury });
      comp.ngOnInit();

      expect(professeurService.query).toHaveBeenCalled();
      expect(professeurService.addProfesseurToCollectionIfMissing).toHaveBeenCalledWith(professeurCollection, ...additionalProfesseurs);
      expect(comp.professeursSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Soutenance query and add missing value', () => {
      const jury: IJury = { id: 456 };
      const soutenance: ISoutenance = { id: 26287 };
      jury.soutenance = soutenance;

      const soutenanceCollection: ISoutenance[] = [{ id: 7253 }];
      jest.spyOn(soutenanceService, 'query').mockReturnValue(of(new HttpResponse({ body: soutenanceCollection })));
      const additionalSoutenances = [soutenance];
      const expectedCollection: ISoutenance[] = [...additionalSoutenances, ...soutenanceCollection];
      jest.spyOn(soutenanceService, 'addSoutenanceToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ jury });
      comp.ngOnInit();

      expect(soutenanceService.query).toHaveBeenCalled();
      expect(soutenanceService.addSoutenanceToCollectionIfMissing).toHaveBeenCalledWith(soutenanceCollection, ...additionalSoutenances);
      expect(comp.soutenancesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const jury: IJury = { id: 456 };
      const professeurs: IProfesseur = { id: 96689 };
      jury.professeurs = [professeurs];
      const soutenance: ISoutenance = { id: 42061 };
      jury.soutenance = soutenance;

      activatedRoute.data = of({ jury });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(jury));
      expect(comp.professeursSharedCollection).toContain(professeurs);
      expect(comp.soutenancesSharedCollection).toContain(soutenance);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Jury>>();
      const jury = { id: 123 };
      jest.spyOn(juryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ jury });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: jury }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(juryService.update).toHaveBeenCalledWith(jury);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Jury>>();
      const jury = new Jury();
      jest.spyOn(juryService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ jury });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: jury }));
      saveSubject.complete();

      // THEN
      expect(juryService.create).toHaveBeenCalledWith(jury);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Jury>>();
      const jury = { id: 123 };
      jest.spyOn(juryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ jury });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(juryService.update).toHaveBeenCalledWith(jury);
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

    describe('trackSoutenanceById', () => {
      it('Should return tracked Soutenance primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackSoutenanceById(0, entity);
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
  });
});
