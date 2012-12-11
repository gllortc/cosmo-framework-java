package com.cosmo.ui.controls;

import com.cosmo.ui.templates.Template;
import com.cosmo.ui.templates.TemplateControl;
import java.util.ArrayList;

import javax.servlet.http.HttpSession;

/**
 * Implementa una barra de navegación.
 * 
 * @author Gerard Llort
 */
public class BreadcrumbsControl extends Control
{
   private static final String CONTROL_ID = "CosmoUiCtrlBreadcrumbs";

   private static final String CPART_HEADER = "navbar-top-header";
   private static final String CPART_FOOTER = "navbar-top-footer";
   // private static final String CPART_ITEM = "navbar-top-item";
         
   // private static final String TAG_TITLE = "FTITLE";
   // private static final String TAG_HREF = "HREF";
   
   // Variables privadas
   private ArrayList<BreadcrumbsItem> items;
   
   //==============================================
   // Contructors
   //==============================================
   
   /**
    * Contructor de la clase.
    */
   public BreadcrumbsControl()
   {
      super();
      initialize();
   }
   
   /**
    * Contructor de la clase.
    */
   public BreadcrumbsControl(String id)
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
      return BreadcrumbsControl.CONTROL_ID;
   }

   //==============================================
   // Methods
   //==============================================
   
   /**
    * Elimina todos los elementos de la barra de bavegación.
    */
   public void clear()
   {
      this.items.clear();
   }
   
   /**
    * Agrega un nuevo elemento en la barra de navegación.
    * 
    * @param item Una instancia de {@link BreadcrumbsItem} que representa el elemento a agregar.
    */
   public void addItem(BreadcrumbsItem item)
   {
      this.items.add(item);
   }
   
   /**
    * Renderiza el control y genera el código XHTML de representación.
    *
    * @return Devuelve una cadena en formato XHTML que representa el control. 
    */
   @Override
   public String render(HttpSession session, Template template) 
   {
      TemplateControl ctrl;
      StringBuilder str = new StringBuilder();
      
      // Si no tiene elementos, no representa el control
      if (items.isEmpty())
      {
         return "<-- BradcrumbsControl placeholder (void) -->\n";
      }
      
      // Obtiene la plantilla y la parte del control
      ctrl = template.getControl(BreadcrumbsControl.CONTROL_ID);
      
      // Genera la cabecera de la barra de navegación
      str.append(ctrl.getElement(CPART_HEADER));
      
      for (BreadcrumbsItem item : this.items)
      {
         str.append(item.render(ctrl));
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
      items = new ArrayList<BreadcrumbsItem>();
   }
}
