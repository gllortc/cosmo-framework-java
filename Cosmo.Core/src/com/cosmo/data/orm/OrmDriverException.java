package com.cosmo.data.orm;

public class OrmDriverException extends Exception
{
   /** Serial Version UID */
   private static final long serialVersionUID = 9134296343466070263L;

   /**
    * Constructor de la classe.
    */
   public OrmDriverException() 
   { 
      super(); 
   }
   
   /**
    * Constructor de la classe.
    * 
    * @param msg Missatge que ha de mostrar l'excepción
    */
   public OrmDriverException(String msg) 
   { 
      super(msg); 
   }
   
   /**
    * Constructor de la classe.
    * 
    * @param msg Missatge que ha de mostrar l'excepción
    * @param throwable Excepción que l'ha provocat.
    */
   public OrmDriverException(String msg, Throwable throwable) 
   { 
      super(msg, throwable); 
   }
}
