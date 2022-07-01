import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { CycleService } from '../service/cycle.service';
import { ICycle, Cycle } from '../cycle.model';

import { CycleUpdateComponent } from './cycle-update.component';

describe('Cycle Management Update Component', () => {
  let comp: CycleUpdateComponent;
  let fixture: ComponentFixture<CycleUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let cycleService: CycleService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [CycleUpdateComponent],
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
      .overrideTemplate(CycleUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CycleUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    cycleService = TestBed.inject(CycleService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const cycle: ICycle = { id: 456 };

      activatedRoute.data = of({ cycle });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(cycle));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Cycle>>();
      const cycle = { id: 123 };
      jest.spyOn(cycleService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cycle });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: cycle }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(cycleService.update).toHaveBeenCalledWith(cycle);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Cycle>>();
      const cycle = new Cycle();
      jest.spyOn(cycleService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cycle });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: cycle }));
      saveSubject.complete();

      // THEN
      expect(cycleService.create).toHaveBeenCalledWith(cycle);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Cycle>>();
      const cycle = { id: 123 };
      jest.spyOn(cycleService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cycle });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(cycleService.update).toHaveBeenCalledWith(cycle);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
