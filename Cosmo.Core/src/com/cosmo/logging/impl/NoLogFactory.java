package com.cosmo.logging.impl;

import java.util.Map;

import com.cosmo.logging.LogFactory;
import com.cosmo.logging.Logger;

public class NoLogFactory implements LogFactory 
{

   @SuppressWarnings("rawtypes")
   @Override
   public Logger getLogger(Class clazz) 
   {
      return new NoLogger();
   }

   @Override
   public void setProperties(Map<String, String> props) { }
   
   public static final class NoLogger implements Logger 
   {
      public void debug(String message) { }

      public void debug(String msg, Throwable error) { }

      public void debug(Throwable error) { }

      public void error(String message) { }

      public void error(String msg, Throwable error) { }

      public void error(Throwable error) { }

      public void info(String message) { }

      public void info(String msg, Throwable error) { }

      public void info(Throwable error) { }

      public boolean isDebugEnabled() 
      {
          return false;
      }

      public boolean isErrorEnabled() 
      {
          return false;
      }

      public boolean isInfoEnabled()
      {
          return false;
      }

      public boolean isTraceEnabled()
      {
          return false;
      }

      public boolean isVerboseEnabled()
      {
          return false;
      }

      public boolean isWarnEnabled()
      {
          return false;
      }

      public void trace(String message) { }

      public void trace(String msg, Throwable error) { }

      public void trace(Throwable error) { }

      public void warn(String message) { }

      public void warn(String msg, Throwable error) { }

      public void warn(Throwable error) { }

      public void verbose(String message) { }

      public void verbose(String msg, Throwable error) { }

      public void verbose(Throwable error) { }
  }

}
