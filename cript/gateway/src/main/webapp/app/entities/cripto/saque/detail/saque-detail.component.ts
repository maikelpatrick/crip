import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISaque } from '../saque.model';

@Component({
  selector: 'jhi-saque-detail',
  templateUrl: './saque-detail.component.html',
})
export class SaqueDetailComponent implements OnInit {
  saque: ISaque | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ saque }) => {
      this.saque = saque;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
