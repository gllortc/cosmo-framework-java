package com.cosmo.security;

/**
 * Implementa un rol de usuario (o grupo).
 * @author Gerard Llort
 */
public class Role 
{
   private int id;
   private String name;
   private String description;
   private String applicationId;
   
   /**
    * Constructor de la clase.
    */
   public Role() 
   {
      id = 0;
      name = "";
      description = "";
      applicationId = "";
   }
   
   /**
    * Devuelve el identificador del rol.
    */
   public int getId() 
   {
      return id;
   }

   /**
    * Establece el identificador del rol.
    */
   public void setId(int id) 
   {
      this.id = id;
   }
   
   /**
    * Devuelve el nombre del rol.
    */
   public String getName() 
   {
      return name;
   }
   
   /**
    * Establece el nombre del rol.
    */
   public void setName(String name) 
   {
      this.name = name;
   }
   
   /**
    * Devuelve la descripción del rol.
    */
   public String getDescription() 
   {
      return description;
   }
   
   /**
    * Establece la descripción del rol.
    */
   public void setDescription(String description) 
   {
      this.description = description;
   }
   
   /**
    * Devuelve el identificador de la aplicación.
    * <br /><br />
    * Una cadena vacía indica que el rol es genérico y se aplica a todas las aplicacions. 
    * Si contiene un identificador indica que el rol sólo se aplica a la aplicación concreta.
    */
   public String getApplicationId() 
   {
      return applicationId;
   }
   
   /**
    * Establece el identificador de la aplicación a la que pertenece el rol.
    * <br /><br />
    * Una cadena vacía indica que el rol es genérico y se aplica a todas las aplicacions. 
    * Si contiene un identificador indica que el rol sólo se aplica a la aplicación concreta.
    */
   public void setApplicationId(String applicationId) 
   {
      this.applicationId = applicationId;
   }
}
