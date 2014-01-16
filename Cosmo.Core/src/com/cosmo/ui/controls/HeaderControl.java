package com.cosmo.ui.controls;

import com.cosmo.Workspace;
import com.cosmo.ui.templates.TemplateControl;
import com.cosmo.util.StringUtils;

/**
 * Implementa un control de contenido que representa la cabecera (título) de una página.
 * 
 * @author Gerard Llort
 */
public class HeaderControl extends Control
{
   /** Control Type Unique ID */
   private static final String CTUID = "CosmoUiCtrlHeader";

   // Declaración de variables internas
   private String title;
   private String description;
   private String author;


   //==============================================
   // Contructors
   //==============================================

   /**
    * Contructor de la clase {@link HeaderControl}.
    * 
    * @param workspace Una instancia de {@link Workspace} que representa el espacio de aplicación actual.
    */
   public HeaderControl(Workspace workspace)
   {
      super(workspace);
      initialize();
   }

   /**
    * Contructor de la clase {@link HeaderControl}.
    * 
    * @param workspace Una instancia de {@link Workspace} que representa el espacio de aplicación actual.
    * @param id Identificador único del control en la página.
    */
   public HeaderControl(Workspace workspace, String id)
   {
      super(workspace, id);
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
   public String render()
   {
      String xhtml;
      String xitem;
      TemplateControl ctrl;

      // Obtiene la plantilla y la parte del control
      ctrl = getWorkspace().getTemplate().getControl(HeaderControl.CTUID);

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


   //==============================================
   // Private Members
   //==============================================

   /**
    * Inicializa la instancia.
    */
   private void initialize()
   {
      this.title = StringUtils.EMPTY;
      this.description = StringUtils.EMPTY;
      this.author = StringUtils.EMPTY;
   }
}
