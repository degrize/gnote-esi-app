import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { FiliereService } from '../service/filiere.service';
import { IFiliere, Filiere } from '../filiere.model';
import { ICycle } from 'app/entities/cycle/cycle.model';
import { CycleService } from 'app/entities/cycle/service/cycle.service';
import { IModule } from 'app/entities/module/module.model';
import { ModuleService } from 'app/entities/module/service/module.service';

import { FiliereUpdateComponent } from './filiere-update.component';

describe('Filiere Management Update Component', () => {
  let comp: FiliereUpdateComponent;
  let fixture: ComponentFixture<FiliereUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let filiereService: FiliereService;
  let cycleService: CycleService;
  let moduleService: ModuleService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [FiliereUpdateComponent],
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
      .overrideTemplate(FiliereUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FiliereUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    filiereService = TestBed.inject(FiliereService);
    cycleService = TestBed.inject(CycleService);
    moduleService = TestBed.inject(ModuleService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Cycle query and add missing value', () => {
      const filiere: IFiliere = { id: 456 };
      const etudiant: ICycle = { id: 63280 };
      filiere.etudiant = etudiant;

      const cycleCollection: ICycle[] = [{ id: 56120 }];
      jest.spyOn(cycleService, 'query').mockReturnValue(of(new HttpResponse({ body: cycleCollection })));
      const additionalCycles = [etudiant];
      const expectedCollection: ICycle[] = [...additionalCycles, ...cycleCollection];
      jest.spyOn(cycleService, 'addCycleToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ filiere });
      comp.ngOnInit();

      expect(cycleService.query).toHaveBeenCalled();
      expect(cycleService.addCycleToCollectionIfMissing).toHaveBeenCalledWith(cycleCollection, ...additionalCycles);
      expect(comp.cyclesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Module query and add missing value', () => {
      const filiere: IFiliere = { id: 456 };
      const modules: IModule[] = [{ id: 42218 }];
      filiere.modules = modules;

      const moduleCollection: IModule[] = [{ id: 47785 }];
      jest.spyOn(moduleService, 'query').mockReturnValue(of(new HttpResponse({ body: moduleCollection })));
      const additionalModules = [...modules];
      const expectedCollection: IModule[] = [...additionalModules, ...moduleCollection];
      jest.spyOn(moduleService, 'addModuleToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ filiere });
      comp.ngOnInit();

      expect(moduleService.query).toHaveBeenCalled();
      expect(moduleService.addModuleToCollectionIfMissing).toHaveBeenCalledWith(moduleCollection, ...additionalModules);
      expect(comp.modulesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const filiere: IFiliere = { id: 456 };
      const etudiant: ICycle = { id: 2641 };
      filiere.etudiant = etudiant;
      const modules: IModule = { id: 20919 };
      filiere.modules = [modules];

      activatedRoute.data = of({ filiere });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(filiere));
      expect(comp.cyclesSharedCollection).toContain(etudiant);
      expect(comp.modulesSharedCollection).toContain(modules);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Filiere>>();
      const filiere = { id: 123 };
      jest.spyOn(filiereService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ filiere });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: filiere }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(filiereService.update).toHaveBeenCalledWith(filiere);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Filiere>>();
      const filiere = new Filiere();
      jest.spyOn(filiereService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ filiere });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: filiere }));
      saveSubject.complete();

      // THEN
      expect(filiereService.create).toHaveBeenCalledWith(filiere);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Filiere>>();
      const filiere = { id: 123 };
      jest.spyOn(filiereService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ filiere });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(filiereService.update).toHaveBeenCalledWith(filiere);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackCycleById', () => {
      it('Should return tracked Cycle primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackCycleById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackModuleById', () => {
      it('Should return tracked Module primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackModuleById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });

  describe('Getting selected relationships', () => {
    describe('getSelectedModule', () => {
      it('Should return option if no Module is selected', () => {
        const option = { id: 123 };
        const result = comp.getSelectedModule(option);
        expect(result === option).toEqual(true);
      });

      it('Should return selected Module for according option', () => {
        const option = { id: 123 };
        const selected = { id: 123 };
        const selected2 = { id: 456 };
        const result = comp.getSelectedModule(option, [selected2, selected]);
        expect(result === selected).toEqual(true);
        expect(result === selected2).toEqual(false);
        expect(result === option).toEqual(false);
      });

      it('Should return option if this Module is not selected', () => {
        const option = { id: 123 };
        const selected = { id: 456 };
        const result = comp.getSelectedModule(option, [selected]);
        expect(result === option).toEqual(true);
        expect(result === selected).toEqual(false);
      });
    });
  });
});
