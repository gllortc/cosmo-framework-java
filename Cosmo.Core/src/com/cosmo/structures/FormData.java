package com.cosmo.structures;

import java.util.HashMap;

public class FormData 
{
   private String formId;
   private HashMap<String, String> map;
   
   public FormData(String formId)
   {
      this.formId = formId;
      this.map = null;
   }

   public String getFormId() 
   {
      return formId;
   }

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
}
