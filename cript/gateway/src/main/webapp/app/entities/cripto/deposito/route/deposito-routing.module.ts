import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { DepositoComponent } from '../list/deposito.component';
import { DepositoDetailComponent } from '../detail/deposito-detail.component';
import { DepositoUpdateComponent } from '../update/deposito-update.component';
import { DepositoRoutingResolveService } from './deposito-routing-resolve.service';

const depositoRoute: Routes = [
  {
    path: '',
    component: DepositoComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DepositoDetailComponent,
    resolve: {
      deposito: DepositoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DepositoUpdateComponent,
    resolve: {
      deposito: DepositoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DepositoUpdateComponent,
    resolve: {
      deposito: DepositoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(depositoRoute)],
  exports: [RouterModule],
})
export class DepositoRoutingModule {}
