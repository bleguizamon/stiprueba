import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'actividad',
        data: { pageTitle: 'stipruebaApp.actividad.home.title' },
        loadChildren: () => import('./actividad/actividad.module').then(m => m.ActividadModule),
      },
      {
        path: 'empleado',
        data: { pageTitle: 'stipruebaApp.empleado.home.title' },
        loadChildren: () => import('./empleado/empleado.module').then(m => m.EmpleadoModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
