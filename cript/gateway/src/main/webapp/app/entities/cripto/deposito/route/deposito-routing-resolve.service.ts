import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDeposito, Deposito } from '../deposito.model';
import { DepositoService } from '../service/deposito.service';

@Injectable({ providedIn: 'root' })
export class DepositoRoutingResolveService implements Resolve<IDeposito> {
  constructor(protected service: DepositoService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDeposito> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((deposito: HttpResponse<Deposito>) => {
          if (deposito.body) {
            return of(deposito.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Deposito());
  }
}
