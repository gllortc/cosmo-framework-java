package com.cosmo.ui.templates;

import com.cosmo.ui.PageContext.PageLayout;
import com.cosmo.ui.templates.TemplateScript.ScriptType;
import com.cosmo.util.IOUtils;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
 * Representa una plantilla de presentación de Cosmno.
 * 
 * @author Gerard Llort
 */
public class Template 
{
   public static final String FILENAME_TEMPLATE = "cosmo.template.xml";

   public static final String PATH_TEMPLATES = "templates";

   private String id;
   private String name;
   private String author;
   private String version;
   private String copyright;
   private HashMap<String, String> layouts;
   private HashMap<String, TemplateControl> controls;
   private ArrayList<TemplateLink> headLinks;
   private ArrayList<TemplateScript> headScripts;

   private static final String XML_NODE_TEMPLATE = "template";
   private static final String XML_ATT_DEFINITIONVER = "cdt-ver";
   private static final String XML_NODE_NAME = "name";
   private static final String XML_NODE_AUTHOR = "author";
   private static final String XML_NODE_VERSION = "version";
   private static final String XML_NODE_COPYRIGHT = "copyright";
   private static final String XML_NODE_LAYOUT = "layout";
   private static final String XML_ATT_ID = "id";
   private static final String XML_NODE_PAGECONTROL = "pcontrol";
   private static final String XML_NODE_PAGECONTROLPART = "celement";
   private static final String XML_NODE_SCRIPT = "cscript";
   private static final String XML_ATT_TYPE = "type";
   private static final String XML_ATT_SRC = "src";
   private static final String XML_NODE_LINK = "clink";
   private static final String XML_ATT_REL = "rel";
   private static final String XML_ATT_HREF = "href";
   private static final String XML_NODE_HEADLINK = "hlink";
   private static final String XML_ATT_MEDIA = "media";
   private static final String XML_NODE_HEADSCRIPT = "hscript";

   private static final String XML_DEFINITION_VERSION = "1.0";

   public static final String LAYOUT_ID_HOMEPAGE = "home-page";
   public static final String LAYOUT_ID_2COLSLEFT = "two-cols-left";
   public static final String LAYOUT_ID_2COLSRIGHT = "two-cols-right";
   public static final String LAYOUT_ID_3COLS = "three-cols";
   public static final String LAYOUT_ID_1COL = "one-col";


   //==============================================
   // Constructors
   //==============================================

   /**
    * Constructor de la clase.
    * 
    * @param context Contexto de la llamada al workspace.
    * @param id Identificador de la plantilla.
    */
   public Template(ServletContext context, String id) throws TemplateLoadException 
   {
      initialize();

      this.id = id;

      loadTemplate(context, id);
   }


   //==============================================
   // Properties
   //==============================================

   public String getId() 
   {
      return id;
   }

   public String getName() 
   {
      return name;
   }

   public String getAuthor() 
   {
      return author;
   }

   public String getVersion() 
   {
      return version;
   }

   public String getCopyright() 
   {
      return copyright;
   }

   /**
    * Devuelve un iterador que permite recorrer todos los elementos LINK de cabecera.
    */
   public Iterator<TemplateLink> getLinks()
   {
      return this.headLinks.iterator();
   }

   /**
    * Devuelve un iterador que permite recorrer todos los elementos SCRIPT de cabecera.
    */
   public Iterator<TemplateScript> getScripts()
   {
      return this.headScripts.iterator();
   }


   //==============================================
   // Methods
   //==============================================

   /**
    * Obtiene el código XHTML correspondiente al esqueleto básico de una estructura.
    * 
    * @param layout Tipo de estructura a recuperar.
    * 
    * @return Una cadena XHTML que representa el esqueleto de la estructura seleccionada.
    */
   public String getLayout(PageLayout layout) throws TemplateControlException
   {
      String xhtml = "";

      switch (layout)
      {
         case HomePage:
            xhtml = this.layouts.get(Template.LAYOUT_ID_HOMEPAGE);
            break;

         case OneColumn:
            xhtml = this.layouts.get(Template.LAYOUT_ID_1COL);
            break;

         case TwoColumnsLeft:
            xhtml = this.layouts.get(Template.LAYOUT_ID_2COLSLEFT);
            break;

         case TwoColumnsRight:
            xhtml = this.layouts.get(Template.LAYOUT_ID_2COLSRIGHT);
            break;

         case ThreeColumns:
            xhtml = this.layouts.get(Template.LAYOUT_ID_3COLS);
            break;
      }

      if (xhtml == null || xhtml.equals(""))
      {
         throw new TemplateControlException("No layout definition found: " + layout.toString());
      }

      return xhtml;
   }

