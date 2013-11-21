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


   //==============================================
   // Contructors
   //==============================================

   /**
    * Constructor de la clase {@link PageRenderException}.
    */
   public PageRenderException()
   {
      super();
   }

   /**
    * Constructor de la clase {@link PageRenderException}.
    * 
    * @param msg Mensaje descriptivo de la excepción.
    */
   public PageRenderException(String message)
   {
      super(message);
   }
}
