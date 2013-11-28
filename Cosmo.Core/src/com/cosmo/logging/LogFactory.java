package com.cosmo.logging;

import org.apache.log4j.Logger;

import com.cosmo.Workspace;
import com.cosmo.util.PluginProperties;

public class LogFactory 
{
   public static final String DEFAULT_PATTERN = "> %d [%p|%c|%C{1}] %m%n";

   // Declaración de variables locales
   private static boolean loggingIsInitialized = false;


   //==============================================
   // Static Properties
   //==============================================

   /**
    * @return the loggingIsInitialized
    */
   public static boolean isLoggingIsInitialized()
   {
      return loggingIsInitialized;
   }

   /**
    * @param loggingIsInitialized the loggingIsInitialized to set
    */
   public static void setLoggingIsInitialized(boolean loggingIsInitialized)
   {
      LogFactory.loggingIsInitialized = loggingIsInitialized;
   }


   //==============================================
   // Static Methods
   //==============================================

   /**
    * Inicializa el servicio de <em>logger</em> de Cosmo.
    * 
    * @param workspace Una instancia de {@link Workspace} que representa el workspace actual.
    */
   public static void initialize(Workspace workspace)
   {
      LogAppender appender;
      
      if (!isLoggingIsInitialized())
      {
         // Realiza un reset del logger Log4J
         Logger.getRootLogger().getLoggerRepository().resetConfiguration();

         for (PluginProperties logger : workspace.getProperties().getLogProperties().getLoggingAgents())
         {
            appender = getAppenderInstance(logger);

            if (appender != null)
            {
               Logger.getRootLogger().addAppender(appender.getAppender());
            }
         }

         setLoggingIsInitialized(true);
      }
   }

   /**
    * Obtiene el <em>logger</em> listo para usar.
    * 
    * @param className Nombre de la clase a la que se agrega el <em>logger</em>.
    * 
    * @return Una instancia de {@link Logger} lista para usar en cualquier clase.
    */
   public static Logger getLogger(String className)
   {
      return Logger.getLogger(className);
   }

   /**
    * Obtiene el <em>logger</em> listo para usar.
    * 
    * @param classObject Una clase que representa la clase dónde se ha invocado el <em>logger</em>.
    * 
    * @return Una instancia de {@link Logger} lista para usar en cualquier clase.
    */
   public static Logger getLogger(Class<?> classObject)
   {
      return Logger.getLogger(classObject);
   }


   //==============================================
   // Private Members
   //==============================================

   /**
    * Genera una instancia del <em>appender</em> a partir de una definición de agente de logging.
    *  
    * @param properties Una instancia de {@link PluginProperties} que contiene la definición y configuración
    *   del <em>appender</em>.
    * 
    * @return Una instancia de {@link LogAppender} si la definición es correcta o {@code null} si falla la 
    *   creación de la instancia.
    */
   private static LogAppender getAppenderInstance(PluginProperties properties)
   {
      String className;
      Class<?> cls;

      try
      {
         // Obtiene la clase
         className = properties.getModuleClass();
         cls = Class.forName(className);

         // Configura los tipos y valores de los argumentos
         Class<?>[] types = { PluginProperties.class };
         Object[] arguments = { properties };

         // Genera la instancia y la devuelve
         return (LogAppender) cls.getDeclaredConstructor(types).newInstance(arguments);
      } 
      catch (Exception ex)
      {
         return null;
      }
   }
}
