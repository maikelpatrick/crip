import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { DepositoComponent } from './list/deposito.component';
import { DepositoDetailComponent } from './detail/deposito-detail.component';
import { DepositoUpdateComponent } from './update/deposito-update.component';
import { DepositoDeleteDialogComponent } from './delete/deposito-delete-dialog.component';
import { DepositoRoutingModule } from './route/deposito-routing.module';

@NgModule({
  imports: [SharedModule, DepositoRoutingModule],
  declarations: [DepositoComponent, DepositoDetailComponent, DepositoUpdateComponent, DepositoDeleteDialogComponent],
  entryComponents: [DepositoDeleteDialogComponent],
})
export class CriptoDepositoModule {}
