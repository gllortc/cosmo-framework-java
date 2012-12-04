package com.cosmo.security;

/**
 *
 * @author gllort
 */
public class DuplicatedUserException extends Exception
{
   public DuplicatedUserException()
   {
      super();
   }
   
   public DuplicatedUserException(String message)
   {
      super(message);
   }
}
