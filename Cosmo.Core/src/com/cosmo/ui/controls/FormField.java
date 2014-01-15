package com.cosmo.ui.controls;

import com.cosmo.Workspace;

/**
 * Clase abstracta que deben implementar los controles de formulario.
 * 
 * @author Gerard Llort
 */
public abstract class FormField 
{

   //==============================================
   // Constructors
   //==============================================

   /**
    * Constructor de la clase {@link FormField}.
    */
   public FormField()
   {
      // Constructor vac�o
   }


   //==============================================
   // Abstract members
   //==============================================

   /**
    * Obtiene el nombre (identificador único) del campo.
    */
   public abstract String getName();

   /**
    * Establece el valor del campo.
    */
   public abstract void setValue(Object value);

   /**
    * Convierte la instancia en una cadena XHTML que representa el elemento en una página web.
    */
   public abstract String render(Workspace workspace);
}
