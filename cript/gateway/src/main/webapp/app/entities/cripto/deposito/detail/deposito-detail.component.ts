import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDeposito } from '../deposito.model';

@Component({
  selector: 'jhi-deposito-detail',
  templateUrl: './deposito-detail.component.html',
})
export class DepositoDetailComponent implements OnInit {
  deposito: IDeposito | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ deposito }) => {
      this.deposito = deposito;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
