package com.cosmo.security.auth;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import com.cosmo.Workspace;
import com.cosmo.structures.PluginProperties;
import com.cosmo.util.StringUtils;

/**
 * Interface para los proveedores de autorización de Cosmo.
 * 
 * @author Gerard Llort
 */
public abstract class AuthorizationFactory 
{
   private static Authorization instance = null;
   
   
   //==============================================
   // Methods
   //==============================================
   
   /**
    * Carga la información de autorización de un usuario determinado.
    * 
    * @param login Una cadena que contiene el <em>login</em> del usuario.
    * 
    * @throws AuthorizationException
    */
   //public abstract SecurityInfo loadAuthorizationData(String login, SecurityInfo si) throws AuthorizationException;
   
   /**
    * Determina si un usuario tiene un rol especifico.
    * 
    * @param login Una cadena que contiene el <em>login</em> del usuario.
    * @param role Una cadena que contiene el nombre (ID) del rol.
    * 
    * @return {@code true} si el usuario tiene asignado el rol o {@code false} en cualquier otro caso.
    */
   //public abstract boolean isUserInRole(String login, String role) throws AuthorizationException;
   
   /**
    * Determina si un usuario tiene un rol especifico entre una lista de roles.
    * 
    * @param login Una cadena que contiene el <em>login</em> del usuario.
    * @param roles Un array con los identificadores de rol.
    * 
    * @return {@code true} si el usuario tiene asignado al menos un rol de los contenidos en la lista o {@code false} en cualquier otro caso.
    * 
    * @throws AuthorizationException
    */
   //public abstract boolean isUserInRole(String login, ArrayList<String> roles) throws AuthorizationException;

   /**
    * Determina si un usuario tiene permiso para ejecutar determinada actividad.
    * 
    * @param login Una cadena que contiene el <em>login</em> del usuario.
    * @param activityId Una cadena que contiene el nombre (ID) de la actividad.
    * 
    * @return @return {@code true} si el usuario tiene permiso para ejecutar la actividad o {@code false} en cualquier otro caso.
    */
   // public abstract boolean isActivityGranted(String login, String activityId);

   /**
    * Obtiene la lista de roles asignados al usuario.
    * 
    * @param login Una cadena que contiene el <em>login</em> del usuario.
    * 
    * @return Un array con los nombres (IDs) de los roles asignados al usuario.
    */
   // public abstract ArrayList<Role> getRolesByUser(String login) throws AuthorizationException;
   
   //==============================================
   // Static members
   //==============================================
   
   /**
    * Devuelve una instancia de {@link AuthorizationFactory} convenientemente instanciada y con
    * el proveedor de autenticación de usuarios cargado.
    * 
    * @param workspace Una instancia de {@link Workspace} que representa el workspace actual.
    * @return Una instancia única de {@link AuthorizationFactory} (sigleton).
    * 
    * @throws AuthorizationException 
    */
   public static Authorization getInstance(Workspace workspace) throws AuthorizationException 
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
    * @throws AuthorizationException 
    */
   private static Authorization loadProvider(Workspace workspace) throws AuthorizationException
   {
      PluginProperties agent;
      String className = "-- no authorization provider defined in proprties --";
      Authorization provider;
      
      // Obtiene el agente de autorización
      agent = workspace.getProperties().getSecurityProperties().getAuthorizationAgent();
      if (agent == null)
      {
         throw new AuthorizationException("Security Configuration Exception: No authorization agent found");
      }
      
      // Obtiene el driver de autorización
      className = agent.getModuleClass();
      if (StringUtils.isNullOrEmptyTrim(className))
      {
         throw new AuthorizationException("Security Configuration Exception: No authorization driver found");
      }
      
      try 
		{
         Class<?> cls = Class.forName(className);
         Constructor<?> cons = cls.getConstructor(Workspace.class);
         provider = (Authorization) cons.newInstance(workspace);
         
         return provider;
		}
      catch (NoSuchMethodException ex) 
		{
         throw new AuthorizationException("NoSuchMethodException: " + className, ex);
      }
      catch (InvocationTargetException ex) 
		{
         throw new AuthorizationException("InvocationTargetException: " + className, ex);
      }
		catch (ClassNotFoundException ex) 
		{
         throw new AuthorizationException("ClassNotFoundException: " + className, ex);
		}
      catch (InstantiationException ex)
      {
         throw new AuthorizationException("InstantiationException: " + className, ex);
      }
      catch (IllegalAccessException ex)
      {
         throw new AuthorizationException("IllegalAccessException: " + className, ex);
      }
   }
}
