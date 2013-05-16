package com.cosmo.security.providers;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import com.cosmo.Workspace;
import com.cosmo.security.Role;
import com.cosmo.security.UserSession.SecurityInfo;
import com.cosmo.structures.PluginProperties;
import com.cosmo.util.StringUtils;

/**
 * Interface para los proveedores de autorización de Cosmo.
 * 
 * @author Gerard Llort
 */
public abstract class AuthorizationProvider 
{
   private static AuthorizationProvider instance = null;
   
   
   //==============================================
   // Methods
   //==============================================
   
   /**
    * Carga la información de autorización de un usuario determinado.
    * 
    * @param login Una cadena que contiene el <em>login</em> del usuario.
    * 
    * @throws AuthorizationProviderException
    */
   public abstract SecurityInfo loadAuthorizationData(String login, SecurityInfo si) throws AuthorizationProviderException;
   
   /**
    * Determina si un usuario tiene un rol especifico.
    * 
    * @param login Una cadena que contiene el <em>login</em> del usuario.
    * @param role Una cadena que contiene el nombre (ID) del rol.
    * 
    * @return {@code true} si el usuario tiene asignado el rol o {@code false} en cualquier otro caso.
    */
   public abstract boolean isUserInRole(String login, String role) throws AuthorizationProviderException;
   
   /**
    * Determina si un usuario tiene un rol especifico entre una lista de roles.
    * 
    * @param login Una cadena que contiene el <em>login</em> del usuario.
    * @param roles Un array con los identificadores de rol.
    * 
    * @return {@code true} si el usuario tiene asignado al menos un rol de los contenidos en la lista o {@code false} en cualquier otro caso.
    * 
    * @throws AuthorizationProviderException
    */
   public abstract boolean isUserInRole(String login, ArrayList<String> roles) throws AuthorizationProviderException;

   /**
    * Determina si un usuario tiene permiso para ejecutar determinada actividad.
    * 
    * @param login Una cadena que contiene el <em>login</em> del usuario.
    * @param activityId Una cadena que contiene el nombre (ID) de la actividad.
    * 
    * @return @return {@code true} si el usuario tiene permiso para ejecutar la actividad o {@code false} en cualquier otro caso.
    */
   public abstract boolean isActivityGranted(String login, String activityId);

   /**
    * Obtiene la lista de roles asignados al usuario.
    * 
    * @param login Una cadena que contiene el <em>login</em> del usuario.
    * 
    * @return Un array con los nombres (IDs) de los roles asignados al usuario.
    */
   public abstract ArrayList<Role> getRolesByUser(String login) throws AuthorizationProviderException;
   
   //==============================================
   // Static members
   //==============================================
   
   /**
    * Devuelve una instancia de {@link AuthorizationProvider} convenientemente instanciada y con
    * el proveedor de autenticación de usuarios cargado.
    * 
    * @param workspace Una instancia de {@link Workspace} que representa el workspace actual.
    * @return Una instancia única de {@link AuthorizationProvider} (sigleton).
    * 
    * @throws AuthorizationProviderException 
    */
   public static AuthorizationProvider getInstance(Workspace workspace) throws AuthorizationProviderException 
   {
      if (instance == null) 
      {
         instance = loadProvider(workspace);
      }

      return instance;
   }
   
   
   //==============================================
   // Private members
   //==============================================
   
   /**
    * Carga el controlador de usuarios.
    * 
    * @throws AuthorizationProviderException 
    */
   private static AuthorizationProvider loadProvider(Workspace workspace) throws AuthorizationProviderException
   {
      PluginProperties agent;
      String className = "-- no authorization provider defined in proprties --";
      AuthorizationProvider provider;
      
      // Obtiene el agente de autorización
      agent = workspace.getProperties().getAuthorizationAgent();
      if (agent == null)
      {
         throw new AuthorizationProviderException("Security Configuration Exception: No authorization agent found");
      }
      
      // Obtiene el driver de autorización
      className = agent.getModuleClass();
      if (StringUtils.isNullOrEmptyTrim(className))
      {
         throw new AuthorizationProviderException("Security Configuration Exception: No authorization driver found");
      }
      
      try 
		{
         Class<?> cls = Class.forName(className);
         Constructor<?> cons = cls.getConstructor(Workspace.class);
         provider = (AuthorizationProvider) cons.newInstance(workspace);
         
         return provider;
		}
      catch (NoSuchMethodException ex) 
		{
         throw new AuthorizationProviderException("NoSuchMethodException: " + className, ex);
      }
      catch (InvocationTargetException ex) 
		{
         throw new AuthorizationProviderException("InvocationTargetException: " + className, ex);
      }
		catch (ClassNotFoundException ex) 
		{
         throw new AuthorizationProviderException("ClassNotFoundException: " + className, ex);
		}
      catch (InstantiationException ex)
      {
         throw new AuthorizationProviderException("InstantiationException: " + className, ex);
      }
      catch (IllegalAccessException ex)
      {
         throw new AuthorizationProviderException("IllegalAccessException: " + className, ex);
      }
   }
}
