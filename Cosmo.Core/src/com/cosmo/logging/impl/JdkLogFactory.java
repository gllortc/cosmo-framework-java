package com.cosmo.logging.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;

import com.cosmo.logging.LogFactory;
import com.cosmo.logging.Logger;

/**
 * Implementació del logger pel sistema de LOG del JDK de Java.<br />
 * Implementació basada en el projecte <strong>esl4j</strong>.
 * <br /><br />
 * Més informació: https://code.google.com/p/esl4j/
 * 
 * @author Gerard Llort
 */
public class JdkLogFactory implements LogFactory
{
   /**
    * Devuelve la implementación del <em>logger</em>.
    */
   @SuppressWarnings("rawtypes")
   @Override
   public Logger getLogger(Class className)
   {
      return new JdkLogger(java.util.logging.Logger.getLogger(className.getName()));
   }

   @Override
   public void setProperties(Map<String, String> props) 
   {
   }

   public static final class JdkLogger implements Logger 
   {
      private final java.util.logging.Logger logger;

      public JdkLogger(java.util.logging.Logger logger) 
      {
         this.logger = logger;
         
         try 
         {
            Properties properties = new Properties();
            // properties.setProperty(key, value);
            
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            properties.store(output, null);
            
            ByteArrayInputStream input = new ByteArrayInputStream(output.toByteArray());
            LogManager.getLogManager().readConfiguration(input);
         } 
         catch (IOException e) 
         {
            e.printStackTrace();
         }
      }

      public void log(String message, Level level) 
      {
         if (logger.isLoggable(level)) 
         {
            LogRecord log = create(level, message);
            logger.log(log);
         }
      }

      public void log(String message, Throwable error, Level level) 
      {
         if (logger.isLoggable(level)) 
         {
            LogRecord log = create(level, message);
            log.setThrown(error);
            logger.log(log);
         }
      }

      public void log(Throwable error, Level level)
      {
         if (logger.isLoggable(level))
         {
            LogRecord log = create(level, "");
            log.setThrown(error);
            logger.log(log);
         }
      }

      private LogRecord create(Level level, String message)
      {
         StackTraceElement[] stackTrace = new Throwable().getStackTrace();

         String logSourceClassName = null;
         String logSourceMethodName = null;

         int logSourceFrame = 3;
         if (logSourceFrame < stackTrace.length)
         {
            StackTraceElement logSource = stackTrace[logSourceFrame];
            if (logSource != null)
            {
               logSourceClassName = logSource.getClassName();
               logSourceMethodName = logSource.getMethodName();
            }
         }

         LogRecord log = new LogRecord(level, message);
         log.setSourceClassName(logSourceClassName == null ? "Unknown" : logSourceClassName);
         log.setSourceMethodName(logSourceMethodName == null ? "Unknown" : logSourceMethodName);

         return log;
      }

      public void debug(String message)
      {
         log(message, Level.FINER);
      }

      public void debug(String message, Throwable error)
      {
         log(message, error, Level.FINER);
      }

      public void debug(Throwable error)
      {
         log(error, Level.FINER);
      }

      public void error(String message)
      {
         log(message, Level.SEVERE);
      }

      public void error(String message, Throwable error)
      {
         log(message, error, Level.SEVERE);
      }

      public void error(Throwable error)
      {
         log(error, Level.SEVERE);
      }

      public void info(String message)
      {
         log(message, Level.INFO);
      }

      public void info(String message, Throwable error)
      {
         log(message, error, Level.INFO);
      }

      public void info(Throwable error)
      {
         log(error, Level.INFO);
      }

      public boolean isDebugEnabled()
      {
         return logger.isLoggable(Level.FINER);
      }

      public boolean isErrorEnabled() {
         return logger.isLoggable(Level.SEVERE);
      }

      public boolean isInfoEnabled()
      {
         return logger.isLoggable(Level.INFO);
      }

      public boolean isTraceEnabled()
      {
         return logger.isLoggable(Level.FINEST);
      }

      public boolean isVerboseEnabled()
      {
         return logger.isLoggable(Level.FINE);
      }

      public boolean isWarnEnabled()
      {
         return logger.isLoggable(Level.WARNING);
      }

      public void trace(String message) 
      {
         log(message, Level.FINEST);
      }

      public void trace(String message, Throwable error)
      {
         log(message, error, Level.FINEST);
      }

      public void trace(Throwable error) 
      {
         log(error, Level.FINEST);
      }

      public void warn(String message)
      {
         log(message, Level.WARNING);
      }

      public void warn(String message, Throwable error) 
      {
         log(message, error, Level.WARNING);
      }

      public void warn(Throwable error) 
      {
         log(error, Level.WARNING);
      }

      public void verbose(String message) 
      {
         log(message, Level.FINE);
      }

      public void verbose(String message, Throwable error) 
      {
         log(message, error, Level.FINE);
      }

      public void verbose(Throwable error) 
      {
         log(error, Level.FINE);
      }
   }

}
