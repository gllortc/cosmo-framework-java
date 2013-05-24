package com.cosmo.security.auth;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import com.cosmo.security.User;
import com.cosmo.security.UserNotFoundException;

/**
 * Interface que deben implementar los agentes de autenticación.
 * 
 * @author Gerard Llort
 */
public interface Authentication 
{
   public static String TOKEN_LOGIN_VALIDATED = "cosmo.authentication.logingateway.success";

   
   //------------------------------------------
   // Properties
   //------------------------------------------
   
   /**
    * Devuelve un {@code hash} que contiene los parámetros de configuración del agente de seguridad.
    */
   public HashMap<String, String> getParameters();
   
   
   //------------------------------------------
   // Standard authentication methods
   //------------------------------------------
   
   /**
    * Verifica las credenciales de un usuario.
    * 
    * @param login Login del usuario.
    * @param password Contraseña (sin encriptar) del usuario.
    * 
    * @return Una instancia de {@link User} que representa el usuario al que corresponden las credenciales proporcionadas.
    * 
    * @throws UserNotFoundException
    * @throws AuthenticationException 
    */
   public User login(String login, String password) throws UserNotFoundException, AuthenticationException;

   /**
    * Elimina la información de autenticación actual.
    */
   public void logout();
   
   /**
    * Revalida la sesión de usuario.
    */
   public void validate();
   
   //------------------------------------------
   // Login Gateway authentication methods
   //------------------------------------------
   
   /**
    * Indica si el servicio usa un gateway para la autenticación de usuarios.
    */
   public boolean isLoginGatewayRequired();
   
   /**
    * Devuelve la URL usada para la autenticación de usuarios.
    */
   public String getLoginGatewayUrl();
   
   /**
    * Indica si una respuesta corresponde al retorno de la acción de login.
    * 
    * @param request Una instancia de {@link HttpServletRequest} que cotniene el contexto de la llamada.
    * 
    * @return {@code true} si la petición corresponde al retorno de la pantalla de login o {@coe false} en cualquier otro caso.
    */
   public boolean isLoginGatewayResponse(HttpServletRequest request);
   
   /**
    * Detecta si una autenticación delegada (Login Gateway) ha sido exitosa.<br />
    * Las clases que extiendan a {@link AuthenticationFactory} serán responsables de obtener los datos del usuario 
    * autenticado en el sistema externo, ya sea mediante servicios REST u otros mecanismos.
    * 
    * @param request Una instancia de {@link HttpServletRequest} que cotniene el contexto de la llamada.
    * 
    * @return Una instancia de {@link User} que contiene las propiedades del usuario autenticado o {@code null} en cualquier otro caso. 
    */
   public User getLoginGatewayUser(HttpServletRequest request);
}
