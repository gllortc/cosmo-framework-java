package com.cosmo.logging;

/**
 * Interface que deben implementar los proveedores de LOG.
 * <br /><br />
 * Basado en el proyecto: <a href="https://code.google.com/p/esl4j/">https://code.google.com/p/esl4j/</a>
 * 
 * @author Gerard Llort
 */
public interface Logger 
{
   /**
    * Returns true if ERROR logging is enabled
    * 
    * @return boolean
    */
   public boolean isErrorEnabled();

   /**
    * Returns true if WARN logging is enabled
    * 
    * @return boolean
    */
   public boolean isWarnEnabled();

   /**
    * Returns true if INFO logging is enabled
    * 
    * @return boolean
    */
   public boolean isInfoEnabled();

   /**
    * Returns true if DEBUG logging is enabled
    * 
    * @return boolean
    */
   public boolean isDebugEnabled();

   /**
    * Returns true if VERBOSE logging is enabled
    * 
    * @return boolean
    */
   public boolean isVerboseEnabled();

   /**
    * Returns true if TRACE logging is enabled
    * 
    * @return boolean
    */
   public boolean isTraceEnabled();

   /**
    * Logs ERROR messages
    * 
    * @param message
    */
   public void error(String message);

   /**
    * Logs ERROR messages
    * 
    * @param message
    * @param error
    */
   public void error(String message, Throwable error);

   /**
    * Logs ERROR messages
    * 
    * @param error
    */
   public void error(Throwable error);

   /**
    * Logs WARN messages
    * 
    * @param message
    */
   public void warn(String message);

   /**
    * Logs WARN messages
    * 
    * @param message
    * @param error
    */
   public void warn(String message, Throwable error);

   /**
    * Logs WARN messages
    * 
    * @param error
    */
   public void warn(Throwable error);

   /**
    * Logs INFO messages
    * 
    * @param message
    */
   public void info(String message);

   /**
    * Logs INFO messages
    * 
    * @param message
    * @param error
    */
   public void info(String message, Throwable error);

   /**
    * Logs INFO messages
    * 
    * @param error
    */
   public void info(Throwable error);

   /**
    * Logs DEBUG messages
    * 
    * @param message
    */
   public void debug(String message);

   /**
    * Logs DEBUG messages
    * 
    * @param message
    * @param error
    */
   public void debug(String message, Throwable error);

   /**
    * Logs DEBUG messages
    * 
    * @param error
    */
   public void debug(Throwable error);

   /**
    * Logs VERBOSE messages
    * 
    * @param message
    */
   public void verbose(String message);

   /**
    * Logs VERBOSE messages
    * 
    * @param message
    * @param error
    */
   public void verbose(String message, Throwable error);

   /**
    * Logs VERBOSE messages
    * 
    * @param error
    */
   public void verbose(Throwable error);

   /**
    * Logs TRACE messages
    * 
    * @param message
    */
   public void trace(String message);

   /**
    * Logs TRACE messages
    * 
    * @param message
    * @param error
    */
   public void trace(String message, Throwable error);

   /**
    * Logs TRACE messages
    * 
    * @param error
    */
   public void trace(Throwable error);
}
