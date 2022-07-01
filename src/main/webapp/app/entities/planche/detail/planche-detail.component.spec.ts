import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PlancheDetailComponent } from './planche-detail.component';

describe('Planche Management Detail Component', () => {
  let comp: PlancheDetailComponent;
  let fixture: ComponentFixture<PlancheDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PlancheDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ planche: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(PlancheDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(PlancheDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load planche on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.planche).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
