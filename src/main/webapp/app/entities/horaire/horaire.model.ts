import dayjs from 'dayjs/esm';
import { ISalle } from 'app/entities/salle/salle.model';

export interface IHoraire {
  id?: number;
  dateSout?: dayjs.Dayjs;
  dateEffet?: dayjs.Dayjs | null;
  salles?: ISalle[] | null;
}

export class Horaire implements IHoraire {
  constructor(public id?: number, public dateSout?: dayjs.Dayjs, public dateEffet?: dayjs.Dayjs | null, public salles?: ISalle[] | null) {}
}

export function getHoraireIdentifier(horaire: IHoraire): number | undefined {
  return horaire.id;
}
