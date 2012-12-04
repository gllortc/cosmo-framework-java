package com.cosmo.ui.controls;

import com.cosmo.ui.templates.Template;

/**
 * Clase para la construcción de controles identificables.<br />
 * Los controles identificables permiten ser accedidos desde la página en tiempo de ejecución.
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

   //==============================================
   // Methods
   //==============================================

   /**
    * Renderiza el control y genera el código XHTML de representación.
    *
    * @return Devuelve una cadena en formato XHTML que representa el control. 
    */
   @Override
   public abstract String render(Template template);
}
