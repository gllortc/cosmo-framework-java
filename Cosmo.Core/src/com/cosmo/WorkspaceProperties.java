package com.cosmo;

import com.cosmo.data.DataConnection;
import com.cosmo.data.DataSource;
import com.cosmo.security.Agent;
import com.cosmo.util.IOUtils;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import javax.servlet.ServletContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Representa la configuración del workspace.
 */
public class WorkspaceProperties 
{
   public static final String PROPERTIES_FILENAME = "cosmo.config.xml";

   // Literales para los nodos del archivo de configuración
   // private static final String XML_NODE_ROOT = "cosmo-settings";
   private static final String XML_TAG_CONNECTION = "connection";
   private static final String XML_ATT_ID = "id";
   private static final String XML_ATT_JDBC_DRIVER = "jdbc.driver";
   private static final String XML_ATT_CORM_DRIVER = "corm.driver";
   private static final String XML_ATT_SERVER = "server";
   private static final String XML_ATT_PORT = "port";
   private static final String XML_ATT_SCHEMA = "schema";
   private static final String XML_ATT_USER = "user";
   private static final String XML_ATT_PASSWORD = "pwd";
   private static final String XML_ATT_KEY = "key";
   private static final String XML_ATT_VALUE = "value";
   private static final String XML_TAG_AUTHENTICATION = "authentication-agent";
   private static final String XML_TAG_AUTHORIZATION = "authorization-agent";
   private static final String XML_ATT_DRIVER = "driver";
   private static final String XML_TAG_PARAMETER = "param";
   
   private HashMap<String, String> properties;
   private HashMap<String, DataSource> dataSources;
   private HashMap<String, Agent> authenticationAgents;
   private HashMap<String, Agent> authorizationAgents;

   //==============================================
   // Constructors
   //==============================================
   
   /**
    * Constructor de la clase.
    * 
    * @param context Contexto del servidor web.
    * 
    * @throws ParserConfigurationException
    * @throws SAXException
    * @throws IOException 
    */
   public WorkspaceProperties(ServletContext context) throws WorkspaceLoadException
   {
      properties = new HashMap<String, String>();
      dataSources = new HashMap<String, DataSource>();
      authenticationAgents = new HashMap<String, Agent>();
      authorizationAgents = new HashMap<String, Agent>();
      
      loadConfig(context);
   }
   
   //==============================================
   // Properties
   //==============================================
   
   /**
    * Devuelve el número de propiedades de configuración leídas.
    */
   public int getNumProperties()
   {
      return this.properties.size();
   }
   
   /**
    * Devuelve el número de DataSources leídos.
    */
   public int getNumDatasources()
   {
      return this.dataSources.size();
   }
   
   //==============================================
   // Methods
   //==============================================
   
   /**
    * Obtiene el valor de configuración asociado a una clave.
    * 
    * @param key Clave asociada al valor deseado.
    * @return Devuelve una cadena de texto que corresponde al valor asociado a la clave especificada.
    */
   public String getString(String key) 
   {
      return properties.get(key);
   }
   
   /**
    * Obtiene el valor de configuración asociado a una clave.
    * 
    * @param key Clave asociada al valor deseado.
    * @return Devuelve una cadena de texto que corresponde al valor asociado a la clave especificada.
    */
   public Integer getInteger(String key, Integer defaultNum) 
   {
      String snum = properties.get(key);
      
      try
      {
         return Integer.valueOf(snum);
      }
      catch (NumberFormatException ex)
      {      
         return defaultNum;
      }
   }
   
   /**
    * Obtiene el valor de configuración asociado a una clave.
    * 
    * @param key Clave asociada al valor deseado.
    * @return Devuelve una cadena de texto que corresponde al valor asociado a la clave especificada.
    */
   public Boolean getBoolean(String key) 
   {
      String snum = properties.get(key);
      return Boolean.valueOf(snum);
   }
   
   /**
    * Obtiene el valor de configuración asociado a una clave.
    * 
    * @param key Clave asociada al valor deseado.
    * @return Devuelve una cadena de texto que corresponde al valor asociado a la clave especificada.
    */
   @Deprecated
   public String getWorkspaceProperty(String key) 
   {
      return properties.get(key);
   }
   
   /**
    * Obtiene la consexión al servidor Cosmo.
    * 
    * @return Una instancia de {@link DataSource} que contiene los parámetros de conexión a la base de datos.
    */
   public DataSource getDataSource()
   {
      return getDataSource(DataConnection.CONNECTION_SERVER);
   }
   
   /**
    * Obtiene una conxeión a un orígen de datos.
    * 
    * @param key Clave identificativa de la conexión.
    * @return Una instancia de {@link DataSource} que contiene los parámetros de conexión a la base de datos.
    */
   public DataSource getDataSource(String key)
   {
      return this.dataSources.get(key);
   }
   
   //==============================================
   // Private members
   //==============================================
   
