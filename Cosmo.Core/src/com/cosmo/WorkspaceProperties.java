package com.cosmo;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
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

import com.cosmo.data.DataSource;
import com.cosmo.data.lists.DynamicList;
import com.cosmo.data.lists.List;
import com.cosmo.data.lists.ListItem;
import com.cosmo.data.lists.StaticList;
import com.cosmo.data.orm.apps.OrmApplication;
import com.cosmo.structures.PluginProperties;
import com.cosmo.util.IOUtils;
import com.cosmo.util.StringUtils;

/**
 * Representa la configuración del workspace.
 */
public class WorkspaceProperties 
{
   public static final String PROPERTIES_FILENAME = "cosmo.config.xml";

   // Literales para los nodos del archivo de configuración
   private static final String XML_TAG_CONNECTIONS = "connections";
   private static final String XML_ATT_COSMOSERVER = "cosmo-server";
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
   private static final String XML_TAG_SECURITY = "security";
   private static final String XML_ATT_AUTHENTICATIONAGENT = "authentication-agent";
   private static final String XML_ATT_AUTHORIZATIONAGENT = "authorization-agent";
   private static final String XML_ATT_LOGINPAGE = "login-page";
   private static final String XML_TAG_CORM_APPS = "corm-apps";
   private static final String XML_TAG_CORM_APP = "corm-app";
   private static final String XML_ATT_CLASS = "class";
   private static final String XML_ATT_CONNECTION = "connection";
   private static final String XML_TAG_APPACTION = "app-action";
   private static final String XML_ATT_TYPE = "type";
   private static final String XML_ATT_TITLE = "title";
   private static final String XML_ATT_DESCRIPTION = "description";
   private static final String XML_TAG_DATALISTS = "data-lists";
   private static final String XML_TAG_STATICLIST = "static-list";
   private static final String XML_TAG_DYNAMICLIST = "dynamic-list";
   private static final String XML_TAG_STATICLISTITEM = "static-list-item";
   private static final String XML_ATT_DEFAULTVALUE = "default-value";
   private static final String XML_TAG_SQLSTATEMENT = "sql-statement";
   private static final String XML_TAG_COMM_AGENTS = "communications";
   private static final String XML_TAG_COMM_AGENT = "comm-agent";
   private static final String XML_ATT_COMM_DEFAULTDRIVER = "communication-agent";
   
   // Parámetros de configuración
   private HashMap<String, String> properties;
   
   // Datasources (conexiones a datos)
   private HashMap<String, DataSource> dataSources;
   private String serverDatasource;
   
   // Seguridad
   private String loginPage;
   private String authenticationAgentId;
   private String authorizationAgentId;
   private String commAgentId;
   private HashMap<String, PluginProperties> authenticationAgents;
   private HashMap<String, PluginProperties> authorizationAgents;
   private HashMap<String, PluginProperties> commAgents;
   private HashMap<String, OrmApplication> ormApps;
   private HashMap<String, List> ormLists;
      

   //==============================================
   // Constructors
   //==============================================
   
