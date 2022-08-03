import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISaque, Saque } from '../saque.model';
import { SaqueService } from '../service/saque.service';

@Injectable({ providedIn: 'root' })
export class SaqueRoutingResolveService implements Resolve<ISaque> {
  constructor(protected service: SaqueService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISaque> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((saque: HttpResponse<Saque>) => {
          if (saque.body) {
            return of(saque.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Saque());
  }
}
