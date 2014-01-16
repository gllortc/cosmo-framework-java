package com.cosmo.ui.controls;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import com.cosmo.Workspace;
import com.cosmo.data.lists.List;
import com.cosmo.data.lists.ListItem;
import com.cosmo.data.lists.StaticList;
import com.cosmo.net.HttpRequestUtils;
import com.cosmo.util.KeyValue;
import com.cosmo.util.StringUtils;

/**
 * Implementa una lista de opciones representable dentro de un formulario Cosmo.
 * 
 * @author Gerard Llort
 */
public class FormFieldList extends FormField
{
   // Declaración de variables internas
   private String name;
   private String value;
   private String label;
   private String description;
   private ListType listType;
   private List list;


   //==============================================
   // Enumerations
   //==============================================

   /**
    * Enumera los tipos de lista admitidos por el control <em>FormFieldList</em>.
    */
   public enum ListType
   {
      /** Lista desplegable. */
      ComboBox,
      /** Lista. */
      ListBox,
      /** Conjunto de controles <em>radio buttons</em>. */
      RadioButtons
   }


   //==============================================
   // Contructors
   //==============================================

   /**
    * Contructor de la clase {@link FormFieldList}.
    * 
    * @param name Nombre identificativo del elemento dentro de la página.
    * @param label Etiqueta que se mostrará junto el control.
    */
   public FormFieldList(String name, String label) 
   {
      this.name = name;
      this.label = label;
      this.description = StringUtils.EMPTY;
      this.value = StringUtils.EMPTY;
      this.listType = ListType.ComboBox;
      this.list = new StaticList();
   }

   /**
    * Contructor de la clase {@link FormFieldList}.
    * 
    * @param name Nombre identificativo del elemento dentro de la página.
    * @param label Etiqueta que se mostrará junto el control.
    * @param request Una instancia de {@link HttpServletRequest} que permite establecer el valor del campo en una 
    *    recarga del formulario.
    */
   public FormFieldList(String name, String label, HttpServletRequest request) 
   {
      this.name = name;
      this.label = label;
      this.description = StringUtils.EMPTY;
      this.value = HttpRequestUtils.getValue(request, name);
      this.listType = ListType.ComboBox;
      this.list = new StaticList();
   }

   /**
    * Contructor de la clase {@link FormFieldList}.
    * 
    * @param name Nombre identificativo del elemento dentro de la página.
    * @param label Etiqueta que se mostrará junto el control.
    * @param list Una instancia de una clase que implemente {@link List} y que contenga los datos de la lista.
    */
   public FormFieldList(String name, String label, List list) 
   {
      this.name = name;
      this.label = label;
      this.description = StringUtils.EMPTY;
      this.value = StringUtils.EMPTY;
      this.listType = ListType.ComboBox;
      this.list = list;
   }

   /**
    * Contructor de la clase {@link FormFieldList}.
    * 
    * @param name Nombre identificativo del elemento dentro de la página.
    * @param label Etiqueta que se mostrará junto el control.
    * @param list Una instancia de una clase que implemente {@link List} y que contenga los datos de la lista.
    * @param request Una instancia de {@link HttpServletRequest} que permite establecer el valor del campo en una 
    *    recarga del formulario.
    */
   public FormFieldList(String name, String label, List list, HttpServletRequest request) 
   {
      this.name = name;
      this.label = label;
      this.description = StringUtils.EMPTY;
      this.value = HttpRequestUtils.getValue(request, name);
      this.listType = ListType.ComboBox;
      this.list = list;
   }


   //==============================================
   // Properties
   //==============================================

   /**
    * Devuelve el nombre del control.
    */
   @Override
   public String getName() 
   {
      return name;
   }

   /**
    * Establece el nombre del control.
    */
   public void setName(String name) 
   {
      this.name = name;
   }

   /**
    * Devuelve el valor del elemento seleccionado.
    */
   public String getValue() 
   {
      return value;
   }

   /**
    * Establece el valor del elemento que debe estar seleccionado.
    */
   @Override
   public void setValue(Object value) 
   {
      this.value = value.toString();
   }

   /**
    * Devuelve el texto que se mostrará como etiqueta del control.
    */
   public String getLabel() 
   {
      return label;
   }

