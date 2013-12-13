package com.cosmo.util;

import java.util.HashMap;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.cosmo.data.DataQuery;

/**
 * Wrapper para determinadas utilidades de lectura en archivos XML.
 * 
 * @author Gerard Llort
 */
public class XmlUtils
{
   private static final String XML_ATT_ID = "id";
   private static final String XML_ATT_KEY = "key";
   private static final String XML_ATT_DRIVER = "driver";
   private static final String XML_ATT_VALUE = "value";
   private static final String XML_ATT_CONNECTION = "connection";

   private static final String XML_TAG_PARAMETER = "param";
   private static final String XML_TAG_DATAQUERY = "dataquery";

   /**
    * Obtiene el valor de un determinado nodo.
    */
   public static String getTextValue(Element doc, String tag) 
   {
      return XmlUtils.getTextValue(doc, tag, "");
   }

   /**
    * Obtiene el valor de un determinado nodo.
    */
   public static String getTextValue(Element doc, String tag, String defaultValue) 
   {
      String value = defaultValue;
      NodeList nl;

      nl = doc.getElementsByTagName(tag);
      if (nl.getLength() > 0 && nl.item(0).hasChildNodes()) 
      {
        value = nl.item(0).getFirstChild().getNodeValue();
      }

      return value;
   }

   /**
    * Lee un mapa de parámetros que se encuentre dentro de un determinado tag:
    * <br />
    * {@code <static-values>}<br />
    * {@code   <param key="pattern" value="value1" />}<br />
    * {@code   <param key="threshold" value="ALL" />}<br />
    * {@code   <param key="immediateFlush" value="true" />}<br />
    * {@code </static-values>}
    * 
    * @param doc Una instancia de {@link Document} que representa el archivo XML.
    * @param tagName Nombre del TAG que contiene el mapa de parámetros a obtener.
    * 
    * @return Un mapa de pares clave,valor.
    */
   public static HashMap<String, String> readParameterMap(Document doc, String tagName)
   {
      Node mapNode;
      Node paramNode;
      Element mapElement;
      Element paramElement;
      NodeList paramList;
      HashMap<String, String> params = new HashMap<String, String>();

      NodeList pluginList = doc.getElementsByTagName(tagName);
      for (int midx = 0; midx < pluginList.getLength(); midx++)
      {
         mapNode = pluginList.item(midx);
         if (mapNode.getNodeType() == Node.ELEMENT_NODE)
         {
            mapElement = (Element) mapNode;
            paramList = mapElement.getElementsByTagName(XmlUtils.XML_TAG_PARAMETER);
            for (int pidx = 0; pidx < paramList.getLength(); pidx++)
            {
               paramNode = paramList.item(pidx);
               if (paramNode.getNodeType() == Node.ELEMENT_NODE)
               {
                  paramElement = (Element) paramNode;
                  params.put(paramElement.getAttribute(XML_ATT_KEY),
                             paramElement.getAttribute(XML_ATT_VALUE));
               }
            }

         }
      }

      return params;
   }
   
   /**
    * Obtiene todas las definiciones de <em>DataQuery</em> de un documento XML.
    * 
    * @param doc Una instancia de {@link Document} que representa el archivo XML.
    * 
    * @return Un mapa de instancias {@link DataQuery} que contienen los datos de las consultas.
    */
   public static HashMap<String, DataQuery> readDataQueries(Document doc)
   {
      Element pluginElement;
      DataQuery dq;
      HashMap<String, DataQuery> queries = new HashMap<String, DataQuery>();
      
      NodeList pluginList = doc.getElementsByTagName(XmlUtils.XML_TAG_DATAQUERY);
      for (int pidx = 0; pidx < pluginList.getLength(); pidx++)
      {
         Node pluginNode = pluginList.item(pidx);
         if (pluginNode.getNodeType() == Node.ELEMENT_NODE)
         {
            pluginElement = (Element) pluginNode;

            dq = new DataQuery();
            dq.setId(pluginElement.getAttribute(XML_ATT_ID));
            dq.setConnectionId(pluginElement.getAttribute(XML_ATT_CONNECTION));
            dq.setSqlQuery(pluginElement.getFirstChild().getNodeValue());
            queries.put(dq.getId(), dq);
         }
      }
      
      return queries;
   }
   
   /**
    * Lee todas las definiciones de plugin de un determinado tipo.
    *
    * @param doc Una instancia de {@link Document} que representa el documento XML.
    * @param pluginTag Una cadena que contiene el nombre del TAG que reciben todas las definiciones del tipo de plugin a leer.
    *
    * @return Una instancia de {@link HashMap} que contiene una lista de instancias de {@link PluginProperties}.
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
            plugin.setId(pluginElement.getAttribute(XmlUtils.XML_ATT_ID));
            plugin.setModuleClass(pluginElement.getAttribute(XmlUtils.XML_ATT_DRIVER));

            attribList = pluginElement.getElementsByTagName(XmlUtils.XML_TAG_PARAMETER);
            for (int aidx = 0; aidx < attribList.getLength(); aidx++) 
            {
               attribNode = attribList.item(aidx);
               if (attribNode.getNodeType() == Node.ELEMENT_NODE)
               {
                  attribElement = (Element) attribNode;
                  plugin.setParam(attribElement.getAttribute(XmlUtils.XML_ATT_KEY), 
                                  attribElement.getAttribute(XmlUtils.XML_ATT_VALUE));
               }
            }

            plugins.put(plugin.getId(), plugin);
         }
      }

      return plugins;
   }
}
