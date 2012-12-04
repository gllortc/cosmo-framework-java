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

   public DataTypeException()
   {
      super();
   }
   
   public DataTypeException(String message)
   {
      super(message);
   }
}
