package com.cosmo.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

/**
 * Anotación que sirve para indicar a las páginas que se precisa ser usuario autenticado.
 */
@Retention( value = RetentionPolicy.RUNTIME )
@Target( value = ElementType.METHOD )
public @interface CosmoFieldGetter 
{
   public enum FieldType
   {
      Text,
      Integer,
      Decimal
   }
   
   /**
    * Define el tipo de datos del campo.
    */
   FieldType fieldType() default FieldType.Text;

   /**
    * Nombre del campo en la BBDD.
    */
   String tableColumnName();
   
   /**
    * Nombre del campo (se usará como nombre de campo en el formulario).
    */
   String name();
   
   /**
    * Contiene la etiqueta que debe mostrarse junto al campo en un formulario.
    */
   String label() default "";
   
   /**
    * Indica si el campo es de sólo lectura.
    */
   boolean readOnly() default false;
}
