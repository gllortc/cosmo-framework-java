package com.cosmo.ui.controls;

import com.cosmo.ui.templates.TemplateControl;

/**
 * Implementa un botón para las barras de botones {@link ButtonBarControl}.
 * 
 * @author Gerard Llort
 */
public class ButtonBarItem 
{
   private static final String CPART_ITEM = "buttonbar-item";
         
   private static final String TAG_TITLE = "FTITLE";
   private static final String TAG_HREF = "HREF";
   private static final String TAG_ICON = "ICON";
   
   private String title;
   private String href;
   private String icon;
   
   //==============================================
   // Constructors
   //==============================================
   
   /**
    * Constructor de la clase.
    */
   public ButtonBarItem()
   {
      this.title = "";
      this.href = "";
      this.icon = "";
   }
   
   /**
    * Constructor de la clase.
    * 
    * @param title Título del elemento.
    * @param href URL del enlace.
    */
   public ButtonBarItem(String title, String href)
   {
      this.title = title;
      this.href = href;
      this.icon = "";
   }
   
   /**
    * Constructor de la clase.
    * 
    * @param title Título del elemento.
    * @param href URL del enlace.
    * @param icon Un identificador de icono para decorar el elemento.
    */
   public ButtonBarItem(String title, String href, String icon)
   {
      this.title = title;
      this.href = href;
      this.icon = icon;
   }

   //==============================================
   // Properties
   //==============================================
   
   /**
    * Devuelve el título del botón.
    */
   public String getTitle() 
   {
      return title;
   }

   /**
    * Establece el título del botón.
    */
   public void setTitle(String title) 
   {
      this.title = title;
   }

   /**
    * Devuelve la URL correspondiente al botón.
    */
   public String getHref() 
   {
      return href;
   }

   /**
    * Establece la URL correspondiente al botón.
    */
   public void setHref(String href) 
   {
      this.href = href;
   }

   /**
    * Devuelve el identificador del icono usado para decorar el botón.
    */
   public String getIcon() 
   {
      return icon;
   }

   /**
    * Establece el identificador del icono usado para decorar el botón.
    */
   public void setIcon(String icon) 
   {
      this.icon = icon;
   }
   
   //==============================================
   // Methods
   //==============================================
   
   /**
    * Convierte la instancia en una cadena en formato XHTML a partir de la estructura de la plantilla.
    */
   public String render(TemplateControl tc)
   {
      String xhtml;
      
      xhtml = tc.getElement(CPART_ITEM);
      xhtml = Control.replaceTag(xhtml, TAG_ICON, this.icon);
      xhtml = Control.replaceTag(xhtml, TAG_TITLE, this.title);
      xhtml = Control.replaceTag(xhtml, TAG_HREF, this.href);

      return xhtml;
   }
}
