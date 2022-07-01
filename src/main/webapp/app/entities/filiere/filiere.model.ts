import { ICycle } from 'app/entities/cycle/cycle.model';
import { IModule } from 'app/entities/module/module.model';
import { IClasse } from 'app/entities/classe/classe.model';

export interface IFiliere {
  id?: number;
  nomFiliere?: string;
  etudiant?: ICycle | null;
  modules?: IModule[] | null;
  classes?: IClasse[] | null;
}

export class Filiere implements IFiliere {
  constructor(
    public id?: number,
    public nomFiliere?: string,
    public etudiant?: ICycle | null,
    public modules?: IModule[] | null,
    public classes?: IClasse[] | null
  ) {}
}

export function getFiliereIdentifier(filiere: IFiliere): number | undefined {
  return filiere.id;
}
