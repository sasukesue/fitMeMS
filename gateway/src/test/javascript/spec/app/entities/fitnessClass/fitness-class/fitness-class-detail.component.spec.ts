import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { GatewayTestModule } from '../../../../test.module';
import { FitnessClassDetailComponent } from 'app/entities/fitnessClass/fitness-class/fitness-class-detail.component';
import { FitnessClass } from 'app/shared/model/fitnessClass/fitness-class.model';

describe('Component Tests', () => {
  describe('FitnessClass Management Detail Component', () => {
    let comp: FitnessClassDetailComponent;
    let fixture: ComponentFixture<FitnessClassDetailComponent>;
    const route = ({ data: of({ fitnessClass: new FitnessClass(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [GatewayTestModule],
        declarations: [FitnessClassDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(FitnessClassDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(FitnessClassDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load fitnessClass on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.fitnessClass).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
