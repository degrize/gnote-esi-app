import { IModule } from 'app/entities/module/module.model';
import { IProfesseur } from 'app/entities/professeur/professeur.model';
import { INote } from 'app/entities/note/note.model';
import { IClasse } from 'app/entities/classe/classe.model';

export interface IMatiere {
  id?: number;
  nomEC?: string;
  coeff?: number;
  module?: IModule | null;
  professeurs?: IProfesseur[] | null;
  notes?: INote[] | null;
  classes?: IClasse[] | null;
}

export class Matiere implements IMatiere {
  constructor(
    public id?: number,
    public nomEC?: string,
    public coeff?: number,
    public module?: IModule | null,
    public professeurs?: IProfesseur[] | null,
    public notes?: INote[] | null,
    public classes?: IClasse[] | null
  ) {}
}

export function getMatiereIdentifier(matiere: IMatiere): number | undefined {
  return matiere.id;
}
