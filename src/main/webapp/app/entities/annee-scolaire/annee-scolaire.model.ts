import dayjs from 'dayjs/esm';

export interface IAnneeScolaire {
  id?: number;
  periode?: string;
  dateDebut?: dayjs.Dayjs;
  dateFin?: dayjs.Dayjs | null;
}

export class AnneeScolaire implements IAnneeScolaire {
  constructor(public id?: number, public periode?: string, public dateDebut?: dayjs.Dayjs, public dateFin?: dayjs.Dayjs | null) {}
}

export function getAnneeScolaireIdentifier(anneeScolaire: IAnneeScolaire): number | undefined {
  return anneeScolaire.id;
}
