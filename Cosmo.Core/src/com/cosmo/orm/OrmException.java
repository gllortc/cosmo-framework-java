package com.cosmo.orm;

/**
 * Excepción que indica que se ha producido un error durante el tratamiento de alguna operación de ORM Services.
 * 
 * @author Gerard Llort
 */
public class OrmException extends Exception
{
   /** Serial Version UID */
   private static final long serialVersionUID = 9134296343466070263L;


   //==============================================
   // Contructors
   //==============================================

   /**
    * Constructor de la clase {@link OrmException}.
    */
   public OrmException() 
   { 
      super(); 
   }

   /**
    * Constructor de la clase {@link OrmException}.
    * 
    * @param msg Mensaje descriptivo de la excepción.
    */
   public OrmException(String msg) 
   { 
      super(msg); 
   }

   /**
    * Constructor de la clase {@link OrmException}.
    * 
    * @param msg Mensaje descriptivo de la excepción.
    * @param innerException Una instancia de {@link Throwable} que contiene la información del orígen del problema.
    */
   public OrmException(String msg, Throwable throwable) 
   { 
      super(msg, throwable); 
   }
}
