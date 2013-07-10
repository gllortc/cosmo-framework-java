package com.cosmo.ui.templates;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Representa una especificació de control en una plantilla.
 * 
 * @author Gerard Llort
 */
public class TemplateControl 
{
   private String id;
   private ArrayList<TemplateScript> scripts;
   private ArrayList<TemplateLink> links;
   private HashMap<String, String> parts;
   
   //==============================================
   // Constructors
   //==============================================
   
   /**
    * Constructor de la clase.
    * 
    * @param id Identificador único del control.
    */
   public TemplateControl(String id)
   {
      this.id = id;
      this.scripts = new ArrayList<TemplateScript>();
      this.links = new ArrayList<TemplateLink>();
      this.parts = new HashMap<String, String>();
   }

   //==============================================
   // Properties
   //==============================================
   
   public String getId() 
   {
      return id;
   }

   public void setId(String id) 
   {
      this.id = id;
   }
   
   //==============================================
   // Methods
   //==============================================
   
   public void addScript(TemplateScript script)
   {
      this.scripts.add(script);
   }
   
   public void addLink(TemplateLink link)
   {
      this.links.add(link);
   }
   
   /**
    * Añade una parte del control.
    * 
    * @param id Identificador único del control.
    * @param xhtml Código XHTML correspondiente a la parte del control.
    */
   public void addControlPart(String id, String xhtml)
   {
      this.parts.put(id, xhtml);
   }
   
   /**
    * Obtiene un elemento del control.
    * 
    * @param id El identificador de la parte que se desea obtener.
    * @return Una cadena de texto que contiene el código XHTML que corresponde a la parte del control solicitada.
    */
   public String getElement(String id)
   {
      return this.parts.get(id);
   }
   
   /**
    * Obtiene una lista de los links necesarios para el control.
    * 
    * @return Un array de instancias de {@link TemplateLink} que representa la lista de links necesarios para el control.
    */
   public ArrayList<TemplateLink> getLinks()
   {
      return this.links;
   }
   
   /**
    * Obtiene una lista de los scripts necesarios para el control.
    * 
    * @return Un array de instancias de {@link TemplateScript} que apunta a la lista de scripts necesarios para el control.
    */
   public ArrayList<TemplateScript> getScripts()
   {
      return this.scripts;
   }
}
