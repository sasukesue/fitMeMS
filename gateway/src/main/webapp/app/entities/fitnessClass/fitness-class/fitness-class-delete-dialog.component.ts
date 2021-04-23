import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IFitnessClass } from 'app/shared/model/fitnessClass/fitness-class.model';
import { FitnessClassService } from './fitness-class.service';

@Component({
  templateUrl: './fitness-class-delete-dialog.component.html'
})
export class FitnessClassDeleteDialogComponent {
  fitnessClass?: IFitnessClass;

  constructor(
    protected fitnessClassService: FitnessClassService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.fitnessClassService.delete(id).subscribe(() => {
      this.eventManager.broadcast('fitnessClassListModification');
      this.activeModal.close();
    });
  }
}
