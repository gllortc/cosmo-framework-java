package com.cosmo.security.providers;

import com.cosmo.security.User;
import com.cosmo.security.UserAlreadyExistsException;
import com.cosmo.security.UserNotFoundException;

/**
 * Interface que deben implementar los proveedores de autenticación de usuario.
 * 
 * @author Gerard Llort
 */
public interface AuthenticationProviderInterface 
{
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
   public User login(String login, String password) throws UserNotFoundException, AuthenticationProviderException;

   /**
    * Elimina la información de autenticación actual.
    */
   public void logout();
   
   /**
    * Crea una nueva cuenta de usuario.
    * 
    * @param user Una instancia de {@link User} que representa el nuevo usuario.
    *     
    * @throws DuplicatedUserException
    * @throws AuthenticationProviderException
    */
   // public void add(User user) throws UserAlreadyExistsException, AuthenticationProviderException;
}
