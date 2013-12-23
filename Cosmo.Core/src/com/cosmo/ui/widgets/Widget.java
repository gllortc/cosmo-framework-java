package com.cosmo.ui.widgets;

import com.cosmo.Workspace;
import com.cosmo.ui.templates.TemplateUnavailableException;
import java.text.DecimalFormat;

/**
 * Clase abstracta que deben implementar los widgets de página.
 * 
 * @author Gerard Llort
 */
public abstract class Widget 
{
   private String id;
   private Workspace workspace;


   //==============================================
   // Constructors
   //==============================================

   /**
    * Constructor de la clase {@link Widget}.
    * 
    * @param workspace Una instancia de {@link Workspace} que representa el workspace actual.
    */
   public Widget(Workspace workspace)
   {
      this.workspace = workspace;
   }


   //==============================================
   // Properties
   //==============================================

   /**
    * Devuelve un identificador único del tipo de widget.
    */
   public abstract String getWidgetTypeId();

   /**
    * Devuelve un identificador único de widget.
    * Cada widget de una página tendrá un ID único.
    */
   public String getWidgetId() 
   {
      return this.id;
   }

   /**
    * Devuelve el workspace actual.
    */
   public Workspace getWorkspace() 
   {
      return this.workspace;
   }

   /**
    * Establece el identificador único del widget.
    */
   public void setWidgetId(int id) 
   {
      DecimalFormat df = new DecimalFormat("0000000");
      this.id = "CtrlId" + df.format(id);
   }


   //==============================================
   // Methods
   //==============================================

   /**
    * Renderiza el widget y genera el código XHTML de representación.
    *
    * @return Devuelve una cadena en formato XHTML que representa el widget.
    * 
    * @throws TemplateUnavailableException
    */
   public abstract String render() throws TemplateUnavailableException;


   //==============================================
   // Static members
   //==============================================

   public static String getTag(String name)
   {
      return "[@" + name.trim().toUpperCase() + "]";
   }

   public static void replaceTag(StringBuilder sb, String tag, String text)
   {
      int index = sb.indexOf(tag);
      sb.replace(index, index + tag.length(), text);
   }
}
