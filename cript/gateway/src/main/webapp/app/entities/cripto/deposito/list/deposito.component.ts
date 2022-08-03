import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IDeposito } from '../deposito.model';
import { DepositoService } from '../service/deposito.service';
import { DepositoDeleteDialogComponent } from '../delete/deposito-delete-dialog.component';

@Component({
  selector: 'jhi-deposito',
  templateUrl: './deposito.component.html',
})
export class DepositoComponent implements OnInit {
  depositos?: IDeposito[];
  isLoading = false;

  constructor(protected depositoService: DepositoService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.depositoService.query().subscribe({
      next: (res: HttpResponse<IDeposito[]>) => {
        this.isLoading = false;
        this.depositos = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IDeposito): number {
    return item.id!;
  }

  delete(deposito: IDeposito): void {
    const modalRef = this.modalService.open(DepositoDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.deposito = deposito;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
