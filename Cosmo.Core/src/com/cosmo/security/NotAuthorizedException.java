package com.cosmo.security;

/**
 * Indica que el usuario ya existe (<em>login</em> o <em>mail</em> duplicados).
 *
 * @author Gerard Llort
 */
public class NotAuthorizedException extends Exception
{
   /** Serial Version UID */
   private static final long serialVersionUID = 2470642478480808946L;


   //==============================================
   // Contructors
   //==============================================

   /**
    * Constructor de la clase {@link NotAuthorizedException}.
    */
   public NotAuthorizedException()
   {
      super();
   }

   /**
    * Constructor de la clase {@link NotAuthorizedException}.
    * 
    * @param message Una cadena que contiene el mensaje descriptivo de la excepción.
    */
   public NotAuthorizedException(String message)
   {
      super(message);
   }
}
