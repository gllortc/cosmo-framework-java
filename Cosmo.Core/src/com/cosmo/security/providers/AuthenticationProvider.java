package com.cosmo.security.providers;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import com.cosmo.Workspace;
import com.cosmo.security.Agent;
import com.cosmo.security.User;
import com.cosmo.security.User.UserStates;
import com.cosmo.security.UserNotFoundException;
import com.cosmo.util.StringUtils;

/**
 * Interface para los proveedores de autenticación de Cosmo.
 * 
 * @author Gerard Llort
 */
public abstract class AuthenticationProvider 
{
   private static AuthenticationProvider instance = null;
   
   
   //==============================================
   // Methods
   //==============================================
   
   /**
    * Verifica las credenciales de un usuario.
    * 
    * @param login Login del usuario.
    * @param password Contraseña (sin encriptar) del usuario.
    * @return Una instancia de {@link User} que representa el usuario al que corresponden las credenciales proporcionadas.
    * 
    * @throws UserNotFoundException
    * @throws AuthenticationProviderException 
    */
   public abstract User login(String login, String password) throws UserNotFoundException, AuthenticationProviderException;

   /**
    * Elimina la información de autenticación actual.
    */
   public abstract void logout();
   
   /**
    * Revalida la sesión de usuario.
    */
   public abstract void validate();
   
   
   //==============================================
   // Static members
   //==============================================
   
   /**
    * Devuelve una instancia de {@link AuthenticationProvider} convenientemente instanciada y con
    * el proveedor de autenticación de usuarios cargado.
    * 
    * @param workspace Una instancia de {@link Workspace} que representa el workspace actual.
    * @return Una instancia única de {@link AuthenticationProvider} (sigleton).
    * 
    * @throws AuthenticationProviderException 
    */
   public static AuthenticationProvider getInstance(Workspace workspace) throws AuthenticationProviderException 
   {
      if (instance == null) 
      {
         instance = loadProvider(workspace);
      }

      return instance;
   }

   
   //==============================================
   // Static members
   //==============================================
   
   /**
    * Convierte el estado de un usuario a un valor numérico usable en soportes como BBDD, archivos, etc.
    * 
    * @param state Un elemento de la enumeración {@link UserStates}.
    * @return El valor numérico equivalente al estado proporcionado.
    */
   public static int statusToNumber(UserStates state)
   {
      switch (state)
      {
         case Active:
            return 1;
         case NotConfirmed:
            return 2;
         default:
            return 0;
      }
   }
   
   
   //==============================================
   // Private members
   //==============================================
   
   /**
    * Carga el controlador de usuarios.
    * 
    * @throws AuthenticationProviderException 
    */
   private static AuthenticationProvider loadProvider(Workspace workspace) throws AuthenticationProviderException
   {
      String className = "-- no authentication provider defined in proprties --";
      Agent agent;
      AuthenticationProvider provider;
      
      // Obtiene el agente de autenticación
      agent = workspace.getProperties().getAuthenticationAgent();
      if (agent == null)
      {
         throw new AuthenticationProviderException("Security Configuration Exception: No authentication agent found");
      }
      
      // Obtiene el driver de autenticación
      className = agent.getModuleClass();
      if (StringUtils.isNullOrEmptyTrim(className))
      {
         throw new AuthenticationProviderException("Security Configuration Exception: No authentication driver found");
      }
      
      try 
		{
         Class<?> cls = Class.forName(className);
         Constructor<?> cons = cls.getConstructor(Workspace.class);
         provider = (AuthenticationProvider) cons.newInstance(workspace);
         
         return provider;
		}
      catch (NoSuchMethodException ex) 
		{
         throw new AuthenticationProviderException("NoSuchMethodException: " + className, ex);
      }
      catch (InvocationTargetException ex) 
		{
         throw new AuthenticationProviderException("InvocationTargetException: " + className, ex);
      }
		catch (ClassNotFoundException ex) 
		{
         throw new AuthenticationProviderException("ClassNotFoundException: " + className, ex);
		}
      catch (InstantiationException ex)
      {
         throw new AuthenticationProviderException("InstantiationException: " + className, ex);
      }
      catch (IllegalAccessException ex)
      {
         throw new AuthenticationProviderException("IllegalAccessException: " + className, ex);
      }
   }
}
