package com.cosmo.ui.widgets;

import com.cosmo.Workspace;
import com.cosmo.ui.templates.Template;
import com.cosmo.ui.templates.TemplateControl;
import com.cosmo.ui.templates.TemplateUnavailableException;
import java.util.ArrayList;

/**
 * Representa una área contenedora de banner.
 * 
 * @author Gerard Llort
 */
public class BannerAreaWidget extends Widget
{
   /** Identificador único del tipo de widget */
   private static final String WIDGET_ID = "CosmoUiWdgtBannerArea";

   private static final String WPART_HEADER = "banners-header";
   private static final String WPART_BANNER = "banner-item";
   private static final String WPART_FOOTER = "banners-footer";
   
   private static final String TAG_BANNER_ID = "BANNER-ID";
   private static final String TAG_BANNER_OBJ = "BANNER-OBJECT";

   /**
    * Defina las distintas areas de banners en un layout
    */
   public enum BannerAreas
   {
      Left,
      Right,
      CenterTop,
      CenterBottom
   }

   // Declaración de variables internas
   private BannerAreas area;


   //==============================================
   // Constructor
   //==============================================

   /**
    * Constructor de la clase {@link BannerAreaWidget}.
    * 
    * @param workspace Una instancia de {@link Workspace} que representa el workspace actual.
    * @param area Zona dónde debe ir situado el banner.
    */
   public BannerAreaWidget(Workspace workspace, BannerAreas area)
   {
      super(workspace);

      this.area = area;
   }


   //==============================================
   // properties
   //==============================================

   /**
    * Devuelve un identificador único del tipo de widget.
    */
   @Override
   public String getWidgetTypeId() 
   {
      return BannerAreaWidget.WIDGET_ID;
   }

   /**
    * Devuelve la zona de la página dónde va situado el widget.
    */
   public BannerAreas getArea() 
   {
      return area;
   }

   /**
    * Establece la zona de la página dónde va situado el widget.
    */
   public void setArea(BannerAreas area) 
   {
      this.area = area;
   }   


   //==============================================
   // Methods
   //==============================================

   /**
    * Renderiza el widget y genera el código XHTML de representación.
    *
    * @return Devuelve una cadena en formato XHTML que representa el widget. 
    */
   @Override
   public String render() throws TemplateUnavailableException 
   {
      String xhtml = "";
      String xitem;
      TemplateControl ctrl;
      Template template;
      ArrayList<BannerAreaItem> items = new ArrayList<BannerAreaItem>();

      if (!items.isEmpty())
      {
         // Obtiene la plantilla y la parte del widget
         template = this.getWorkspace().getTemplate();
         ctrl = template.getControl(WIDGET_ID);

         // Renderiza el control
         xhtml = "";
         xhtml += ctrl.getElement(WPART_HEADER);
         for (BannerAreaItem item : items)
         {
            xitem = ctrl.getElement(WPART_BANNER);
            xitem = xitem.replace(Widget.getTag(TAG_BANNER_ID), item.getId());
            xitem = xitem.replace(Widget.getTag(TAG_BANNER_OBJ), item.render());
            xhtml += xitem;
         }
         xhtml += ctrl.getElement(WPART_FOOTER);
      }

      return xhtml;
   }
}
