package com.cosmo.ui.controls;

import javax.servlet.http.HttpSession;

import com.cosmo.ui.templates.Template;

/**
 * Implementa un control que permite incorporar código XHTML directament a una página de Cosmo.
 * 
 * @author Gerard Llort
 */
public class XhtmlControl extends Control
{
   private static final String CONTROL_ID = "CosmoUiCtrlXhtml";
   
   private StringBuilder xhtml;
   
   //==============================================
   // Contructors
   //==============================================
   
   /**
    * Contructor de la clase.
    */
   public XhtmlControl()
   {
      super();
      initialize("");
   }
   
   /**
    * Contructor de la clase.
    */
   public XhtmlControl(String id)
   {
      super(id);
      initialize("");
   }
   
   /**
    * Contructor de la clase.
    * 
    * @param xhtml Una cadena que contiene el código XHTML con el que se rellenará el control.
    */
   public XhtmlControl(String id, String xhtml)
   {
      super(id);
      initialize(xhtml);
   }
   
   //==============================================
   // Properties
   //==============================================
   
   /**
    * Devuelve un identificador único del tipo de control.
    */
   @Override
   public String getControlTypeId() 
   {
      return XhtmlControl.CONTROL_ID;
   }

   /**
    * Devuelve la longitud del contenido del control.
    */
   public int length() 
   {
      return this.xhtml.length();
   }
   
   //==============================================
   // Methods
   //==============================================

   /**
    * Concatena una cadena de texto al contenido actual del control.
    * 
    * @param xhtml Código XHTML a concatenar.
    */
   public void append(String xhtml)
   {
      this.xhtml.append(xhtml);
   }
   
   /**
    * Concatena una cadena de texto al contenido actual del control.
    * 
    * @param obj Código XHTML a concatenar.
    */
   public void append(Object obj)
   {
      this.xhtml.append(obj.toString());
   }
   
   /**
    * Renderiza el control y genera el código XHTML de representación.
    *
    * @param session Una instancia de {@link HttpSession}.
    * @param template Una instancia de {@link Template} que representa la plantilla actual.
    * @return Devuelve una cadena en formato XHTML que representa el control. 
    */
   @Override
   public String render(HttpSession session, Template template) 
   {
      return xhtml.toString();
   }
   
   //==============================================
   // Private members
   //==============================================
   
   /**
    * Inicializa la instancia.
    */
   private void initialize(String text)
   {
      this.xhtml = new StringBuilder(text);
   }   
}