   /**
    * Establece el texto que se mostrará como etiqueta del control.
    */
   public void setLabel(String label) 
   {
      this.label = label;
   }

   /**
    * Devuelve la descripción del campo.
    */
   public String getDescription() 
   {
      return description;
   }

   /**
    * Establece la descripción del campo.
    */
   public void setDescription(String description) 
   {
      this.description = description;
   }

   /**
    * Devuelve el tipo de representación de la lista.
    */
   public ListType getListType() 
   {
      return listType;
   }

   /**
    * Establece el tipo de representación de la lista.
    */
   public void setListType(ListType listType) 
   {
      this.listType = listType;
   }

   /**
    * Establece la lista de opciones.
    * 
    * @param list Una instancia de {@link List} que contiene todos los elementos que se mostrarán en la lista.
    */
   public void setList(List list) 
   {
      this.list = list;
   }


   //==============================================
   // Methods
   //==============================================

   /**
    * Agrega un valor a la lista de valores posibles que puede adoptar el campo.
    * 
    * @param keyvalue Una instancia de {@link KeyValue} que contiene el par clave/valor a agregar.
    */
   public void addListOption(KeyValue keyvalue)
   {
      this.list.addListItem(new ListItem(keyvalue.getKey(), 
                                         keyvalue.getValue().toString(), 
                                         keyvalue.isDefaultOption()));
   }

   /**
    * Elimina todos los elementos de la lista de valores posibles que puede adoptar el campo.
    */
   public void clearList()
   {
      this.list.clear();
   }

   /**
    * Convierte el campo en un TAG XHTML.
    */
   @Override
   public String render(Workspace workspace)
   {
      boolean selected = false;
      StringBuilder sb = new StringBuilder();

      try 
      {
         // Obtiene los elementos del menú
         ArrayList<ListItem> items = list.getListItems(workspace);

         switch (this.listType)
         {
            case ListBox:
               sb.append("<select name=\"" + this.getName() + "\" id=\"" + this.getName() + "\" multiple=\"multiple\">\n");
               for (ListItem item : items)
               {
                  if (StringUtils.isNullOrEmptyTrim(this.getValue()))
                  {
                     selected = item.isDefault();
                  }
                  else
                  {
                     selected = (item.getValue().equals(this.getValue()));
                  }

                  sb.append("  <option value=\"" + item.getValue() + "\"" + (selected ? " selected=\"selected\"" : StringUtils.EMPTY) + ">" + item.getCaption() + "</option>\n");
               }
               sb.append("</select>\n");
               break;

            case RadioButtons:
               for (ListItem item : items)
               {
                  sb.append("<label class=\"radio\">\n");
                  sb.append("   <input type=\"radio\" name=\"" + this.getName() + "\" id=\"" + this.getName() + "\" value=\"" + item.getValue() + "\">\n");
                  sb.append("   " + item.getCaption() + "\n");
                  sb.append("</label>\n");
               }
               break;

            default:
               sb.append("<select name=\"" + this.getName() + "\" id=\"" + this.getName() + "\">\n");
               for (ListItem item : items)
               {
                  if (StringUtils.isNullOrEmptyTrim(this.getValue()))
                  {
                     selected = item.isDefault();
                  }
                  else
                  {
                     selected = (item.getValue().equals(this.getValue()));
                  }

                  sb.append("   <option value=\"" + item.getValue() + "\"" + (selected ? " selected=\"selected\"" : StringUtils.EMPTY) + ">" + item.getCaption() + "</option>\n");
               }
               sb.append("</select>\n");
               break;
         }
      }
      catch (Exception e) 
      {
         e.printStackTrace();
      }

      return sb.toString();
   }

   /**
    * Convierte la instancia en una cadena de texto.
    */
   @Override
   public String toString()
   {
      StringBuilder sb = new StringBuilder();

      sb.append("    <div>").append("\n");
      sb.append("      <label for=\"").append(this.name).append("\">"); 
      sb.append(this.label).append(" ");
      // sb.append("<input type=\"").append(password ? "password" : "text").append("\" id=\"").append(this.name).append("\" name=\"").append(this.name).append("\" value=\"").append(this.value).append("\" />");
      sb.append("</label>").append("\n");
      sb.append("    </div>");

      return sb.toString();
   }
}
