package com.cosmo.data;

public class DataException extends Exception
{
   private static final long serialVersionUID = -2577150645305791318L;

   /**
    * Constructor de la classe.
    * 
    * @param msg Missatge que ha de mostrar l'excepció
    */
   public DataException(String msg) 
   { 
      super(msg); 
   }
   
   /**
    * Constructor de la classe.
    * 
    * @param msg Missatge que ha de mostrar l'excepció
    * @param throwable Excepció que l'ha provocat.
    */
   public DataException(String msg, Throwable throwable) 
   { 
      super(msg, throwable); 
   }
}
