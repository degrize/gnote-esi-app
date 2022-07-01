import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { BulletinService } from '../service/bulletin.service';
import { IBulletin, Bulletin } from '../bulletin.model';
import { IEtudiant } from 'app/entities/etudiant/etudiant.model';
import { EtudiantService } from 'app/entities/etudiant/service/etudiant.service';
import { ISemestre } from 'app/entities/semestre/semestre.model';
import { SemestreService } from 'app/entities/semestre/service/semestre.service';
import { IProfesseur } from 'app/entities/professeur/professeur.model';
import { ProfesseurService } from 'app/entities/professeur/service/professeur.service';

import { BulletinUpdateComponent } from './bulletin-update.component';

describe('Bulletin Management Update Component', () => {
  let comp: BulletinUpdateComponent;
  let fixture: ComponentFixture<BulletinUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let bulletinService: BulletinService;
  let etudiantService: EtudiantService;
  let semestreService: SemestreService;
  let professeurService: ProfesseurService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [BulletinUpdateComponent],
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
      .overrideTemplate(BulletinUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(BulletinUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    bulletinService = TestBed.inject(BulletinService);
    etudiantService = TestBed.inject(EtudiantService);
    semestreService = TestBed.inject(SemestreService);
    professeurService = TestBed.inject(ProfesseurService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Etudiant query and add missing value', () => {
      const bulletin: IBulletin = { id: 456 };
      const etudiant: IEtudiant = { id: 14728 };
      bulletin.etudiant = etudiant;

      const etudiantCollection: IEtudiant[] = [{ id: 69554 }];
      jest.spyOn(etudiantService, 'query').mockReturnValue(of(new HttpResponse({ body: etudiantCollection })));
      const additionalEtudiants = [etudiant];
      const expectedCollection: IEtudiant[] = [...additionalEtudiants, ...etudiantCollection];
      jest.spyOn(etudiantService, 'addEtudiantToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ bulletin });
      comp.ngOnInit();

      expect(etudiantService.query).toHaveBeenCalled();
      expect(etudiantService.addEtudiantToCollectionIfMissing).toHaveBeenCalledWith(etudiantCollection, ...additionalEtudiants);
      expect(comp.etudiantsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Semestre query and add missing value', () => {
      const bulletin: IBulletin = { id: 456 };
      const semestre: ISemestre = { id: 89372 };
      bulletin.semestre = semestre;

      const semestreCollection: ISemestre[] = [{ id: 91634 }];
      jest.spyOn(semestreService, 'query').mockReturnValue(of(new HttpResponse({ body: semestreCollection })));
      const additionalSemestres = [semestre];
      const expectedCollection: ISemestre[] = [...additionalSemestres, ...semestreCollection];
      jest.spyOn(semestreService, 'addSemestreToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ bulletin });
      comp.ngOnInit();

      expect(semestreService.query).toHaveBeenCalled();
      expect(semestreService.addSemestreToCollectionIfMissing).toHaveBeenCalledWith(semestreCollection, ...additionalSemestres);
      expect(comp.semestresSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Professeur query and add missing value', () => {
      const bulletin: IBulletin = { id: 456 };
      const professeurs: IProfesseur[] = [{ id: 57135 }];
      bulletin.professeurs = professeurs;

      const professeurCollection: IProfesseur[] = [{ id: 47610 }];
      jest.spyOn(professeurService, 'query').mockReturnValue(of(new HttpResponse({ body: professeurCollection })));
      const additionalProfesseurs = [...professeurs];
      const expectedCollection: IProfesseur[] = [...additionalProfesseurs, ...professeurCollection];
      jest.spyOn(professeurService, 'addProfesseurToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ bulletin });
      comp.ngOnInit();

      expect(professeurService.query).toHaveBeenCalled();
      expect(professeurService.addProfesseurToCollectionIfMissing).toHaveBeenCalledWith(professeurCollection, ...additionalProfesseurs);
      expect(comp.professeursSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const bulletin: IBulletin = { id: 456 };
      const etudiant: IEtudiant = { id: 81549 };
      bulletin.etudiant = etudiant;
      const semestre: ISemestre = { id: 14928 };
      bulletin.semestre = semestre;
      const professeurs: IProfesseur = { id: 57225 };
      bulletin.professeurs = [professeurs];

      activatedRoute.data = of({ bulletin });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(bulletin));
      expect(comp.etudiantsSharedCollection).toContain(etudiant);
      expect(comp.semestresSharedCollection).toContain(semestre);
      expect(comp.professeursSharedCollection).toContain(professeurs);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Bulletin>>();
      const bulletin = { id: 123 };
      jest.spyOn(bulletinService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ bulletin });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: bulletin }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(bulletinService.update).toHaveBeenCalledWith(bulletin);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Bulletin>>();
      const bulletin = new Bulletin();
      jest.spyOn(bulletinService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ bulletin });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: bulletin }));
      saveSubject.complete();

      // THEN
      expect(bulletinService.create).toHaveBeenCalledWith(bulletin);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Bulletin>>();
      const bulletin = { id: 123 };
      jest.spyOn(bulletinService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ bulletin });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(bulletinService.update).toHaveBeenCalledWith(bulletin);
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

    describe('trackSemestreById', () => {
      it('Should return tracked Semestre primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackSemestreById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackProfesseurById', () => {
      it('Should return tracked Professeur primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackProfesseurById(0, entity);
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
