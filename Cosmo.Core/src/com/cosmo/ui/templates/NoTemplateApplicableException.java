package com.cosmo.ui.templates;

/**
 * Excepción que indica que no se ha podido aplicar un tema.
 * 
 * @author Gerard llort
 */
public class NoTemplateApplicableException extends Exception 
{
   /** Serial Version UID */
   private static final long serialVersionUID = 5924404941548886648L;

   private Exception innerException;
   
   public NoTemplateApplicableException()
   {
      super();
      this.innerException = null;
   }
   
   public NoTemplateApplicableException(String message, Exception innerException)
   {
      super(message);
      this.innerException = innerException;
   }
   
   public NoTemplateApplicableException(String message)
   {
      super(message);
      this.innerException = null;
   }
   
   public Exception getInnerException() 
   {
      return innerException;
   }
}
