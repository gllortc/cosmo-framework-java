package com.cosmo.security.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Anotación que sirve para indicar si una página contiene el formulario de autenticación (login).
 * 
 * @author Gerard Llort 
 */
@Retention( value = RetentionPolicy.RUNTIME )
@Target( value = ElementType.TYPE )
public @interface AuthenticationForm 
{
    // String message() default "";
}
