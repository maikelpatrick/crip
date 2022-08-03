import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SaqueComponent } from '../list/saque.component';
import { SaqueDetailComponent } from '../detail/saque-detail.component';
import { SaqueUpdateComponent } from '../update/saque-update.component';
import { SaqueRoutingResolveService } from './saque-routing-resolve.service';

const saqueRoute: Routes = [
  {
    path: '',
    component: SaqueComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SaqueDetailComponent,
    resolve: {
      saque: SaqueRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SaqueUpdateComponent,
    resolve: {
      saque: SaqueRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SaqueUpdateComponent,
    resolve: {
      saque: SaqueRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(saqueRoute)],
  exports: [RouterModule],
})
export class SaqueRoutingModule {}
