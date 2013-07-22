package com.cosmo.data.lists;

import java.util.ArrayList;

/**
 * Implementa una lista estática de elementos.
 * 
 * @author Gerard Llort
 */
public class StaticList implements List
{
   private String id;
   private ArrayList<ListItem> items;

   
   //==============================================
   // Constructors
   //==============================================
   
   /**
    * Constructor de la clase.
    *
    * @param id Una cadena que contiene el identificador único de la lista.
    */
   public StaticList(String id)
   {
      initialize();
      
      this.id = id;
   }
   

   //==============================================
   // Properties
   //==============================================
  
   /**
    * Devuelve el identificador único de la lista.
    */
   public String getId() 
   {
      return id;
   }

   /**
    * Establece el identificador único de la lista.
    */
   public void setId(String id) 
   {
      this.id = id;
   }
   
   
   //==============================================
   // Methods
   //==============================================

   /**
    * Agrega un nuevo elemento a la lista.
    * 
    * @param item Una instancia de {@link ListItem} que representa el nuevo elemento.
    */
   @Override
   public void addListItem(ListItem item) 
   {
      if (item.isDefault())
      {
         clearDefaultValue();
      }
      
      items.add(item);
   }
   
   /**
    * Elimina un elemento de la lista.
    * 
    * @param value Una cadena que contiene el valor correspondiente al elemento a eliminar.
    */
   @Override
   public void removeListItem(String value) 
   {
      int idx = 0;
      
      for (ListItem item : items)
      {
         if (item.getValue().equals(value))
         {
            removeListItem(idx);
            return;
         }
         
         idx++;
      }
   }
   
   /**
    * Elimina un elemento de la lista.
    * 
    * @param value Un entero que indica el índice del elemento a eliminar (con base 0).
    */
   @Override
   public void removeListItem(int index) 
   {
      items.remove(index);
   }

   /**
    * Elimina todos los elementos de la lista.
    */
   @Override
   public void clear() 
   {
      items.clear();
   }

   /**
    * Devuelve la lista de elementos.
    * 
    * @return Una lista de instancias de {@link ListItem} que representan los elementos de la lista.
    */
   @Override
   public ArrayList<ListItem> getListItems() 
   {
      return items;
   }
   
   
   //==============================================
   // Private members
   //==============================================
   
   /**
    * Inicializa la instancia.
    */
   private void initialize()
   {
      this.id = "";
      this.items = new ArrayList<ListItem>();
   }
   
   /**
    * Elimina cualquier valor por defecto definido.
    */
   private void clearDefaultValue()
   {
      for (ListItem item : items)
      {
         item.setDefault(false);
      }
   }
   
}
