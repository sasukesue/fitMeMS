import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IFitnessClass, FitnessClass } from 'app/shared/model/fitnessClass/fitness-class.model';
import { FitnessClassService } from './fitness-class.service';

@Component({
  selector: 'jhi-fitness-class-update',
  templateUrl: './fitness-class-update.component.html'
})
export class FitnessClassUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    className: [null, [Validators.required]],
    duration: [null, [Validators.required]],
    level: [null, [Validators.required]],
    instructorName: [null, [Validators.required]],
    type: [null, [Validators.required]]
  });

  constructor(protected fitnessClassService: FitnessClassService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ fitnessClass }) => {
      this.updateForm(fitnessClass);
    });
  }

  updateForm(fitnessClass: IFitnessClass): void {
    this.editForm.patchValue({
      id: fitnessClass.id,
      className: fitnessClass.className,
      duration: fitnessClass.duration,
      level: fitnessClass.level,
      instructorName: fitnessClass.instructorName,
      type: fitnessClass.type
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const fitnessClass = this.createFromForm();
    if (fitnessClass.id !== undefined) {
      this.subscribeToSaveResponse(this.fitnessClassService.update(fitnessClass));
    } else {
      this.subscribeToSaveResponse(this.fitnessClassService.create(fitnessClass));
    }
  }

  private createFromForm(): IFitnessClass {
    return {
      ...new FitnessClass(),
      id: this.editForm.get(['id'])!.value,
      className: this.editForm.get(['className'])!.value,
      duration: this.editForm.get(['duration'])!.value,
      level: this.editForm.get(['level'])!.value,
      instructorName: this.editForm.get(['instructorName'])!.value,
      type: this.editForm.get(['type'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFitnessClass>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }
}
