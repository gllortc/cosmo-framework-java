package com.cosmo.security.auth;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import com.cosmo.Workspace;
import com.cosmo.security.annotations.LoginGatewayAgent;
import com.cosmo.structures.PluginProperties;
import com.cosmo.util.StringUtils;

/**
 * Implementa una clase que permite obtener la instancia del agente de autenticación configurado en el workspace.
 * <br /><br />
 * Esta clase está diseñada según el patrón <em>Singleton</em> para asegurar que sólo se instancia una vez
 * la implementación del agente de autenticación.
 * 
 * @author Gerard Llort
 */
public abstract class AuthenticationFactory 
{
   // Instancia única del agente de autenticación
   private static Authentication instance = null;
   
   
   //==============================================
   // Static members
   //==============================================
   
   /**
    * Devuelve una instancia de {@link Authentication} convenientemente instanciada y con
    * el proveedor de autenticación de usuarios cargado.
    * 
    * @param workspace Una instancia de {@link Workspace} que representa el workspace actual.
    * 
    * @return Una instancia única de {@link Authentication} (sigleton).
    * 
    * @throws AuthenticationException 
    */
   public static Authentication getInstance(Workspace workspace) throws AuthenticationException 
   {
      if (instance == null) 
      {
         instance = loadProvider(workspace);
      }

      return instance;
   }
   
   /**
    * Indica si una determinada instancia de un objeto corresponde a un agente con el mecanismo <em>Login Gateway</em>.
    * 
    * @param agent Instancia a comprobar.
    * 
    * @return {@code true} si la instancia pertenece a un agente que usa <em>Login Gateway</em> o {@code false} en cualquier otro caso. 
    */
   public static boolean isLoginGatewayAgent(Object agent)
   {
      return (agent.getClass().isAnnotationPresent(LoginGatewayAgent.class));
   }

   
   //==============================================
   // Private members
   //==============================================
   
   /**
    * Carga el controlador de usuarios.
    * 
    * @throws AuthenticationException 
    */
   private static Authentication loadProvider(Workspace workspace) throws AuthenticationException
   {
      String className = "-- no authentication provider defined in proprties --";
      PluginProperties agent;
      Authentication provider;
      
      // Obtiene el agente de autenticación
      agent = workspace.getProperties().getSecurityProperties().getAuthenticationAgent();
      if (agent == null)
      {
         throw new AuthenticationException("Security Configuration Exception: No authentication agent found");
      }
      
      // Obtiene el driver de autenticación
      className = agent.getModuleClass();
      if (StringUtils.isNullOrEmptyTrim(className))
      {
         throw new AuthenticationException("Security Configuration Exception: No authentication driver found");
      }
      
      try 
		{
         Class<?> cls = Class.forName(className);
         Constructor<?> cons = cls.getConstructor(Workspace.class);
         provider = (Authentication) cons.newInstance(workspace);
         
         return provider;
		}
      catch (NoSuchMethodException ex) 
		{
         throw new AuthenticationException("NoSuchMethodException: " + className, ex);
      }
      catch (InvocationTargetException ex) 
		{
         throw new AuthenticationException("InvocationTargetException: " + className, ex);
      }
		catch (ClassNotFoundException ex) 
		{
         throw new AuthenticationException("ClassNotFoundException: " + className, ex);
		}
      catch (InstantiationException ex)
      {
         throw new AuthenticationException("InstantiationException: " + className, ex);
      }
      catch (IllegalAccessException ex)
      {
         throw new AuthenticationException("IllegalAccessException: " + className, ex);
      }
   }
}
