package com.cosmo.ui.controls;

/**
 * Excepción que indica que un formulario no está correctamente compuesto.
 * 
 * @author Gerard Llort
 */
public class MalformedFormException extends Exception
{
   /** Serial Version UID */
   private static final long serialVersionUID = 4985425210084943743L;

   public MalformedFormException()
   {
      super();
   }
   
   public MalformedFormException(String msg)
   {
      super(msg);
   }
}
