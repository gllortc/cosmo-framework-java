package com.cosmo.ui.templates;

/**
 * Indica que se ha producido una excepción durante la carga de una plantilla.
 * 
 * @author Gerard Llort
 */
public class TemplateLoadException extends Exception 
{
   /** Serial Version UID */
   private static final long serialVersionUID = 6717283397611708242L;

   private Exception innerException;
   
   public TemplateLoadException()
   {
      super();
      this.innerException = null;
   }
   
   public TemplateLoadException(String message, Exception innerException)
   {
      super(message);
      this.innerException = innerException;
   }
   
   public TemplateLoadException(String message)
   {
      super(message);
      this.innerException = null;
   }
   
   public Exception getInnerException() 
   {
      return innerException;
   }
}
