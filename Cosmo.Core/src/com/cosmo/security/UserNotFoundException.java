package com.cosmo.security;

/**
 *
 * @author gllort
 */
public class UserNotFoundException extends Exception
{
   public UserNotFoundException()
   {
      super();
   }
   
   public UserNotFoundException(String message)
   {
      super(message);
   }
}
