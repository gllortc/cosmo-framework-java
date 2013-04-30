package com.cosmo.security.providers;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import com.cosmo.Workspace;
import com.cosmo.security.Agent;
import com.cosmo.security.User;
import com.cosmo.security.User.UserStates;
import com.cosmo.security.UserAlreadyExistsException;
import com.cosmo.security.UserNotFoundException;
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
    * Verifica las credenciales de un usuario.
    * 
    * @param login Login del usuario.
    * @param password Contraseña (sin encriptar) del usuario.
    * @return Una instancia de {@link User} que representa el usuario al que corresponden las credenciales proporcionadas.
    * 
    * @throws UserNotFoundException
    * @throws AuthorizationProviderException 
    */
   public abstract User login(String login, String password) throws UserNotFoundException, AuthorizationProviderException;

   /**
    * Crea una nueva cuenta de usuario.
    * 
    * @param user Una instancia de {@link User} que representa el nuevo usuario.
    *     
    * @throws UserAlreadyExistsException
    * @throws AuthorizationProviderException
    */
   public abstract void add(User user) throws UserAlreadyExistsException, AuthorizationProviderException;
   
   //==============================================
   // Static members
   //==============================================
   
   /**
    * Devuelve una instancia de {@link AuthorizationProvider} convenientemente instanciada y con
    * el proveedor de autenticación de usuarios cargado.
    * 
    * @param workspace Una instancia de {@link Workspace} que representa el workspace actual.
    * @return Una instancia Ãºnica de {@link AuthorizationProvider} (sigleton).
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
    * @throws AuthorizationProviderException 
    */
   private static AuthorizationProvider loadProvider(Workspace workspace) throws AuthorizationProviderException
   {
      Agent agent;
      String className = "-- no user provider defined in proprties --";
      AuthorizationProvider provider;
      
      // Obtiene el agente de autorización
      agent = workspace.getProperties().getAuthorizationAgent();
      if (agent == null)
      {
         throw new AuthorizationProviderException("Security Configuration Exception: No authorization agent found");
      }
      
      // Obtiene el driver de autorización
      className = workspace.getProperties().getString(agent.getModuleClass());
      if (StringUtils.isNullOrEmptyTrim(agent.getModuleClass()))
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
