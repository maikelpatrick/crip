import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IDeposito, Deposito } from '../deposito.model';
import { DepositoService } from '../service/deposito.service';

@Component({
  selector: 'jhi-deposito-update',
  templateUrl: './deposito-update.component.html',
})
export class DepositoUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    volume: [],
    id_deposito: [],
    client_account: [],
    entidade_account: [],
  });

  constructor(protected depositoService: DepositoService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ deposito }) => {
      this.updateForm(deposito);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const deposito = this.createFromForm();
    if (deposito.id !== undefined) {
      this.subscribeToSaveResponse(this.depositoService.update(deposito));
    } else {
      this.subscribeToSaveResponse(this.depositoService.create(deposito));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDeposito>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(deposito: IDeposito): void {
    this.editForm.patchValue({
      id: deposito.id,
      volume: deposito.volume,
      id_deposito: deposito.id_deposito,
      client_account: deposito.client_account,
      entidade_account: deposito.entidade_account,
    });
  }

  protected createFromForm(): IDeposito {
    return {
      ...new Deposito(),
      id: this.editForm.get(['id'])!.value,
      volume: this.editForm.get(['volume'])!.value,
      id_deposito: this.editForm.get(['id_deposito'])!.value,
      client_account: this.editForm.get(['client_account'])!.value,
      entidade_account: this.editForm.get(['entidade_account'])!.value,
    };
  }
}
