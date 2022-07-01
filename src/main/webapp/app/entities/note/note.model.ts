import { IEtudiant } from 'app/entities/etudiant/etudiant.model';
import { IMatiere } from 'app/entities/matiere/matiere.model';
import { TypeNote } from 'app/entities/enumerations/type-note.model';

export interface INote {
  id?: number;
  note?: number;
  typeNote?: TypeNote;
  etudiants?: IEtudiant[] | null;
  matieres?: IMatiere[] | null;
}

export class Note implements INote {
  constructor(
    public id?: number,
    public note?: number,
    public typeNote?: TypeNote,
    public etudiants?: IEtudiant[] | null,
    public matieres?: IMatiere[] | null
  ) {}
}

export function getNoteIdentifier(note: INote): number | undefined {
  return note.id;
}
