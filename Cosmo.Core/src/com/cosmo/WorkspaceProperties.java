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

import com.cosmo.comm.CommServiceProperties;
import com.cosmo.data.DataServiceProperties;
import com.cosmo.data.DataSource;
import com.cosmo.data.lists.DynamicList;
import com.cosmo.data.lists.List;
import com.cosmo.data.lists.ListItem;
import com.cosmo.data.lists.StaticList;
import com.cosmo.data.orm.apps.OrmApplication;
import com.cosmo.security.SecurityServiceProperties;
import com.cosmo.structures.PluginProperties;
import com.cosmo.ui.UIServiceProperties;
import com.cosmo.util.IOUtils;
import com.cosmo.util.StringUtils;

/**
 * Representa la configuración del workspace.
 */
public class WorkspaceProperties 
{
   public static final String PROPERTIES_FILENAME = "cosmo.config.xml";

   // Literales para los nodos del archivo de configuración
   // private static final String XML_TAG_DATA_CONNECTIONS = "connections";
   // private static final String XML_ATT_DATA_DEFAULTCONN = "default-connection";
   // private static final String XML_TAG_CONNECTION = "connection";
   private static final String XML_ATT_ID = "id";
   // private static final String XML_ATT_JDBC_DRIVER = "jdbc.driver";
   // private static final String XML_ATT_CORM_DRIVER = "corm.driver";
   // private static final String XML_ATT_SERVER = "server";
   // private static final String XML_ATT_PORT = "port";
   // private static final String XML_ATT_SCHEMA = "schema";
   // private static final String XML_ATT_USER = "user";
   // private static final String XML_ATT_PASSWORD = "pwd";
   // private static final String XML_ATT_KEY = "key";
   // private static final String XML_ATT_VALUE = "value";
   // private static final String XML_TAG_AUTHENTICATION = "authentication-agent";
   // private static final String XML_TAG_AUTHORIZATION = "authorization-agent";
   private static final String XML_ATT_DRIVER = "driver";
   private static final String XML_TAG_PARAMETER = "param";
   //private static final String XML_TAG_SECURITY = "security";
   //private static final String XML_ATT_AUTHENTICATIONAGENT = "authentication-agent";
   //private static final String XML_ATT_AUTHORIZATIONAGENT = "authorization-agent";
   //private static final String XML_ATT_LOGINPAGE = "login-page";
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

   // Definición de tags y atributos para Communication Services
   // private static final String XML_TAG_COMM_AGENTS = "communications";
   // private static final String XML_TAG_COMM_AGENT = "comm-agent";
   // private static final String XML_ATT_COMM_DEFAULTDRIVER = "communication-agent";

   // Definición de tags y atributos para UI Services
   // private static final String XML_UI_TAG = "ui";
   // private static final String XML_UI_TAG_TEMPLATE_RULE = "template-rule";
   // private static final String XML_UI_ATT_DEFAULTTEMPLATE = "default-template";
   // private static final String XML_UI_ATT_RULE_CONTAINS = "contains";
   // private static final String XML_UI_ATT_RULE_TEMPLATE = "template";

   // Declaración de variables locales (genérico)
   private HashMap<String, String> properties;
   private HashMap<String, OrmApplication> ormApps;
   private HashMap<String, List> ormLists;
   private UIServiceProperties uiProps;
   private SecurityServiceProperties securityProps;
   private DataServiceProperties dataProps;
   private CommServiceProperties commProps;

   // Declaración de variables locales para Data Services
   // private String serverDatasource;
   // private HashMap<String, DataSource> dataSources;

   // Declaración de variables locales para UI Services
   // private String uiDefaultTemplateId;
   // private ArrayList<Rule> uiTemplateRules;

   // Declaración de variables locales para Security Services
   // private String authenticationAgentId;
   // private String authorizationAgentId;
   // private String loginPage;
   // private HashMap<String, PluginProperties> authenticationAgents;
   // private HashMap<String, PluginProperties> authorizationAgents;

   // Declaración de variables locales para Communication Services
   // private String commAgentId;
   // private HashMap<String, PluginProperties> commAgents;


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
    * Devuelve las propiedades de configuración de UI Services.
    */
   public UIServiceProperties getUiProperties()
   {
      return uiProps;
   }

   /**
    * Devuelve las propiedades de configuración de Security Services.
    */
   public SecurityServiceProperties getSecurityProperties()
   {
      return securityProps;
   }

   /**
    * Devuelve las propiedades de configuración de Data Services.
    */
   public DataServiceProperties getDataProperties()
   {
      return dataProps;
   }

   /**
    * Devuelve las propiedades de configuración de Data Services.
    */
   public CommServiceProperties getCommProperties()
   {
      return commProps;
   }
   
   
   
   
   
   
   
   
   
   
   /**
    * Devuelve el número de propiedades de configuración leídas.
    */
   public int getNumProperties()
   {
      return this.properties.size();
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
   // static members
   //==============================================

   /**
    * Lee todas las definiciones de plugin de un determinado tipo.
    *
    * @param doc Una instancia de {@link Document} que representa el documento XML.
    * @param pluginTag Una cadena que contiene el nombre del TAG que reciben todas las definiciones del tipo de plugin a leer.
    *
    * @return Una instancia de {@link HashMap} que contiene las definiciones de plugin recopiladas.
    */
   public static HashMap<String, PluginProperties> readPluginsByType(Document doc, String pluginTag)
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


   //==============================================
   // Private members
   //==============================================

   /**
    * Carga la configuración del workspace. 
    *
    * @param context Contexto del servidor web.
    *
    * @throws WorkspaceLoadException
    */
   private void loadConfig(ServletContext context) throws WorkspaceLoadException
   {
      Node nNode;
      Element eElement;
      NodeList nList;
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

         // Lectura de la configuración de UI Services
         uiProps = new UIServiceProperties(doc);

         // Lectura de la configuración de Security Services
         securityProps = new SecurityServiceProperties(doc);

         // Lectura de la configuración de Data Services
         dataProps = new DataServiceProperties(doc);

         // Lectura de la configuración de Communication Services
         commProps = new CommServiceProperties(doc);

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
      this.uiProps = null;
      this.securityProps = null;
      this.dataProps = null;
      this.commProps = null;
      
      properties = new HashMap<String, String>();
      ormApps = new HashMap<String, OrmApplication>();
   }
}