   /**
    * Carga la configuración del workspace. 
    * 
    * @param context Contexto del servidor web.
    * 
    * @throws ParserConfigurationException
    * @throws SAXException
    * @throws IOException
    */
   private void loadConfig(ServletContext context) throws WorkspaceLoadException
   {
      Node nNode;
      Node pNode;
      Element eElement;
      Element pElement;
      NodeList nList;
      NodeList pList;
      DataSource ds;
      Agent agent;
      InputStream is = null;
      
      try
      {
         is = new FileInputStream(context.getRealPath("/" + WorkspaceProperties.PROPERTIES_FILENAME));

         DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
         DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
         Document doc = dBuilder.parse(is);
         doc.getDocumentElement().normalize();

         // Obtiene los valores de configuración clave/valor
         nList = doc.getElementsByTagName(WorkspaceProperties.XML_TAG_PARAMETER);
         for (int temp = 0; temp < nList.getLength(); temp++) 
         {
            nNode = nList.item(temp);
            if (nNode.getNodeType() == Node.ELEMENT_NODE)
            {
               eElement = (Element) nNode;
               String name = eElement.getAttribute(WorkspaceProperties.XML_ATT_KEY);
               String val = eElement.getAttribute(WorkspaceProperties.XML_ATT_VALUE); 

               this.properties.put(name, val);
            }
         }

         // Obtiene las conexiones a base de datos
         nList = doc.getElementsByTagName(WorkspaceProperties.XML_TAG_CONNECTION);
         for (int temp = 0; temp < nList.getLength(); temp++) 
         {
            nNode = nList.item(temp);
            if (nNode.getNodeType() == Node.ELEMENT_NODE)
            {
               eElement = (Element) nNode;

               ds = new DataSource();
               ds.setId(eElement.getAttribute(WorkspaceProperties.XML_ATT_ID));
               ds.setJdbcDriver(eElement.getAttribute(WorkspaceProperties.XML_ATT_JDBC_DRIVER));
               ds.setCormDriver(eElement.getAttribute(WorkspaceProperties.XML_ATT_CORM_DRIVER));
               ds.setHost(eElement.getAttribute(WorkspaceProperties.XML_ATT_SERVER));
               ds.setPort(eElement.getAttribute(WorkspaceProperties.XML_ATT_PORT));
               ds.setSchema(eElement.getAttribute(WorkspaceProperties.XML_ATT_SCHEMA));
               ds.setLogin(eElement.getAttribute(WorkspaceProperties.XML_ATT_USER));
               ds.setPassword(eElement.getAttribute(WorkspaceProperties.XML_ATT_PASSWORD));

               this.dataSources.put(ds.getId(), ds);
            }
         }
         
         // Obtiene todos los agentes de autenticación
         nList = doc.getElementsByTagName(WorkspaceProperties.XML_TAG_AUTHENTICATION);
         for (int temp = 0; temp < nList.getLength(); temp++) 
         {
            nNode = nList.item(temp);
            if (nNode.getNodeType() == Node.ELEMENT_NODE)
            {
               eElement = (Element) nNode;

               agent = new Agent();
               agent.setId(eElement.getAttribute(WorkspaceProperties.XML_ATT_ID));
               agent.setModuleClass(eElement.getAttribute(WorkspaceProperties.XML_ATT_DRIVER));
               
               pList = eElement.getElementsByTagName(WorkspaceProperties.XML_TAG_PARAMETER);
               for (int pNum = 0; pNum < pList.getLength(); pNum++) 
               {
                  pNode = pList.item(pNum);
                  if (pNode.getNodeType() == Node.ELEMENT_NODE)
                  {
                     pElement = (Element) pNode;
                     agent.setParam(pElement.getAttribute(WorkspaceProperties.XML_ATT_KEY), 
                                    pElement.getAttribute(WorkspaceProperties.XML_ATT_VALUE));
                  }
               }
               
               this.authenticationAgents.put(agent.getId(), agent);
            }
         }
         
         // Obtiene todos los agentes de autorización
         nList = doc.getElementsByTagName(WorkspaceProperties.XML_TAG_AUTHORIZATION);
         for (int temp = 0; temp < nList.getLength(); temp++) 
         {
            nNode = nList.item(temp);
            if (nNode.getNodeType() == Node.ELEMENT_NODE)
            {
               eElement = (Element) nNode;

               agent = new Agent();
               agent.setId(eElement.getAttribute(WorkspaceProperties.XML_ATT_ID));
               agent.setModuleClass(eElement.getAttribute(WorkspaceProperties.XML_ATT_DRIVER));
               
               pList = eElement.getElementsByTagName(WorkspaceProperties.XML_TAG_PARAMETER);
               for (int pNum = 0; pNum < pList.getLength(); pNum++) 
               {
                  pNode = pList.item(pNum);
                  if (pNode.getNodeType() == Node.ELEMENT_NODE)
                  {
                     pElement = (Element) pNode;
                     agent.setParam(pElement.getAttribute(WorkspaceProperties.XML_ATT_KEY), 
                                    pElement.getAttribute(WorkspaceProperties.XML_ATT_VALUE));
                  }
               }
               
               this.authorizationAgents.put(agent.getId(), agent);
            }
         }
         
         is.close();
      }
      catch (ParserConfigurationException ex)
      {
         throw new WorkspaceLoadException(ex.getMessage(), ex);
      }
      catch (SAXException ex)
      {
         throw new WorkspaceLoadException(ex.getMessage(), ex);
      }
      catch (IOException ex)
      {
         throw new WorkspaceLoadException(ex.getMessage(), ex);
      }
      finally
      {
         IOUtils.closeStream(is);
      }
   }
}
