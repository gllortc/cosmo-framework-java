package com.cosmo.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.log4j.Logger;

import com.cosmo.logging.LogFactory;

/**
 * Helper con utilidades de entrada/salida.
 * 
 * @author Gerard Llort
 */
public class IOUtils 
{


   //==============================================
   // Static Methods
   //==============================================

   /**
    * Cierra un strema de entrada sin generar excepciÃ³n en caso de fallo.
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

   /**
    * Asegura que una determinada ruta (path) exista. Si no existe la crea.
    * 
    * @param existingPath Ruta existente. Si no existe genera una excepción.
    * @param ensuredPath Ruta inferior a {@code existingPath}. Si no existe, intenta crearla.
    * 
    * @throws IOException 
    */
   public static void ensurePathExists(String existingPath, String ensuredPath) throws IOException
   {
      File file = new File(existingPath);

      // Asegura la existencia de la ruta fija
      if (!file.exists()) 
      {
         throw new IOException("Path " + existingPath + " does not exist.");
      } 
      else if (!file.isDirectory()) 
      {
         throw new IOException("Path " + existingPath + " isn't a folder.");
      }

      // Asegura la terminación de la ruta con separador
      existingPath += (existingPath.endsWith(File.separator) ? "" : File.separator);

      // Asegura la existencia de cada carpeta
      String[] folders = ensuredPath.split(regexpScapeChar(File.separator));
      for (String folder : folders)
      {
         existingPath += (folder + (folder.endsWith(File.separator) ? "" : File.separator));

         file = new File(existingPath);
         if (!file.exists() || !file.isDirectory()) 
         {
            Logger log = LogFactory.getLogger("Cosmo Framework");
            log.debug("Creating folder " + file.toString());

            file.mkdir();
         } 
      }
   }


   //==============================================
   // Private Members
   //==============================================

   /**
    * Escapa un carácter para una expresión regular si es necesario (corresponde a un carácter reservado
    * de control de expresiones regulares).
    * 
    * @param character Carácter a comprobar.
    * 
    * @return El carácter original escapado si es necesario.
    */
   private static String regexpScapeChar(String character)
   {
      String sChars[] = { "\\", "^", "$", ".", "|", "?", "*", "+", "(", ")", "[", "{" };

      for (String sChar : sChars)
      {
         if (sChar.equals(character))
         {
            return "\\" + character;
         }
      }

      return character;
   }
}
