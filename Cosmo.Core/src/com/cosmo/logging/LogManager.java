package com.cosmo.logging;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.cosmo.logging.impl.ConsoleLogFactory;
import com.cosmo.logging.impl.NoLogFactory;

public class LogManager 
{
   public static final String CONFIG_BUNDLE = "esl4j.properties";
   private static LogFactory logFactory = null;
   private static final Object lock = new Object();
   private static Properties config = new Properties();
   private static ClassLoader factoryClassloader;

   /**
    * Returns the configurered LogFactory instance
    * 
    * @return LogFactory
    */
   public static LogFactory getFactory() 
   {
      synchronized (lock) 
      {
         if (logFactory == null)
         {
            createFactory();
         }
      }

      return logFactory;
   }

   private static void createFactory() 
   {
      String className = System.getProperty(LogFactory.class.getName());
      
      if (className != null) 
      {
         LogFactory factory = createFactory(className);
         if (factory != null) 
         {
            logFactory = factory;
            return;
         }
      }

      try 
      {
         config.load(getCurrentClassLoader().getResourceAsStream(CONFIG_BUNDLE));
         className = config.getProperty(LogFactory.class.getName());
      } 
      catch (IOException e) 
      {
         className = ConsoleLogFactory.class.getName();
      }

      LogFactory factory = createFactory(className);
      if (factory != null) 
      {
         logFactory = factory;
         return;
      }

      logFactory = new NoLogFactory();
   }

   @SuppressWarnings("rawtypes")
   private static LogFactory createFactory(String className) 
   {
      try
      {
         if (factoryClassloader == null)
         {
            factoryClassloader = getCurrentClassLoader();
         }

         Class factoryClass = factoryClassloader.loadClass(className);
         Object factory = factoryClass.newInstance();

         if (!(factory instanceof LogFactory))
         {
            System.err.println("Class '" + className
                  + "' is not an implementation of "
                  + LogFactory.class.getName());
            return null;
         }

         LogFactory lf = (LogFactory) factory;
         lf.setProperties(loadProperties(className));

         return lf;
      } 
      catch (Throwable t) 
      {
         t.printStackTrace();
      }
      
      return null;
   }

   private static ClassLoader getCurrentClassLoader() 
   {
      ClassLoader classloader = Thread.currentThread().getContextClassLoader();
      if (classloader == null)
      {
         classloader = LogManager.class.getClassLoader();
      }

      return classloader;
   }

   private static Map<String, String> loadProperties(String className)
   {
      Enumeration<Object> e = config.keys();
      Map<String, String> props = new HashMap<String, String>();

      while (e.hasMoreElements())
      {
         String key = (String) e.nextElement();
         if (key.startsWith(className)) 
         {
            String val = config.getProperty(key);
            props.put(key.substring(className.length() + 1), val);
         }
      }

      return props;
   }

   /**
    * Sets the ClassLoader of the LogFactory implementation
    * 
    * @param ClassLoader fcl
    */
   public static void setFactoryClassLoader(ClassLoader fcl)
   {
      factoryClassloader = fcl;
   }

   /**
    * Sets the default LogFactory implementation
    * 
    * @param LogFactory newFactory
    */
   public static void setFactory(LogFactory newFactory) 
   {
      logFactory = newFactory;
   }

   /**
    * Returns the Logger instance
    * 
    * @param Class sourceClass
    * 
    * @return Logger
    */
   @SuppressWarnings("rawtypes")
   public static Logger getLogger(Class sourceClass)
   {
      return getFactory().getLogger(sourceClass);
   }
}
