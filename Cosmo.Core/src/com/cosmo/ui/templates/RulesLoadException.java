package com.cosmo.ui.templates;

/**
 * Excepción que indica que se han producido errores durante la carga de las reglas de presentación.
 * 
 * @author Gerard Llort
 */
public class RulesLoadException extends Exception 
{
   /** Serial Version UID */
   private static final long serialVersionUID = -8343012851203730237L;

   // Declaración de variables locales
   private Exception innerException;


   //==============================================
   // Constructors
   //==============================================

   /**
    * Constructor de la clase {@link RulesLoadException}.
    */
   public RulesLoadException()
   {
      super();
      this.innerException = null;
   }

   /**
    * Constructor de la clase {@link RulesLoadException}.
    * 
    * @param message Una cadena que contiene un mensaje descriptivo referente a la excepción ocurrida.
    */
   public RulesLoadException(String message)
   {
      super(message);
      this.innerException = null;
   }

   /**
    * Constructor de la clase {@link RulesLoadException}.
    * 
    * @param message Una cadena que contiene un mensaje descriptivo referente a la excepción ocurrida.
    * @param innerException Una instancia de {@link Exception} que contiene la excepción que ha ocasionado el error.
    */
   public RulesLoadException(String message, Exception innerException)
   {
      super(message);
      this.innerException = innerException;
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
      return innerException;
   }
}
