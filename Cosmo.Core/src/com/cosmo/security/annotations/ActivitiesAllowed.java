package com.cosmo.security.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Anotación que sirve para indicar a las páginas que actividades deben poder ejecutar los usuarios que accedan a ellas.
 */
@Retention( value = RetentionPolicy.RUNTIME )
@Target( value = ElementType.TYPE )
public @interface ActivitiesAllowed 
{
   public String[] value();
}
