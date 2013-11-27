package com.cosmo.util;

import java.util.HashMap;

/**
 * Implementa un contenedor para almacenar los datos de un formulario para una determinada sesión.
 * 
 * @author Gerard Llort
 */
public class FormData 
{
   private String formId;
   private HashMap<String, String> map;


   //==============================================
   // Constructors
   //==============================================

   /**
    * Constructor de la clase.
    * 
    * @param formId Identificador único del formulario.
    */
   public FormData(String formId)
   {
      this.formId = formId;
      this.map = null;
   }


   //==============================================
   // Properties
   //==============================================

   /**
    * Devuelve el identificador único del formulario.
    */
   public String getFormId() 
   {
      return formId;
   }


   //==============================================
   // Methods
   //==============================================

   /**
    * Almacena el valor de un campo del formulario.
    * 
    * @param name Nombre del campo.
    * @param value Valor del campo.
    */
   public void addParameterValue(String name, String value)
   {
      // Instancia el mapa la primera vez que se usa
      if (this.map == null)
      {
         this.map = new HashMap<String, String>();
      }

      // Almacena el valor
      this.map.put(name, value);
   }

   /**
    * Obtiene el valor de un campo del formulario.
    * 
    * @param name Nombre del campo.
    * @return Devuelve una cadena que contiene el valor asociado al campo o {@code null} si no se encuentra el campo en el contenedor.
    */
   public String getParameterValue(String name)
   {
      // Instancia el mapa la primera vez que se usa
      if (this.map == null)
      {
         return "";
      }

      // Devuelve el valor
      return this.map.get(name);
   }

   /**
    * Devuelve una cadena de texto que representa los valores contenidos en la instancia.
    */
   @Override
   public String toString()
   {
      String val = "";

      val += "[";
      for (String str : this.map.keySet())
      {
         val += "[" + str + ": " + this.map.get(str) + "]";
      }
      val += "]";

      return val;
   }
}
