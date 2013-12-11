package com.cosmo.util;

import java.util.HashMap;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XmlUtils
{
   private static final String XML_ATT_ID = "id";
   private static final String XML_ATT_KEY = "key";
   private static final String XML_ATT_DRIVER = "driver";
   private static final String XML_ATT_VALUE = "value";

   private static final String XML_TAG_PARAMETER = "param";

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
