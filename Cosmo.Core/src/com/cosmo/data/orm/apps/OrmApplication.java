package com.cosmo.data.orm.apps;

import com.cosmo.Workspace;
import com.cosmo.net.URL;

/**
 * Representa una aplicación ORM.
 * 
 * @author Gerard Llort
 */
public class OrmApplication 
{
   private String id;
   private String className;
   private String connectionId;
   private String title;
   private String description;
   private boolean canCreate;
   private boolean canDelete;
   private boolean canEdit;
   
   //==============================================
   // Constructors
   //==============================================
   
   /**
    * Constructor de la clase.
    */
   public OrmApplication()
   { 
      this.canCreate = false;
      this.canDelete = false;
      this.canEdit = false;
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

   public String getClassName() 
   {
      return className;
   }

   public void setClassName(String className) 
   {
      this.className = className;
   }

   public String getConnectionId() 
   {
      return connectionId;
   }

   public void setConnectionId(String connectionId) 
   {
      this.connectionId = connectionId;
   }

   public String getTitle() 
   {
      return title;
   }

   public void setTitle(String title) 
   {
      this.title = title;
   }

   public String getDescription() 
   {
      return description;
   }

   public void setDescription(String description) 
   {
      this.description = description;
   }

   public boolean isCreateEnabled() 
   {
      return canCreate;
   }

   public void setCreateEnabled(boolean enabled) 
   {
      this.canCreate = enabled;
   }

   public boolean isDeleteEnabled() 
   {
      return canDelete;
   }

   public void setDeleteEnabled(boolean enabled) 
   {
      this.canDelete = enabled;
   }

   public boolean isEditEnabled() 
   {
      return canEdit;
   }

   public void setEditEnabled(boolean enabled) 
   {
      this.canEdit = enabled;
   }

   //==============================================
   // Methods
   //==============================================

   /**
    * Obtiene la URL inicial de la aplicación.
    * 
    * @param workspace Una instancia de {@link Workspace}.
    * 
    * @return Una cadena que representa la URL de inicio de la aplicación.
    */
   public String getApplicationUrl(Workspace workspace) 
   {
      URL url = new URL(workspace.getUrl() + OrmDataService.URL_BASE);
      url.addParameter("appid", this.id);
      
      return url.toString();
   }
}
