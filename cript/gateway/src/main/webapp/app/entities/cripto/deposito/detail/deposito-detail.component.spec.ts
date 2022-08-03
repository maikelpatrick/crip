import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DepositoDetailComponent } from './deposito-detail.component';

describe('Deposito Management Detail Component', () => {
  let comp: DepositoDetailComponent;
  let fixture: ComponentFixture<DepositoDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DepositoDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ deposito: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(DepositoDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(DepositoDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load deposito on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.deposito).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
