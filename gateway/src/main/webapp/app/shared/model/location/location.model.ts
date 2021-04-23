export interface ILocation {
  id?: number;
  branchName?: string;
  blkNo?: string;
  streetName?: string;
  postalCode?: number;
}

export class Location implements ILocation {
  constructor(
    public id?: number,
    public branchName?: string,
    public blkNo?: string,
    public streetName?: string,
    public postalCode?: number
  ) {}
}
