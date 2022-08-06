import { IActividad } from 'app/entities/actividad/actividad.model';

export interface IEmpleado {
  id?: number;
  nombre?: string | null;
  apellido?: string | null;
  nickname?: string | null;
  email?: string | null;
  telefono?: string | null;
  actividads?: IActividad[] | null;
}

export class Empleado implements IEmpleado {
  constructor(
    public id?: number,
    public nombre?: string | null,
    public apellido?: string | null,
    public nickname?: string | null,
    public email?: string | null,
    public telefono?: string | null,
    public actividads?: IActividad[] | null
  ) {}
}

export function getEmpleadoIdentifier(empleado: IEmpleado): number | undefined {
  return empleado.id;
}
