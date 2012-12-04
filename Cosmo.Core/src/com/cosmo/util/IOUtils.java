package com.cosmo.util;

import java.io.IOException;
import java.io.InputStream;

/**
 * Helper con utilidades de entrada/salida.
 * 
 * @author Gerard Llort
 */
public class IOUtils 
{
   /**
    * Cierra un strema de entrada sin generar excepción en caso de fallo.
    * 
    * @param is Stream de lectura.
    */
   public static void closeStream(InputStream is)
   {
      try
      {
         if (is != null)
         {
            is.close();
         }
      }
      catch (IOException ex)
      {
         // Nothing to do
      }
   }
}
