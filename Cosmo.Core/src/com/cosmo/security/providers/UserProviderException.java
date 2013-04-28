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
   
   /**
    * Constructor de la clase.
    */
   public UserProviderException()
   {
      super();
   }
   
   /**
    * Constructor de la clase.
    * 
    * @param message Una cadena que contiene el mensaje descriptivo de la excepción.
    */
   public UserProviderException(String message)
   {
      super(message);
   }
   
   /**
    * Constructor de la clase.
    * 
    * @param message Una cadena que contiene el mensaje descriptivo de la excepción.
    * @param innerException Una instancia de {@link Exception} que contiene la información del orígen del problema.
    */
   public UserProviderException(String message, Exception innerException)
   {
      super(message);
      this.inner = innerException;
   }
   
   /**
    * Devuelve una instancia de {@link Exception} que contiene la información del orígen del problema.
    */
   public Exception getInnerException()
   {
      return this.inner;
   }
}
