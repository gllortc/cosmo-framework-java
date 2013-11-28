package com.cosmo.logging;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

public class LogFactory 
{
   public static final String DEFAULT_PATTERN = "> %d [%p|%c|%C{1}] %m%n";

   // Declaración de variables locales
   private static boolean loggingIsInitialized = false;

   /**
    * Inicializa el servicio de <em>logger</em> de Cosmo.
    */
   public static void initialize()
   {
      // Realiza un reset del logger Log4J
      Logger.getRootLogger().getLoggerRepository().resetConfiguration();

      ConsoleAppender console = new ConsoleAppender(); //create appender
      console.setName("ConsoleLogger");
      console.setLayout(new PatternLayout(DEFAULT_PATTERN)); 
      console.setThreshold(Level.ALL);
      console.setImmediateFlush(true);
      console.activateOptions();
      
      //add appender to any Logger (here is root)
      Logger.getRootLogger().addAppender(console);
   
      FileAppender fa = new FileAppender();
      fa.setName("FileLogger");
      fa.setFile("C:\\Users\\usuari\\mylog.log");
      fa.setLayout(new PatternLayout(DEFAULT_PATTERN));
      fa.setThreshold(Level.DEBUG);
      fa.setAppend(true);
      fa.setImmediateFlush(true);
      fa.activateOptions();

      //add appender to any Logger (here is root)
      Logger.getRootLogger().addAppender(fa);

      loggingIsInitialized = true;
   }

   public static Logger getLogger(String className)
   {
      return Logger.getLogger(className);
   }

   public static Logger getLogger(Class<?> classObject)
   {
      return Logger.getLogger(classObject);
   }
}
