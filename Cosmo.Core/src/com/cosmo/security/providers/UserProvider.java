package com.cosmo.security.providers;

import com.cosmo.Cosmo;
import com.cosmo.Workspace;
import com.cosmo.security.User;
import com.cosmo.security.User.UserStates;
import com.cosmo.security.UserAlreadyExistsException;
import com.cosmo.security.UserNotFoundException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Interface para los proveedores de seguridad de Cosmo.
 * 
 * @author Gerard Llort
 */
public abstract class UserProvider 
{
   private static UserProvider instance = null;
   
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
    * @throws UserProviderException 
    */
   public abstract User login(String login, String password) throws UserNotFoundException, UserProviderException;

   /**
    * Crea una nueva cuenta de usuario.
    * 
    * @param user Una instancia de {@link User} que representa el nuevo usuario.
    *     
    * @throws UserAlreadyExistsException
    * @throws UserProviderException
    */
   public abstract void add(User user) throws UserAlreadyExistsException, UserProviderException;
   
   //==============================================
   // Static members
   //==============================================
   
   /**
    * Devuelve una instancia de {@link UserProvider} convenientemente instanciada y con
    * el proveedor de autenticación de usuarios cargado.
    * 
    * @param workspace Una instancia de {@link Workspace} que representa el workspace actual.
    * @return Una instancia Ãºnica de {@link UserProvider} (sigleton).
    * 
    * @throws UserProviderException 
    */
   public static UserProvider getInstance(Workspace workspace) throws UserProviderException 
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
    * @throws UserProviderException 
    */
   private static UserProvider loadProvider(Workspace workspace) throws UserProviderException
   {
      String className = "-- no user provider defined in proprties --";
      UserProvider provider;
      
      try 
		{
         className = workspace.getProperties().getString(Cosmo.PROPERTY_WORKSPACE_SECURITY_PROVIDER);
         
         Class<?> cls = Class.forName(className);
         Constructor<?> cons = cls.getConstructor(Workspace.class);
         provider = (UserProvider) cons.newInstance(workspace);
         
         return provider;
		}
      catch (NoSuchMethodException ex) 
		{
         throw new UserProviderException("NoSuchMethodException: " + className, ex);
      }
      catch (InvocationTargetException ex) 
		{
         throw new UserProviderException("InvocationTargetException: " + className, ex);
      }
		catch (ClassNotFoundException ex) 
		{
         throw new UserProviderException("ClassNotFoundException: " + className, ex);
		}
      catch (InstantiationException ex)
      {
         throw new UserProviderException("InstantiationException: " + className, ex);
      }
      catch (IllegalAccessException ex)
      {
         throw new UserProviderException("IllegalAccessException: " + className, ex);
      }
   }
}
