package com.cosmo.ui.controls;

import java.util.ArrayList;

import com.cosmo.Workspace;
import com.cosmo.ui.templates.TemplateControl;
import com.cosmo.util.StringUtils;

/**
 * Implementa una lista de objetos.
 * 
 * @author Gerard Llort
 */
public class ListViewControl extends Control
{
   /** Control Type Unique ID */
   private static final String CTUID = "CosmoUiCtrlObjectLst";

   private static final String CPART_TITLE = "object-list-title";
   private static final String CPART_HEADER = "object-list-header";
   private static final String CPART_FOOTER = "object-list-footer";

   private static final String TAG_TITLE = "TITLE";
   private static final String TAG_DESCRIPTION = "DESCRIPTION";
   private static final String TAG_ITEMS = "ITEMS";

   // Declaraci�n de variables internas
   private String caption;
   private String description;
   private ArrayList<ListViewItem> items;


   //==============================================
   // Contructors
   //==============================================

   /**
    * Contructor de la clase {@link ListViewControl}.
    * 
    * @param workspace Una instancia de {@link Workspace} que representa el espacio de aplicaci�n actual.
    */
   public ListViewControl(Workspace workspace)
   {
      super(workspace);
      initialize();
   }

   /**
    * Contructor de la clase {@link ListViewControl}.
    * 
    * @param workspace Una instancia de {@link Workspace} que representa el espacio de aplicaci�n actual.
    * @param id Identificador �nico del control en la p�gina.
    */
   public ListViewControl(Workspace workspace, String id)
   {
      super(workspace, id);
      initialize();
   }


   //==============================================
   // Properties
   //==============================================

   /**
    * Devuelve un identificador �nico del tipo de control.
    */
   @Override
   public String getControlTypeId() 
   {
      return ListViewControl.CTUID;
   }


   //==============================================
   // Methods
   //==============================================

   /**
    * Agrega 
    * 
    * @param item Una instancia de {@link ListViewItem} que representa el elemento de la lista.
    */
   public void addListItem(ListViewItem item)
   {
      items.add(item);
   }

   /**
    * Renderiza el control y genera el c�digo XHTML de representaci�n.
    *
    * @return Devuelve una cadena en formato XHTML que representa el control. 
    */
   @Override
   public String render() 
   {
      int nitems = 0;
      String xitem;
      TemplateControl ctrl = getWorkspace().getTemplate().getControl(ListViewControl.CTUID);
      StringBuilder sb = new StringBuilder();

      xitem = ctrl.getElement(CPART_TITLE);
      xitem = Control.replaceTag(xitem, TAG_TITLE, this.caption);
      xitem = Control.replaceTag(xitem, TAG_DESCRIPTION, this.description);
      sb.append(xitem);

      sb.append(ctrl.getElement(CPART_HEADER));
      
      for (ListViewItem item : this.items)
      {
         sb.append(item.render(ctrl));
         nitems++;
      }

      xitem = ctrl.getElement(CPART_FOOTER);
      xitem = Control.replaceTag(xitem, TAG_ITEMS, StringUtils.EMPTY + nitems);
      sb.append(xitem);

      return sb.toString();
   }


   //==============================================
   // Private members
   //==============================================

   /**
    * Inicializa la instancia.
    */
   private void initialize()
   {
      this.caption = StringUtils.EMPTY;
      this.description = StringUtils.EMPTY;
      this.items = new ArrayList<ListViewItem>();
   }   
}
