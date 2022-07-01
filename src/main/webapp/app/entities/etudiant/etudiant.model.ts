import { IClasse } from 'app/entities/classe/classe.model';
import { IEncadreur } from 'app/entities/encadreur/encadreur.model';
import { IInspecteur } from 'app/entities/inspecteur/inspecteur.model';
import { IProfesseur } from 'app/entities/professeur/professeur.model';
import { ISoutenance } from 'app/entities/soutenance/soutenance.model';
import { INote } from 'app/entities/note/note.model';
import { IDemandeInspecteurEtudiant } from 'app/entities/demande-inspecteur-etudiant/demande-inspecteur-etudiant.model';

export interface IEtudiant {
  id?: number;
  matriculeET?: string;
  nomET?: string;
  prenomET?: string;
  photoContentType?: string | null;
  photo?: string | null;
  numeroParent?: string | null;
  numeroTuteur?: string | null;
  contactET?: string | null;
  classes?: IClasse[] | null;
  encadreur?: IEncadreur | null;
  inspecteurs?: IInspecteur[] | null;
  professeurs?: IProfesseur[] | null;
  soutenances?: ISoutenance[] | null;
  notes?: INote[] | null;
  demandeInspecteurEtudiants?: IDemandeInspecteurEtudiant[] | null;
}

export class Etudiant implements IEtudiant {
  constructor(
    public id?: number,
    public matriculeET?: string,
    public nomET?: string,
    public prenomET?: string,
    public photoContentType?: string | null,
    public photo?: string | null,
    public numeroParent?: string | null,
    public numeroTuteur?: string | null,
    public contactET?: string | null,
    public classes?: IClasse[] | null,
    public encadreur?: IEncadreur | null,
    public inspecteurs?: IInspecteur[] | null,
    public professeurs?: IProfesseur[] | null,
    public soutenances?: ISoutenance[] | null,
    public notes?: INote[] | null,
    public demandeInspecteurEtudiants?: IDemandeInspecteurEtudiant[] | null
  ) {}
}

export function getEtudiantIdentifier(etudiant: IEtudiant): number | undefined {
  return etudiant.id;
}
