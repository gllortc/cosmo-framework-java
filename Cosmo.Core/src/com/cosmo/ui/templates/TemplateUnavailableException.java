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
   private Exception innerException;
   
   public TemplateUnavailableException()
   {
      super();
      this.innerException = null;
   }
   
   public TemplateUnavailableException(String message, Exception innerException)
   {
      super(message);
      this.innerException = innerException;
   }
   
   public TemplateUnavailableException(String message)
   {
      super(message);
      this.innerException = null;
   }
   
   public Exception getInnerException() 
   {
      return innerException;
   }
}
