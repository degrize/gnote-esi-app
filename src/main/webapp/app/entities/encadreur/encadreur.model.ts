import { IEtudiant } from 'app/entities/etudiant/etudiant.model';

export interface IEncadreur {
  id?: number;
  nomEnc?: string;
  prenomsEnc?: string | null;
  emailEnc?: string | null;
  etudiants?: IEtudiant[] | null;
}

export class Encadreur implements IEncadreur {
  constructor(
    public id?: number,
    public nomEnc?: string,
    public prenomsEnc?: string | null,
    public emailEnc?: string | null,
    public etudiants?: IEtudiant[] | null
  ) {}
}

export function getEncadreurIdentifier(encadreur: IEncadreur): number | undefined {
  return encadreur.id;
}
