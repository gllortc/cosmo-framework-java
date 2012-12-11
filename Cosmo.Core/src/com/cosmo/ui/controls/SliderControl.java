package com.cosmo.ui.controls;

import com.cosmo.ui.templates.Template;
import com.cosmo.ui.templates.TemplateControl;
import java.util.ArrayList;

import javax.servlet.http.HttpSession;

/**
 * Implementa un slider (pase de diapositivas) para cualquier contenido XHTML.
 * 
 * @author Gerard Llort
 */
public class SliderControl extends Control
{
   private static final String CONTROL_ID = "CosmoUiCtrlSlider";
   
   private static final String CPART_HEADER = "slider-header";
   private static final String CPART_FOOTER = "slider-footer";
   private static final String CPART_ITEM = "slider-item";
         
   private static final String TAG_SLIDER_CONTENTS = "CONTENTS";
   private static final String TAG_SLIDER_HEIGHT = "HEIGHT";
   private static final String TAG_SLIDER_WIDTH = "WIDTH";
   
   // Variables privadas
   private int width;
   private int height;
   private ArrayList<SliderItem> items;
   
   //==============================================
   // Contructors
   //==============================================
   
   /**
    * Contructor de la clase.
    */
   public SliderControl()
   {
      super();
      initialize();
   }
   
   /**
    * Contructor de la clase.
    */
   public SliderControl(String id)
   {
      super(id);
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
    * @param session Una instancia de {@link HttpSession}.
    * @param template Una instancia de {@link Template} que representa la plantilla actual.
    * @return Devuelve una cadena en formato XHTML que representa el control. 
    */
   @Override
   public String render(HttpSession session, Template template)
   {
      String xitem;
      TemplateControl ctrl;
      StringBuilder str = new StringBuilder();
      
      // Si no tiene elementos, no representa el control
      if (items.isEmpty())
      {
         return "<-- SliderControl placeholder (void) -->\n";
      }
      
      // Obtiene la plantilla y la parte del control
      ctrl = template.getControl(SliderControl.CONTROL_ID);
      
      // Genera la cabecera de la barra de navegación
      xitem = ctrl.getElement(CPART_HEADER);
      xitem = Control.replaceTag(xitem, TAG_SLIDER_WIDTH, "" + this.width);
      xitem = Control.replaceTag(xitem, TAG_SLIDER_HEIGHT, "" + this.height);
      str.append(xitem);
      
      for (SliderItem item : this.items)
      {
         xitem = ctrl.getElement(CPART_ITEM);
         xitem = Control.replaceTag(xitem, TAG_SLIDER_CONTENTS, item.render());
         str.append(xitem);
      }
      
      // Genera el piÃ© de la barra de navegación
      str.append(ctrl.getElement(CPART_FOOTER));
      
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
