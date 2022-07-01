import { IProfesseur } from 'app/entities/professeur/professeur.model';
import { IInspecteur } from 'app/entities/inspecteur/inspecteur.model';

export interface IDemandeInspecteurDE {
  id?: number;
  message?: string;
  professeurs?: IProfesseur[] | null;
  inspecteurs?: IInspecteur[] | null;
}

export class DemandeInspecteurDE implements IDemandeInspecteurDE {
  constructor(
    public id?: number,
    public message?: string,
    public professeurs?: IProfesseur[] | null,
    public inspecteurs?: IInspecteur[] | null
  ) {}
}

export function getDemandeInspecteurDEIdentifier(demandeInspecteurDE: IDemandeInspecteurDE): number | undefined {
  return demandeInspecteurDE.id;
}
