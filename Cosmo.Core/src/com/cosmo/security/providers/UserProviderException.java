package com.cosmo.security.providers;

/**
 * Se produce cuando hay un error con el proveedor de perfiles de usuario.
 * 
 * @author Gerard Llort
 */
public class UserProviderException extends Exception
{
   /** Serial Version UID */
   private static final long serialVersionUID = 1837331881629720686L;

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
