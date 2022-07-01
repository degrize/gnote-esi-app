import { IProfesseur } from 'app/entities/professeur/professeur.model';
import { ISoutenance } from 'app/entities/soutenance/soutenance.model';

export interface IJury {
  id?: number;
  presidentJury?: string;
  professeurs?: IProfesseur[] | null;
  soutenance?: ISoutenance | null;
}

export class Jury implements IJury {
  constructor(
    public id?: number,
    public presidentJury?: string,
    public professeurs?: IProfesseur[] | null,
    public soutenance?: ISoutenance | null
  ) {}
}

export function getJuryIdentifier(jury: IJury): number | undefined {
  return jury.id;
}