   /**
    * Constructor de la clase.
    * 
    * @param context Contexto del servidor web.
    * 
    * @throws WorkspaceLoadException
    */
   public WorkspaceProperties(ServletContext context) throws WorkspaceLoadException
   {
      initialize();
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
   
   /**
    * Obtiene el nombre de la conexión al servidor Cosmo.
    * 
    * @return Una cadena que contiene el nombre del datasource definido como servidor Cosmo.
    */
   public String getServerDataSourceName()
   {
      return this.serverDatasource;
   }
   
   /**
    * Obtiene la página de login.
    * 
    * @return Una cadena que contiene el nombre del servlet que actúa de página de login.
    */
   public String getLoginPage()
   {
      return this.loginPage;
   }

   /**
    * Obtiene el identificador del agente de comunicaciones a usar por defecto.
    * 
    * @return Una cadena que contiene el identificador del agente de comunicaciones a usar por defecto.
    */
   public String getDefaultCommunicationsAgentsId()
   {
      return this.commAgentId;
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
   public DataSource getServerDataSource()
   {
      if (StringUtils.isNullOrEmptyTrim(this.serverDatasource))
      {
         return null;
      }

      return getDataSource(this.serverDatasource);
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
    * Obtiene el agente de autenticación.
    * 
    * @return Una instancia de {@link PluginProperties} que contiene la información del agente de autenticación a usar o {@code null} si no se ha configurado.
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
    * @return Una instancia de {@link PluginProperties} que contiene la información del agente de autorización a usar o {@code null} si no se ha configurado.
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
    * Obtiene todas las aplicaciones CORM registradas.
    *
    * @return Una colección de instancias de {@link OrmApplication}.
    */
   public Collection<OrmApplication> getOrmApplications()
   {
      return this.ormApps.values();
   }

   /**
    * Obtiene la definición de una aplicación CORM.
    *
    * @param appId Identificador de la aplicación.
    *
    * @return Una instancia de {@link OrmApplication} que representa la aplicación.
    */
   public OrmApplication getOrmApplication(String appId)
   {
      return this.ormApps.get(appId);
   }

   /**
    * Obtiene una lista de datos.
    *
    * @param id Identificador único de la lista.
    *
    * @return Una instancia de {@link List} que representa la lista de opciones.
    */
   public List getDataList(String id)
   {
      return this.ormLists.get(id);
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
      Element eElement;
      NodeList nList;
      DataSource ds;
      InputStream iStream = null;

      try
      {
         iStream = new FileInputStream(context.getRealPath("/" + WorkspaceProperties.PROPERTIES_FILENAME));

         DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
         DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
         Document doc = dBuilder.parse(iStream);
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

         // Obtiene la información de conexiones a base de datos
         nList = doc.getElementsByTagName(WorkspaceProperties.XML_TAG_CONNECTIONS);
         if (nList.getLength() >= 1)
         {
            // Obtiene la configuración
            nNode = nList.item(0);
            if (nNode.getNodeType() == Node.ELEMENT_NODE)
            {
               eElement = (Element) nNode;
               this.serverDatasource = eElement.getAttribute(WorkspaceProperties.XML_ATT_COSMOSERVER);
            }

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
         }

         // Obtiene la información de seguridad
         nList = doc.getElementsByTagName(WorkspaceProperties.XML_TAG_SECURITY);
         if (nList.getLength() >= 1)
         {
            // Obtiene la configuración
            nNode = nList.item(0);
            if (nNode.getNodeType() == Node.ELEMENT_NODE)
            {
               eElement = (Element) nNode;

               this.authenticationAgentId = eElement.getAttribute(WorkspaceProperties.XML_ATT_AUTHENTICATIONAGENT);
               this.authorizationAgentId = eElement.getAttribute(WorkspaceProperties.XML_ATT_AUTHORIZATIONAGENT);
               this.loginPage = eElement.getAttribute(WorkspaceProperties.XML_ATT_LOGINPAGE);
            }

            // Obtiene todos los agentes de autenticación
            this.authenticationAgents = readPluginsByType(doc, WorkspaceProperties.XML_TAG_AUTHENTICATION);

            // Obtiene todos los agentes de autorización
            this.authorizationAgents = readPluginsByType(doc, WorkspaceProperties.XML_TAG_AUTHORIZATION);
         }

         // Obtiene la información de comunicaciones
         nList = doc.getElementsByTagName(WorkspaceProperties.XML_TAG_COMM_AGENTS);
         if (nList.getLength() >= 1)
         {
            // Obtiene la configuración
            nNode = nList.item(0);
            if (nNode.getNodeType() == Node.ELEMENT_NODE)
            {
               eElement = (Element) nNode;

               this.commAgentId = eElement.getAttribute(WorkspaceProperties.XML_ATT_COMM_DEFAULTDRIVER);
            }

            // Obtiene todos los agentes de autenticación
            this.commAgents = readPluginsByType(doc, WorkspaceProperties.XML_TAG_COMM_AGENT);
         }

         // Obtiene las listas de datos
         this.ormLists = readDataLists(doc);

         // Obtiene la configuración de las aplicaciones ORM
         this.ormApps = readOrmApps(doc);

         iStream.close();
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
         IOUtils.closeStream(iStream);
      }
   }

   /**
    * Lee todas las definiciones de plugin de un determinado tipo.
    *
    * @param doc Una instancia de {@link Document} que representa el documento XML.
    * @param pluginTag Una cadena que contiene el nombre del TAG que reciben todas las definiciones del tipo de plugin a leer.
    *
    * @return Una instancia de {@link HashMap} que contiene las definiciones de plugin recopiladas.
    */
   private HashMap<String, PluginProperties> readPluginsByType(Document doc, String pluginTag)
   {
      Node attribNode;
      NodeList attribList;
      Element pluginElement;
      Element attribElement;
      PluginProperties plugin;

      HashMap<String, PluginProperties> plugins = new HashMap<String, PluginProperties>();

      NodeList pluginList = doc.getElementsByTagName(pluginTag);
      for (int pidx = 0; pidx < pluginList.getLength(); pidx++)
      {
         Node pluginNode = pluginList.item(pidx);
         if (pluginNode.getNodeType() == Node.ELEMENT_NODE)
         {
            pluginElement = (Element) pluginNode;

            plugin = new PluginProperties();
            plugin.setId(pluginElement.getAttribute(WorkspaceProperties.XML_ATT_ID));
            plugin.setModuleClass(pluginElement.getAttribute(WorkspaceProperties.XML_ATT_DRIVER));

            attribList = pluginElement.getElementsByTagName(WorkspaceProperties.XML_TAG_PARAMETER);
            for (int aidx = 0; aidx < attribList.getLength(); aidx++) 
            {
               attribNode = attribList.item(aidx);
               if (attribNode.getNodeType() == Node.ELEMENT_NODE)
               {
                  attribElement = (Element) attribNode;
                  plugin.setParam(attribElement.getAttribute(WorkspaceProperties.XML_ATT_KEY), 
                                  attribElement.getAttribute(WorkspaceProperties.XML_ATT_VALUE));
               }
            }
            
            plugins.put(plugin.getId(), plugin);
         }
      }

      return plugins;
   }

   /**
    * Lee todas las definiciones de listas de datos.
    *
    * @param doc Una instancia de {@link Document} que representa el documento XML.
    *
    * @return Una instancia de {@link HashMap} que contiene las definiciones de listas de datos recopiladas.
    */
   private HashMap<String, List> readDataLists(Document doc)
   {
      Node attribNode;
      Node listNode;
      NodeList attribList;
      NodeList listDefs;
      Element listElement;
      Element attribElement;
      StaticList sList = null;
      DynamicList dList = null;
      ListItem item;

      // Inicializa el contenedor de listas
      HashMap<String, List> lists = new HashMap<String, List>();

      // Comprueba si existe la definición
      attribList = doc.getElementsByTagName(WorkspaceProperties.XML_TAG_DATALISTS);
      if (attribList.getLength() < 1)
      {
         return lists;
      }

      // Carga las listas estáticas
      listDefs = doc.getElementsByTagName(WorkspaceProperties.XML_TAG_STATICLIST);
      for (int pidx = 0; pidx < listDefs.getLength(); pidx++)
      {
         listNode = listDefs.item(pidx);
         if (listNode.getNodeType() == Node.ELEMENT_NODE)
         {
            listElement = (Element) listNode;

            sList = new StaticList(listElement.getAttribute(WorkspaceProperties.XML_ATT_ID));

            attribList = listElement.getElementsByTagName(WorkspaceProperties.XML_TAG_STATICLISTITEM);
            for (int aidx = 0; aidx < attribList.getLength(); aidx++) 
            {
               attribNode = attribList.item(aidx);
               if (attribNode.getNodeType() == Node.ELEMENT_NODE)
               {
                  attribElement = (Element) attribNode;
                  item = new ListItem(attribElement.getAttribute(WorkspaceProperties.XML_ATT_VALUE),
                                      attribElement.getAttribute(WorkspaceProperties.XML_ATT_TITLE));

                  if (!StringUtils.isNullOrEmptyTrim(attribElement.getAttribute(WorkspaceProperties.XML_ATT_DEFAULTVALUE)))
                  {
                     item.setDefault(attribElement.getAttribute(WorkspaceProperties.XML_ATT_DEFAULTVALUE).equals("true") ||
                                     attribElement.getAttribute(WorkspaceProperties.XML_ATT_DEFAULTVALUE).equals("1"));
                  }

                  sList.addListItem(item);
               }
            }

            lists.put(sList.getId(), sList);
         }
      }

      // Carga las listas dinámicas SQL
      listDefs = doc.getElementsByTagName(WorkspaceProperties.XML_TAG_DYNAMICLIST);
      for (int pidx = 0; pidx < listDefs.getLength(); pidx++)
      {
         listNode = listDefs.item(pidx);
         if (listNode.getNodeType() == Node.ELEMENT_NODE)
         {
            listElement = (Element) listNode;

            dList = new DynamicList(listElement.getAttribute(WorkspaceProperties.XML_ATT_ID));
            dList.setConnection(listElement.getAttribute(WorkspaceProperties.XML_ATT_CONNECTION));
            dList.setValueFieldName(listElement.getAttribute(WorkspaceProperties.XML_ATT_VALUE));
            dList.setTitleFieldName(listElement.getAttribute(WorkspaceProperties.XML_ATT_TITLE));

            attribList = listElement.getElementsByTagName(WorkspaceProperties.XML_TAG_SQLSTATEMENT);
            if (attribList.getLength() > 0)
            {
               attribNode = attribList.item(0);
               dList.setSqlStatement(attribNode.getFirstChild().getNodeValue());

               lists.put(dList.getId(), dList);
            }
         }
      }

      return lists;
   }

   /**
    * Lee todas las definiciones de plugin de un determinado tipo.
    *
    * @param doc Una instancia de {@link Document} que representa el documento XML.
    * @param pluginTag Una cadena que contiene el nombre del TAG que reciben todas las definiciones del tipo de plugin a leer.
    *
    * @return Una instancia de {@link HashMap} que contiene las definiciones de plugin recopiladas.
    */
   private HashMap<String, OrmApplication> readOrmApps(Document doc)
   {
      Node actionNode;
      NodeList appsList;
      NodeList attribList;
      Element appElement;
      Element actionElement;
      OrmApplication oa;

      HashMap<String, OrmApplication> apps = new HashMap<String, OrmApplication>();

      // Comprueba si existe la definición
      attribList = doc.getElementsByTagName(WorkspaceProperties.XML_TAG_CORM_APPS);
      if (attribList.getLength() < 1)
      {
         return apps;
      }

      appsList = doc.getElementsByTagName(WorkspaceProperties.XML_TAG_CORM_APP);
      for (int pidx = 0; pidx < appsList.getLength(); pidx++)
      {
         Node appNode = appsList.item(pidx);
         if (appNode.getNodeType() == Node.ELEMENT_NODE)
         {
            appElement = (Element) appNode;

            oa = new OrmApplication();
            oa.setId(appElement.getAttribute(WorkspaceProperties.XML_ATT_ID));
            oa.setClassName(appElement.getAttribute(WorkspaceProperties.XML_ATT_CLASS));
            oa.setConnectionId(appElement.getAttribute(WorkspaceProperties.XML_ATT_CONNECTION));
            oa.setTitle(appElement.getAttribute(WorkspaceProperties.XML_ATT_TITLE));
            oa.setDescription(appElement.getAttribute(WorkspaceProperties.XML_ATT_DESCRIPTION));

            attribList = appElement.getElementsByTagName(WorkspaceProperties.XML_TAG_APPACTION);
            for (int aidx = 0; aidx < attribList.getLength(); aidx++) 
            {
               actionNode = attribList.item(aidx);
               if (actionNode.getNodeType() == Node.ELEMENT_NODE)
               {
                  actionElement = (Element) actionNode;

                  if (actionElement.getAttribute(WorkspaceProperties.XML_ATT_TYPE).toLowerCase().trim().equals("delete"))
                  {
                     oa.setDeleteEnabled(true);
                  }
                  else if (actionElement.getAttribute(WorkspaceProperties.XML_ATT_TYPE).toLowerCase().trim().equals("create"))
                  {
                     oa.setCreateEnabled(true);
                  }
                  else if (actionElement.getAttribute(WorkspaceProperties.XML_ATT_TYPE).toLowerCase().trim().equals("edit"))
                  {
                     oa.setEditEnabled(true);
                  }
               }
            }

            apps.put(oa.getId(), oa);
         }
      }

      return apps;
   }

   /**
    * Inicializa la instancia.
    */
   private void initialize()
   {
      properties = new HashMap<String, String>();
      dataSources = new HashMap<String, DataSource>();
      serverDatasource = "";
      authenticationAgents = new HashMap<String, PluginProperties>();
      authorizationAgents = new HashMap<String, PluginProperties>();
      ormApps = new HashMap<String, OrmApplication>();
      loginPage = "";
      authenticationAgentId = "";
      authorizationAgentId = "";
   }
}
