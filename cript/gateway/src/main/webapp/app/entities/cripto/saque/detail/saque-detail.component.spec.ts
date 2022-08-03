import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SaqueDetailComponent } from './saque-detail.component';

describe('Saque Management Detail Component', () => {
  let comp: SaqueDetailComponent;
  let fixture: ComponentFixture<SaqueDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [SaqueDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ saque: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(SaqueDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(SaqueDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load saque on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.saque).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
