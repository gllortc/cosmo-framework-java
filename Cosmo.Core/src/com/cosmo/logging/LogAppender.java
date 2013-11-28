package com.cosmo.logging;

import org.apache.log4j.Appender;
import org.apache.log4j.Level;

import com.cosmo.util.PluginProperties;

/**
 * Clase base para desarrollar Loggers (appenders) para el Logging Services.
 * 
 * @author Gerard Llort
 */
public abstract class LogAppender
{
   /**
    * Patrón de representación por defecto.<br />
    * @see <a href="http://logging.apache.org/log4j/2.x/manual/layouts.html">http://logging.apache.org/log4j/2.x/manual/layouts.html</a>
    */
   public static final String DEFAULT_PATTERN = "> %d [%p|%c|%C{1}] %m%n";

   // Declaración de variables locales
   private PluginProperties properties;


   //==============================================
   // Constructors
   //==============================================

   /**
    * Constructor de la clase {@link LogAppender}.
    * 
    * @param properties Una instancia de {@link PluginProperties} que contiene los parámetros de configuración del agente de <em>logging</em>.
    */
   public LogAppender(PluginProperties properties)
   {
      this.properties = properties;
   }


   //==============================================
   // Methods
   //==============================================

   /**
    * Devuelve una instancia de {@link PluginProperties} que contiene los parámetros de configuración del agente de <em>logging</em>.
    */
   public PluginProperties getProperties()
   {
      return this.properties;
   }

   /**
    * Devuelve una instancia de {@link Appender} que será usada por <em>Log4j</em>.
    */
   public abstract Appender getAppender();


   //==============================================
   // Static Members
   //==============================================

   /**
    * Convierte una cadena de texto en un nivel de log.<br />
    * Cadenas soportadas: ALL, DEBUG, ERROR, FATAL, INFO, OFF, TRACE, WARN 
    */
   public static Level stringToLevel(String level)
   {
      level = level.trim().toUpperCase();
      
      if (level.equals("ALL"))
      {
         return Level.ALL;
      }
      else if (level.equals("DEBUG"))
      {
         return Level.DEBUG;
      }
      else if (level.equals("ERROR"))
      {
         return Level.ERROR;
      }
      else if (level.equals("FATAL"))
      {
         return Level.FATAL;
      }
      else if (level.equals("INFO"))
      {
         return Level.INFO;
      }
      else if (level.equals("OFF"))
      {
         return Level.OFF;
      }
      else if (level.equals("TRACE"))
      {
         return Level.TRACE;
      }
      else if (level.equals("WARN"))
      {
         return Level.WARN;
      }

      return Level.OFF;
   }
}
