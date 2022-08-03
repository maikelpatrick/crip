import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ISaque } from '../saque.model';
import { SaqueService } from '../service/saque.service';

@Component({
  templateUrl: './saque-delete-dialog.component.html',
})
export class SaqueDeleteDialogComponent {
  saque?: ISaque;

  constructor(protected saqueService: SaqueService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.saqueService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
