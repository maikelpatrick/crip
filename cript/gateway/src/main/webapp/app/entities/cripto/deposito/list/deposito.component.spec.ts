import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { DepositoService } from '../service/deposito.service';

import { DepositoComponent } from './deposito.component';

describe('Deposito Management Component', () => {
  let comp: DepositoComponent;
  let fixture: ComponentFixture<DepositoComponent>;
  let service: DepositoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [DepositoComponent],
    })
      .overrideTemplate(DepositoComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DepositoComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(DepositoService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        })
      )
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.depositos?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
