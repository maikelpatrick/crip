import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { SaqueService } from '../service/saque.service';
import { ISaque, Saque } from '../saque.model';

import { SaqueUpdateComponent } from './saque-update.component';

describe('Saque Management Update Component', () => {
  let comp: SaqueUpdateComponent;
  let fixture: ComponentFixture<SaqueUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let saqueService: SaqueService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [SaqueUpdateComponent],
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
      .overrideTemplate(SaqueUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SaqueUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    saqueService = TestBed.inject(SaqueService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const saque: ISaque = { id: 456 };

      activatedRoute.data = of({ saque });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(saque));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Saque>>();
      const saque = { id: 123 };
      jest.spyOn(saqueService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ saque });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: saque }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(saqueService.update).toHaveBeenCalledWith(saque);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Saque>>();
      const saque = new Saque();
      jest.spyOn(saqueService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ saque });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: saque }));
      saveSubject.complete();

      // THEN
      expect(saqueService.create).toHaveBeenCalledWith(saque);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Saque>>();
      const saque = { id: 123 };
      jest.spyOn(saqueService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ saque });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(saqueService.update).toHaveBeenCalledWith(saque);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
