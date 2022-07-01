import { IJury } from 'app/entities/jury/jury.model';
import { ISalle } from 'app/entities/salle/salle.model';
import { IHoraire } from 'app/entities/horaire/horaire.model';
import { IEtudiant } from 'app/entities/etudiant/etudiant.model';
import { TypeSoutenance } from 'app/entities/enumerations/type-soutenance.model';

export interface ISoutenance {
  id?: number;
  typeSout?: TypeSoutenance;
  themeSout?: string;
  noteSout?: number;
  juries?: IJury[] | null;
  salle?: ISalle | null;
  horaire?: IHoraire | null;
  etudiants?: IEtudiant[] | null;
}

export class Soutenance implements ISoutenance {
  constructor(
    public id?: number,
    public typeSout?: TypeSoutenance,
    public themeSout?: string,
    public noteSout?: number,
    public juries?: IJury[] | null,
    public salle?: ISalle | null,
    public horaire?: IHoraire | null,
    public etudiants?: IEtudiant[] | null
  ) {}
}

export function getSoutenanceIdentifier(soutenance: ISoutenance): number | undefined {
  return soutenance.id;
}
