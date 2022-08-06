package com.sti.stiprueba.domain;

import com.sti.stiprueba.domain.enumeration.Estatus;
import java.time.Instant;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Actividad.class)
public abstract class Actividad_ {

	public static volatile SingularAttribute<Actividad, String> descripcion;
	public static volatile SingularAttribute<Actividad, Estatus> estatus;
	public static volatile SingularAttribute<Actividad, Instant> fechaInicio;
	public static volatile SingularAttribute<Actividad, Empleado> empleado;
	public static volatile SingularAttribute<Actividad, Long> id;
	public static volatile SingularAttribute<Actividad, Instant> fechaFin;

	public static final String DESCRIPCION = "descripcion";
	public static final String ESTATUS = "estatus";
	public static final String FECHA_INICIO = "fechaInicio";
	public static final String EMPLEADO = "empleado";
	public static final String ID = "id";
	public static final String FECHA_FIN = "fechaFin";

}

