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

   /**
    * Constructor de la clase.
    */
   public UserNotFoundException()
   {
      super();
   }
   
   /**
    * Constructor de la clase.
    * 
    * @param message Una cadena que contiene el mensaje descriptivo de la excepción.
    */
   public UserNotFoundException(String message)
   {
      super(message);
   }
}
