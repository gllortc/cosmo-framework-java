package com.cosmo.security;

import java.util.HashMap;

/**
 * Implementa un agente de configuración de seguridad.
 * @author Gerard Llort
  */
public class Agent 
{
   private String id;
   private String moduleClass;
   private HashMap<String, String> params;
   
   /**
    * Constructor de la clase.
    */
   public Agent()
   {
      this.id = "";
      this.moduleClass = "";
      this.params = new HashMap<String, String>();
   }
   
   /**
    * Constructor de la clase.
    * 
    * @param id Identificador del agente.
    * @param moduleClass Nombre completo de la clase (incluyendo el package). 
    */
   public Agent(String id, String moduleClass)
   {
      this.id = id;
      this.moduleClass = moduleClass;
      this.params = new HashMap<String, String>();
   }
   
   /**
    * Devuelve el identificador del agente.
    */
   public String getId() 
   {
      return id;
   }
   
   /**
    * Establece el identificador del agente.
    */
   public void setId(String id) 
   {
      this.id = id;
   }
   
   /**
    * Devuelve el nombre completo de la clase (incluyendo el package) del agente. 
    */
   public String getModuleClass() 
   {
      return moduleClass;
   }

   /**
    * Establece el nombre completo de la clase (incluyendo el package) del agente.
    */
   public void setModuleClass(String moduleClass) 
   {
      this.moduleClass = moduleClass;
   }

   /**
    * Devuelve un {@link HashMap} con todos los parámetros definidos para el agente.
    */
   public HashMap<String, String> getParams() 
   {
      return params;
   }
   
   /**
    * Establece un valor de confioguración del agente.
    * 
    * @param key Clave de configuración.
    * @param value Valor asociado a la clave.
    */
   public void setParam(String key, String value)
   {
      params.put(key, value);
   }
   
   /**
    * Permite obtener el valor asociado a una clave de configuración.
    * 
    * @param key Clave de configuración.
    * 
    * @return Una cadena que representa el valor asociado a la clave definida por {@value key}.
    */
   public String getParam(String key)
   {
      return params.get(key);
   }
   
   /**
    * Transforma la información de la instancia en una cadena con información comprensible. 
    */
   @Override
   public String toString()
   {
      return "[Cosmo Security Agent Info]\n" +
             " ID     = " + this.getId() + "\n" +
             " DRIVER = " + this.getModuleClass() + "\n" +
             " PARAMS = " + this.params.toString() + "\n";
   }
}
