package com.cosmo.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Utilidades para cadenas de texto (String).
 * 
 * @author Gerard Llort
 */
public class StringUtils
{
   /**
    * Separador de línea específico del SO, por ejemplo {@code "\n"} o {@code "\r\n"}.
    */
   public static final String LINE_SEPARATOR = System.getProperty("line.separator");


   //==============================================
   // Static Members
   //==============================================

   /**
    * Permet esbrinar si una cadena está vacía o nula.
    * 
    * @param text Cadena de carácteres a evaluar.
    * 
    * @return {@code true} si la cadena está vacía o nula o {@code false} en qualsevol otro caso.
    */
   public static boolean isNullOrEmpty(String text)
   {
      if (text == null)
      {
         return true;
      }

      return (text.isEmpty());
   }

   /**
    * Indica si una cadena está vacía o es nula.
    * 
    * @param text Cadena de carácteres a evaluar.
    * 
    * @return {@code true} si la cadena está vacía o es nula o {@code false} en cualquier otro caso.
    */
   public static boolean isNullOrEmptyTrim(String text)
   {
      if (text == null)
      {
         return true;
      }

      text = text.trim();

      return (text.isEmpty());
   }

   /**
    * Determina si una cadena contiene sólo números.
    * 
    * @param str Una cadena de texto a comprobar.
    * 
    * @return {@code true} si la cadena sólo contiene números o {@code false} en cualquier otro caso.
    */
   public static boolean isNumeric(String str)
   {
      for (char c : str.toCharArray())
      {
         if (!Character.isDigit(c))
         {
            return false;
         }
      }

      return true;
   }

   /**
    * Formatea un objeto por defecto.
    * 
    * @param value Valor a formatear.
    * 
    * @return Una cadena que representa el valor formateado.
    */
   public static String formatValue(Object value)
   {
      Class<?> paramType = value.getClass();

      if ((paramType == Integer.class) || (paramType == int.class) || (paramType == Long.class) || (paramType == long.class) || 
          (paramType == Short.class) || (paramType == short.class) || (paramType == Byte.class) || (paramType == byte.class))
      {
         return String.format("%d", (Integer) value);
      }
      else if ((paramType == Float.class) || (paramType == float.class) || (paramType == Double.class) || (paramType == double.class))
      {
         return String.format("%s", value);
      }
      else if ((paramType == Boolean.class) || (paramType == boolean.class))
      {
         return ((Boolean) value ? "Sí" : "No");
      }
      else if ((paramType == Date.class) || (paramType == Timestamp.class))
      {
         SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

         return sdf.format(value);
      }

      return value.toString();
   }
}
