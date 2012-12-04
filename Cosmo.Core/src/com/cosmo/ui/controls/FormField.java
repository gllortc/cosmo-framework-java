package com.cosmo.ui.controls;

/**
 * Clase abstracta que sirve de base para todos los elementos que pueden ser 
 * representados en un formulario mediante XHTML.
 * @author gllort
 */
public abstract class FormField 
{
   /**
    * Obtiene el nombre (identificador único) del campo.
    */
   public abstract String getName();
   
   /**
    * Establece el valor del campo.
    */
   public abstract void setValue(String value);
   
   /**
    * Convierte la instancia en una cadena XHTML que representa el elemento en una página web.
    */
   public abstract String render();
}
