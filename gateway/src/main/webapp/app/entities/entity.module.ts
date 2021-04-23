import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'location',
        loadChildren: () => import('./location/location/location.module').then(m => m.LocationLocationModule)
      },
      {
        path: 'fitness-class',
        loadChildren: () => import('./fitnessClass/fitness-class/fitness-class.module').then(m => m.FitnessClassFitnessClassModule)
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ]
})
export class GatewayEntityModule {}
