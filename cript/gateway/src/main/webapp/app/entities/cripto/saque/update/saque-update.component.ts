import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ISaque, Saque } from '../saque.model';
import { SaqueService } from '../service/saque.service';

@Component({
  selector: 'jhi-saque-update',
  templateUrl: './saque-update.component.html',
})
export class SaqueUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    volume: [],
    client_account: [],
    entidade_account: [],
  });

  constructor(protected saqueService: SaqueService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ saque }) => {
      this.updateForm(saque);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const saque = this.createFromForm();
    if (saque.id !== undefined) {
      this.subscribeToSaveResponse(this.saqueService.update(saque));
    } else {
      this.subscribeToSaveResponse(this.saqueService.create(saque));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISaque>>): void {
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

  protected updateForm(saque: ISaque): void {
    this.editForm.patchValue({
      id: saque.id,
      volume: saque.volume,
      client_account: saque.client_account,
      entidade_account: saque.entidade_account,
    });
  }

  protected createFromForm(): ISaque {
    return {
      ...new Saque(),
      id: this.editForm.get(['id'])!.value,
      volume: this.editForm.get(['volume'])!.value,
      client_account: this.editForm.get(['client_account'])!.value,
      entidade_account: this.editForm.get(['entidade_account'])!.value,
    };
  }
}
