package com.cosmo.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Representa una URL con parámetros.
 * 
 * @author Gerard Llort
 */
public class URL 
{
   private static final String TOKEN_PARAM_INIT = "?";
   private static final String TOKEN_PARAM_SEPARATOR = "&";
   
   private String url;
   private ArrayList<UrlParameter> params;
   
   //==============================================
   // Constructors
   //==============================================
   
   /**
    * Constructor de la clase.
    * 
    * @param url Una cadena que representa la URL base (p. ej. www.ibm.es o http://www.microsoft.com).
    */
   public URL(String url)
   {
      this.url = url;
      this.params = new ArrayList<URL.UrlParameter>();
   }

   //==============================================
   // Properties
   //==============================================
   
   /**
    * Devuelve la URL base.
    */
   public String getUrl() 
   {
      return url;
   }

   /**
    * Establece la URL base.
    */
   public void setUrl(String url)
   {
      this.url = url;
   }
   
   //==============================================
   // Methods
   //==============================================

   /**
    * Agrega un nuevo parámetro a la URL.
    * 
    * @param name Nombre del parámetro.
    * @param value Valor del parámetro.
    */
   public void addParameter(String name, String value)
   {
      this.params.add(new UrlParameter(name, value));
   }
   
   /**
    * Agrega un nuevo parámetro a la URL.
    * 
    * @param name Nombre del parámetro.
    * @param value Valor del parámetro.
    */
   public void addParameter(String name, Integer value)
   {
      this.params.add(new UrlParameter(name, value.toString()));
   }
   
   /**
    * Obtiene el valor de un parámetro de tipo {@link String}.
    * 
    * @param name Nombre del parámetro.
    * @return Una cadena que corresponde al valor asociado al parámetro especificado.
    */
   public String getParameterString(String name)
   {
      return getParameterString(name, "");
   }
   
   /**
    * Obtiene el valor de un parámetro de tipo {@link String}.
    * 
    * @param name Nombre del parámetro.
    * @param defaultValue En caso de no encontrar el parámetro, valor por defecto que se devolverá.
    * @return Una cadena que corresponde al valor asociado al parámetro especificado.
    */
   public String getParameterString(String name, String defaultValue)
   {
      name = name.trim().toLowerCase();
      
      for (UrlParameter param : this.params)
      {
         if (name.equals(param.name))
         {
            return param.value;
         }
      }
      
      return defaultValue;
   }
   
   /**
    * Obtiene el valor de un parámetro de tipo {@link Integer}.
    * 
    * @param name Nombre del parámetro.
    * @return Una valor entero que corresponde al valor asociado al parámetro especificado.
    */
   public Integer getParameterInt(String name)
   {
      return getParameterInt(name, 0);
   }
   
   /**
    * Obtiene el valor de un parámetro de tipo {@link Integer}.
    * 
    * @param name Nombre del parámetro.
    * @param defaultValue En caso de no encontrar el parámetro, valor por defecto que se devolverá.
    * @return Una valor entero que corresponde al valor asociado al parámetro especificado.
    */
   public Integer getParameterInt(String name, Integer defaultValue)
   {
      name = name.trim().toLowerCase();
      
      for (UrlParameter param : this.params)
      {
         if (name.equals(param.name))
         {
            try
            {
               return Integer.valueOf(param.value);
            }
            catch (NumberFormatException ex)
            {
               return defaultValue;
            }
         }
      }
      
      return defaultValue;
   }
   
   public String toString(String enc) 
   {
      boolean first = true;
      String url = "";
      String encodedValue;
      
      url += this.url + URL.TOKEN_PARAM_INIT;
      for (UrlParameter param : this.params)
      {
         try 
         {
            encodedValue =  URLEncoder.encode(param.value, enc);
         } 
         catch (Exception e) 
         {
            encodedValue =  enc;
         }
         
         url += (first ? "" : URL.TOKEN_PARAM_SEPARATOR) + param.name + "=" + encodedValue;
         first = false;
      }
      
      return url;
   }
   
   //==============================================
   // Subclasses
   //==============================================
   
   /**
    * Implementa un parámetro de URL.
    */
   class UrlParameter
   {
      private String name;
      private String value;
      
      // Constructors
      
      public UrlParameter(String name, String value)
      {
         this.name = name;
         this.value = value;
      }

      // Properties
      
      public String getName() 
      {
         return name;
      }

      public void setName(String name)
      {
         this.name = name;
      }

      public String getValue() 
      {
         return value;
      }

      public void setValue(String value) 
      {
         this.value = value;
      }
   }
}
