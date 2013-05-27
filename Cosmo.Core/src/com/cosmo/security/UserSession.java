package com.cosmo.security;

import java.util.ArrayList;
import java.util.Date;

import com.cosmo.Workspace;
import com.cosmo.security.auth.Authentication;
import com.cosmo.security.auth.AuthenticationException;
import com.cosmo.security.auth.AuthenticationFactory;
import com.cosmo.security.auth.Authorization;
import com.cosmo.security.auth.AuthorizationException;
import com.cosmo.security.auth.AuthorizationFactory;

/**
 * Representa una sesión de usuario en el workspace.
 * 
 * @author Gerard Llort
 */
public class UserSession 
{
   private Workspace workspace;
   private User currentUser;
   private Date created;
   private UserSecurityPolicy securityInfo;
   
   //==============================================
   // Constructors
   //==============================================
   
   /**
    * Constructor de la clase.
    * 
    * @param workspace Una instancia de {@link Workspace} que representa el workspace actual.
    * @param login Una cadena que contiene el login del usuario.
    * @param pwd Una cadena que contiene la contraseña del usuario.
    * 
    * @throws UserNotFoundException
    * @throws AuthenticationException
    * @throws AuthorizationException
    */
   public UserSession(Workspace workspace, String login, String pwd) throws UserNotFoundException, AuthenticationException, AuthorizationException
   {
      initialize();
      
      this.workspace = workspace;
      
      // Instancia el proveedor de autenticación
      Authentication authenticator = AuthenticationFactory.getInstance(workspace);
      
      if (authenticator != null)
      {
         // Autenticación
         this.currentUser = authenticator.login(login, pwd);
         
         // Instancia el proveedor de seguridad
         Authorization authorizator = AuthorizationFactory.getInstance(workspace);
         
         if (authorizator != null)
         {
         // Obtiene las políticas de autorización para el usuario autenticado
            this.securityInfo = authorizator.getAuthorizationData(login);
         }
      }
      else
      {
         throw new AuthenticationException("La seguridad no está habilitada para este workspace.");
      }
   }
   
   /**
    * Constructor de la clase.
    * 
    * @param workspace Una instancia de {@link Workspace} que representa el workspace actual.
    * @param user Una instancia de {@link User} que representa el usuario para el que se desea crear la sesión.
    * 
    * @throws UserNotFoundException
    * @throws AuthenticationException
    * @throws AuthorizationException
    */
   public UserSession(Workspace workspace, User user) throws UserNotFoundException, AuthenticationException, AuthorizationException
   {
      initialize();
      
      this.workspace = workspace;
      
      // Verifica que la seguridad se encuentre habilitada
      Authentication authenticator = AuthenticationFactory.getInstance(workspace);
      if (authenticator == null)
      {
         throw new AuthenticationException("La seguridad no está habilitada para este workspace.");
      }
      
      // Establece el usuario actual
      this.currentUser = user;
         
      // Obtiene la información sobre autorización del usuario
      Authorization authorizator = AuthorizationFactory.getInstance(workspace);
      if (authorizator != null)
      {
         // Obtiene las políticas de autorización para el usuario autenticado
         this.securityInfo = authorizator.getAuthorizationData(user.getLogin());
      }
   }
   
   //==============================================
   // Properties
   //==============================================

   /**
    * Indica si la sesión es válida.
    * 
    * @return {@code true} si la sesión es válida o {@code false} en cualquier otro caso.
    */
   public boolean isValidSession()
   {
      if (this.currentUser == null)
      {
         return false;
      }
      else if (this.securityInfo == null)
      {
         return false;
      }
      
      return true;
   }
   
   /**
    * Indica si el usuario, según la información de los roles, tiene rango de Super Usuario (tiene accesoa  todo).
    */
   public boolean isSuperUser()
   {
      return this.securityInfo.isSuperUser();      
   }
   
   /**
    * Devuelve una instancia de {@link User} que representa el usuario propietario de la sesión.
    */
   public User getCurrentUser() 
   {
      return currentUser;
   }

