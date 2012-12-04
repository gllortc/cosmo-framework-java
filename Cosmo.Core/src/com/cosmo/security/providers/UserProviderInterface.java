package com.cosmo.security.providers;

import com.cosmo.security.User;
import com.cosmo.security.UserAlreadyExistsException;
import com.cosmo.security.UserNotFoundException;

/**
 * Interface que deben implementar los proveedores de autenticación de usuario.
 * 
 * @author Gerard Llort
 */
public interface UserProviderInterface 
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
    * @throws UserProviderException 
    */
   public User login(String login, String password) throws UserNotFoundException, UserProviderException;

   /**
    * Crea una nueva cuenta de usuario.
    * 
    * @param user Una instancia de {@link User} que representa el nuevo usuario.
    *     
    * @throws DuplicatedUserException
    * @throws UserProviderException
    */
   public void add(User user) throws UserAlreadyExistsException, UserProviderException;
}
