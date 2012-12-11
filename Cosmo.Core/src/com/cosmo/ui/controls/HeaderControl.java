package com.cosmo.ui.controls;

import javax.servlet.http.HttpSession;

import com.cosmo.ui.templates.Template;
import com.cosmo.ui.templates.TemplateControl;

/**
 * Implementa un control de contenido que representa la cabecera (título) de una página.
 * 
 * @author Gerard Llort
 */
public class HeaderControl extends Control
{
   private static final String CONTROL_ID = "CosmoUiCtrlHeader";
   
   private String title;
   private String description;
   private String author;

   //==============================================
   // Contructors
   //==============================================
   
   /**
    * Contructor de la clase.
    */
   public HeaderControl(String id)
   {
      super(id);
      initialize();
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
      return this.getClass().getName();
   }

   /**
    * Devuelve el título de la cabecera.
    */
   public String getTitle() 
   {
      return title;
   }

   /**
    * Establece el título de la cabecera.
    */
   public void setTitle(String title) 
   {
      this.title = title;
   }

   /**
    * Devuelve el texto descriptivo de la cabecera.
    */
   public String getDescription() 
   {
      return description;
   }

   /**
    * Establece el texto descriptivo de la cabecera.
    */
   public void setDescription(String description) 
   {
      this.description = description;
   }

   /**
    * Obtiene el autor del documento.
    */
   public String getAuthor() 
   {
      return author;
   }

   /**
    * Establece el autor del documento.
    */
   public void setAuthor(String author) 
   {
      this.author = author;
   }
   
   //==============================================
   // Methods
   //==============================================
   
   private static final String CPART_TITLE = "page-title";
   private static final String CPART_UTILITIES = "page-utilities";
   
   private static final String TAG_TITLE = "TITLE";
   private static final String TAG_UTILITIES = "UTILITIES";
   private static final String TAG_DESCRIPTION = "DESCRIPTION";
   private static final String TAG_AUTHOR = "AUTHOR";
   
   /**
    * Renderiza el control y genera el código XHTML de representación.
    *
    * @return Devuelve una cadena en formato XHTML que representa el control.
    */
   @Override
   public String render(HttpSession session, Template template)
   {
      String xhtml;
      String xitem;
      TemplateControl ctrl;
      
      // Obtiene la plantilla y la parte del control
      ctrl = template.getControl(HeaderControl.CONTROL_ID);
      
      // Genera la cabecera del formulario
      xhtml = "";
      xitem = ctrl.getElement(CPART_TITLE);
      xitem = Control.replaceTag(xitem, TAG_TITLE, this.getTitle());
      xitem = Control.replaceTag(xitem, TAG_AUTHOR, this.author);
      xitem = Control.replaceTag(xitem, TAG_DESCRIPTION, this.getDescription());
      xitem = Control.replaceTag(xitem, TAG_UTILITIES, ctrl.getElement(CPART_UTILITIES));
      xhtml += xitem;
      
      return xhtml;
   }

   @Override
   public String toString()
   {
      StringBuilder str = new StringBuilder();

      str.append("<div id=\"").append(this.getId()).append("\">").append("\n");
      str.append("  <h1>").append(this.title).append("</h1>").append("\n");
      if (!this.title.equals(""))
      {
         str.append("  <p>").append(this.description).append("</p>").append("\n");
      }
      str.append("</div>").append("\n");
      
      return str.toString();
   }
   
   private void initialize()
   {
      this.title = "";
      this.description = "";
      this.author = "";
   }
}
