package com.cosmo.ui.templates;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 *
 * @author gllort
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
    * Obtiene un iterador que permite recorrer los links necesarios para el control.
    * 
    * @return Una instancia de {@link Iterator} que apunta a la lista de links necesarios para el control.
    */
   public Iterator<TemplateLink> getLinks()
   {
      return this.links.iterator();
   }
   
   /**
    * Obtiene un iterador que permite recorrer los scripts necesarios para el control.
    * 
    * @return Una instancia de {@link Iterator} que apunta a la lista de scripts necesarios para el control.
    */
   public Iterator<TemplateScript> getScripts()
   {
      return this.scripts.iterator();
   }
}
