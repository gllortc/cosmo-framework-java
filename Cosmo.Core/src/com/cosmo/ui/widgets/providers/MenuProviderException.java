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

   private Exception inner = null;
   
   public MenuProviderException()
   {
      super();
   }
   
   public MenuProviderException(String message)
   {
      super(message);
   }
   
   public MenuProviderException(String message, Exception innerException)
   {
      super(message);
      this.inner = innerException;
   }
   
   public Exception getInnerException()
   {
      return this.inner;
   }
}
