package com.cosmo.data.orm.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Anotación que sirve para indicar a las páginas que se precisa ser usuario autenticado.
 * 
 * @author Gerard Llort
 */
@Documented
@Retention( value = RetentionPolicy.RUNTIME )
@Target( value = ElementType.METHOD )
public @interface CormObjectFieldStaticList 
{
   /**
    * Contiene un array con los valores de las opciones.
    */
   String[] values();
   
   /**
    * Contiene un array con las etiquetas que se mostrarán para cada opción al usuario.
    */
   String[] labels();
   
   /**
    * Valor del elemento seleccionado por defecto.
    */
   String defaultValue() default "";
}
