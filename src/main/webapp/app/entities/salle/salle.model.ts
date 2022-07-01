import { IHoraire } from 'app/entities/horaire/horaire.model';

export interface ISalle {
  id?: number;
  numeroSalle?: string;
  nbrePlace?: number | null;
  etat?: string | null;
  horaires?: IHoraire[] | null;
}

export class Salle implements ISalle {
  constructor(
    public id?: number,
    public numeroSalle?: string,
    public nbrePlace?: number | null,
    public etat?: string | null,
    public horaires?: IHoraire[] | null
  ) {}
}

export function getSalleIdentifier(salle: ISalle): number | undefined {
  return salle.id;
}
