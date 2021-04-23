import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { GatewayTestModule } from '../../../../test.module';
import { FitnessClassUpdateComponent } from 'app/entities/fitnessClass/fitness-class/fitness-class-update.component';
import { FitnessClassService } from 'app/entities/fitnessClass/fitness-class/fitness-class.service';
import { FitnessClass } from 'app/shared/model/fitnessClass/fitness-class.model';

describe('Component Tests', () => {
  describe('FitnessClass Management Update Component', () => {
    let comp: FitnessClassUpdateComponent;
    let fixture: ComponentFixture<FitnessClassUpdateComponent>;
    let service: FitnessClassService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [GatewayTestModule],
        declarations: [FitnessClassUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(FitnessClassUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(FitnessClassUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(FitnessClassService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new FitnessClass(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new FitnessClass();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
