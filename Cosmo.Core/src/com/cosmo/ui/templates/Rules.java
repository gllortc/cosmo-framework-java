package com.cosmo.ui.templates;

import com.cosmo.util.IOUtils;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
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
 * Implementa un contenedor de las reglas de aplicación de plantillas.
 * 
 * @author Gerard Llort
 */
public class Rules 
{
   public static String RULES_FILENAME = "cosmo.rules.xml";

   // Literales para los nodos del archivo de configuración
   private static final String XML_NODE_RULES = "rules";
   private static final String XML_ATT_DEFINITIONVER = "cdt-ver";
   private static final String XML_ATT_DEFAULTTMPLT = "default";
   private static final String XML_NODE_RULE = "rule";
   private static final String XML_ATT_CONTAINS = "contains";
   private static final String XML_ATT_TEMPLATE = "template";

   private static final String XML_DEFINITION_VERSION = "1.0";

   private ArrayList<Rule> rules;
   private int defaultTemplateId;
   private ServletContext context;

   //==============================================
   // Constructors
   //==============================================

   /**
    * Constructor de la clase.
    * 
    * @param context Contexto del servidor web.
    * 
    * @throws RulesLoadException 
    */
   public Rules(ServletContext context) throws RulesLoadException
   {
      this.defaultTemplateId = -1;
      this.context = context;
      
      rules = new ArrayList<Rule>();
      loadRules(context);
   }


   //==============================================
   // Methods
   //==============================================

   /**
    * Aplica las reglas de presentación para obtener la plantilla adecuada.
    * 
    * @param browserAgent Cadena de texto proporcionada por el navegador cliente.
    * @return Una instancia de {@link Template} que representa la plantilla a aplicar.
    * 
    * @throws TemplateUnavailableException 
    */
   public Template checkRules(String browserAgent) throws TemplateUnavailableException, TemplateLoadException
   {
      // Aplica las reglas
      for (Rule rule : this.rules)
      {
         if (rule.matchRule(browserAgent))
         {
            return new Template(this.context, rule.getTemplateId());
         }
      }

      // Aplica la plantilla por defecto
      if (this.defaultTemplateId > 0)
      {
         return new Template(this.context, this.defaultTemplateId);
      }

      // No se ha encontrado plantilla: se genera una excepción
      throw new TemplateUnavailableException();
   }


   //==============================================
   // Private members
   //==============================================

   /**
    * Carega las reglas de aplicación de plantillas.
    * 
    * @param context Contexto del servidor web.
    * 
    * @throws RulesLoadException 
    */
   private void loadRules(ServletContext context) throws RulesLoadException
   {
      Node nNode;
      Element eElement;
      NodeList nList;
      Rule rule;
      InputStream is = null;

      try
      {
         is = new FileInputStream(context.getRealPath("/templates/" + Rules.RULES_FILENAME));

         DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
         DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
         Document doc = dBuilder.parse(is);
         doc.getDocumentElement().normalize();

         // Comprueba 
         nList = doc.getElementsByTagName(Rules.XML_NODE_RULES);
         nNode = nList.item(0);
         if (nNode.getNodeType() == Node.ELEMENT_NODE && nNode.getNodeName().equals(Rules.XML_NODE_RULES))
         {
            eElement = (Element) nNode;
            this.defaultTemplateId = Integer.parseInt(eElement.getAttribute(Rules.XML_ATT_DEFAULTTMPLT));

            if (eElement.getAttribute(Rules.XML_ATT_DEFINITIONVER).compareTo(Rules.XML_DEFINITION_VERSION) < 0)
            {
               throw new RulesLoadException("File defining rules must be compatible with version " + Rules.XML_DEFINITION_VERSION + " or higher");
            }
         }

         // Obtiene los valores de configuración clave/valor
         nList = doc.getElementsByTagName(Rules.XML_NODE_RULE);
         for (int temp = 0; temp < nList.getLength(); temp++) 
         {
            nNode = nList.item(temp);
            if (nNode.getNodeType() == Node.ELEMENT_NODE)
            {
               eElement = (Element) nNode;
               rule = new Rule();
               rule.setType(Rule.RuleType.BrowserAgent);
               rule.setPattern(eElement.getAttribute(Rules.XML_ATT_CONTAINS));
               rule.setTemplateId(Integer.parseInt(eElement.getAttribute(Rules.XML_ATT_TEMPLATE)));

               this.rules.add(rule);
            }
         }
      }
      catch (FileNotFoundException ex)
      {
         throw new RulesLoadException(ex.getMessage(), ex);
      }
      catch (ParserConfigurationException ex)
      {
         throw new RulesLoadException(ex.getMessage(), ex);
      }
      catch (SAXException ex)
      {
         throw new RulesLoadException(ex.getMessage(), ex);
      }
      catch (IOException ex)
      {
         throw new RulesLoadException(ex.getMessage(), ex);
      }
      finally
      {
         IOUtils.closeStream(is);
      }
   }
}
