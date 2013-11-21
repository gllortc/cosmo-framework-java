package com.cosmo.orm;

/**
 * Excepción que indica que existe algún problema con el mapeado ORM en algun objeto.
 * 
 * @author Gerard Llort
 */
public class InvalidMappingException extends Exception
{
   /** Serial Version UID */
   private static final long serialVersionUID = 9134296343466070263L;


   //==============================================
   // Contructors
   //==============================================

   /**
    * Constructor de la clase {@link InvalidMappingException}.
    */
   public InvalidMappingException() 
   { 
      super(); 
   }

   /**
    * Constructor de la clase {@link InvalidMappingException}.
    * 
    * @param msg Mensaje descriptivo de la excepción.
    */
   public InvalidMappingException(String msg) 
   { 
      super(msg); 
   }

   /**
    * Constructor de la clase {@link InvalidMappingException}.
    * 
    * @param msg Mensaje descriptivo de la excepción.
    * @param throwable Una instancia de {@link Throwable} que contiene la información del orígen del problema.
    */
   public InvalidMappingException(String msg, Throwable throwable) 
   { 
      super(msg, throwable); 
   }
}
