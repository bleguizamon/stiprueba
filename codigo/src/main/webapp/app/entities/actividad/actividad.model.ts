import dayjs from 'dayjs/esm';
import { IEmpleado } from 'app/entities/empleado/empleado.model';
import { Estatus } from 'app/entities/enumerations/estatus.model';

export interface IActividad {
  id?: number;
  descripcion?: string;
  estatus?: Estatus | null;
  fechaInicio?: dayjs.Dayjs | null;
  fechaFin?: dayjs.Dayjs | null;
  diasRetraso?: number | null;
  empleado?: IEmpleado | null;
}

export class Actividad implements IActividad {
  constructor(
    public id?: number,
    public descripcion?: string,
    public estatus?: Estatus | null,
    public fechaInicio?: dayjs.Dayjs | null,
    public fechaFin?: dayjs.Dayjs | null,
    public diasRetraso?: number | null,
    public empleado?: IEmpleado | null
  ) {}
}

export function getActividadIdentifier(actividad: IActividad): number | undefined {
  return actividad.id;
}
