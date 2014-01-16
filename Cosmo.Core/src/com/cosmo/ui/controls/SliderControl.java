package com.cosmo.ui.controls;

import java.util.ArrayList;

import com.cosmo.Workspace;
import com.cosmo.ui.templates.TemplateControl;
import com.cosmo.util.StringUtils;

/**
 * Implementa un slider (pase de diapositivas) para cualquier contenido XHTML.
 * 
 * @author Gerard Llort
 */
public class SliderControl extends Control
{
   private static final String CONTROL_ID = "CosmoUiCtrlSlider";

   private static final String CPART_FIRST = "slider-element-first";
   private static final String CPART_HEADER = "slider-header";
   private static final String CPART_ITEM_HEADER = "slider-item-header";
   private static final String CPART_ITEM = "slider-item";
   private static final String CPART_ITEM_FOOTER = "slider-item-footer";
   private static final String CPART_CONTENT_HEADER = "slider-content-header";
   private static final String CPART_CONTENT = "slider-content";
   private static final String CPART_CONTENT_FOOTER = "slider-content-footer";
   private static final String CPART_FOOTER = "slider-footer";

   private static final String TAG_SLIDER_CONTENTS = "CONTENTS";
   private static final String TAG_SLIDER_HEIGHT = "HEIGHT";
   private static final String TAG_SLIDER_WIDTH = "WIDTH";
   private static final String TAG_SLIDER_COUNT = "COUNT";
   private static final String TAG_SLIDER_ID = "ID";
   private static final String TAG_SLIDER_TITLE = "TITLE";
   private static final String TAG_SLIDER_DESCRIPTION = "DESCRIPTION";
   private static final String TAG_SLIDER_FIRST = "FIRST";

   // Declaración de variables internas
   private int width;
   private int height;
   private ArrayList<SliderItem> items;


   //==============================================
   // Contructors
   //==============================================

   /**
    * Contructor de la clase {@link SliderControl}.
    * 
    * @param workspace Una instancia de {@link Workspace} que representa el workspace actual.
    */
   public SliderControl(Workspace workspace)
   {
      super(workspace);
      initialize();
   }

   /**
    * Contructor de la clase {@link SliderControl}.
    * 
    * @param workspace Una instancia de {@link Workspace} que representa el workspace actual.
    * @param id Una cadena que contiene el identificador único del control.
    */
   public SliderControl(Workspace workspace, String id)
   {
      super(workspace, id);
      initialize();
   }


   //==============================================
   // Properties
   //==============================================

   public int getWidth() 
   {
      return width;
   }

   public void setWidth(int width) 
   {
      this.width = width;
   }

   public int getHeight() 
   {
      return height;
   }

   public void setHeight(int height) 
   {
      this.height = height;
   }

   /**
    * Devuelve un identificador único del tipo de control.
    */
   @Override
   public String getControlTypeId() 
   {
      return SliderControl.CONTROL_ID;
   }


   //==============================================
   // Methods
   //==============================================

   /**
    * Elimina todos los elementos de la barra de navegación.
    */
   public void clear()
   {
      this.items.clear();
   }

   /**
    * Agrega un nuevo elemento en la barra de navegación.
    * 
    * @param item Una instancia de {@link SliderItem} que representa el elemento a agregar.
    */
   public void addItem(SliderItem item)
   {
      this.items.add(item);
   }

