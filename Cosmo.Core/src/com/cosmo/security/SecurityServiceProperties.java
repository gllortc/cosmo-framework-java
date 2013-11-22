package com.cosmo.security;

import java.util.HashMap;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.cosmo.WorkspaceProperties;
import com.cosmo.util.PluginProperties;
import com.cosmo.util.StringUtils;

/**
 * Contenedor encargado de leer y almacenar toda la configuración de Security Services.
 * 
 * @author Gerard Llort
 */
public class SecurityServiceProperties
{
   private static final String XML_TAG = "security-services";
   private static final String XML_TAG_AUTHENTICATION = "authentication-agent";
   private static final String XML_TAG_AUTHORIZATION = "authorization-agent";

   private static final String XML_ATT_AUTHENTICATIONAGENT = "authentication-agent";
   private static final String XML_ATT_AUTHORIZATIONAGENT = "authorization-agent";
   private static final String XML_ATT_LOGINPAGE = "login-page";

   // Declaración de variables locales para Security Services
   private String authenticationAgentId;
   private String authorizationAgentId;
   private String loginPage;
   private HashMap<String, PluginProperties> authenticationAgents;
   private HashMap<String, PluginProperties> authorizationAgents;


   //==============================================
   // Constructors
   //==============================================

   /**
    * Constructor de la clase {@link SecurityServiceProperties}.
    * 
    * @param xmlDocument Una instancia de {@link Document} que representa el documento XML de configuración de Cosmo.
    */
   public SecurityServiceProperties(Document xmlDocument)
   {
      initialize();

      loadProperties(xmlDocument);
   }


   //==============================================
   // Properties
   //==============================================

   /**
    * Devuelve el identificador del agente de autenticación a usar.
    */
   public String getAuthenticationAgentId()
   {
      return authenticationAgentId;
   }

   /**
    * Devuelve el identificador del agente de autorización a usar.
    */
   public String getAuthorizationAgentId()
   {
      return authorizationAgentId;
   }

   /**
    * Nombre de la página (servlet) que implementa la página de <em>login</em>.
    */
   public String getLoginPage()
   {
      return loginPage;
   }


   //==============================================
   // Methods
   //==============================================

   /**
    * Obtiene el agente de autenticación.
    * 
    * @return Una instancia de {@link PluginProperties} que contiene la información del agente de autenticación a 
    *    usar o {@code null} si no se ha configurado.
    */
   public PluginProperties getAuthenticationAgent()
   {
      if (StringUtils.isNullOrEmptyTrim(this.authenticationAgentId))
      {
         return null;
      }
      
      return this.authenticationAgents.get(this.authenticationAgentId);
   }

   /**
    * Obtiene el agente de autorización.
    *
    * @return Una instancia de {@link PluginProperties} que contiene la información del agente de autorización a 
    *    usar o {@code null} si no se ha configurado.
    */
   public PluginProperties getAuthorizationAgent()
   {
      if (StringUtils.isNullOrEmptyTrim(this.authorizationAgentId))
      {
         return null;
      }

      return this.authorizationAgents.get(this.authorizationAgentId);
   }

   /**
    * Lee y almacena las propiedades de configuración de UI Services.
    * 
    * @param doc Una instancia de {@link Document} que contiene la configuración (XML) de la aplicación.
    */
   public void loadProperties(Document doc)
   {
      Node nNode;
      Element eElement;
      NodeList nList;

      initialize();

      nList = doc.getElementsByTagName(SecurityServiceProperties.XML_TAG);
      if (nList.getLength() >= 1)
      {
         nNode = nList.item(0);
         if (nNode.getNodeType() == Node.ELEMENT_NODE)
         {
            eElement = (Element) nNode;

            this.authenticationAgentId = eElement.getAttribute(SecurityServiceProperties.XML_ATT_AUTHENTICATIONAGENT);
            this.authorizationAgentId = eElement.getAttribute(SecurityServiceProperties.XML_ATT_AUTHORIZATIONAGENT);
            this.loginPage = eElement.getAttribute(SecurityServiceProperties.XML_ATT_LOGINPAGE);
         }

         // Obtiene todos los agentes de autenticación
         this.authenticationAgents = WorkspaceProperties.readPluginsByType(doc, SecurityServiceProperties.XML_TAG_AUTHENTICATION);

         // Obtiene todos los agentes de autorización
         this.authorizationAgents = WorkspaceProperties.readPluginsByType(doc, SecurityServiceProperties.XML_TAG_AUTHORIZATION);
      }
   }


   //==============================================
   // Methods
   //==============================================

   /**
    * Inicializa la instancia.
    */
   private void initialize()
   {
      this.authenticationAgentId = "";
      this.authorizationAgentId = "";
      this.loginPage = "";
      this.authenticationAgents = new HashMap<String, PluginProperties>();
      this.authorizationAgents = new HashMap<String, PluginProperties>();
   }
}
