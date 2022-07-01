import { IFiliere } from 'app/entities/filiere/filiere.model';
import { IMatiere } from 'app/entities/matiere/matiere.model';
import { IProfesseur } from 'app/entities/professeur/professeur.model';
import { IEtudiant } from 'app/entities/etudiant/etudiant.model';

export interface IClasse {
  id?: number;
  nomClasse?: string;
  filiere?: IFiliere | null;
  matieres?: IMatiere[] | null;
  professeurs?: IProfesseur[] | null;
  etudiants?: IEtudiant[] | null;
}

export class Classe implements IClasse {
  constructor(
    public id?: number,
    public nomClasse?: string,
    public filiere?: IFiliere | null,
    public matieres?: IMatiere[] | null,
    public professeurs?: IProfesseur[] | null,
    public etudiants?: IEtudiant[] | null
  ) {}
}

export function getClasseIdentifier(classe: IClasse): number | undefined {
  return classe.id;
}
