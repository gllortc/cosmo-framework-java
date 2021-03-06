package com.cosmo.security;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Clase que contiene las pol�ticas de seguridad de un determinado usuario.
 * 
 * @author Gerard Llort
 */
public class UserSecurityPolicy 
{
   // Declaraci�n de variables locales
   private boolean superUser;
   private HashMap<String, Role> roles;
   private HashMap<String, Permission> permissions;


   //==============================================
   // Constructors
   //==============================================

   /**
    * Constructor de la clase {@link UserSecurityPolicy}.
    */
   public UserSecurityPolicy()
   {
      this.superUser = false;
      this.roles = new HashMap<String, Role>();
      this.permissions = new HashMap<String, Permission>();
   }


   //==============================================
   // Properties
   //==============================================

   /**
    * Indica si el usuario, seg�n la informaci�n de los roles, tiene rango de Super Usuario (tiene accesoa  todo).
    */
   public boolean isSuperUser()
   {
      return this.superUser;
   }


   //==============================================
   // Methods
   //==============================================

   /**
    * Devuelve un vector con todos los roles assignados al usuario.
    */
   public ArrayList<Role> getRoles()
   {
      return new ArrayList<Role>(this.roles.values());
   }

   /**
    * Agrega un rol a la informaci�n de seguridad de una sesi�n de usuario.
    * 
    * @param role Una instancia de {@link Role} que representa el rol a agregar.
    */
   public void addRole(Role role)
   {
      // A�ade el rol
      this.roles.put(role.getId(), role);

      // Refresca el indicador de Super Usuario
      this.superUser = this.superUser || role.isSuperUser();
   }

   /**
    * Agrega un conjunto de roles a la informaci�n de seguridad de una sesi�n de usuario.
    * 
    * @param roles Un array de instancias de {@link Role} que representan los roles a agregar.
    */
   public void addRoles(ArrayList<Role> roles)
   {
      this.roles = new HashMap<String, Role>();

      for (Role role : roles)
      {
         addRole(role);
      }
   }

   /**
    * Devuelve un vector con todos los roles assignados al usuario.
    */
   public ArrayList<Permission> getPermissions()
   {
      return new ArrayList<Permission>(this.permissions.values());
   }

   /**
    * Agrega un permiso sobre una actividad a la informaci�n de seguridad de una sesi�n de usuario.
    * <br /><br />
    * La pol�tica de permisos indica que si el usuario tiene varios permisos sobre una actividad y al menos
    * una de ellas es afirmativa, el usuario tendr� permiso de ejecuci�n de dicha actividad.
    * 
    * @param permission Una instancia de {@link Permission} que representa el permiso a agregar.
    */
   public void addPermission(Permission permission)
   {
      Permission perm = this.permissions.get(permission.getId());

      if (perm == null)
      {
         this.permissions.put(permission.getId(), permission);
      }
      else if (!perm.isGranted() && permission.isGranted())
      {
         this.permissions.put(permission.getId(), permission);
      }
   }

   /**
    * Agrega un conjunto de permisos a la informaci�n de seguridad de una sesi�n de usuario.
    * 
    * @param permissions Un array de instancias de {@link Permission} que representan los permisos a agregar.
    */
   public void addPermissions(ArrayList<Permission> permissions)
   {
      this.permissions = new HashMap<String, Permission>();

      for (Permission permission : permissions)
      {
         addPermission(permission);
      }
   }

   /**
    * Determina si el usuario propietario de la sesi�n tiene asignado un determinado rol.
    * 
    * @param roleId Una cadena que contiene el identificador (nombre) del rol.
    * 
    * @return {@code true} si el usuario pertenece al rol especificado o {@code false} en cualquier otro caso.
    */
   public boolean isInRole(String roleId)
   {
      if (this.superUser)
      {
         return true;
      }

      Role role = this.roles.get(roleId);
      return (role != null);
   }

   /**
    * Determina si el usuario propietario de la sesi�n tiene asignado como m�nimo un rol de entre la lista 
    * de roles proporcionada.
    * 
    * @param roleList Un array que contiene los identificadores (nombre) de los roles.
    * 
    * @return {@code true} si el usuario pertenece a como m�nimo uno de los roles de la lista o {@code false} en cualquier otro caso.
    */
   public boolean isInRole(ArrayList<String> roleList)
   {
      if (this.superUser)
      {
         return true;
      }

      Role role;
      for (String roleId : roleList)
      {
         role = this.roles.get(roleId);

         if (role != null)
         {
            return true;
         }
      }

      return false;
   }

   /**
    * Determina si el usuario propietario de la sesi�n tiene permiso para ejecutar determinada actividad.
    * 
    * @param activityId Una cadena que contiene el identificador (nombre) de la actividad.
    * 
    * @return {@code true} si el usuario puede ejecutar la actividad o {@code false} en cualquier otro caso.
    */
   public boolean isActivityGranted(String activityId)
   {
      if (this.superUser)
      {
         return true;
      }

      Permission permission = this.permissions.get(activityId);
      if (permission == null)
      {
         return false;
      }
      else
      {      
         return permission.isGranted();
      }
   }

   /**
    * Determina si el usuario propietario de la sesi�n tiene permiso para ejecutar como m�nimo una determinada actividad
    * de las contenidas en una lista.
    * 
    * @param activityList Array de cadenas que contiene los identificadores (nombre) de las actividades.
    * 
    * @return {@code true} si el usuario puede ejecutar como m�nimo una actividad de las contenidas en la lista o {@code false} en cualquier otro caso.
    */
   public boolean isActivityGranted(ArrayList<String> activityList)
   {
      if (this.superUser)
      {
         return true;
      }

      Permission permission;
      for (String activityId : activityList)
      {
         permission = this.permissions.get(activityId);

         if (permission != null && permission.isGranted())
         {
            return true;
         }
      }

      return false;
   }
}
