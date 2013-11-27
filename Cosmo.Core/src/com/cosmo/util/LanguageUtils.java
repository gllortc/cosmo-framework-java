package com.cosmo.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Implementa el gestor de literales de texto usado por el framework.
 * 
 * @author Gerard Llort
 */
public class LanguageUtils 
{
   // Configuration file name 
   // private final static String CONFIG_FILE_NAME = "/i18n/message_ca_ES.properties";

   // Configuration path
   private final static String CONFIG_FILE_PATH = "/i18n/";

   // Códigos de idioma
   public final static String LANG_SPANISH = "es_ES";
   public final static String LANG_CATALAN = "ca_ES";
   public final static String LANG_ENGLISH = "en_ES";

   /*
    * Gestiona el lenguaje activo.
    * De momento:
    *       0 = LANG_SPANISH
    *       1 = LANG_CATALAN
    *       2 = LANG_ENGLISH
    */
   private Integer activeLanguage = 0;
   private Properties properties = null;

   /**
    * Constructor de la clase.
    * 
    * @param languageCode Código de idioma.
    */
   public LanguageUtils(String languageCode)
   {
      loadFile(languageCode);
      setActiveLanguage(languageCode);
   }

   /**
    * Constructor de la clase.
    */
   public LanguageUtils() 
   {
      loadFile(LanguageUtils.LANG_ENGLISH);
   }

   public static String getMessage(String key) 
   {
      return new String();
   }

   /**
    * Marca el lenguaje activo 
    */
   public void setActiveLanguage(String key)
   {
      if (key.equals(LanguageUtils.LANG_SPANISH)) 
      {
         this.activeLanguage = 0;
      }
      else if (key.equals(LanguageUtils.LANG_CATALAN)) 
      {
         this.activeLanguage = 1;
      }
      else if (key.equals(LanguageUtils.LANG_ENGLISH)) 
      {
         this.activeLanguage = 2;
      }
   }

   /**
    * Retorna el lenguaje activo 
    */
   public String getActiveLanguage() 
   {
      String lang = null;

      if (this.activeLanguage == 0) 
      {
         lang = LanguageUtils.LANG_SPANISH;
      }
      else if (this.activeLanguage == 1) 
      {
         lang = LanguageUtils.LANG_CATALAN;
      }
      else if (this.activeLanguage == 2)
      {
         lang = LanguageUtils.LANG_ENGLISH;
      }

      return lang;
   }

   /**
    * Retorna la propiedad de configuración solicitada
    *
    * @param key Clave que identifica el texto.
    * @return Una cadena que corresponde a la clave solicitada.
    */
   public String getProperty(String key) 
   {
      return this.properties.getProperty(key);
   }

   private void loadFile(String languageCode) 
   {
      this.properties = new Properties();

      try 
      {
         final String propertiesFile = new File("").getAbsolutePath() + CONFIG_FILE_PATH + "message_" + languageCode + ".properties";
         properties.load(new FileInputStream(propertiesFile));
      } 
      catch (IOException ex) 
      {
         // LOG EXCEPTION
      }
   }
}
