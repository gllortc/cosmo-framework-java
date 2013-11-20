package com.cosmo.data.lists;

import java.sql.ResultSet;
import java.util.ArrayList;

import com.cosmo.Workspace;
import com.cosmo.data.DataConnection;

/**
 * Implementa una lista dinámica cuidos elementos se obtienen de una consulta SQL
 * y opcionalmente se cachean.
 * 
 * @author Gerard Llort
 */
public class DynamicList implements List
{
   /**
    * Enumera el tipo de cache que se aplicará a los elementos de la lista.
    */
   public enum CacheMode
   {
      /** Sin caché */
      None,
      /** Caché de usuario. */
      User,
      /** Caché de aplicación. */
      Application
   }

   private boolean isLoaded;
   private String id;
   private String connection;
   private String sql;
   private String valueFieldName;
   private String titleFieldName;
   private CacheMode cacheMode;
   private ArrayList<ListItem> items;


   //==============================================
   // Constructors
   //==============================================

   /**
    * Constructor de la clase.
    *
    * @param id Una cadena que contiene el identificador único de la lista.
    */
   public DynamicList(String id)
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

   /**
    * Devuelve el identificador de la conexión a datos dónde se obtienen los elementos de la lista.
    */
   public String getConnection() 
   {
      return connection;
   }

   /**
    * Establece el identificador de la conexión a datos dónde se obtienen los elementos de la lista.
    */
   public void setConnection(String connection) 
   {
      this.connection = connection;
   }

   /**
    * Devuelve la sentencia SQL usada para obtener los elementos de la lista.
    */
   public String getSqlStatement() 
   {
      return sql;
   }

   /**
    * Establece la sentencia SQL usada para obtener los elementos de la lista.
    */
   public void setSqlStatement(String sqlStatement) 
   {
      this.sql = sqlStatement;
   }

   /**
    * Devuelve el nombre del campo de la consulta SQL que contiene el valor de los elementos.
    */
   public String getValueFieldName() 
   {
      return valueFieldName;
   }

   /**
    * Establece el nombre del campo de la consulta SQL que contiene el valor de los elementos.
    */
   public void setValueFieldName(String valueFieldName) 
   {
      this.valueFieldName = valueFieldName;
   }

   /**
    * Devuelve el nombre del campo de la consulta SQL que contiene el título de los elementos.
    */
   public String getTitleFieldName() 
   {
      return titleFieldName;
   }

   /**
    * Establece el nombre del campo de la consulta SQL que contiene el título de los elementos.
    */
   public void setTitleFieldName(String titleFieldName) 
   {
      this.titleFieldName = titleFieldName;
   }

   /**
    * Devuelve el tipo de cache a aplicar.
    */
   public CacheMode getCacheMode()
   {
      return cacheMode;
   }

   /**
    * Establece el tipo de cache a aplicar.
    */
   public void setCacheMode(CacheMode cacheMode) 
   {
      this.cacheMode = cacheMode;
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

      this.isLoaded = false;
   }

   /**
    * Devuelve la lista de elementos.
    * 
    * @return Una lista de instancias de {@link ListItem} que representan los elementos de la lista.
    */
   @Override
   public ArrayList<ListItem> getListItems(Workspace workspace) throws Exception 
   {
      ListItem item;
      ResultSet rs;
      DataConnection connection = null;

      // Si existe caché y ya está cargada la lista la devuelve.
      if (this.cacheMode != CacheMode.None && this.isLoaded)
      {
         return items;
      }

      try
      {
         items.clear();

         connection = new DataConnection(workspace.getProperties().getDataProperties().getDataSource(this.connection));
         connection.connect();
         rs = connection.executeSql(this.sql);
         while (rs.next())
         {
            item = new ListItem(rs.getString(this.valueFieldName), 
                                rs.getString(this.titleFieldName));
            items.add(item);
         }
         
         this.isLoaded = true;
      }
      catch (Exception ex)
      {
         throw ex;
      }
      finally
      {
         if (connection != null)
         {
            connection.disconnect();
         }
      }

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
      this.isLoaded = false;
      this.id = "";
      this.connection = "";
      this.valueFieldName = "";
      this.titleFieldName = "";
      this.cacheMode = CacheMode.None;
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
