import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'deposito',
        data: { pageTitle: 'gatewayApp.criptoDeposito.home.title' },
        loadChildren: () => import('./cripto/deposito/deposito.module').then(m => m.CriptoDepositoModule),
      },
      {
        path: 'saque',
        data: { pageTitle: 'gatewayApp.criptoSaque.home.title' },
        loadChildren: () => import('./cripto/saque/saque.module').then(m => m.CriptoSaqueModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
