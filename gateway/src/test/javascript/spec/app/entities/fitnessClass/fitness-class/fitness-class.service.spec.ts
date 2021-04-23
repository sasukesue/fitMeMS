import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { FitnessClassService } from 'app/entities/fitnessClass/fitness-class/fitness-class.service';
import { IFitnessClass, FitnessClass } from 'app/shared/model/fitnessClass/fitness-class.model';
import { Level } from 'app/shared/model/enumerations/level.model';
import { Type } from 'app/shared/model/enumerations/type.model';

describe('Service Tests', () => {
  describe('FitnessClass Service', () => {
    let injector: TestBed;
    let service: FitnessClassService;
    let httpMock: HttpTestingController;
    let elemDefault: IFitnessClass;
    let expectedResult: IFitnessClass | IFitnessClass[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule]
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(FitnessClassService);
      httpMock = injector.get(HttpTestingController);

      elemDefault = new FitnessClass(0, 'AAAAAAA', 0, Level.BEGINNER, 'AAAAAAA', Type.ALL);
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a FitnessClass', () => {
        const returnedFromService = Object.assign(
          {
            id: 0
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new FitnessClass()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a FitnessClass', () => {
        const returnedFromService = Object.assign(
          {
            className: 'BBBBBB',
            duration: 1,
            level: 'BBBBBB',
            instructorName: 'BBBBBB',
            type: 'BBBBBB'
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of FitnessClass', () => {
        const returnedFromService = Object.assign(
          {
            className: 'BBBBBB',
            duration: 1,
            level: 'BBBBBB',
            instructorName: 'BBBBBB',
            type: 'BBBBBB'
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a FitnessClass', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
