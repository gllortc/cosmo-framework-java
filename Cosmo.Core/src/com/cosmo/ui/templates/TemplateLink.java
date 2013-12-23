package com.cosmo.ui.templates;

/**
 * Implementa un elemento LINK que se debe incorporar a la página final.
 * 
 * @author Gerard Llort
 */
public class TemplateLink 
{
   public static final String LINK_REL_STYLESHEET = "stylesheet";
   public static final String LINK_REL_ICON = "icon";

   public static final String LINK_TYPE_CSS = "text/css";
   public static final String LINK_TYPE_ICON = "image/x-icon";

   // Declaración de variables internas
   private String rel;
   private String type;
   private String href;


   //==============================================
   // Constructors
   //==============================================

   /**
    * Constructor de la clase {@link TemplateLink}.
    */
   public TemplateLink()
   {
      this.rel = "";
      this.type = "";
      this.href = "";
   }

   /**
    * Constructor de la clase {@link TemplateLink}.
    * 
    * @param rel Una cadena que contiene el parámetro <em>rel</em> del <em>tag</em> LINK de HTML.
    * @param type Una cadena que contiene el tipo de <em>tag</em> LINK de HTML.
    * @param href Una cadena que contiene la URL asociada al <em>tag</em> LINK de HTML.
    */
   public TemplateLink(String rel, String type, String href)
   {
      this.rel = rel;
      this.type = type;
      this.href = href;
   }

   /**
    * Constructor de la clase {@link TemplateLink}.
    * 
    * @param rel Una cadena que contiene el parámetro <em>rel</em> del <em>tag</em> LINK de HTML.
    * @param type Una cadena que contiene el tipo de <em>tag</em> LINK de HTML.
    * @param href Una cadena que contiene la URL asociada al <em>tag</em> LINK de HTML.
    * @param media Una cadena que describe el tipo de medio al que se debe aplicar el elemento LINK.
    */
   public TemplateLink(String rel, String type, String href, String media)
   {
      this.rel = rel;
      this.type = type;
      this.href = href;
   }


   //==============================================
   // Properties
   //==============================================

   public String getRel() 
   {
      return rel;
   }

   public void setRel(String rel) 
   {
      this.rel = rel.trim().toLowerCase();
   }

   public String getType() 
   {
      return type;
   }

   public void setType(String type) 
   {
      this.type = type.trim().toLowerCase();
   }

   public String getHref() 
   {
      return href;
   }

   public void setHref(String href) 
   {
      this.href = href;
   }


   //==============================================
   // Methods
   //==============================================

   /**
    * Transforma el link a código XHTML.
    * 
    * @return Una cadena de texto que contiene el código XHTML.
    */
   public String render()
   {
      StringBuilder sb = new StringBuilder();

      sb.append("<link ");
      sb.append("rel=\"" + this.rel + "\" ");
      sb.append("type=\"" + this.type + "\" ");
      sb.append("href=\"" + this.href + "\" ");
      sb.append("></link>\n");

      return sb.toString();
   }

   @Override
   public String toString()
   {
      return render();
   }
}
