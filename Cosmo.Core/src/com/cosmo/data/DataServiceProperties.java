package com.cosmo.data;

import java.util.HashMap;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.cosmo.data.lists.DynamicList;
import com.cosmo.data.lists.List;
import com.cosmo.data.lists.ListItem;
import com.cosmo.data.lists.StaticList;
import com.cosmo.util.PluginProperties;
import com.cosmo.util.StringUtils;

/**
 * Contenedor encargado de leer y almacenar toda la configuración de Data Services.
 * 
 * @author Gerard Llort
 */
public class DataServiceProperties
{
   // Definición de tags y atributos para Data Services
   // private static final String XML_TAG_DATA = "data-services";
   private static final String XML_TAG_CONNECTIONS = "connections";
   private static final String XML_TAG_CONNECTION = "connection";
   private static final String XML_TAG_DATALISTS = "data-lists";
   private static final String XML_TAG_STATICLIST = "static-list";
   private static final String XML_TAG_DYNAMICLIST = "dynamic-list";
   private static final String XML_TAG_STATICLISTITEM = "static-list-item";
   private static final String XML_TAG_SQLSTATEMENT = "sql-statement";

   private static final String XML_ATT_DEFAULTCONN = "default-connection";
   private static final String XML_ATT_ID = "id";
   private static final String XML_ATT_JDBC_DRIVER = "jdbc.driver";
   private static final String XML_ATT_CORM_DRIVER = "corm.driver";
   private static final String XML_ATT_SERVER = "server";
   private static final String XML_ATT_PORT = "port";
   private static final String XML_ATT_SCHEMA = "schema";
   private static final String XML_ATT_USER = "user";
   private static final String XML_ATT_PASSWORD = "pwd";
   private static final String XML_ATT_DEFAULTVALUE = "default-value";
   private static final String XML_ATT_CONNECTION = "connection";
   private static final String XML_ATT_TITLE = "title";
   private static final String XML_ATT_VALUE = "value";

   // Declaración de variables locales para UI Services
   private String serverDatasource;
   private HashMap<String, DataSource> dataSources;
   private HashMap<String, PluginProperties> agents;
   private HashMap<String, List> ormLists;


   //==============================================
   // Constructors
   //==============================================
   
   public DataServiceProperties(Document xmlDocument)
   {
      initialize();

      loadProperties(xmlDocument);
   }


   //==============================================
   // Properties
   //==============================================
   
   /**
    * Devuelve el número de DataSources leídos.
    */
   public int getNumDatasources()
   {
      return this.dataSources.size();
   }

   /**
    * Obtiene el identificador de la conexión de datos a usar por defecto.
    * 
    * @return Una cadena que contiene el nombre del datasource definido como servidor Cosmo.
    */
   public String getDefaultDataSourceId()
   {
      return this.serverDatasource;
   }


   //==============================================
   // Methods
   //==============================================

