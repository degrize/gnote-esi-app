import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { JuryDetailComponent } from './jury-detail.component';

describe('Jury Management Detail Component', () => {
  let comp: JuryDetailComponent;
  let fixture: ComponentFixture<JuryDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [JuryDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ jury: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(JuryDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(JuryDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load jury on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.jury).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
