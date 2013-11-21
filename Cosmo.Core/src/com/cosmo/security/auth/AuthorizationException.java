package com.cosmo.security.auth;

/**
 * Se produce cuando hay un error con el proveedor de perfiles de usuario.
 * 
 * @author Gerard Llort
 */
public class AuthorizationException extends Exception
{
   /** Serial Version UID */
   private static final long serialVersionUID = 1837331881629720686L;

   // Declaración de variables locales
   private Exception inner = null;


   //==============================================
   // Constructors
   //==============================================

   /**
    * Constructor de la clase {@link AuthorizationException}.
    */
   public AuthorizationException()
   {
      super();
   }

   /**
    * Constructor de la clase {@link AuthorizationException}.
    * 
    * @param message Una cadena que contiene el mensaje descriptivo de la excepción.
    */
   public AuthorizationException(String message)
   {
      super(message);
   }

   /**
    * Constructor de la clase {@link AuthorizationException}.
    * 
    * @param message Una cadena que contiene el mensaje descriptivo de la excepción.
    * @param innerException Una instancia de {@link Exception} que contiene la información del orígen del problema.
    */
   public AuthorizationException(String message, Exception innerException)
   {
      super(message);
      this.inner = innerException;
   }


   //==============================================
   // Methods
   //==============================================

   /**
    * Devuelve una instancia de {@link Exception} que contiene la información del orígen del problema.
    */
   public Exception getInnerException()
   {
      return this.inner;
   }
}