   /**
    * Obtiene la conexión a datos configurada por defecto.
    *
    * @return Una instancia de {@link DataSource} que contiene los parámetros de conexión a la base de datos.
    */
   public DataSource getDataSource()
   {
      if (StringUtils.isNullOrEmptyTrim(this.serverDatasource))
      {
         return null;
      }

      return getDataSource(this.serverDatasource);
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

   /**
    * Obtiene una conxeión a un orígen de datos.
    *
    * @param key Clave identificativa de la conexión.
    * 
    * @return Una instancia de {@link DataSource} que contiene los parámetros de conexión a la base de datos.
    */
   public DataSource getDataSource(String key)
   {
      return this.dataSources.get(key);
   }

   /**
    * Obtiene la conexión a datos configurada por defecto.
    *
    * @return Una instancia de {@link PluginProperties} que contiene los parámetros de conexión a la base de datos.
    */
   public PluginProperties getDataAgent()
   {
      if (StringUtils.isNullOrEmptyTrim(this.serverDatasource))
      {
         return null;
      }

      return getDataAgent(this.serverDatasource);
   }

   /**
    * Obtiene una conxeión a un orígen de datos.
    *
    * @param key Clave identificativa de la conexión.
    * 
    * @return Una instancia de {@link PluginProperties} que contiene los parámetros de conexión a la base de datos.
    */
   public PluginProperties getDataAgent(String key)
   {
      return this.agents.get(key);
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
      DataSource ds;

      initialize();

      nList = doc.getElementsByTagName(DataServiceProperties.XML_TAG_CONNECTIONS);
      if (nList.getLength() >= 1)
      {
         nNode = nList.item(0);
         if (nNode.getNodeType() == Node.ELEMENT_NODE)
         {
            eElement = (Element) nNode;
            this.serverDatasource = eElement.getAttribute(DataServiceProperties.XML_ATT_DEFAULTCONN);
         }

         nList = doc.getElementsByTagName(DataServiceProperties.XML_TAG_CONNECTION);
         for (int temp = 0; temp < nList.getLength(); temp++) 
         {
            nNode = nList.item(temp);
            if (nNode.getNodeType() == Node.ELEMENT_NODE)
            {
               eElement = (Element) nNode;

               ds = new DataSource();
               ds.setId(eElement.getAttribute(DataServiceProperties.XML_ATT_ID));
               ds.setJdbcDriver(eElement.getAttribute(DataServiceProperties.XML_ATT_JDBC_DRIVER));
               ds.setCormDriver(eElement.getAttribute(DataServiceProperties.XML_ATT_CORM_DRIVER));
               ds.setHost(eElement.getAttribute(DataServiceProperties.XML_ATT_SERVER));
               ds.setPort(eElement.getAttribute(DataServiceProperties.XML_ATT_PORT));
               ds.setSchema(eElement.getAttribute(DataServiceProperties.XML_ATT_SCHEMA));
               ds.setLogin(eElement.getAttribute(DataServiceProperties.XML_ATT_USER));
               ds.setPassword(eElement.getAttribute(DataServiceProperties.XML_ATT_PASSWORD));

               this.dataSources.put(ds.getId(), ds);
            }
         }
      }

      // Obtiene las listas de datos
      this.ormLists = readDataLists(doc);
   }


   //==============================================
   // Private members
   //==============================================

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
      attribList = doc.getElementsByTagName(DataServiceProperties.XML_TAG_DATALISTS);
      if (attribList.getLength() < 1)
      {
         return lists;
      }

      // Carga las listas estáticas
      listDefs = doc.getElementsByTagName(DataServiceProperties.XML_TAG_STATICLIST);
      for (int pidx = 0; pidx < listDefs.getLength(); pidx++)
      {
         listNode = listDefs.item(pidx);
         if (listNode.getNodeType() == Node.ELEMENT_NODE)
         {
            listElement = (Element) listNode;

            sList = new StaticList(listElement.getAttribute(DataServiceProperties.XML_ATT_ID));

            attribList = listElement.getElementsByTagName(DataServiceProperties.XML_TAG_STATICLISTITEM);
            for (int aidx = 0; aidx < attribList.getLength(); aidx++) 
            {
               attribNode = attribList.item(aidx);
               if (attribNode.getNodeType() == Node.ELEMENT_NODE)
               {
                  attribElement = (Element) attribNode;
                  item = new ListItem(attribElement.getAttribute(DataServiceProperties.XML_ATT_VALUE),
                                      attribElement.getAttribute(DataServiceProperties.XML_ATT_TITLE));

                  if (!StringUtils.isNullOrEmptyTrim(attribElement.getAttribute(DataServiceProperties.XML_ATT_DEFAULTVALUE)))
                  {
                     item.setDefault(attribElement.getAttribute(DataServiceProperties.XML_ATT_DEFAULTVALUE).equals("true") ||
                                     attribElement.getAttribute(DataServiceProperties.XML_ATT_DEFAULTVALUE).equals("1"));
                  }

                  sList.addListItem(item);
               }
            }

            lists.put(sList.getId(), sList);
         }
      }

      // Carga las listas dinámicas SQL
      listDefs = doc.getElementsByTagName(DataServiceProperties.XML_TAG_DYNAMICLIST);
      for (int pidx = 0; pidx < listDefs.getLength(); pidx++)
      {
         listNode = listDefs.item(pidx);
         if (listNode.getNodeType() == Node.ELEMENT_NODE)
         {
            listElement = (Element) listNode;

            dList = new DynamicList(listElement.getAttribute(DataServiceProperties.XML_ATT_ID));
            dList.setConnection(listElement.getAttribute(DataServiceProperties.XML_ATT_CONNECTION));
            dList.setValueFieldName(listElement.getAttribute(DataServiceProperties.XML_ATT_VALUE));
            dList.setTitleFieldName(listElement.getAttribute(DataServiceProperties.XML_ATT_TITLE));

            attribList = listElement.getElementsByTagName(DataServiceProperties.XML_TAG_SQLSTATEMENT);
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
    * Inicializa la instancia.
    */
   private void initialize()
   {
      this.serverDatasource = "";
      this.dataSources = new HashMap<String, DataSource>();
      this.agents = new HashMap<String, PluginProperties>();
      this.ormLists = new HashMap<String, List>();
   }
}
