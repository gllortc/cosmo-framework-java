package com.cosmo.data.lists;

import java.util.ArrayList;

import com.cosmo.Workspace;

/**
 * Interface que han d'implementar les llistes.
 * 
 * @author Gerard Llort
 */
public interface List 
{
   /**
    * Agrega un nuevo elemento a la lista.
    * 
    * @param item Una instancia de {@link ListItem} que representa el nuevo elemento.
    */
   public void addListItem(ListItem item);
   
   /**
    * Elimina un elemento de la lista.
    * 
    * @param value Un entero que indica el índice del elemento a eliminar (con base 0).
    */
   public void removeListItem(int index);
   
   /**
    * Elimina un elemento de la lista.
    * 
    * @param value Una cadena que contiene el valor correspondiente al elemento a eliminar.
    */
   public void removeListItem(String value);
   
   /**
    * Elimina todos los elementos de la lista.
    */
   public void clear();
   
   /**
    * Devuelve la lista de elementos.
    * 
    * @param workspace Una instancia de {@link Workspace} que representa el workspace actual.
    * 
    * @return Una lista de instancias de {@link ListItem} que representan los elementos de la lista.
    */
   public ArrayList<ListItem> getListItems(Workspace workspace) throws Exception;
}
