import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { SoutenanceService } from '../service/soutenance.service';
import { ISoutenance, Soutenance } from '../soutenance.model';
import { ISalle } from 'app/entities/salle/salle.model';
import { SalleService } from 'app/entities/salle/service/salle.service';
import { IHoraire } from 'app/entities/horaire/horaire.model';
import { HoraireService } from 'app/entities/horaire/service/horaire.service';
import { IEtudiant } from 'app/entities/etudiant/etudiant.model';
import { EtudiantService } from 'app/entities/etudiant/service/etudiant.service';

import { SoutenanceUpdateComponent } from './soutenance-update.component';

describe('Soutenance Management Update Component', () => {
  let comp: SoutenanceUpdateComponent;
  let fixture: ComponentFixture<SoutenanceUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let soutenanceService: SoutenanceService;
  let salleService: SalleService;
  let horaireService: HoraireService;
  let etudiantService: EtudiantService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [SoutenanceUpdateComponent],
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
      .overrideTemplate(SoutenanceUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SoutenanceUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    soutenanceService = TestBed.inject(SoutenanceService);
    salleService = TestBed.inject(SalleService);
    horaireService = TestBed.inject(HoraireService);
    etudiantService = TestBed.inject(EtudiantService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Salle query and add missing value', () => {
      const soutenance: ISoutenance = { id: 456 };
      const salle: ISalle = { id: 49188 };
      soutenance.salle = salle;

      const salleCollection: ISalle[] = [{ id: 62238 }];
      jest.spyOn(salleService, 'query').mockReturnValue(of(new HttpResponse({ body: salleCollection })));
      const additionalSalles = [salle];
      const expectedCollection: ISalle[] = [...additionalSalles, ...salleCollection];
      jest.spyOn(salleService, 'addSalleToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ soutenance });
      comp.ngOnInit();

      expect(salleService.query).toHaveBeenCalled();
      expect(salleService.addSalleToCollectionIfMissing).toHaveBeenCalledWith(salleCollection, ...additionalSalles);
      expect(comp.sallesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Horaire query and add missing value', () => {
      const soutenance: ISoutenance = { id: 456 };
      const horaire: IHoraire = { id: 69103 };
      soutenance.horaire = horaire;

      const horaireCollection: IHoraire[] = [{ id: 11842 }];
      jest.spyOn(horaireService, 'query').mockReturnValue(of(new HttpResponse({ body: horaireCollection })));
      const additionalHoraires = [horaire];
      const expectedCollection: IHoraire[] = [...additionalHoraires, ...horaireCollection];
      jest.spyOn(horaireService, 'addHoraireToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ soutenance });
      comp.ngOnInit();

      expect(horaireService.query).toHaveBeenCalled();
      expect(horaireService.addHoraireToCollectionIfMissing).toHaveBeenCalledWith(horaireCollection, ...additionalHoraires);
      expect(comp.horairesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Etudiant query and add missing value', () => {
      const soutenance: ISoutenance = { id: 456 };
      const etudiants: IEtudiant[] = [{ id: 27150 }];
      soutenance.etudiants = etudiants;

      const etudiantCollection: IEtudiant[] = [{ id: 56796 }];
      jest.spyOn(etudiantService, 'query').mockReturnValue(of(new HttpResponse({ body: etudiantCollection })));
      const additionalEtudiants = [...etudiants];
      const expectedCollection: IEtudiant[] = [...additionalEtudiants, ...etudiantCollection];
      jest.spyOn(etudiantService, 'addEtudiantToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ soutenance });
      comp.ngOnInit();

      expect(etudiantService.query).toHaveBeenCalled();
      expect(etudiantService.addEtudiantToCollectionIfMissing).toHaveBeenCalledWith(etudiantCollection, ...additionalEtudiants);
      expect(comp.etudiantsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const soutenance: ISoutenance = { id: 456 };
      const salle: ISalle = { id: 15940 };
      soutenance.salle = salle;
      const horaire: IHoraire = { id: 43020 };
      soutenance.horaire = horaire;
      const etudiants: IEtudiant = { id: 97678 };
      soutenance.etudiants = [etudiants];

      activatedRoute.data = of({ soutenance });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(soutenance));
      expect(comp.sallesSharedCollection).toContain(salle);
      expect(comp.horairesSharedCollection).toContain(horaire);
      expect(comp.etudiantsSharedCollection).toContain(etudiants);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Soutenance>>();
      const soutenance = { id: 123 };
      jest.spyOn(soutenanceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ soutenance });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: soutenance }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(soutenanceService.update).toHaveBeenCalledWith(soutenance);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Soutenance>>();
      const soutenance = new Soutenance();
      jest.spyOn(soutenanceService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ soutenance });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: soutenance }));
      saveSubject.complete();

      // THEN
      expect(soutenanceService.create).toHaveBeenCalledWith(soutenance);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Soutenance>>();
      const soutenance = { id: 123 };
      jest.spyOn(soutenanceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ soutenance });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(soutenanceService.update).toHaveBeenCalledWith(soutenance);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackSalleById', () => {
      it('Should return tracked Salle primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackSalleById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackHoraireById', () => {
      it('Should return tracked Horaire primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackHoraireById(0, entity);
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
