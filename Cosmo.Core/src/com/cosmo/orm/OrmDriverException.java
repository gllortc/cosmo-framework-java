package com.cosmo.orm;

/**
 * Excepción que indica que se ha producido un error al cargar un driver para ORM Services.
 * 
 * @author Gerard Llort
 */
public class OrmDriverException extends Exception
{
   /** Serial Version UID */
   private static final long serialVersionUID = 9134296343466070263L;


   //==============================================
   // Contructors
   //==============================================

   /**
    * Constructor de la clase {@link OrmDriverException}.
    */
   public OrmDriverException() 
   { 
      super(); 
   }
   
   /**
    * Constructor de la clase {@link OrmDriverException}.
    * 
    * @param msg Mensaje descriptivo de la excepción.
    */
   public OrmDriverException(String msg) 
   { 
      super(msg); 
   }
   
   /**
    * Constructor de la clase {@link OrmDriverException}.
    * 
    * @param msg Mensaje descriptivo de la excepción.
    * @param innerException Una instancia de {@link Throwable} que contiene la información del orígen del problema.
    */
   public OrmDriverException(String msg, Throwable throwable) 
   { 
      super(msg, throwable); 
   }
}
