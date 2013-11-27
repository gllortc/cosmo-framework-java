package com.cosmo.comm;

import java.util.HashMap;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.cosmo.WorkspaceProperties;
import com.cosmo.util.PluginProperties;
import com.cosmo.util.StringUtils;

/**
 * Contenedor encargado de leer y almacenar toda la configuración de Communication Services.
 * 
 * @author Gerard Llort
 */
public class CommServiceProperties
{
   // Definición de tags y atributos para Communication Services
   private static final String XML_TAG_AGENTS = "communications-service";
   private static final String XML_TAG_AGENT = "comm-agent";

   private static final String XML_ATT_DEFAULTDRIVER = "communication-agent";

   // Declaración de variables locales
   private String commAgentId;
   private HashMap<String, PluginProperties> commAgents;


   //==============================================
   // Constructors
   //==============================================

   /**
    * Constructor de la clase {@link CommServiceProperties}.
    *  
    * @param xmlDocument Una instancia de {@link Document} que representa el archivo XML de configuración.
    */
   public CommServiceProperties(Document xmlDocument)
   {
      loadProperties(xmlDocument);
   }


   //==============================================
   // Properties
   //==============================================
   
   /**
    * Obtiene el identificador del agente de comunicaciones a usar por defecto.
    * 
    * @return Una cadena que contiene el identificador del agente de comunicaciones a usar por defecto.
    */
   public String getDefaultCommunicationsAgentId()
   {
      return this.commAgentId;
   }


   //==============================================
   // Methods
   //==============================================

   /**
    * Obtiene el agente de comunicación por defecto.
    * 
    * @return Una instancia de {@link PluginProperties} que contiene la información del agente de comunicación a usar o {@code null} si no se ha configurado.
    */
   public PluginProperties getCommunicationAgent()
   {
      if (StringUtils.isNullOrEmptyTrim(this.commAgentId))
      {
         return null;
      }

      return this.getCommunicationAgent(this.commAgentId);
   }

   /**
    * Obtiene un determinado agente de comunicación.
    * 
    * @param commAgentId Identificador del agente de comunicaciones a obtener.
    * 
    * @return Una instancia de {@link PluginProperties} que contiene la información del agente de comunicación especificado o {@code null} en cualquier otro caso.
    */
   public PluginProperties getCommunicationAgent(String commAgentId)
   {
      return this.commAgents.get(commAgentId);
   }

   /**
    * Lee y almacena las propiedades de configuración de Data Services.
    * 
    * @param doc Una instancia de {@link Document} que contiene la configuración (XML) de la aplicación.
    */
   public void loadProperties(Document doc)
   {
      Node nNode;
      Element eElement;
      NodeList nList;

      nList = doc.getElementsByTagName(CommServiceProperties.XML_TAG_AGENTS);
      if (nList.getLength() >= 1)
      {
         nNode = nList.item(0);
         if (nNode.getNodeType() == Node.ELEMENT_NODE)
         {
            eElement = (Element) nNode;

            this.commAgentId = eElement.getAttribute(CommServiceProperties.XML_ATT_DEFAULTDRIVER);
         }

         // Obtiene todos los agentes de comunicaciones
         this.commAgents = WorkspaceProperties.readPluginsByType(doc, CommServiceProperties.XML_TAG_AGENT);
      }
   }
}
