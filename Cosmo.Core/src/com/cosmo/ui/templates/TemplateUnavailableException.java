package com.cosmo.ui.templates;

/**
 * Indica que se ha producido un error al intentar cargar una plantilla que no está disponible.
 * 
 * @author Gerard Llort
 */
public class TemplateUnavailableException extends Exception 
{
   /** Serial Version UID */
   private static final long serialVersionUID = -9200934743663166710L;

   // Declaración de variables locales
   private Exception innerException;


   //==============================================
   // Constructors
   //==============================================

   /**
    * Constructor de la clase {@link TemplateUnavailableException}.
    */
   public TemplateUnavailableException()
   {
      super();
      this.innerException = null;
   }

   /**
    * Constructor de la clase {@link TemplateUnavailableException}.
    * 
    * @param message Una cadena que contiene un mensaje descriptivo referente a la excepción ocurrida.
    */
   public TemplateUnavailableException(String message)
   {
      super(message);
      this.innerException = null;
   }

   /**
    * Constructor de la clase {@link TemplateUnavailableException}.
    * 
    * @param message Una cadena que contiene un mensaje descriptivo referente a la excepción ocurrida.
    * @param innerException Una instancia de {@link Exception} que contiene la excepción que ha ocasionado el error.
    */
   public TemplateUnavailableException(String message, Exception innerException)
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
