package com.cosmo.structures;

/**
 * Se produce cuando se intenta acceder a celdas fuera de los límites de la tabla.
 * 
 * @author Gerard Llort
 */
public class TableLimitsException extends Exception
{
   /** Serial Version UID */
   private static final long serialVersionUID = -7170715618135972794L;

   public TableLimitsException()
   {
      super();
   }
}
