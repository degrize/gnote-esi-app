import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { EncadreurService } from '../service/encadreur.service';
import { IEncadreur, Encadreur } from '../encadreur.model';

import { EncadreurUpdateComponent } from './encadreur-update.component';

describe('Encadreur Management Update Component', () => {
  let comp: EncadreurUpdateComponent;
  let fixture: ComponentFixture<EncadreurUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let encadreurService: EncadreurService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [EncadreurUpdateComponent],
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
      .overrideTemplate(EncadreurUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EncadreurUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    encadreurService = TestBed.inject(EncadreurService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const encadreur: IEncadreur = { id: 456 };

      activatedRoute.data = of({ encadreur });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(encadreur));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Encadreur>>();
      const encadreur = { id: 123 };
      jest.spyOn(encadreurService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ encadreur });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: encadreur }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(encadreurService.update).toHaveBeenCalledWith(encadreur);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Encadreur>>();
      const encadreur = new Encadreur();
      jest.spyOn(encadreurService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ encadreur });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: encadreur }));
      saveSubject.complete();

      // THEN
      expect(encadreurService.create).toHaveBeenCalledWith(encadreur);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Encadreur>>();
      const encadreur = { id: 123 };
      jest.spyOn(encadreurService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ encadreur });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(encadreurService.update).toHaveBeenCalledWith(encadreur);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
