package com.cosmo.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Anotación que sirve para indicar a las páginas que se precisa ser usuario autenticado.
 */
@Retention( value = RetentionPolicy.RUNTIME )
@Target( value = ElementType.TYPE )
public @interface CosmoTable 
{
   /**
    * Nombre del campo en la BBDD.
    */
   String tableName();
   
   /**
    * Nombre de la tabla (se usará como nombre de formulario).
    */
   String name();
}
