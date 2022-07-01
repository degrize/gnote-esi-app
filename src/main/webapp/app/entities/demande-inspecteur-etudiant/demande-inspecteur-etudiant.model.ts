import { IEtudiant } from 'app/entities/etudiant/etudiant.model';
import { IInspecteur } from 'app/entities/inspecteur/inspecteur.model';

export interface IDemandeInspecteurEtudiant {
  id?: number;
  message?: string;
  etudiants?: IEtudiant[] | null;
  inspecteurs?: IInspecteur[] | null;
}

export class DemandeInspecteurEtudiant implements IDemandeInspecteurEtudiant {
  constructor(
    public id?: number,
    public message?: string,
    public etudiants?: IEtudiant[] | null,
    public inspecteurs?: IInspecteur[] | null
  ) {}
}

export function getDemandeInspecteurEtudiantIdentifier(demandeInspecteurEtudiant: IDemandeInspecteurEtudiant): number | undefined {
  return demandeInspecteurEtudiant.id;
}
