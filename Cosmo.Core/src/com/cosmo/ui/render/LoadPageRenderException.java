package com.cosmo.ui.render;

/**
 * Exceptción que indica que no se ha podido cargar el renderizador de páginas.
 * 
 * @author Gerard Llort
 */
public class LoadPageRenderException extends Exception
{
   /** Serial Version UID */
   private static final long serialVersionUID = 7538912157946062566L;

   // Declaración de variables locales.
   private Exception inner = null;


   //==============================================
   // Contructors
   //==============================================

   /**
    * Constructor de la clase {@link LoadPageRenderException}.
    */
   public LoadPageRenderException()
   {
      super();
   }

   /**
    * Constructor de la clase {@link LoadPageRenderException}.
    * 
    * @param msg Mensaje descriptivo de la excepción.
    */
   public LoadPageRenderException(String message)
   {
      super(message);
   }

   /**
    * Constructor de la clase {@link LoadPageRenderException}.
    * 
    * @param msg Mensaje descriptivo de la excepción.
    * @param innerException Una instancia de {@link Exception} que contiene la información del orígen del problema.
    */
   public LoadPageRenderException(String message, Exception innerException)
   {
      super(message);
      this.inner = innerException;
   }


   //==============================================
   // Contructors
   //==============================================

   /**
    * Devuelve la excepción original que ocasionó el lanzamiento de la excepción actual.
    * 
    * @return Una instancia de {@link Exception}.
    */
   public Exception getInnerException()
   {
      return this.inner;
   }
}
