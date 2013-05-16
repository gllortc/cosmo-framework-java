package com.cosmo.logging.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import com.cosmo.logging.LogFactory;
import com.cosmo.logging.Logger;

public class ConsoleLogFactory implements LogFactory
{
   public static enum Level
   {
      ERROR(1), WARN(2), INFO(3), VERBOSE(4), DEBUG(5), TRACE(6);

      private final int level;

      Level(int level) 
      {
         this.level = level;
      }

      public int level()
      {
         return level;
      }
   }

   private Map<String, String> props;
   private static Level level = Level.INFO;

   @SuppressWarnings("rawtypes")
   @Override
   public Logger getLogger(Class clazz)
   {
      return new ConsoleLogger(clazz.getName());
   }

   @Override
   public void setProperties(Map<String, String> props)
   {
      this.props = props;
      afterPropertiesSet();
   }
   
   public static final class ConsoleLogger implements Logger
   {
      private final String name;

      public ConsoleLogger(String name)
      {
         this.name = name;
      }

      private synchronized void log(String message, Level lvl)
      {
         if (level.level() >= lvl.level())
         {
            String msg = "[" + name + "] " + message;
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
            System.out.println("[" + sdf.format(new Date()) + "] " + msg);
         }
      }

      private synchronized void log(String message, Throwable error, Level lvl) 
      {
         StringBuffer buff = new StringBuffer();

         StackTraceElement[] ste = error.getStackTrace();
         for (StackTraceElement s : ste) 
         {
            buff.append(s.toString() + "\n");
         }

         String msg = "[" + name + "] " + message + " : " + error.getMessage() + "\n" + buff.toString();
         log(msg, lvl);
      }

      public void debug(String message) 
      {
         log(message, Level.DEBUG);
      }

      public void debug(String message, Throwable error) 
      {
         log(message, error, Level.DEBUG);
      }

      public void debug(Throwable error) 
      {
         log("", error, Level.DEBUG);
      }

      public void error(String message)
      {
         log(message, Level.ERROR);
      }

      public void error(String message, Throwable error)
      {
         log(message, error, Level.ERROR);
      }

      public void error(Throwable error)
      {
         log("", error, Level.ERROR);
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
         log("", error, Level.INFO);
      }

      public boolean isDebugEnabled()
      {
         return level.level() >= Level.DEBUG.level();
      }

      public boolean isErrorEnabled()
      {
         return level.level() >= Level.ERROR.level();
      }

      public boolean isInfoEnabled()
      {
         return level.level() >= Level.INFO.level();
      }

      public boolean isTraceEnabled()
      {
         return level.level() >= Level.TRACE.level();
      }

      public boolean isVerboseEnabled()
      {
         return level.level() >= Level.VERBOSE.level();
      }

      public boolean isWarnEnabled()
      {
         return level.level() >= Level.WARN.level();
      }

      public void trace(String message)
      {
         log(message, Level.TRACE);
      }

      public void trace(String message, Throwable error) 
      {
         log(message, error, Level.TRACE);
      }

      public void trace(Throwable error) 
      {
         log("", error, Level.TRACE);
      }

      public void warn(String message)
      {
         log(message, Level.WARN);
      }

      public void warn(String message, Throwable error) 
      {
         log(message, error, Level.WARN);
      }

      public void warn(Throwable error)
      {
         log("", error, Level.WARN);
      }

      public void verbose(String message)
      {
         log(message, Level.VERBOSE);
      }

      public void verbose(String message, Throwable error)
      {
         log(message, error, Level.VERBOSE);
      }

      public void verbose(Throwable error)
      {
         log("", error, Level.VERBOSE);
      }
   }

   private void afterPropertiesSet() 
   {
      if (props.containsKey("level"))
      {
         level = Level.valueOf(props.get("level").toUpperCase());
         if (level == null)
         {
            level = Level.INFO;
         }
      }
   }
}
