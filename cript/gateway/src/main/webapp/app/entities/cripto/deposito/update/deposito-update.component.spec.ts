import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { DepositoService } from '../service/deposito.service';
import { IDeposito, Deposito } from '../deposito.model';

import { DepositoUpdateComponent } from './deposito-update.component';

describe('Deposito Management Update Component', () => {
  let comp: DepositoUpdateComponent;
  let fixture: ComponentFixture<DepositoUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let depositoService: DepositoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [DepositoUpdateComponent],
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
      .overrideTemplate(DepositoUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DepositoUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    depositoService = TestBed.inject(DepositoService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const deposito: IDeposito = { id: 456 };

      activatedRoute.data = of({ deposito });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(deposito));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Deposito>>();
      const deposito = { id: 123 };
      jest.spyOn(depositoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ deposito });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: deposito }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(depositoService.update).toHaveBeenCalledWith(deposito);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Deposito>>();
      const deposito = new Deposito();
      jest.spyOn(depositoService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ deposito });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: deposito }));
      saveSubject.complete();

      // THEN
      expect(depositoService.create).toHaveBeenCalledWith(deposito);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Deposito>>();
      const deposito = { id: 123 };
      jest.spyOn(depositoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ deposito });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(depositoService.update).toHaveBeenCalledWith(deposito);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});