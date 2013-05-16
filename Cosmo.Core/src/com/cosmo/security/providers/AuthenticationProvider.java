package com.cosmo.security.providers;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import javax.servlet.http.HttpServletRequest;

import com.cosmo.Workspace;
import com.cosmo.security.User;
import com.cosmo.security.User.UserStates;
import com.cosmo.security.UserNotFoundException;
import com.cosmo.structures.PluginProperties;
import com.cosmo.util.StringUtils;

/**
 * Interface para los proveedores de autenticación de Cosmo.
 * 
 * @author Gerard Llort
 */
public abstract class AuthenticationProvider 
{
   private static AuthenticationProvider instance = null;
   
   public static String TOKEN_LOGIN_VALIDATED = "cosmo.authentication.logingateway.success";
   
   //==============================================
   // Methods
   //==============================================
   
   /**
    * Verifica las credenciales de un usuario.
    * 
    * @param login Login del usuario.
    * @param password Contraseña (sin encriptar) del usuario.
    * 
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
   
   /**
    * Indica si el servicio usa un gateway para la autenticación de usuarios.
    */
   public abstract boolean isLoginGatewayRequired();
   
   /**
    * Devuelve la URL usada para la autenticación de usuarios.
    */
   public abstract String getLoginGatewayUrl();
   
   /**
    * Indica si una respuesta corresponde al retorno de la acción de login.
    * 
    * @param request Una instancia de {@link HttpServletRequest} que cotniene el contexto de la llamada.
    * 
    * @return {@code true} si la petición corresponde al retorno de la pantalla de login o {@coe false} en cualquier otro caso.
    */
   public abstract boolean isLoginGatewayResponse(HttpServletRequest request);
   
   /**
    * Detecta si una autenticación delegada (Login Gateway) ha sido exitosa.<br />
    * Las clases que extiendan a {@link AuthenticationProvider} serán responsables de obtener los datos del usuario 
    * autenticado en el sistema externo, ya sea mediante servicios REST u otros mecanismos.
    * 
    * @param request Una instancia de {@link HttpServletRequest} que cotniene el contexto de la llamada.
    * 
    * @return Una instancia de {@link User} que contiene las propiedades del usuario autenticado o {@code null} en cualquier otro caso. 
    */
   public abstract User getLoginGatewayUser(HttpServletRequest request);
   
   
   //==============================================
   // Static members
   //==============================================
   
   /**
    * Devuelve una instancia de {@link AuthenticationProvider} convenientemente instanciada y con
    * el proveedor de autenticación de usuarios cargado.
    * 
    * @param workspace Una instancia de {@link Workspace} que representa el workspace actual.
    * 
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
    * 
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
      PluginProperties agent;
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