   /**
    * Renderiza el control y genera el código XHTML de representación.
    *
    * @return Devuelve una cadena en formato XHTML que representa el control. 
    */
   @Override
   public String render()
   {
      int count = 0;
      String xitem;
      String first = StringUtils.EMPTY;
      TemplateControl ctrl;
      StringBuilder str = new StringBuilder();

      // Si no tiene elementos, no representa el control
      if (items.isEmpty())
      {
         return "<-- SliderControl placeholder (void) -->\n";
      }

      // Obtiene la plantilla y la parte del control
      ctrl = getWorkspace().getTemplate().getControl(SliderControl.CONTROL_ID);

      // Obtiene el indicador de primer elemento (permite diferenciar la clase cuando es el primer elemento)
      first = ctrl.getElement(CPART_FIRST);

      // Genera la cabecera de la barra de navegación
      xitem = ctrl.getElement(CPART_HEADER);
      xitem = Control.replaceTag(xitem, TAG_SLIDER_ID, this.getId());
      xitem = Control.replaceTag(xitem, TAG_SLIDER_WIDTH, StringUtils.EMPTY + this.width);
      xitem = Control.replaceTag(xitem, TAG_SLIDER_HEIGHT, StringUtils.EMPTY + this.height);
      str.append(xitem);

      xitem = ctrl.getElement(CPART_ITEM_HEADER);
      xitem = Control.replaceTag(xitem, TAG_SLIDER_ID, this.getId());
      str.append(xitem);

      for (SliderItem item : this.items)
      {
         count++;

         xitem = ctrl.getElement(CPART_ITEM);
         xitem = Control.replaceTag(xitem, TAG_SLIDER_ID, this.getId());
         xitem = Control.replaceTag(xitem, TAG_SLIDER_CONTENTS, item.render());
         xitem = Control.replaceTag(xitem, TAG_SLIDER_COUNT, StringUtils.EMPTY + count);
         xitem = Control.replaceTag(xitem, TAG_SLIDER_FIRST, first);
         str.append(xitem);

         first = StringUtils.EMPTY;
      }

      xitem = ctrl.getElement(CPART_ITEM_FOOTER);
      xitem = Control.replaceTag(xitem, TAG_SLIDER_ID, this.getId());
      xitem = Control.replaceTag(xitem, TAG_SLIDER_COUNT, StringUtils.EMPTY + count);  // Número total de diapositivas
      str.append(xitem);

      // Obtiene el indicador de primer elemento (permite diferenciar la clase cuando es el primer elemento)
      first = ctrl.getElement(CPART_FIRST);

      xitem = ctrl.getElement(CPART_CONTENT_HEADER);
      xitem = Control.replaceTag(xitem, TAG_SLIDER_ID, this.getId());
      str.append(xitem);

      for (SliderItem item : this.items)
      {
         count++;

         xitem = ctrl.getElement(CPART_CONTENT);
         xitem = Control.replaceTag(xitem, TAG_SLIDER_ID, this.getId());
         xitem = Control.replaceTag(xitem, TAG_SLIDER_CONTENTS, item.render());
         xitem = Control.replaceTag(xitem, TAG_SLIDER_COUNT, StringUtils.EMPTY + count);
         xitem = Control.replaceTag(xitem, TAG_SLIDER_TITLE, item.getTitle());
         xitem = Control.replaceTag(xitem, TAG_SLIDER_DESCRIPTION, item.getDescription());
         xitem = Control.replaceTag(xitem, TAG_SLIDER_FIRST, first);
         str.append(xitem);

         first = StringUtils.EMPTY;
      }

      xitem = ctrl.getElement(CPART_CONTENT_FOOTER);
      xitem = Control.replaceTag(xitem, TAG_SLIDER_ID, this.getId());
      xitem = Control.replaceTag(xitem, TAG_SLIDER_COUNT, StringUtils.EMPTY + count);  // Número total de diapositivas
      str.append(xitem);

      // Genera el piÃ© de la barra de navegación
      xitem = ctrl.getElement(CPART_FOOTER);
      xitem = Control.replaceTag(xitem, TAG_SLIDER_ID, this.getId());
      str.append(xitem);

      return str.toString();
   }


   //==============================================
   // Private members
   //==============================================

   /**
    * Inicializa la instancia.
    */
   private void initialize()
   {
      this.width = 0;
      this.height = 0;
      this.items = new ArrayList<SliderItem>();
   }
}
