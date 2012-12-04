package com.cosmo.ui.templates;

/**
 * Indica que se ha producido un error en la lectura o manejo de un control de plantilla.
 * 
 * @author Gerard Llort
 */
public class TemplateControlException extends Exception 
{
   /** Serial Version UID */
   private static final long serialVersionUID = 4282790500754859617L;

   private Exception innerException;
   
   public TemplateControlException()
   {
      super();
      this.innerException = null;
   }
   
   public TemplateControlException(String message, Exception innerException)
   {
      super(message);
      this.innerException = innerException;
   }
   
   public TemplateControlException(String message)
   {
      super(message);
      this.innerException = null;
   }
   
   public Exception getInnerException() 
   {
      return innerException;
   }
}
