package com.cosmo.ui;

/**
 * Excepción que se lanza si se produce un error durante el renderizado de una página.
 * @author Gerard
 */
public class PageRenderException extends Exception
{
   public PageRenderException()
   {
      super();
   }
   
   public PageRenderException(String message)
   {
      super(message);
   }
}
