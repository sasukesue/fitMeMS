import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption, SearchWithPagination } from 'app/shared/util/request-util';
import { IFitnessClass } from 'app/shared/model/fitnessClass/fitness-class.model';

type EntityResponseType = HttpResponse<IFitnessClass>;
type EntityArrayResponseType = HttpResponse<IFitnessClass[]>;

@Injectable({ providedIn: 'root' })
export class FitnessClassService {
  public resourceUrl = SERVER_API_URL + 'services/fitnessclass/api/fitness-classes';
  public resourceSearchUrl = SERVER_API_URL + 'services/fitnessclass/api/_search/fitness-classes';

  constructor(protected http: HttpClient) {}

  create(fitnessClass: IFitnessClass): Observable<EntityResponseType> {
    return this.http.post<IFitnessClass>(this.resourceUrl, fitnessClass, { observe: 'response' });
  }

  update(fitnessClass: IFitnessClass): Observable<EntityResponseType> {
    return this.http.put<IFitnessClass>(this.resourceUrl, fitnessClass, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IFitnessClass>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IFitnessClass[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IFitnessClass[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }
}
