package com.cosmo.ui.widgets;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Implementa un elemento de menú.
 * 
 * @author Gerard Llort
 */
public class MenuItem 
{
   private String id;
   private boolean current;
   private String title;
   private String href;
   private String parentId;
   private ArrayList<MenuItem> subitems;
   
   //==============================================
   // Constructors
   //==============================================
   
   /**
    * Constructor de la clase.
    * 
    * @param id Identificador único del elemento.
    * @param title Título visible del elemento de menú.
    * @param href URL de acceso a la página.
    */
   public MenuItem(String id, String title, String href, String parentId)
   {
      this.id = id;
      this.current = false;
      this.title = title;
      this.href = href;
      this.parentId = parentId;
      this.subitems = new ArrayList<MenuItem>();
   }

   //==============================================
   // Properties
   //==============================================

   public String getId() 
   {
      return id;
   }

   public void setId(String id) 
   {
      this.id = id;
   }
   
   public boolean isCurrent() 
   {
      return current;
   }

   public void setCurrent(boolean current) 
   {
      this.current = current;
   }

   public String getTitle() 
   {
      return title;
   }

   public void setTitle(String title) 
   {
      this.title = title;
   }

   public String getHref() 
   {
      return href;
   }

   public void setHref(String href) 
   {
      this.href = href;
   }
   
   public String getParentId() 
   {
      return parentId;
   }

   public void setParentId(String parentId) 
   {
      this.parentId = parentId;
   }
   
   public Iterator<MenuItem> getSubItems()
   {
      return this.subitems.iterator();
   }
   
   public boolean hasSubItems()
   {
      return !this.subitems.isEmpty();
   }
   
   //==============================================
   // Methods
   //==============================================

   /**
    * Agrega un subelemento al elemento actual.
    * 
    * @param item Una instancia de {@link MenuItem} que representa el subelemento a agregar.
    */
   public void addSubItem(MenuItem item)
   {
      this.subitems.add(item);
   }
}
