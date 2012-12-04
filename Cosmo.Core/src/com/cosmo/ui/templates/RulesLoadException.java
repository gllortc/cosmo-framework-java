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

   private Exception innerException;
   
   public RulesLoadException()
   {
      super();
      this.innerException = null;
   }
   
   public RulesLoadException(String message, Exception innerException)
   {
      super(message);
      this.innerException = innerException;
   }
   
   public RulesLoadException(String message)
   {
      super(message);
      this.innerException = null;
   }
   
   public Exception getInnerException() 
   {
      return innerException;
   }
}
