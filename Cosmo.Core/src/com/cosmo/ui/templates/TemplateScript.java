package com.cosmo.ui.templates;

/**
 * Implementa un elemento SCRIPT que se debe incorporar a la página final.
 * 
 * @author Gerard Llort
 */
public class TemplateScript 
{
   /**
    * Enumera los tipos de scripts soportados.
    */
   public enum ScriptType
   {
      /** Incluye el código JavaScript */
      Code,
      /** Referencia a un archivo {@code .js} (ya sea interno o accedido por HTTP) */
      Referenced
   }

   // Declaración de variables internas
   private ScriptType type;
   private String script;


   //==============================================
   // Constructors
   //==============================================

   /**
    * Constructor de la clase {@link TemplateScript}.
    * 
    * @param type Tipo de script.
    * @param script Si el tipo es {@code Code} entonces este parámetro contiene el código JavaScript a incorporar. Si el tipo es {@code Referenced}
    *    entonces contiene la URL al script a incorporar.
    */   
   public TemplateScript(ScriptType type, String script)
   {
      this.type = type;
      this.script = script;
   }


   //==============================================
   // Properties
   //==============================================

   public ScriptType getType() 
   {
      return type;
   }

   public void setType(ScriptType type) 
   {
      this.type = type;
   }

   public String getScript() 
   {
      return script;
   }

   public void setScript(String script) 
   {
      this.script = script;
   }


   //==============================================
   // Methods
   //==============================================

   /**
    * Transforma el script a código XHTML.
    * 
    * @return Una cadena de texto que contiene el código XHTML.
    */
   public String render()
   {
      String xhtml;

      xhtml = "";
      xhtml += "<script ";
      xhtml += "type=\"text/javascript\" ";
      if (this.type == ScriptType.Referenced)
      {
         xhtml += "src=\"" + this.script + "\" ";
      }
      xhtml += ">";
      if (this.type == ScriptType.Code)
      {
         xhtml += "\n" + this.script + "\n";   
      }
      xhtml += "</script>\n";

      return xhtml;
   }

   @Override
   public String toString()
   {
      return render();
   }
}
