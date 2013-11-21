package com.cosmo.ui.widgets.providers;

/**
 * Indica que se ha producido un error en el proveedor del menú.
 * 
 * @author Gerard Llort
 */
public class MenuProviderException extends Exception
{
   /** Serial Version UID */
   private static final long serialVersionUID = 5399108489302105210L;

   // Declaración de variables locales
   private Exception inner = null;


   //==============================================
   // Constructors
   //==============================================

   /**
    * Constructor de la clase {@link MenuProviderException}.
    */
   public MenuProviderException()
   {
      super();
   }

   /**
    * Constructor de la clase {@link MenuProviderException}.
    * 
    * @param message Una cadena que contiene un mensaje descriptivo referente a la excepción ocurrida.
    */
   public MenuProviderException(String message)
   {
      super(message);
   }

   /**
    * Constructor de la clase {@link MenuProviderException}.
    * 
    * @param message Una cadena que contiene un mensaje descriptivo referente a la excepción ocurrida.
    * @param innerException Una instancia de {@link Exception} que contiene la excepción que ha ocasionado el error.
    */
   public MenuProviderException(String message, Exception innerException)
   {
      super(message);
      this.inner = innerException;
   }


   //==============================================
   // Methods
   //==============================================

   /**
    * Devuelve la excepción original que ocasionó el lanzamiento de la excepción actual.
    * 
    * @return Una instancia de {@link Exception}.
    */
   public Exception getInnerException()
   {
      return this.inner;
   }
}
