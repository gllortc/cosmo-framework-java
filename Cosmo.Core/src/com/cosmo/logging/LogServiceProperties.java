package com.cosmo.logging;

import java.util.Collection;
import java.util.HashMap;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import com.cosmo.WorkspaceProperties;
import com.cosmo.util.PluginProperties;

/**
 * Contenedor encargado de leer y almacenar toda la configuración de Logging Services.
 * 
 * @author Gerard Llort
 */
public class LogServiceProperties
{
   // Definición de tags y atributos para Communication Services
   private static final String XML_TAG_AGENTS = "logging-services";
   private static final String XML_TAG_AGENT = "logger";

   // Declaración de variables locales
   private HashMap<String, PluginProperties> loggers;


   //==============================================
   // Constructors
   //==============================================

   /**
    * Constructor de la clase {@link LogServiceProperties}.
    *  
    * @param xmlDocument Una instancia de {@link Document} que representa el archivo XML de configuración.
    */
   public LogServiceProperties(Document xmlDocument)
   {
      loadProperties(xmlDocument);
   }


   //==============================================
   // Properties
   //==============================================

   /**
    * Devuelve la colección de <em>loggers</em> que se deben registrar en el servicio.
    */
   public Collection<PluginProperties> getLoggingAgents()
   {
      return this.loggers.values();
   }

   /**
    * Devuelve el número de <em>loggers</em> que se deben registrar en el servicio.
    */
   public int getLoggingAgentsCount()
   {
      return this.loggers.size();
   }

   //==============================================
   // Methods
   //==============================================

   /**
    * Obtiene un determinado <em>logger</em>.
    * 
    * @param logAgentId Identificador del <em>logger</em> a obtener.
    * 
    * @return Una instancia de {@link PluginProperties} que contiene la información del <em>logger</em> especificado o {@code null} en cualquier otro caso.
    */
   public PluginProperties getLoggingAgent(String logAgentId)
   {
      return this.loggers.get(logAgentId);
   }

   /**
    * Lee y almacena las propiedades de configuración de Data Services.
    * 
    * @param doc Una instancia de {@link Document} que contiene la configuración (XML) de la aplicación.
    */
   public void loadProperties(Document doc)
   {
      NodeList nList;

      nList = doc.getElementsByTagName(LogServiceProperties.XML_TAG_AGENTS);
      if (nList.getLength() >= 1)
      {
         // Obtiene todos los agentes de comunicaciones
         this.loggers = WorkspaceProperties.readPluginsByType(doc, LogServiceProperties.XML_TAG_AGENT);
      }
   }
}
