package com.cosmo.ui.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Anotación que permite indicar a una página como debe <em>cachear</em> el contenido.
 * 
 * @author Gerard Llort
 */
@Retention( value = RetentionPolicy.RUNTIME )
@Target( value = ElementType.TYPE )
public @interface CacheScope 
{
   /**
    * Enumera los distintos ámbitos que puede tener la caché de página:<br />
    * <ul>
    * <li>{@code SCOPE_APPLICATION}: El contenido de la página es común a todos y sólo se configura una vez</li>
    * <li>{@code SCOPE_SESSION}: El contenido de la página es común a todos y sólo se configura una vez</li>
    * <li>{@code NO_CACHE}: La caché estará deshabilirtada para la página</li>
    * </ul>
    * 
    * @author Gerard Llort
    */
   public enum PageCacheScopes
   {
      /** El contenido de la página estará disponible para todos los usuarios */
      SCOPE_APPLICATION,
      /** El contenido de la página estará disponible para todos los usuarios */
      SCOPE_SESSION,
      /** La caché estará deshabilirtada para la página */
      NO_CACHE
   }
   
   public PageCacheScopes scope();
}
