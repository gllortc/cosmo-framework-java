package com.cosmo.ui.controls;

import com.cosmo.ui.templates.TemplateControl;
import com.cosmo.util.StringUtils;

/**
 * Implementa un elemento de la barra de navegación (breadcrumbs).
 * 
 * @author Gerard Llort
 */
public class BreadcrumbsItem 
{
   private static final String CPART_ITEM = "navbar-top-item";

   private static final String TAG_TITLE = "FTITLE";
   private static final String TAG_HREF = "HREF";
   private static final String TAG_ICON = "ICON";

   // Declaración de variables internas
   private String title;
   private String href;
   private String icon;


   //==============================================
   // Constructors
   //==============================================

   /**
    * Constructor de la clase {@link BreadcrumbsItem}.
    */
   public BreadcrumbsItem()
   {
      this.title = StringUtils.EMPTY;
      this.href = StringUtils.EMPTY;
      this.icon = StringUtils.EMPTY;
   }

   /**
    * Constructor de la clase {@link BreadcrumbsItem}.
    * 
    * @param title Título del elemento.
    */
   public BreadcrumbsItem(String title)
   {
      this.title = title;
      this.href = StringUtils.EMPTY;
      this.icon = StringUtils.EMPTY;
   }

   /**
    * Constructor de la clase {@link BreadcrumbsItem}.
    * 
    * @param title Título del elemento.
    * @param href URL del enlace.
    */
   public BreadcrumbsItem(String title, String href)
   {
      this.title = title;
      this.href = href;
      this.icon = StringUtils.EMPTY;
   }

   /**
    * Constructor de la clase {@link BreadcrumbsItem}.
    * 
    * @param title Título del elemento.
    * @param href URL del enlace.
    */
   public BreadcrumbsItem(String title, String href, String icon)
   {
      this.title = title;
      this.href = href;
      this.icon = icon;
   }


   //==============================================
   // Properties
   //==============================================

   /**
    * Devuelve el título visible por el usuario.
    */
   public String getTitle() 
   {
      return title;
   }

   /**
    * Establece el título visible por el usuario.
    */
   public void setTitle(String title) 
   {
      this.title = title;
   }

   /**
    * Devuelve la dirección URL que contiene el elemento.
    */
   public String getHref() 
   {
      return href;
   }

   /**
    * establece la dirección URL que contiene el elemento.
    */
   public void setHref(String href) 
   {
      this.href = href;
   }

   /**
    * Devuelve el icono a mostrar junto al enlace.
    */
   public String getIcon() 
   {
      return icon;
   }

   /**
    * Establece el icono a mostrar junto al enlace.
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
      xhtml = Control.replaceTag(xhtml, TAG_ICON, Icon.render(this.icon, Icon.ICON_SIZE_DEFAULT));
      xhtml = Control.replaceTag(xhtml, TAG_TITLE, this.title);
      xhtml = Control.replaceTag(xhtml, TAG_HREF, this.href);

      return xhtml;
   }
}
