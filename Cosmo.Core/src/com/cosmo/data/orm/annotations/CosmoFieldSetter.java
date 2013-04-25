package com.cosmo.data.orm.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

/**
 * Anotación que sirve para indicar a las páginas que se precisa ser usuario autenticado.
 */
@Retention( value = RetentionPolicy.RUNTIME )
@Target( value = ElementType.METHOD )
public @interface CosmoFieldSetter 
{
   /**
    * Nombre del campo (se usará como nombre de campo en el formulario).
    */
   String name();
}
