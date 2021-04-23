import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { GatewaySharedModule } from 'app/shared/shared.module';
import { FitnessClassComponent } from './fitness-class.component';
import { FitnessClassDetailComponent } from './fitness-class-detail.component';
import { FitnessClassUpdateComponent } from './fitness-class-update.component';
import { FitnessClassDeleteDialogComponent } from './fitness-class-delete-dialog.component';
import { fitnessClassRoute } from './fitness-class.route';

@NgModule({
  imports: [GatewaySharedModule, RouterModule.forChild(fitnessClassRoute)],
  declarations: [FitnessClassComponent, FitnessClassDetailComponent, FitnessClassUpdateComponent, FitnessClassDeleteDialogComponent],
  entryComponents: [FitnessClassDeleteDialogComponent]
})
export class FitnessClassFitnessClassModule {}