   /**
    * Devuelve la fecha/hora de creación de la sesión de usuario.
    */
   public Date getCreated() 
   {
      return created;
   }

   /**
    * Devuelve el workspace para el que es válida la sesión de usuario.
    */
   public Workspace getWorkspace() 
   {
      return workspace;
   }
   
   
   //==============================================
   // Methods
   //==============================================

   /**
    * Destruye la sesión de usuario, eliminando cualquir dato que contenga.<br />
    * Este método equivale a realizar la acción de <em>logout</em> en el agente activo.
    */
   /*public void destroy()
   {
      try
      {
         // Invoca el método logout() en el agente de autenticación
         Authentication authenticationProvider = AuthenticationFactory.getInstance(workspace);
         authenticationProvider.logout();
      }
      catch (Exception ex)
      {
         // No se tiene en cuenta
      }
      finally
      {
         // Elimina los datos de la instáncia
         this.currentUser = null;
         this.created = new Date();
         this.securityInfo = null;
      }
   }*/
   
   /**
    * Obtiene el número de minutos desde que se creó la sesión de usuario.
    * 
    * @return Devuelve un entero que representa el número de minutos desde que se creó la sesión de usuario.
    */
   public long getSessionMinutes()
   {
      long diffInSeconds;
      Date now;
      
      now = new Date();
      diffInSeconds = (now.getTime() - this.created.getTime()) / 1000;

      return (diffInSeconds = (diffInSeconds / 60)) >= 60 ? diffInSeconds % 60 : diffInSeconds;
   }
   
   /**
    * Devuelve un vector con todos los roles assignados al usuario.
    */
   public ArrayList<Role> getRoles()
   {
      return this.securityInfo.getRoles();
   }
   
   /**
    * Determina si el usuario propietario de la sesión tiene asignado un determinado rol.
    * 
    * @param roleId Una cadena que contiene el identificador (nombre) del rol.
    * 
    * @return {@code true} si el usuario pertenece al rol especificado o {@code false} en cualquier otro caso.
    */
   public boolean isInRole(String roleId)
   {
      return this.securityInfo.isInRole(roleId);
   }

   /**
    * Determina si el usuario propietario de la sesión tiene asignado como mínimo un rol de entre la lista 
    * de roles proporcionada.
    * 
    * @param roleList Un array que contiene los identificadores (nombre) de los roles.
    * 
    * @return {@code true} si el usuario pertenece a como mínimo uno de los roles de la lista o {@code false} en cualquier otro caso.
    */
   public boolean isInRole(ArrayList<String> roleList)
   {
      return this.securityInfo.isInRole(roleList);
   }
   
   /**
    * Devuelve un vector con todos los permisos sobre actividades del usuario.
    */
   public ArrayList<Permission> getPermissions()
   {
      return this.securityInfo.getPermissions();
   }
   
   /**
    * Determina si el usuario propietario de la sesión tiene permiso para ejecutar determinada actividad.
    * 
    * @param activityId Una cadena que contiene el identificador (nombre) de la actividad.
    * 
    * @return {@code true} si el usuario puede ejecutar la actividad o {@code false} en cualquier otro caso.
    */
   public boolean isActivityAllowed(String activityId)
   {
      return this.securityInfo.isActivityGranted(activityId);
   }
   
   /**
    * Determina si el usuario propietario de la sesión tiene permiso para ejecutar como mínimo una determinada actividad
    * de las contenidas en una lista.
    * 
    * @param activityList Array de cadenas que contiene los identificadores (nombre) de las actividades.
    * 
    * @return {@code true} si el usuario puede ejecutar como mínimo una actividad de las contenidas en la lista o {@code false} en cualquier otro caso.
    */
   public boolean isActivityAllowed(ArrayList<String> activityList)
   {
      return this.securityInfo.isActivityGranted(activityList);
   }
   
   //==============================================
   // Private members
   //==============================================
   
   /**
    * Inicializa la instancia.
    */
   private void initialize()
   {
      this.workspace = null;
      this.currentUser = null;
      this.created = new Date();
      this.securityInfo = null;
   }
   
}
