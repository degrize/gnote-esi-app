import { IAnneeScolaire } from 'app/entities/annee-scolaire/annee-scolaire.model';

export interface ISemestre {
  id?: number;
  nomSemestre?: string;
  anneeScolaire?: IAnneeScolaire | null;
}

export class Semestre implements ISemestre {
  constructor(public id?: number, public nomSemestre?: string, public anneeScolaire?: IAnneeScolaire | null) {}
}

export function getSemestreIdentifier(semestre: ISemestre): number | undefined {
  return semestre.id;
}
