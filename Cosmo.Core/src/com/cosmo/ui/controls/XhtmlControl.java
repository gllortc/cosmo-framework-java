package com.cosmo.ui.controls;

import com.cosmo.Workspace;

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
   public XhtmlControl(Workspace workspace)
   {
      super(workspace);
      initialize("");
   }
   
   /**
    * Contructor de la clase.
    */
   public XhtmlControl(Workspace workspace, String id)
   {
      super(workspace, id);
      initialize("");
   }
   
   /**
    * Contructor de la clase.
    * 
    * @param xhtml Una cadena que contiene el código XHTML con el que se rellenará el control.
    */
   public XhtmlControl(Workspace workspace, String id, String xhtml)
   {
      super(workspace, id);
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
    * Agrega un párrafo al contenido.
    * 
    * @param text Texto que contiene el párrafo.
    */
   public void appendParagraph(String text)
   {
      append("<p>" + text + "</p>\n");
   }
   
   /**
    * Agrega un título H1 al contenido.
    * 
    * @param title Texto que contiene el título.
    * @param headderLevel Nivel de importancia del título (1..5).
    */
   public void appendHeadder(String title, int headderLevel)
   {
      append("<h" + headderLevel + ">" + title + "</h" + headderLevel + ">\n");
   }
   
   /**
    * Renderiza el control y genera el código XHTML de representación.
    *
    * @return Devuelve una cadena en formato XHTML que representa el control. 
    */
   @Override
   public String render() 
   {
      return xhtml.toString();
   }
   
   
   //==============================================
   // Static members
   //==============================================
   
   public static String formatBold(String text)
   {
      return "<strong>" + text + "</strong>";
   }
   
   public static String formatEmphatized(String text)
   {
      return "<em>" + text + "</em>";
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
