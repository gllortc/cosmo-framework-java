package com.cosmo.security.providers;

/**
 *
 * @author gllort
 */
public class UserProviderException extends Exception
{
   private Exception inner = null;
   
   public UserProviderException()
   {
      super();
   }
   
   public UserProviderException(String message)
   {
      super(message);
   }
   
   public UserProviderException(String message, Exception innerException)
   {
      super(message);
      this.inner = innerException;
   }
   
   public Exception getInnerException()
   {
      return this.inner;
   }
}
