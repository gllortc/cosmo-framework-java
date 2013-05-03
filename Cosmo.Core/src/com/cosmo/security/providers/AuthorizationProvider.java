package com.cosmo.security.providers;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import com.cosmo.Workspace;
import com.cosmo.security.Agent;
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
    */
   public abstract void loadAuthorizationData(String login);
   
   /**
    * Determina si un usuario tiene un determinado rol.
    * 
    * @param login Una cadena que contiene el <em>login</em> del usuario.
    * @param role Una cadena que contiene el nombre (ID) del rol.
    * 
    * @return {@code true} si el usuario tiene asignado el rol o {@code false} en cualquier otro caso.
    */
   public abstract boolean isUserInRole(String login, String role);

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
   public abstract String[] getUserRoles(String login);
   
   
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
      Agent agent;
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
