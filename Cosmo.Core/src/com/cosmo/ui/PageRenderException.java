package com.cosmo.ui;

/**
 * Excepción que se lanza si se produce un error durante el renderizado de una página.
 * 
 * @author Gerard Llort
 */
public class PageRenderException extends Exception
{
   /** Serial Version UID */
   private static final long serialVersionUID = -1169363998784463562L;

   public PageRenderException()
   {
      super();
   }
   
   public PageRenderException(String message)
   {
      super(message);
   }
}
