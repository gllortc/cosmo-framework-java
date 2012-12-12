package com.cosmo.ui.widgets;

import com.cosmo.Workspace;
import com.cosmo.ui.templates.Template;
import com.cosmo.ui.templates.TemplateControl;
import com.cosmo.ui.templates.TemplateUnavailableException;
import com.cosmo.ui.widgets.providers.MenuProvider;
import com.cosmo.ui.widgets.providers.MenuProviderException;
import com.cosmo.util.DataTypeException;
import java.util.ArrayList;

/**
 * Representa un menú de aplicación Cosmo.
 * 
 * @author Gerard Llort
 */
public class MenuWidget extends Widget
{
   private static final String WIDGET_ID = "CosmoUiWdgtMenu";
   
   private static final String WPART_HEADER = "menu-header";
   private static final String WPART_ITEM = "menu-item";
   private static final String WPART_FOOTER = "menu-footer";
   
   private static final String TAG_MENU_NAME = "MNAME";
   private static final String TAG_ITEM_HREF = "IHREF";
   private static final String TAG_ITEM_CAPTION = "ICAPTION";
   
   private MenuTypes type;
   private MenuProvider provider;
   
   /**
    * Enumera los distintos tipos de menú soportados por Cosmo
    */
   public enum MenuTypes
   {
      Top,
      Lateral,
      Footer
   }
   
   //==============================================
   // Constructor
   //==============================================
   
   /**
    * Constructor de la clase.
    */
   public MenuWidget(Workspace workspace, MenuTypes type) throws MenuProviderException
   {
      super(workspace);
      
      this.type = type;
      this.provider = MenuProvider.getInstance(workspace);
   }
   
   //==============================================
   // properties
   //==============================================

   @Override
   public String getWidgetTypeId() 
   {
      return MenuWidget.WIDGET_ID;
   }

   //==============================================
   // Methods
   //==============================================
   
   /**
    * TODO: Limpia la marca de elemento activo en todos los elementos del menú.
    */
   public void clearCurrentItem()
   {
      
   }
   
   /**
    * Renderiza el widget y genera el código XHTML de representación.
    *
    * @return Devuelve una cadena en formato XHTML que representa el widget. 
    */
   @Override
   public String render() throws TemplateUnavailableException
   {
      String xhtml;
      String xitem;
      ArrayList<MenuItem> menu;
      TemplateControl ctrl;
      Template template;
      
      try
      {
         // Carga el menú a renderizar
         menu = this.provider.loadMenu(this.getWorkspace(), type);
         
         // Obtiene la plantilla y la parte del widget
         template = this.getWorkspace().getTemplate();
         ctrl = template.getControl(WIDGET_ID);

         // Renderiza el control
         xhtml = "";
         xhtml += ctrl.getElement(WPART_HEADER);
         for (MenuItem item : menu)
         {
            xitem = ctrl.getElement(WPART_ITEM);
            xitem = xitem.replace(Widget.getTag(TAG_ITEM_HREF), item.getHref());
            xitem = xitem.replace(Widget.getTag(TAG_ITEM_CAPTION), item.getTitle());
            xhtml += xitem;
         }
         xhtml += ctrl.getElement(WPART_FOOTER);

         // Reemplaza los TAGs comunes a todo el widget
         xhtml = xhtml.replace(Widget.getTag(TAG_MENU_NAME), "Opciones");

         return xhtml;
      }
      catch (MenuProviderException ex)
      {
         return "<!-- Widget failed -->";
      }
   }
   
   //==============================================
   // Static members
   //==============================================
   
   /**
    * Convierte una cadena de texto a un elemento de MenuTypes.
    * 
    * @param strType Una cadena que contiene un texto que corresponde a un elemento de MenuTypes.
    * @return Un elemento de MenuTypes.
    * 
    * @throws DataTypeException 
    */
   public static MenuTypes convertToMenuType(String strType) throws DataTypeException
   {
      strType = strType.trim().toLowerCase();
      
      if (strType.equals(MenuTypes.Lateral.toString().toLowerCase()))
      {
         return MenuTypes.Lateral;
      }
      else if (strType.equals(MenuTypes.Top.toString().toLowerCase()))
      {
         return MenuTypes.Top;
      }
      else if (strType.equals(MenuTypes.Footer.toString().toLowerCase()))
      {
         return MenuTypes.Footer;
      }
      
      throw new DataTypeException(strType + " can't be converted to a MenuTypes enum element");
   }
}
