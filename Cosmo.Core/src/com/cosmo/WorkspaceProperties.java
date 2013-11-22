package com.cosmo;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
import com.cosmo.orm.OrmServiceProperties;
import com.cosmo.security.SecurityServiceProperties;
import com.cosmo.structures.KeyValue;
import com.cosmo.ui.UIServiceProperties;
import com.cosmo.util.IOUtils;
import com.cosmo.util.PluginProperties;

/**
 * Representa la configuración del workspace.
 * 
 * @author Gerard Llort
 */
public class WorkspaceProperties 
{
   public static final String PROPERTIES_FILENAME = "cosmo.config.xml";

   private static final String XML_TAG_PARAMETER = "param";

   private static final String XML_ATT_ID = "id";
   private static final String XML_ATT_KEY = "key";
   private static final String XML_ATT_DRIVER = "driver";
   private static final String XML_ATT_VALUE = "value";

   // Declaración de variables locales
   private HashMap<String, String> properties;
   private UIServiceProperties uiProps;
   private SecurityServiceProperties securityProps;
   private DataServiceProperties dataProps;
   private CommServiceProperties commProps;
   private OrmServiceProperties ormProps;


   //==============================================
   // Constructors
   //==============================================

   /**
    * Constructor de la clase {@link WorkspaceProperties}.
    * 
    * @param context Una instancia de {@link ServletContext} que representa el contexto del servlet.
    * 
    * @throws WorkspaceLoadException
    */
   public WorkspaceProperties(ServletContext context) throws WorkspaceLoadException
   {
      FileInputStream iStream = null;

      // Inicializa la instancia
      initialize();

      // Abre el archivo de configuración en el server
      try
      {
         iStream = new FileInputStream(context.getRealPath("/" + WorkspaceProperties.PROPERTIES_FILENAME));
         loadConfig(iStream);
         iStream.close();
      }
      catch (IOException ex)
      {
         throw new WorkspaceLoadException(ex.getMessage(), ex);
      }
      finally
      {
         if (iStream != null)
         {
            IOUtils.closeStream(iStream);
         }
      }
   }

   /**
    * Constructor de la clase {@link WorkspaceProperties}.
    * 
    * @param context Una instancia de {@link FileInputStream} que representa el archivo de configuración que se desea cargar.
    * 
    * @throws WorkspaceLoadException
    */
   public WorkspaceProperties(FileInputStream inputStream) throws WorkspaceLoadException
   {
      // Inicializa la instancia
      initialize();

      // Abre el archivo de configuración en el server
      try
      {
         loadConfig(inputStream);
         inputStream.close();
      }
      catch (IOException ex)
      {
         throw new WorkspaceLoadException(ex.getMessage(), ex);
      }
      finally
      {
         if (inputStream != null)
         {
            IOUtils.closeStream(inputStream);
         }
      }
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
    * Devuelve las propiedades de configuración de ORM Services.
    */
   public OrmServiceProperties getOrmProperties()
   {
      return ormProps;
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
    * Devuelve una lista de todos los valores de configuración de la aplicación.
    * 
    * @return Una lista de instancias de {@link KeyValue} que representan los valores de la configuración.
    */
   public List<KeyValue> getPropertiesList()
   {
      List<KeyValue> values = new ArrayList<KeyValue>();
      for (String key : properties.keySet())
      {
         values.add(new KeyValue(key, this.properties.get(key)));
      }

      return values;
   }

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
   public Integer getInt(String key, Integer defaultNum)
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
   private void loadConfig(FileInputStream inputStream) throws WorkspaceLoadException
   {
      Node nNode;
      Element eElement;
      NodeList nList;
      // InputStream iStream = null;

      try
      {
         DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
         DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
         Document doc = dBuilder.parse(inputStream);
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

         // Lectura de la configuración de Communication Services
         ormProps = new OrmServiceProperties(doc);
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
      this.ormProps = null;

      properties = new HashMap<String, String>();
   }
}
