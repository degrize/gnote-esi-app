import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { HoraireDetailComponent } from './horaire-detail.component';

describe('Horaire Management Detail Component', () => {
  let comp: HoraireDetailComponent;
  let fixture: ComponentFixture<HoraireDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [HoraireDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ horaire: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(HoraireDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(HoraireDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load horaire on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.horaire).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
