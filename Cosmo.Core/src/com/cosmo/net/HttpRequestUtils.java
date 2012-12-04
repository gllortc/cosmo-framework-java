package com.cosmo.net;

import com.cosmo.util.StringUtils;
import javax.servlet.http.HttpServletRequest;

/**
 * Implementa una clase con utilidades referentes a Request.
 * 
 * @author Gerard Llort
 */
public class HttpRequestUtils 
{
   //==============================================
   // Static members
   //==============================================
   
   /**
    * Obtiene un valor pasado por GET o POST.
    * 
    * @param request Una instancia de {@link HttpServletRequest} que representa la llamada.
    * @param fieldName Nombre del campo cuyo valor se desea recuperar.
    * @return Una cadena de texto con el valor recuperado o {@code null} si el nombre no corresponde a ningún valor.
    */
   public static String getValue(HttpServletRequest request, String fieldName)
   {
      return getValue(request, fieldName, "");
   }
   
   /**
    * Obtiene un valor pasado por GET o POST.
    * 
    * @param request Una instancia de {@link HttpServletRequest} que representa la llamada.
    * @param fieldName Nombre del campo cuyo valor se desea recuperar.
    * @param defaultValue Valor por defecto que se devolverá si no se encuentra el nombre entre los parámetros proporcionados por el servidor.
    * @return Una cadena de texto con el valor recuperado o {@code defaultValue} si el nombre no corresponde a ningún valor.
    */
   public static String getValue(HttpServletRequest request, String fieldName, String defaultValue)
   {
      if (request == null)
      {
         return defaultValue;
      }
      else if (request.getParameter(fieldName) == null)
      {
         return defaultValue;
      }
      else
      {
         return request.getParameter(fieldName);
      }
   }
   
   /**
    * Indica si un parámetro ha sido proporcionado por el servidor.
    *  
    * @param request Una instancia de {@link HttpServletRequest} que representa la llamada.
    * @param fieldName Nombre del campo que se desea comprobar.
    * @return {@code true} si el valor ha sido proporcionado por el servidor o {@code false} en cualquier otro caso.
    */
   public static boolean isNullOrEmpty(HttpServletRequest request, String fieldName)
   {
      if (request == null)
      {
         return true;
      }
      else if (request.getParameter(fieldName) == null)
      {
         return true;
      }
      else
      {
         return StringUtils.isNullOrEmpty(request.getParameter(fieldName));
      }
   }
   
   /**
    * Determina si dos parámetros contienen el mismo valor.
    * 
    * @param request Una instancia de {@link HttpServletRequest} que representa la llamada.
    * @param field1 Nombre del primer campoo a comprobar.
    * @param field2 Nombre del segundo campoo a comprobar.
    * @return {@code true} si el valor de los dos parámetros coincide o {@code false} en cualquier otro caso.
    */
   public static boolean equals(HttpServletRequest request, String field1, String field2)
   {
      if (request == null)
      {
         return false;
      }
      else if (request.getParameter(field1) == null || request.getParameter(field2) == null)
      {
         return false;
      }
      else
      {
         return request.getParameter(field1).equals(request.getParameter(field2));
      }
   }
}
