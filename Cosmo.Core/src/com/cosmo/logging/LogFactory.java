package com.cosmo.logging;

import java.util.Map;

public interface LogFactory 
{
   /**
    * Returns the Logger instance
    * 
    * @param clazz
    * @return Logger
    */
   @SuppressWarnings("rawtypes")
   public Logger getLogger(Class clazz);

   /**
    * Set any factory specific properties.
    * 
    * The properties can also be set in the esl4j.properties file. Below is an
    * example on how to set the properties in the config file:
    * 
    * <pre>
    *   org.kamranzafar.esl4j.LogFactory=myapp.MyLogFactory
    *   myapp.MyLogFactory.property1=value1
    *   myapp.MyLogFactory.property2=value2
    * </pre>
    * 
    * @param props
    */
   public void setProperties(Map<String, String> props);
}
