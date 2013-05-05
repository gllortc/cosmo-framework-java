package com.cosmo.security;

/**
 * Implementa una actividad que precisa autorización en la aplicación.
 * 
 * @author Gerard Llort
 */
public class Activity 
{
   private String id;
   private String description;
   private boolean isGrantedByDefault;
   private boolean enabled;
   
   /**
    * Constructor de la clase.
    */
   public Activity()
   {
      this.id = "";
      this.description = "";
      this.isGrantedByDefault = false;
      this.enabled = true;
   }
   
   /**
    * Constructor de la clase.
    * 
    * @param id Identificador de la actividad.
    */
   public Activity(String id)
   {
      this.id = id;
      this.description = "";
      this.isGrantedByDefault = false;
      this.enabled = true;
   }
   
   /**
    * Devuelve el identificador de la actividad.
    */
   public String getId() 
   {
      return id;
   }
   
   /**
    * Establece el identificador de la actividad.
    */
   public void setId(String id) 
   {
      this.id = id;
   }
   
   /**
    * Devuelve la descripción de la actividad.
    */
   public String getDescription() 
   {
      return description;
   }
   
   /**
    * Establece la descripción de la actividad.
    */
   public void setDescription(String description) 
   {
      this.description = description;
   }
   
   /**
    * Indica si la actividad está permitida por defecto.
    * <br /><br />
    * {@code true} indica que todos los usuarios puede ejecutar la actividad salvo que el rol establezca la denegación.
    * {@code false} (valor por defecto) indica que ningún usuario puede ejecutar la actividad salvo aquellos cuyo rol lo permita.
    */
   public boolean isGrantedByDefault() 
   {
      return isGrantedByDefault;
   }
   
   public void setGrantedByDefault(boolean isGrantedByDefault) 
   {
      this.isGrantedByDefault = isGrantedByDefault;
   }
   
   /**
    * Indica si la actividad está activa.
    * * <br /><br />
    * {@code true} indica que la actividad está activa y se puede ejecutar (según permisos de rol).
    * {@code false} indica que nadie (excepto super-usuarios) pueden ejecutar la actividad.
    */
   public boolean isEnabled() 
   {
      return enabled;
   }
   
   public void setEnabled(boolean enabled) 
   {
      this.enabled = enabled;
   }
   
   /**
    * Transforma la información de la instancia en una cadena con información comprensible. 
    */
   @Override
   public String toString()
   {
      return "[Cosmo Security Activity Info]\n" +
             " ID                    = " + this.getId() + "\n" +
             " Description           = " + this.getDescription() + "\n" +
             " Is granted by default = " + (this.isGrantedByDefault() ? "yes" : "no") + "\n" +
             " Is enabled            = " + (this.isEnabled() ? "yes" : "no") + "\n";
   }
}
