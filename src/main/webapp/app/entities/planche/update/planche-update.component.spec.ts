import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { PlancheService } from '../service/planche.service';
import { IPlanche, Planche } from '../planche.model';
import { ISemestre } from 'app/entities/semestre/semestre.model';
import { SemestreService } from 'app/entities/semestre/service/semestre.service';

import { PlancheUpdateComponent } from './planche-update.component';

describe('Planche Management Update Component', () => {
  let comp: PlancheUpdateComponent;
  let fixture: ComponentFixture<PlancheUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let plancheService: PlancheService;
  let semestreService: SemestreService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [PlancheUpdateComponent],
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
      .overrideTemplate(PlancheUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PlancheUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    plancheService = TestBed.inject(PlancheService);
    semestreService = TestBed.inject(SemestreService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Semestre query and add missing value', () => {
      const planche: IPlanche = { id: 456 };
      const semestre: ISemestre = { id: 59155 };
      planche.semestre = semestre;

      const semestreCollection: ISemestre[] = [{ id: 90449 }];
      jest.spyOn(semestreService, 'query').mockReturnValue(of(new HttpResponse({ body: semestreCollection })));
      const additionalSemestres = [semestre];
      const expectedCollection: ISemestre[] = [...additionalSemestres, ...semestreCollection];
      jest.spyOn(semestreService, 'addSemestreToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ planche });
      comp.ngOnInit();

      expect(semestreService.query).toHaveBeenCalled();
      expect(semestreService.addSemestreToCollectionIfMissing).toHaveBeenCalledWith(semestreCollection, ...additionalSemestres);
      expect(comp.semestresSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const planche: IPlanche = { id: 456 };
      const semestre: ISemestre = { id: 34122 };
      planche.semestre = semestre;

      activatedRoute.data = of({ planche });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(planche));
      expect(comp.semestresSharedCollection).toContain(semestre);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Planche>>();
      const planche = { id: 123 };
      jest.spyOn(plancheService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ planche });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: planche }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(plancheService.update).toHaveBeenCalledWith(planche);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Planche>>();
      const planche = new Planche();
      jest.spyOn(plancheService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ planche });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: planche }));
      saveSubject.complete();

      // THEN
      expect(plancheService.create).toHaveBeenCalledWith(planche);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Planche>>();
      const planche = { id: 123 };
      jest.spyOn(plancheService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ planche });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(plancheService.update).toHaveBeenCalledWith(planche);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackSemestreById', () => {
      it('Should return tracked Semestre primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackSemestreById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
