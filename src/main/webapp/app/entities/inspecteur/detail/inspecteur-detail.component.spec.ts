import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { InspecteurDetailComponent } from './inspecteur-detail.component';

describe('Inspecteur Management Detail Component', () => {
  let comp: InspecteurDetailComponent;
  let fixture: ComponentFixture<InspecteurDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [InspecteurDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ inspecteur: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(InspecteurDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(InspecteurDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load inspecteur on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.inspecteur).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
