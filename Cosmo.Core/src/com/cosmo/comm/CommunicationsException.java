package com.cosmo.comm;

/**
 * Se produce cuando hay un error con el proveedor de perfiles de usuario.
 * 
 * @author Gerard Llort
 */
public class CommunicationsException extends Exception
{
   /** Serial Version UID */
   private static final long serialVersionUID = 1837331881629720686L;

   // Declaración de variables locales
   private Exception inner = null;


   //==============================================
   // Contructors
   //==============================================

   /**
    * Constructor de la clase {@link CommunicationsException}.
    */
   public CommunicationsException()
   {
      super();
   }

   /**
    * Constructor de la clase {@link CommunicationsException}.
    * 
    * @param message Una cadena que contiene el mensaje descriptivo de la excepción.
    */
   public CommunicationsException(String message)
   {
      super(message);
   }

   /**
    * Constructor de la clase {@link CommunicationsException}.
    * 
    * @param message Una cadena que contiene el mensaje descriptivo de la excepción.
    * @param innerException Una instancia de {@link Exception} que contiene la información del orígen del problema.
    */
   public CommunicationsException(String message, Exception innerException)
   {
      super(message);
      this.inner = innerException;
   }


   //==============================================
   // Contructors
   //==============================================

   /**
    * Devuelve la excepción original que ocasionó el lanzamiento de la excepción actual.
    * 
    * @return Una instancia de {@link Exception}.
    */
   public Exception getInnerException() 
   {
      return inner;
   }

   /**
    * Establece una instancia de {@link Exception} que contiene la información del orígen del problema.
    */
   public void setInnerException(Exception innerException) 
   {
      this.inner = innerException;
   }
}
