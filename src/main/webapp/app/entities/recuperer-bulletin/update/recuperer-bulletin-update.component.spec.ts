import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { RecupererBulletinService } from '../service/recuperer-bulletin.service';
import { IRecupererBulletin, RecupererBulletin } from '../recuperer-bulletin.model';
import { IEtudiant } from 'app/entities/etudiant/etudiant.model';
import { EtudiantService } from 'app/entities/etudiant/service/etudiant.service';
import { IBulletin } from 'app/entities/bulletin/bulletin.model';
import { BulletinService } from 'app/entities/bulletin/service/bulletin.service';

import { RecupererBulletinUpdateComponent } from './recuperer-bulletin-update.component';

describe('RecupererBulletin Management Update Component', () => {
  let comp: RecupererBulletinUpdateComponent;
  let fixture: ComponentFixture<RecupererBulletinUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let recupererBulletinService: RecupererBulletinService;
  let etudiantService: EtudiantService;
  let bulletinService: BulletinService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [RecupererBulletinUpdateComponent],
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
      .overrideTemplate(RecupererBulletinUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(RecupererBulletinUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    recupererBulletinService = TestBed.inject(RecupererBulletinService);
    etudiantService = TestBed.inject(EtudiantService);
    bulletinService = TestBed.inject(BulletinService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Etudiant query and add missing value', () => {
      const recupererBulletin: IRecupererBulletin = { id: 456 };
      const etudiant: IEtudiant = { id: 83168 };
      recupererBulletin.etudiant = etudiant;

      const etudiantCollection: IEtudiant[] = [{ id: 86279 }];
      jest.spyOn(etudiantService, 'query').mockReturnValue(of(new HttpResponse({ body: etudiantCollection })));
      const additionalEtudiants = [etudiant];
      const expectedCollection: IEtudiant[] = [...additionalEtudiants, ...etudiantCollection];
      jest.spyOn(etudiantService, 'addEtudiantToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ recupererBulletin });
      comp.ngOnInit();

      expect(etudiantService.query).toHaveBeenCalled();
      expect(etudiantService.addEtudiantToCollectionIfMissing).toHaveBeenCalledWith(etudiantCollection, ...additionalEtudiants);
      expect(comp.etudiantsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Bulletin query and add missing value', () => {
      const recupererBulletin: IRecupererBulletin = { id: 456 };
      const bulletin: IBulletin = { id: 33527 };
      recupererBulletin.bulletin = bulletin;

      const bulletinCollection: IBulletin[] = [{ id: 51652 }];
      jest.spyOn(bulletinService, 'query').mockReturnValue(of(new HttpResponse({ body: bulletinCollection })));
      const additionalBulletins = [bulletin];
      const expectedCollection: IBulletin[] = [...additionalBulletins, ...bulletinCollection];
      jest.spyOn(bulletinService, 'addBulletinToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ recupererBulletin });
      comp.ngOnInit();

      expect(bulletinService.query).toHaveBeenCalled();
      expect(bulletinService.addBulletinToCollectionIfMissing).toHaveBeenCalledWith(bulletinCollection, ...additionalBulletins);
      expect(comp.bulletinsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const recupererBulletin: IRecupererBulletin = { id: 456 };
      const etudiant: IEtudiant = { id: 84287 };
      recupererBulletin.etudiant = etudiant;
      const bulletin: IBulletin = { id: 68913 };
      recupererBulletin.bulletin = bulletin;

      activatedRoute.data = of({ recupererBulletin });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(recupererBulletin));
      expect(comp.etudiantsSharedCollection).toContain(etudiant);
      expect(comp.bulletinsSharedCollection).toContain(bulletin);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<RecupererBulletin>>();
      const recupererBulletin = { id: 123 };
      jest.spyOn(recupererBulletinService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ recupererBulletin });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: recupererBulletin }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(recupererBulletinService.update).toHaveBeenCalledWith(recupererBulletin);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<RecupererBulletin>>();
      const recupererBulletin = new RecupererBulletin();
      jest.spyOn(recupererBulletinService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ recupererBulletin });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: recupererBulletin }));
      saveSubject.complete();

      // THEN
      expect(recupererBulletinService.create).toHaveBeenCalledWith(recupererBulletin);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<RecupererBulletin>>();
      const recupererBulletin = { id: 123 };
      jest.spyOn(recupererBulletinService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ recupererBulletin });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(recupererBulletinService.update).toHaveBeenCalledWith(recupererBulletin);
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

    describe('trackBulletinById', () => {
      it('Should return tracked Bulletin primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackBulletinById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
