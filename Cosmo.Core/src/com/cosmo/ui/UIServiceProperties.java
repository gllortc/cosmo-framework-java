package com.cosmo.ui;

import java.util.ArrayList;

import javax.servlet.ServletContext;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.cosmo.ui.templates.Rule;
import com.cosmo.ui.templates.Template;
import com.cosmo.ui.templates.TemplateLoadException;
import com.cosmo.ui.templates.TemplateUnavailableException;
import com.cosmo.util.StringUtils;

/**
 * Contenedor encargado de leer y almacenar toda la configuración de UI Services.
 * 
 * @author Gerard Llort
 */
public class UIServiceProperties
{
   // Definición de tags y atributos para UI Services
   private static final String XML_UI_TAG = "ui-services";
   private static final String XML_UI_TAG_TEMPLATE_RULE = "template-rule";
   private static final String XML_UI_ATT_DEFAULTTEMPLATE = "default-template";
   private static final String XML_UI_ATT_RULE_CONTAINS = "contains";
   private static final String XML_UI_ATT_RULE_TEMPLATE = "template";

   // Declaración de variables locales para UI Services
   private String defaultTemplateId;
   private ArrayList<Rule> templateRules;
   


   //==============================================
   // Constructors
   //==============================================

   /**
    * Constructor de la clase {@link UIServiceProperties}.
    * 
    * @param xmlDocument Una instancia de {@link Document} que representa el documento XML de configuración de Cosmo.
    */
   public UIServiceProperties(Document xmlDocument)
   {
      this.defaultTemplateId = "";
      this.templateRules = new ArrayList<Rule>();

      loadProperties(xmlDocument);
   }


   //==============================================
   // Properties
   //==============================================

   /**
    * Devuelve el identificador de la plantilla a usar por defecto 
    * (cuando no se encuentra ninguna regla de presentación aplicable).
    */
   public String getDefaultTemplateId()
   {
      return this.defaultTemplateId;
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
   public Template checkRules(ServletContext context, String browserAgent) throws TemplateUnavailableException, TemplateLoadException
   {
      // Aplica las reglas
      for (Rule rule : this.templateRules)
      {
         if (rule.matchRule(browserAgent))
         {
            return new Template(context, rule.getTemplateId());
         }
      }

      // Aplica la plantilla por defecto
      if (StringUtils.isNullOrEmptyTrim(this.defaultTemplateId))
      {
         return new Template(context, this.defaultTemplateId);
      }

      // No se ha encontrado plantilla: se genera una excepción
      throw new TemplateUnavailableException();
   }

   /**
    * Lee y almacena las propiedades de configuración de UI Services.
    * 
    * @param doc Una instancia de {@link Document} que contiene la configuración (XML) de la aplicación.
    */
   public void loadProperties(Document doc)
   {
      Node nNode;
      Element eElement;
      NodeList nList;

      this.templateRules = new ArrayList<Rule>();

      nList = doc.getElementsByTagName(UIServiceProperties.XML_UI_TAG);
      if (nList.getLength() >= 1)
      {
         nNode = nList.item(0);
         if (nNode.getNodeType() == Node.ELEMENT_NODE)
         {
            eElement = (Element) nNode;
            this.defaultTemplateId = eElement.getAttribute(UIServiceProperties.XML_UI_ATT_DEFAULTTEMPLATE);
         }

         nList = doc.getElementsByTagName(UIServiceProperties.XML_UI_TAG_TEMPLATE_RULE);
         for (int temp = 0; temp < nList.getLength(); temp++) 
         {
            nNode = nList.item(temp);
            if (nNode.getNodeType() == Node.ELEMENT_NODE)
            {
               eElement = (Element) nNode;

               this.templateRules.add(new Rule(Rule.RuleType.BrowserAgent, 
                                               eElement.getAttribute(UIServiceProperties.XML_UI_ATT_RULE_CONTAINS), 
                                               eElement.getAttribute(UIServiceProperties.XML_UI_ATT_RULE_TEMPLATE)));
            }
         }
      }
   }
}
