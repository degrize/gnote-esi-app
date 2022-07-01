import { TypeCycle } from 'app/entities/enumerations/type-cycle.model';

export interface ICycle {
  id?: number;
  nomCycle?: TypeCycle;
}

export class Cycle implements ICycle {
  constructor(public id?: number, public nomCycle?: TypeCycle) {}
}

export function getCycleIdentifier(cycle: ICycle): number | undefined {
  return cycle.id;
}
