package com.cosmo.net;

import java.sql.Date;

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
    * @return Una cadena de texto con el valor recuperado o {@code null} si el nombre no corresponde a ning√∫n valor.
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
    * @param defaultValue Valor por defecto que se devolver√° si no se encuentra el nombre entre los par√°metros proporcionados por el servidor.
    * @return Una cadena de texto con el valor recuperado o {@code defaultValue} si el nombre no corresponde a ning√∫n valor.
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
    * Obtiene un valor pasado por GET o POST.
    * 
    * @param request Una instancia de {@link HttpServletRequest} que representa la llamada.
    * @param fieldName Nombre del campo cuyo valor se desea recuperar.
    * @return Una cadena de texto con el valor recuperado o {@code null} si el nombre no corresponde a ning√∫n valor.
    */
   public static Integer getInt(HttpServletRequest request, String fieldName)
   {
      return Integer.valueOf(getValue(request, fieldName, "0"));
   }
   
   /**
    * Obtiene un valor pasado por GET o POST.
    * 
    * @param request Una instancia de {@link HttpServletRequest} que representa la llamada.
    * @param fieldName Nombre del campo cuyo valor se desea recuperar.
    * @return Una cadena de texto con el valor recuperado o {@code null} si el nombre no corresponde a ning√∫n valor.
    */
   public static Integer getInt(HttpServletRequest request, String fieldName, int defaultValue)
   {
      if (request == null)
      {
         return defaultValue;
      }
      else if (request.getParameter(fieldName) == null)
      {
         return defaultValue;
      }
      
      return Integer.valueOf(getValue(request, fieldName, "" + defaultValue));
   }
   
   /**
    * Obtiene un valor pasado por GET o POST de tipo {@code Double}.
    * 
    * @param request Una instancia de {@link HttpServletRequest} que representa la llamada.
    * @param fieldName Nombre del campo cuyo valor se desea recuperar.
    * 
    * @return Una valor {@code Double} que corresponde al par·metro recuperado o {@code 0} si no se puede recuperar el par·metro.
    */
   public static Double getDouble(HttpServletRequest request, String fieldName)
   {
      return Double.valueOf(getValue(request, fieldName, "0"));
   }
   
   /**
    * Obtiene un valor pasado por GET o POST de tipo {@code Double}.
    * 
    * @param request Una instancia de {@link HttpServletRequest} que representa la llamada.
    * @param fieldName Nombre del campo cuyo valor se desea recuperar.
    * @param defaultValue Valor por defecto que se devolver· en caso de no encontrar el valor.
    * 
    * @return Una valor {@code Double} que corresponde al par·metro recuperado o {@code defaultValue} si no se puede recuperar el par·metro.
    */
   public static Double getDouble(HttpServletRequest request, String fieldName, double defaultValue)
   {
      if (request == null)
      {
         return defaultValue;
      }
      else if (request.getParameter(fieldName) == null)
      {
         return defaultValue;
      }
      
      return Double.valueOf(getValue(request, fieldName, "" + defaultValue));
   }
   
   /**
    * Obtiene un valor pasado por GET o POST de tipo {@code Date}.<br />
    * El formato aceptado por Èste mÈtodo es {@code yyyy-mm-dd}.
    * 
    * @param request Una instancia de {@link HttpServletRequest} que representa la llamada.
    * @param fieldName Nombre del campo cuyo valor se desea recuperar.
    * 
    * @return Una valor {@code Double} que corresponde al par·metro recuperado o 
    *         {@code null} si no se puede recuperar el par·metro o se produce alg˙n error en la transformaciÛn.
    */
   public static Date getDate(HttpServletRequest request, String fieldName)
   {
      if (request == null)
      {
         return null;
      }
      else if (request.getParameter(fieldName) == null)
      {
         return null;
      }
      else if (request.getParameter(fieldName).isEmpty())
      {
         return null;
      }
      
      return Date.valueOf(getValue(request, fieldName));
   }
   
   /**
    * Indica si un par√°metro ha sido proporcionado por el servidor.
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
    * Determina si dos par√°metros contienen el mismo valor.
    * 
    * @param request Una instancia de {@link HttpServletRequest} que representa la llamada.
    * @param field1 Nombre del primer campoo a comprobar.
    * @param field2 Nombre del segundo campoo a comprobar.
    * @return {@code true} si el valor de los dos par√°metros coincide o {@code false} en cualquier otro caso.
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
