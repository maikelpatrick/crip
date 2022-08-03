import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { SaqueComponent } from './list/saque.component';
import { SaqueDetailComponent } from './detail/saque-detail.component';
import { SaqueUpdateComponent } from './update/saque-update.component';
import { SaqueDeleteDialogComponent } from './delete/saque-delete-dialog.component';
import { SaqueRoutingModule } from './route/saque-routing.module';

@NgModule({
  imports: [SharedModule, SaqueRoutingModule],
  declarations: [SaqueComponent, SaqueDetailComponent, SaqueUpdateComponent, SaqueDeleteDialogComponent],
  entryComponents: [SaqueDeleteDialogComponent],
})
export class CriptoSaqueModule {}
