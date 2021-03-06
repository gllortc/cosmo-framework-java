package com.cosmo.data;

import java.util.HashMap;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.cosmo.WorkspaceProperties;
import com.cosmo.data.lists.DynamicList;
import com.cosmo.data.lists.List;
import com.cosmo.data.lists.ListItem;
import com.cosmo.data.lists.StaticList;
import com.cosmo.util.PluginProperties;
import com.cosmo.util.StringUtils;

/**
 * Contenedor encargado de leer y almacenar toda la configuraci�n de Data Services.
 * 
 * @author Gerard Llort
 */
public class DataServiceProperties
{
   private static final String XML_TAG_CONNECTIONS = "data-services";
   private static final String XML_TAG_CONNECTION = "connection";
   private static final String XML_TAG_DATALISTS = "data-lists";
   private static final String XML_TAG_STATICLIST = "static-list";
   private static final String XML_TAG_DYNAMICLIST = "dynamic-list";
   private static final String XML_TAG_STATICLISTITEM = "static-list-item";
   private static final String XML_TAG_SQLSTATEMENT = "sql-statement";

   private static final String XML_ATT_DEFAULTCONN = "default-connection";
   private static final String XML_ATT_ID = "id";
   private static final String XML_ATT_DEFAULTVALUE = "default-value";
   private static final String XML_ATT_CONNECTION = "connection";
   private static final String XML_ATT_TITLE = "title";
   private static final String XML_ATT_VALUE = "value";

   // Declaraci�n de variables internas para UI Services
   private String serverDatasource;
   private HashMap<String, PluginProperties> agents;
   private HashMap<String, List> ormLists;


   //==============================================
   // Constructors
   //==============================================

   /**
    * Constructor de la clase {@link DataServiceProperties}.
    * 
    * @param xmlDocument Una instancia de {@link Document} que representa el archivo XML de configuraci�n.
    */
   public DataServiceProperties(Document xmlDocument)
   {
      initialize();

      loadProperties(xmlDocument);
   }


   //==============================================
   // Properties
   //==============================================

   /**
    * Devuelve el n�mero de agentes de conexi�n configurados.
    */
   public int getNumDataAgents()
   {
      return this.agents.size();
   }

   /**
    * Obtiene el identificador de la conexi�n de datos a usar por defecto.
    * 
    * @return Una cadena que contiene el nombre del datasource definido como servidor Cosmo.
    */
   public String getDefaultDataAgentId()
   {
      return this.serverDatasource;
   }

   /**
    * Devuelve el n�mero de fuentes de datos configuradas.
    */
   public int getDataAgentCount()
   {
      return this.agents.size();
   }

   /**
    * Devuelve el n�mero de listas de datos configuradas.
    */
   public int getDataListCount()
   {
      return this.ormLists.size();
   }


   //==============================================
   // Methods
   //==============================================

   /**
    * Obtiene una lista de datos.
    *
    * @param id Identificador �nico de la lista.
    *
    * @return Una instancia de {@link List} que representa la lista de opciones.
    */
   public List getDataList(String id)
   {
      return this.ormLists.get(id);
   }

   /**
    * Obtiene la conexi�n a datos configurada por defecto.
    *
    * @return Una instancia de {@link PluginProperties} que contiene los par�metros de conexi�n a la base de datos.
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
    * Obtiene una conxei�n a un or�gen de datos.
    *
    * @param key Clave identificativa de la conexi�n.
    * 
    * @return Una instancia de {@link PluginProperties} que contiene los par�metros de conexi�n a la base de datos.
    */
   public PluginProperties getDataAgent(String key)
   {
      return this.agents.get(key);
   }

   /**
    * Lee y almacena las propiedades de configuraci�n de Data Services.
    * 
    * @param doc Una instancia de {@link Document} que contiene la configuraci�n (XML) de la aplicaci�n.
    */
   public void loadProperties(Document doc)
   {
      Node nNode;
      Element eElement;
      NodeList nList;

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

         // Obtiene todos los agentes de comunicaciones
         this.agents = WorkspaceProperties.readPluginsByType(doc, DataServiceProperties.XML_TAG_CONNECTION);

         // Obtiene las listas de datos
         this.ormLists = readDataLists(doc);
      }
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

      // Comprueba si existe la definici�n
      attribList = doc.getElementsByTagName(DataServiceProperties.XML_TAG_DATALISTS);
      if (attribList.getLength() < 1)
      {
         return lists;
      }

      // Carga las listas est�ticas
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

      // Carga las listas din�micas SQL
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
      this.agents = new HashMap<String, PluginProperties>();
      this.ormLists = new HashMap<String, List>();
   }
}
