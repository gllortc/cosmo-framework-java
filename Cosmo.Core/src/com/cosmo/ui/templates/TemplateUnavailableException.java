package com.cosmo.ui.templates;

/**
 * Indica que se ha producido un error al intentar cargar una plantilla que no est� disponible.
 * 
 * @author Gerard Llort
 */
public class TemplateUnavailableException extends Exception 
{
   /** Serial Version UID */
   private static final long serialVersionUID = -9200934743663166710L;

   // Declaraci�n de variables locales
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
    * @param message Una cadena que contiene un mensaje descriptivo referente a la excepci�n ocurrida.
    */
   public TemplateUnavailableException(String message)
   {
      super(message);
      this.innerException = null;
   }

   /**
    * Constructor de la clase {@link TemplateUnavailableException}.
    * 
    * @param message Una cadena que contiene un mensaje descriptivo referente a la excepci�n ocurrida.
    * @param innerException Una instancia de {@link Exception} que contiene la excepci�n que ha ocasionado el error.
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
    * Devuelve la excepci�n original que ocasion� el lanzamiento de la excepci�n actual.
    * 
    * @return Una instancia de {@link Exception}.
    */
   public Exception getInnerException() 
   {
      return innerException;
   }
}