   public TemplateControl getControl(String id)
   {
      return this.controls.get(id);
   }


   //==============================================
   // Private members
   //==============================================

   /**
    * Carga una plantilla a partir del archivo de definición.
    * 
    * @param context Contexto de la llamada al workspace.
    * @param templateId Identificador de la plantilla.
    * 
    * @throws TemplateLoadException 
    */
   private void loadTemplate(ServletContext context, String templateId) throws TemplateLoadException
   {
      String href;
      Node nNode;
      Node nPart;
      Element eElement;
      Element ePart;
      NodeList nList;
      NodeList nParts;
      TemplateControl control;
      InputStream is = null;

      try
      {
         is = new FileInputStream(context.getRealPath("/" + Template.PATH_TEMPLATES + "/" + templateId + "/" + Template.FILENAME_TEMPLATE));

         DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
         DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
         Document doc = dBuilder.parse(is);
         doc.getDocumentElement().normalize();

         // Comprueba que el archivo sea el correcto y la versión compatible
         nList = doc.getElementsByTagName(Template.XML_NODE_TEMPLATE);
         nNode = nList.item(0);
         if (nNode.getNodeType() == Node.ELEMENT_NODE && nNode.getNodeName().equals(Template.XML_NODE_TEMPLATE))
         {
            eElement = (Element) nNode;
            if (eElement.getAttribute(Template.XML_ATT_DEFINITIONVER).compareTo(Template.XML_DEFINITION_VERSION) < 0)
            {
               throw new TemplateLoadException("File defining template must be compatible with version " + Template.XML_DEFINITION_VERSION + " or higher");
            }
         }

         // Obtiene las propiedades de la plantilla
         Element root = doc.getDocumentElement();
         this.name = getTextValue(root, Template.XML_NODE_NAME);
         this.version = getTextValue(root, Template.XML_NODE_VERSION);
         this.author = getTextValue(root, Template.XML_NODE_AUTHOR);
         this.copyright = getTextValue(root, Template.XML_NODE_COPYRIGHT);

         // Obtiene LINKS que se deben incorporar al HEAD
         nList = doc.getElementsByTagName(Template.XML_NODE_HEADLINK);
         for (int temp = 0; temp < nList.getLength(); temp++) 
         {
            nNode = nList.item(temp);
            if (nNode.getNodeType() == Node.ELEMENT_NODE)
            {
               eElement = (Element) nNode;

               href = eElement.getAttribute(Template.XML_ATT_HREF).trim().toLowerCase();
               if (!href.startsWith("http")) 
               {
                  href = Template.PATH_TEMPLATES + "/" + templateId + "/" + href;
               }
               this.headLinks.add(new TemplateLink(eElement.getAttribute(Template.XML_ATT_REL), 
                                                   eElement.getAttribute(Template.XML_ATT_TYPE), 
                                                   href,
                                                   eElement.getAttribute(Template.XML_ATT_MEDIA)));
            }
         }

         // Obtiene SCRIPTS que se deben incorporar al HEAD
         nList = doc.getElementsByTagName(Template.XML_NODE_HEADSCRIPT);
         for (int temp = 0; temp < nList.getLength(); temp++) 
         {
            nNode = nList.item(temp);
            if (nNode.getNodeType() == Node.ELEMENT_NODE)
            {
               eElement = (Element) nNode;

               if (!eElement.hasChildNodes())
               {
                  href = eElement.getAttribute(Template.XML_ATT_SRC).trim().toLowerCase();
                  if (!href.startsWith("http")) 
                  {
                     href = Template.PATH_TEMPLATES + "/" + templateId + "/" + href;
                  }
                  this.headScripts.add(new TemplateScript(ScriptType.Referenced, href));
               }
               else
               {
                  this.headScripts.add(new TemplateScript(ScriptType.Code, 
                                                          eElement.getFirstChild().getNodeValue()));
               }
            }
         }

         // Obtiene los layouts
         nList = doc.getElementsByTagName(Template.XML_NODE_LAYOUT);
         for (int temp = 0; temp < nList.getLength(); temp++) 
         {
            nNode = nList.item(temp);
            if (nNode.getNodeType() == Node.ELEMENT_NODE)
            {
               eElement = (Element) nNode;
               if (eElement.hasChildNodes())
               {
                  this.layouts.put(eElement.getAttribute(Template.XML_ATT_ID), 
                                   eElement.getFirstChild().getNodeValue());
               }
               else
               {
                  this.layouts.put(eElement.getAttribute(Template.XML_ATT_ID), "");
               }
            }
         }

         // Obtiene las partes de controles
         nList = doc.getElementsByTagName(Template.XML_NODE_PAGECONTROL);
         for (int temp = 0; temp < nList.getLength(); temp++) 
         {
            nNode = nList.item(temp);
            if (nNode.getNodeType() == Node.ELEMENT_NODE)
            {
               eElement = (Element) nNode;
               control = new TemplateControl(eElement.getAttribute(Template.XML_ATT_ID));

               nParts = nNode.getChildNodes(); // eElement.getElementsByTagName(Template.XML_NODE_PAGECONTROLPART);
               for (int part = 0; part < nParts.getLength(); part++) 
               {
                  nPart = nParts.item(part);
                  if (nPart.getNodeType() == Node.ELEMENT_NODE)
                  {
                     ePart = (Element) nPart;
                     if (ePart.getNodeName().equals(Template.XML_NODE_PAGECONTROLPART))
                     {
                        if (ePart.hasChildNodes())
                        {
                           control.addControlPart(ePart.getAttribute(Template.XML_ATT_ID), 
                                                  ePart.getFirstChild().getNodeValue());
                        }
                        else
                        {
                           control.addControlPart(ePart.getAttribute(Template.XML_ATT_ID), "");
                        }
                     }
                     else if (ePart.getNodeName().equals(Template.XML_NODE_SCRIPT))
                     {
                        if (ePart.getAttribute(Template.XML_ATT_TYPE).equals("external"))
                        {
                           href = ePart.getAttribute(Template.XML_ATT_SRC);
                           href = Template.PATH_TEMPLATES + "/" + templateId + "/" + href;

                           control.addScript(new TemplateScript(TemplateScript.ScriptType.Referenced, href));
                        }
                        else if (ePart.getAttribute(Template.XML_ATT_TYPE).equals("internal"))
                        {
                           if (ePart.hasChildNodes())
                           {
                              control.addScript(new TemplateScript(TemplateScript.ScriptType.Code, 
                                                                   ePart.getFirstChild().getNodeValue()));
                           }
                        }
                     }
                     else if (ePart.getNodeName().equals(Template.XML_NODE_LINK))
                     {
                        href = ePart.getAttribute(Template.XML_ATT_HREF);
                        href = Template.PATH_TEMPLATES + "/" + templateId + "/" + href;

                        control.addLink(new TemplateLink(ePart.getAttribute(Template.XML_ATT_REL), 
                                                         ePart.getAttribute(Template.XML_ATT_TYPE),
                                                         href));
                     }
                  }
               }
               this.controls.put(control.getId(), control);
            }
         }
      }
      catch (FileNotFoundException ex)
      {
         throw new TemplateLoadException(ex.getMessage(), ex);
      }
      catch (ParserConfigurationException ex)
      {
         throw new TemplateLoadException(ex.getMessage(), ex);
      }
      catch (SAXException ex)
      {
         throw new TemplateLoadException(ex.getMessage(), ex);
      }
      catch (IOException ex)
      {
         throw new TemplateLoadException(ex.getMessage(), ex);
      }
      finally
      {
         IOUtils.closeStream(is);
      }
   }

   /**
    * Inicializa la instancia.
    */
   private void initialize()
   {
      this.id = "";
      this.name = "";
      this.author = "";
      this.version = "";
      this.layouts = new HashMap<String, String>();
      this.controls = new HashMap<String, TemplateControl>();
      this.headLinks = new ArrayList<TemplateLink>();
      this.headScripts = new ArrayList<TemplateScript>();
   }

   /**
    * Obtiene el valor de un determinado nodo.
    */
   private String getTextValue(Element doc, String tag) 
   {
      return getTextValue(doc, tag, "");
   }

   /**
    * Obtiene el valor de un determinado nodo.
    */
   private String getTextValue(Element doc, String tag, String defaultValue) 
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
}
