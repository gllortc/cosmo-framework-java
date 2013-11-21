package com.cosmo.util;

/**
 * Indica que se ha producido un error en el tipo de datos proporcionado.
 * 
 * @author Gerard Llort
 */
public class DataTypeException extends Exception
{
   /** Serial Version UID */
   private static final long serialVersionUID = 452907150455854109L;


   //==============================================
   // Constructors
   //==============================================

   /**
    * Constructor de la clase {@link DataTypeException}.
    */
   public DataTypeException()
   {
      super();
   }

   /**
    * Constructor de la clase {@link DataTypeException}.
    * 
    * @param message Una cadena que contiene un mensaje descriptivo referente a la excepción ocurrida.
    */
   public DataTypeException(String message)
   {
      super(message);
   }
}
