import { IMatiere } from 'app/entities/matiere/matiere.model';
import { IFiliere } from 'app/entities/filiere/filiere.model';

export interface IModule {
  id?: number;
  nomUE?: string | null;
  matieres?: IMatiere[] | null;
  filieres?: IFiliere[] | null;
}

export class Module implements IModule {
  constructor(public id?: number, public nomUE?: string | null, public matieres?: IMatiere[] | null, public filieres?: IFiliere[] | null) {}
}

export function getModuleIdentifier(module: IModule): number | undefined {
  return module.id;
}
