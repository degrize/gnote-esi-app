import { IEtudiant } from 'app/entities/etudiant/etudiant.model';
import { IBulletin } from 'app/entities/bulletin/bulletin.model';

export interface IRecupererBulletin {
  id?: number;
  signatureEleve?: string;
  bulletinScanneContentType?: string;
  bulletinScanne?: string;
  etudiant?: IEtudiant | null;
  bulletin?: IBulletin | null;
}

export class RecupererBulletin implements IRecupererBulletin {
  constructor(
    public id?: number,
    public signatureEleve?: string,
    public bulletinScanneContentType?: string,
    public bulletinScanne?: string,
    public etudiant?: IEtudiant | null,
    public bulletin?: IBulletin | null
  ) {}
}

export function getRecupererBulletinIdentifier(recupererBulletin: IRecupererBulletin): number | undefined {
  return recupererBulletin.id;
}
