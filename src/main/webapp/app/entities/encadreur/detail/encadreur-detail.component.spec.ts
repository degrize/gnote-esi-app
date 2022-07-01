import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { EncadreurDetailComponent } from './encadreur-detail.component';

describe('Encadreur Management Detail Component', () => {
  let comp: EncadreurDetailComponent;
  let fixture: ComponentFixture<EncadreurDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [EncadreurDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ encadreur: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(EncadreurDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(EncadreurDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load encadreur on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.encadreur).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
