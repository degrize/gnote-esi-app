import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ClasseService } from '../service/classe.service';
import { IClasse, Classe } from '../classe.model';
import { IFiliere } from 'app/entities/filiere/filiere.model';
import { FiliereService } from 'app/entities/filiere/service/filiere.service';
import { IMatiere } from 'app/entities/matiere/matiere.model';
import { MatiereService } from 'app/entities/matiere/service/matiere.service';

import { ClasseUpdateComponent } from './classe-update.component';

describe('Classe Management Update Component', () => {
  let comp: ClasseUpdateComponent;
  let fixture: ComponentFixture<ClasseUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let classeService: ClasseService;
  let filiereService: FiliereService;
  let matiereService: MatiereService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ClasseUpdateComponent],
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
      .overrideTemplate(ClasseUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ClasseUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    classeService = TestBed.inject(ClasseService);
    filiereService = TestBed.inject(FiliereService);
    matiereService = TestBed.inject(MatiereService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Filiere query and add missing value', () => {
      const classe: IClasse = { id: 456 };
      const filiere: IFiliere = { id: 56677 };
      classe.filiere = filiere;

      const filiereCollection: IFiliere[] = [{ id: 70749 }];
      jest.spyOn(filiereService, 'query').mockReturnValue(of(new HttpResponse({ body: filiereCollection })));
      const additionalFilieres = [filiere];
      const expectedCollection: IFiliere[] = [...additionalFilieres, ...filiereCollection];
      jest.spyOn(filiereService, 'addFiliereToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ classe });
      comp.ngOnInit();

      expect(filiereService.query).toHaveBeenCalled();
      expect(filiereService.addFiliereToCollectionIfMissing).toHaveBeenCalledWith(filiereCollection, ...additionalFilieres);
      expect(comp.filieresSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Matiere query and add missing value', () => {
      const classe: IClasse = { id: 456 };
      const matieres: IMatiere[] = [{ id: 72468 }];
      classe.matieres = matieres;

      const matiereCollection: IMatiere[] = [{ id: 11333 }];
      jest.spyOn(matiereService, 'query').mockReturnValue(of(new HttpResponse({ body: matiereCollection })));
      const additionalMatieres = [...matieres];
      const expectedCollection: IMatiere[] = [...additionalMatieres, ...matiereCollection];
      jest.spyOn(matiereService, 'addMatiereToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ classe });
      comp.ngOnInit();

      expect(matiereService.query).toHaveBeenCalled();
      expect(matiereService.addMatiereToCollectionIfMissing).toHaveBeenCalledWith(matiereCollection, ...additionalMatieres);
      expect(comp.matieresSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const classe: IClasse = { id: 456 };
      const filiere: IFiliere = { id: 12065 };
      classe.filiere = filiere;
      const matieres: IMatiere = { id: 30591 };
      classe.matieres = [matieres];

      activatedRoute.data = of({ classe });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(classe));
      expect(comp.filieresSharedCollection).toContain(filiere);
      expect(comp.matieresSharedCollection).toContain(matieres);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Classe>>();
      const classe = { id: 123 };
      jest.spyOn(classeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ classe });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: classe }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(classeService.update).toHaveBeenCalledWith(classe);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Classe>>();
      const classe = new Classe();
      jest.spyOn(classeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ classe });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: classe }));
      saveSubject.complete();

      // THEN
      expect(classeService.create).toHaveBeenCalledWith(classe);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Classe>>();
      const classe = { id: 123 };
      jest.spyOn(classeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ classe });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(classeService.update).toHaveBeenCalledWith(classe);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackFiliereById', () => {
      it('Should return tracked Filiere primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackFiliereById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackMatiereById', () => {
      it('Should return tracked Matiere primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackMatiereById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });

  describe('Getting selected relationships', () => {
    describe('getSelectedMatiere', () => {
      it('Should return option if no Matiere is selected', () => {
        const option = { id: 123 };
        const result = comp.getSelectedMatiere(option);
        expect(result === option).toEqual(true);
      });

      it('Should return selected Matiere for according option', () => {
        const option = { id: 123 };
        const selected = { id: 123 };
        const selected2 = { id: 456 };
        const result = comp.getSelectedMatiere(option, [selected2, selected]);
        expect(result === selected).toEqual(true);
        expect(result === selected2).toEqual(false);
        expect(result === option).toEqual(false);
      });

      it('Should return option if this Matiere is not selected', () => {
        const option = { id: 123 };
        const selected = { id: 456 };
        const result = comp.getSelectedMatiere(option, [selected]);
        expect(result === option).toEqual(true);
        expect(result === selected).toEqual(false);
      });
    });
  });
});
