package com.cosmo.reports;

/**
 *
 * @author Gerard Llort
 */
public class ReportException extends Exception
{
   /** Serial Version UID */
   private static final long serialVersionUID = 7710741671392850771L;

   // Declaración de variables locales
   private Exception inner = null;


   //==============================================
   // Constructors
   //==============================================

   /**
    * Constructor de la clase {@link ReportException}.
    */
   public ReportException()
   {
      super();
   }

   /**
    * Constructor de la clase {@link ReportException}.
    * 
    * @param msg Mensaje descriptivo de la excepción.
    */
   public ReportException(String message)
   {
      super(message);
   }

   /**
    * Constructor de la clase {@link ReportException}.
    * 
    * @param msg Mensaje descriptivo de la excepción.
    * @param innerException Una instancia de {@link Exception} que contiene la información del orígen del problema.
    */
   public ReportException(String message, Exception innerException)
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
