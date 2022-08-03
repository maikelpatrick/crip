import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IDeposito } from '../deposito.model';
import { DepositoService } from '../service/deposito.service';

@Component({
  templateUrl: './deposito-delete-dialog.component.html',
})
export class DepositoDeleteDialogComponent {
  deposito?: IDeposito;

  constructor(protected depositoService: DepositoService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.depositoService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
