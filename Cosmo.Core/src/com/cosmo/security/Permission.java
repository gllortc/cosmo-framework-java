package com.cosmo.security;

/**
 * Representa un permiso efectivo sobre una actividad que debe ir asociada a un rol.
 * 
 * @author Gerard Llort
 */
public class Permission extends Activity
{
   private boolean isGranted;


   //==============================================
   // Contructors
   //==============================================

   /**
    * Constructor de la clase {@link Permission}.
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
    * @param isGranted {@code true} indica que la actividad est� permitida, {@code false} que est� denegada. 
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


   //==============================================
   // Properties
   //==============================================

   /**
    * Indica si la actividad asociada est� permitida.
    * 
    * @return {@code true} indica que la actividad est� permitida, {@code false} que est� denegada. 
    */
   public boolean isGranted() 
   {
      return isGranted;
   }

   /**
    * Establece el permiso sobre la actividad asociada.
    * 
    * @param isGranted {@code true} indica que la actividad est� permitida, {@code false} que est� denegada. 
    */
   public void setGranted(boolean isGranted) 
   {
      this.isGranted = isGranted;
   }


   //==============================================
   // Methods
   //==============================================

   /**
    * Transforma la informaci�n de la instancia en una cadena con informaci�n comprensible. 
    */
   @Override
   public String toString()
   {
      return this.getId() + " (" + this.getDescription() + ") Granted? " + (this.isGranted() ? "yes" : "no");
   }
}
