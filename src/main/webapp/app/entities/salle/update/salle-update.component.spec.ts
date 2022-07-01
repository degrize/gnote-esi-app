import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { SalleService } from '../service/salle.service';
import { ISalle, Salle } from '../salle.model';
import { IHoraire } from 'app/entities/horaire/horaire.model';
import { HoraireService } from 'app/entities/horaire/service/horaire.service';

import { SalleUpdateComponent } from './salle-update.component';

describe('Salle Management Update Component', () => {
  let comp: SalleUpdateComponent;
  let fixture: ComponentFixture<SalleUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let salleService: SalleService;
  let horaireService: HoraireService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [SalleUpdateComponent],
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
      .overrideTemplate(SalleUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SalleUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    salleService = TestBed.inject(SalleService);
    horaireService = TestBed.inject(HoraireService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Horaire query and add missing value', () => {
      const salle: ISalle = { id: 456 };
      const horaires: IHoraire[] = [{ id: 59868 }];
      salle.horaires = horaires;

      const horaireCollection: IHoraire[] = [{ id: 25925 }];
      jest.spyOn(horaireService, 'query').mockReturnValue(of(new HttpResponse({ body: horaireCollection })));
      const additionalHoraires = [...horaires];
      const expectedCollection: IHoraire[] = [...additionalHoraires, ...horaireCollection];
      jest.spyOn(horaireService, 'addHoraireToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ salle });
      comp.ngOnInit();

      expect(horaireService.query).toHaveBeenCalled();
      expect(horaireService.addHoraireToCollectionIfMissing).toHaveBeenCalledWith(horaireCollection, ...additionalHoraires);
      expect(comp.horairesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const salle: ISalle = { id: 456 };
      const horaires: IHoraire = { id: 61213 };
      salle.horaires = [horaires];

      activatedRoute.data = of({ salle });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(salle));
      expect(comp.horairesSharedCollection).toContain(horaires);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Salle>>();
      const salle = { id: 123 };
      jest.spyOn(salleService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ salle });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: salle }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(salleService.update).toHaveBeenCalledWith(salle);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Salle>>();
      const salle = new Salle();
      jest.spyOn(salleService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ salle });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: salle }));
      saveSubject.complete();

      // THEN
      expect(salleService.create).toHaveBeenCalledWith(salle);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Salle>>();
      const salle = { id: 123 };
      jest.spyOn(salleService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ salle });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(salleService.update).toHaveBeenCalledWith(salle);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackHoraireById', () => {
      it('Should return tracked Horaire primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackHoraireById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });

  describe('Getting selected relationships', () => {
    describe('getSelectedHoraire', () => {
      it('Should return option if no Horaire is selected', () => {
        const option = { id: 123 };
        const result = comp.getSelectedHoraire(option);
        expect(result === option).toEqual(true);
      });

      it('Should return selected Horaire for according option', () => {
        const option = { id: 123 };
        const selected = { id: 123 };
        const selected2 = { id: 456 };
        const result = comp.getSelectedHoraire(option, [selected2, selected]);
        expect(result === selected).toEqual(true);
        expect(result === selected2).toEqual(false);
        expect(result === option).toEqual(false);
      });

      it('Should return option if this Horaire is not selected', () => {
        const option = { id: 123 };
        const selected = { id: 456 };
        const result = comp.getSelectedHoraire(option, [selected]);
        expect(result === option).toEqual(true);
        expect(result === selected).toEqual(false);
      });
    });
  });
});
