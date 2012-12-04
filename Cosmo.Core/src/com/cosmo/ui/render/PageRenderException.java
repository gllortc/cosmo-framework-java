package com.cosmo.ui.render;

/**
 *
 * @author Gerard Llort
 */
public class PageRenderException extends Exception
{
   /** Serial Version UID */
   private static final long serialVersionUID = 7710741671392850771L;

   private Exception inner = null;
   
   public PageRenderException()
   {
      super();
   }
   
   public PageRenderException(String message)
   {
      super(message);
   }
   
   public PageRenderException(String message, Exception innerException)
   {
      super(message);
      this.inner = innerException;
   }
   
   public Exception getInnerException()
   {
      return this.inner;
   }
}
