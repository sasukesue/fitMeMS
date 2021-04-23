import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IFitnessClass, FitnessClass } from 'app/shared/model/fitnessClass/fitness-class.model';
import { FitnessClassService } from './fitness-class.service';
import { FitnessClassComponent } from './fitness-class.component';
import { FitnessClassDetailComponent } from './fitness-class-detail.component';
import { FitnessClassUpdateComponent } from './fitness-class-update.component';

@Injectable({ providedIn: 'root' })
export class FitnessClassResolve implements Resolve<IFitnessClass> {
  constructor(private service: FitnessClassService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IFitnessClass> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((fitnessClass: HttpResponse<FitnessClass>) => {
          if (fitnessClass.body) {
            return of(fitnessClass.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new FitnessClass());
  }
}

export const fitnessClassRoute: Routes = [
  {
    path: '',
    component: FitnessClassComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'FitnessClasses'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: FitnessClassDetailComponent,
    resolve: {
      fitnessClass: FitnessClassResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'FitnessClasses'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: FitnessClassUpdateComponent,
    resolve: {
      fitnessClass: FitnessClassResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'FitnessClasses'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: FitnessClassUpdateComponent,
    resolve: {
      fitnessClass: FitnessClassResolve
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'FitnessClasses'
    },
    canActivate: [UserRouteAccessService]
  }
];
