package com.sti.stiprueba.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Empleado.class)
public abstract class Empleado_ {

	public static volatile SetAttribute<Empleado, Actividad> actividads;
	public static volatile SingularAttribute<Empleado, String> apellido;
	public static volatile SingularAttribute<Empleado, String> nickname;
	public static volatile SingularAttribute<Empleado, Long> id;
	public static volatile SingularAttribute<Empleado, String> telefono;
	public static volatile SingularAttribute<Empleado, String> nombre;
	public static volatile SingularAttribute<Empleado, String> email;

	public static final String ACTIVIDADS = "actividads";
	public static final String APELLIDO = "apellido";
	public static final String NICKNAME = "nickname";
	public static final String ID = "id";
	public static final String TELEFONO = "telefono";
	public static final String NOMBRE = "nombre";
	public static final String EMAIL = "email";

}

