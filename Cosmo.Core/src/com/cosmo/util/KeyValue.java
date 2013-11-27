package com.cosmo.util;

/**
 * Implementa un par clave/valor para uso genérico.
 * 
 * @author Gerard Llort
 */
public class KeyValue 
{
   private String key;
   private Object value;
   private boolean defaultOption;


   //==============================================
   // Constructors
   //==============================================

   /**
    * Constructor de la clase.
    */
   public KeyValue()
   {
      this.key = "";
      this.value = null;
      this.defaultOption = false;
   }

   /**
    * Constructor de la clase.
    * 
    * @param key Una cadena que contiene la clave del par.
    * @param value Un objeto que contiene el valor del par.
    */
   public KeyValue(String key, Object value)
   {
      this.key = key;
      this.value = value;
      this.defaultOption = false;
   }

   /**
    * Constructor de la clase.
    * 
    * @param key Una cadena que contiene la clave del par.
    * @param value Un objeto que contiene el valor del par.
    * @param isDefaultValue Indica si el elemento es el valor por defecto de la lista.
    */
   public KeyValue(String key, Object value, boolean isDefaultValue)
   {
      this.key = key;
      this.value = value;
      this.defaultOption = isDefaultValue;
   }


   //==============================================
   // Properties
   //==============================================

   public String getKey()
   {
      return key;
   }

   public void setKey(String key)
   {
      this.key = key;
   }

   public Object getValue() 
   {
      return value;
   }

   public void setValue(Object value)
   {
      this.value = value;
   }

   public boolean isDefaultOption() 
   {
      return defaultOption;
   }

   public void setDefaultOption(boolean defaultOption) 
   {
      this.defaultOption = defaultOption;
   }


   //==============================================
   // Methods
   //==============================================

   @Override
   public String toString()
   {
      return "[key:" + this.key + ", value:" + this.value.toString() + ", isDefaultValue=" + (this.defaultOption ? "true" : "false") + "]";
   }
}
