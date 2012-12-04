package com.cosmo.security;

/**
 * Se produce cuando no se encuentra un usuario.
 *
 * @author Gerard Llort
 */
public class UserNotFoundException extends Exception
{
   /** Serial Version UID */
   private static final long serialVersionUID = -8978920220915432933L;

   public UserNotFoundException()
   {
      super();
   }
   
   public UserNotFoundException(String message)
   {
      super(message);
   }
}
