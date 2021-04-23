import { Level } from 'app/shared/model/enumerations/level.model';
import { Type } from 'app/shared/model/enumerations/type.model';

export interface IFitnessClass {
  id?: number;
  className?: string;
  duration?: number;
  level?: Level;
  instructorName?: string;
  type?: Type;
}

export class FitnessClass implements IFitnessClass {
  constructor(
    public id?: number,
    public className?: string,
    public duration?: number,
    public level?: Level,
    public instructorName?: string,
    public type?: Type
  ) {}
}
