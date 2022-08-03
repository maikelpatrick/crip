import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISaque, getSaqueIdentifier } from '../saque.model';

export type EntityResponseType = HttpResponse<ISaque>;
export type EntityArrayResponseType = HttpResponse<ISaque[]>;

@Injectable({ providedIn: 'root' })
export class SaqueService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/saques', 'cripto');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(saque: ISaque): Observable<EntityResponseType> {
    return this.http.post<ISaque>(this.resourceUrl, saque, { observe: 'response' });
  }

  update(saque: ISaque): Observable<EntityResponseType> {
    return this.http.put<ISaque>(`${this.resourceUrl}/${getSaqueIdentifier(saque) as number}`, saque, { observe: 'response' });
  }

  partialUpdate(saque: ISaque): Observable<EntityResponseType> {
    return this.http.patch<ISaque>(`${this.resourceUrl}/${getSaqueIdentifier(saque) as number}`, saque, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ISaque>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISaque[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addSaqueToCollectionIfMissing(saqueCollection: ISaque[], ...saquesToCheck: (ISaque | null | undefined)[]): ISaque[] {
    const saques: ISaque[] = saquesToCheck.filter(isPresent);
    if (saques.length > 0) {
      const saqueCollectionIdentifiers = saqueCollection.map(saqueItem => getSaqueIdentifier(saqueItem)!);
      const saquesToAdd = saques.filter(saqueItem => {
        const saqueIdentifier = getSaqueIdentifier(saqueItem);
        if (saqueIdentifier == null || saqueCollectionIdentifiers.includes(saqueIdentifier)) {
          return false;
        }
        saqueCollectionIdentifiers.push(saqueIdentifier);
        return true;
      });
      return [...saquesToAdd, ...saqueCollection];
    }
    return saqueCollection;
  }
}
