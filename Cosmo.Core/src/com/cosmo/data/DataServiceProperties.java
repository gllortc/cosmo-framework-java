package com.cosmo.data;

import java.util.HashMap;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.cosmo.util.StringUtils;

/**
 * Contenedor encargado de leer y almacenar toda la configuración de Data Services.
 * 
 * @author Gerard Llort
 */
public class DataServiceProperties
{
   // Definición de tags y atributos para Data Services
   private static final String XML_TAG_DATA_CONNECTIONS = "connections";
   private static final String XML_TAG_CONNECTION = "connection";

   private static final String XML_ATT_DATA_DEFAULTCONN = "default-connection";
   private static final String XML_ATT_ID = "id";
   private static final String XML_ATT_JDBC_DRIVER = "jdbc.driver";
   private static final String XML_ATT_CORM_DRIVER = "corm.driver";
   private static final String XML_ATT_SERVER = "server";
   private static final String XML_ATT_PORT = "port";
   private static final String XML_ATT_SCHEMA = "schema";
   private static final String XML_ATT_USER = "user";
   private static final String XML_ATT_PASSWORD = "pwd";


   // Declaración de variables locales para UI Services
   private String serverDatasource;
   private HashMap<String, DataSource> dataSources;
   


   //==============================================
   // Constructors
   //==============================================
   
   public DataServiceProperties(Document xmlDocument)
   {
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

      nList = doc.getElementsByTagName(DataServiceProperties.XML_TAG_DATA_CONNECTIONS);
      if (nList.getLength() >= 1)
      {
         nNode = nList.item(0);
         if (nNode.getNodeType() == Node.ELEMENT_NODE)
         {
            eElement = (Element) nNode;
            this.serverDatasource = eElement.getAttribute(DataServiceProperties.XML_ATT_DATA_DEFAULTCONN);
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
   }
}
