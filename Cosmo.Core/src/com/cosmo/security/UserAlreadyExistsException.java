package com.cosmo.security;

/**
 *
 * @author gllort
 */
public class UserAlreadyExistsException extends Exception
{
   public UserAlreadyExistsException()
   {
      super();
   }
   
   public UserAlreadyExistsException(String message)
   {
      super(message);
   }
}
