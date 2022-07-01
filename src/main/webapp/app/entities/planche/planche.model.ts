import { ISemestre } from 'app/entities/semestre/semestre.model';

export interface IPlanche {
  id?: number;
  observation?: string;
  semestre?: ISemestre | null;
}

export class Planche implements IPlanche {
  constructor(public id?: number, public observation?: string, public semestre?: ISemestre | null) {}
}

export function getPlancheIdentifier(planche: IPlanche): number | undefined {
  return planche.id;
}
