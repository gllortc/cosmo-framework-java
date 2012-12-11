package com.cosmo.ui.controls;


/**
 * Clase para la construcción de controles identificables.<br />
 * Los controles identificables permiten ser accedidos desde la pÃ¡gina en tiempo de ejecución.
 * 
 * @author Gerard Llort
 */
public abstract class IdentificableControl extends Control
{
   private String id;
   
   //==============================================
   // Constructors
   //==============================================
   
   /**
    * Constructor de la clase.
    */
   IdentificableControl(String id)
   {
      super();
      this.id = id;
   }

   //==============================================
   // properties
   //==============================================
   
   @Override
   public abstract String getControlTypeId();
   
   public String getId() 
   {
      return id;
   }

   public void setId(String id) 
   {
      this.id = id;
   }
}
