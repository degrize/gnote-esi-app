import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CycleDetailComponent } from './cycle-detail.component';

describe('Cycle Management Detail Component', () => {
  let comp: CycleDetailComponent;
  let fixture: ComponentFixture<CycleDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CycleDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ cycle: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(CycleDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(CycleDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load cycle on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.cycle).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
