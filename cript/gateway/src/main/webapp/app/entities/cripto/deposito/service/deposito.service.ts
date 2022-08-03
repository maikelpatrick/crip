import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDeposito, getDepositoIdentifier } from '../deposito.model';

export type EntityResponseType = HttpResponse<IDeposito>;
export type EntityArrayResponseType = HttpResponse<IDeposito[]>;

@Injectable({ providedIn: 'root' })
export class DepositoService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/depositos', 'cripto');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(deposito: IDeposito): Observable<EntityResponseType> {
    return this.http.post<IDeposito>(this.resourceUrl, deposito, { observe: 'response' });
  }

  update(deposito: IDeposito): Observable<EntityResponseType> {
    return this.http.put<IDeposito>(`${this.resourceUrl}/${getDepositoIdentifier(deposito) as number}`, deposito, { observe: 'response' });
  }

  partialUpdate(deposito: IDeposito): Observable<EntityResponseType> {
    return this.http.patch<IDeposito>(`${this.resourceUrl}/${getDepositoIdentifier(deposito) as number}`, deposito, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IDeposito>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDeposito[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addDepositoToCollectionIfMissing(depositoCollection: IDeposito[], ...depositosToCheck: (IDeposito | null | undefined)[]): IDeposito[] {
    const depositos: IDeposito[] = depositosToCheck.filter(isPresent);
    if (depositos.length > 0) {
      const depositoCollectionIdentifiers = depositoCollection.map(depositoItem => getDepositoIdentifier(depositoItem)!);
      const depositosToAdd = depositos.filter(depositoItem => {
        const depositoIdentifier = getDepositoIdentifier(depositoItem);
        if (depositoIdentifier == null || depositoCollectionIdentifiers.includes(depositoIdentifier)) {
          return false;
        }
        depositoCollectionIdentifiers.push(depositoIdentifier);
        return true;
      });
      return [...depositosToAdd, ...depositoCollection];
    }
    return depositoCollection;
  }
}
