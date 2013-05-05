package com.cosmo.security;

/**
 * Representa un permiso efectivo sobre una actividad que debe ir asociada a un rol.
 * 
 * @author Gerard Llort
 */
public class Permission extends Activity
{
   private boolean isGranted;
   
   /**
    * Constructor de la clase.
    */
   public Permission()
   {
      super();
      
      this.isGranted = false;
   }
   
   /**
    * Constructor de la clase.
    * 
    * @param activity Una instancia de {@link Activity} que representa la actividad sobre la que se debe otorgar el permiso.
    * @param isGranted {@code true} indica que la actividad está permitida, {@code false} que está denegada. 
    */
   public Permission(Activity activity, boolean isGranted)
   {
      super();
      
      this.setId(activity.getId());
      this.setDescription(activity.getDescription());
      this.setGrantedByDefault(activity.isGrantedByDefault());
      this.setEnabled(activity.isEnabled());
      this.isGranted = isGranted;
   }

   /**
    * Indica si la actividad asociada está permitida.
    * 
    * @return {@code true} indica que la actividad está permitida, {@code false} que está denegada. 
    */
   public boolean isGranted() 
   {
      return isGranted;
   }

   /**
    * Establece el permiso sobre la actividad asociada.
    * 
    * @param isGranted {@code true} indica que la actividad está permitida, {@code false} que está denegada. 
    */
   public void setGranted(boolean isGranted) 
   {
      this.isGranted = isGranted;
   }
   
   /**
    * Transforma la información de la instancia en una cadena con información comprensible. 
    */
   @Override
   public String toString()
   {
      return "[Cosmo Security Activity Permision]\n" +
             " ID                    = " + this.getId() + "\n" +
             " Description           = " + this.getDescription() + "\n" +
             " Is granted by default = " + (this.isGrantedByDefault() ? "yes" : "no") + "\n" +
             " Is enabled            = " + (this.isEnabled() ? "yes" : "no") + "\n" +
             " Is granted            = " + (this.isGranted() ? "yes" : "no") + "\n";
   }
}
