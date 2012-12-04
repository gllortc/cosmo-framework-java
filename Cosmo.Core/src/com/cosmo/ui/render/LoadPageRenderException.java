package com.cosmo.ui.render;

/**
 *
 * @author Gerard Llort
 */
public class LoadPageRenderException extends Exception
{
   /** Serial Version UID */
   private static final long serialVersionUID = 7538912157946062566L;

   private Exception inner = null;
   
   public LoadPageRenderException()
   {
      super();
   }
   
   public LoadPageRenderException(String message)
   {
      super(message);
   }
   
   public LoadPageRenderException(String message, Exception innerException)
   {
      super(message);
      this.inner = innerException;
   }
   
   public Exception getInnerException()
   {
      return this.inner;
   }
}
