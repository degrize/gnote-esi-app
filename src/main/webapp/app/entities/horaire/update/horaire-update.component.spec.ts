import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { HoraireService } from '../service/horaire.service';
import { IHoraire, Horaire } from '../horaire.model';

import { HoraireUpdateComponent } from './horaire-update.component';

describe('Horaire Management Update Component', () => {
  let comp: HoraireUpdateComponent;
  let fixture: ComponentFixture<HoraireUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let horaireService: HoraireService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [HoraireUpdateComponent],
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
      .overrideTemplate(HoraireUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(HoraireUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    horaireService = TestBed.inject(HoraireService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const horaire: IHoraire = { id: 456 };

      activatedRoute.data = of({ horaire });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(horaire));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Horaire>>();
      const horaire = { id: 123 };
      jest.spyOn(horaireService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ horaire });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: horaire }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(horaireService.update).toHaveBeenCalledWith(horaire);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Horaire>>();
      const horaire = new Horaire();
      jest.spyOn(horaireService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ horaire });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: horaire }));
      saveSubject.complete();

      // THEN
      expect(horaireService.create).toHaveBeenCalledWith(horaire);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Horaire>>();
      const horaire = { id: 123 };
      jest.spyOn(horaireService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ horaire });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(horaireService.update).toHaveBeenCalledWith(horaire);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
