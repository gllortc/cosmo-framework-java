package com.cosmo.ui.controls;

import javax.servlet.http.HttpSession;

import com.cosmo.ui.templates.Template;
import com.cosmo.util.StringUtils;

/**
 * Interface que debe implementar cualquier control de contenido.<br />
 * Los controles de contenido son componentes que combinados entre sí, rellenan la zona de contenidos.
 * 
 * @author Gerard Llort
 */
public abstract class Control
{
   private String id;

   //==============================================
   // Constructors
   //==============================================
   
   /**
    * Constructor de la clase.
    */
   public Control(String id)
   {
      this.id = id;
   }
   
   //==============================================
   // Properties
   //==============================================
   
   /**
    * Devuelve un identificador único del tipo de control.
    */
   public abstract String getControlTypeId();
   
   /**
    * Devuelve un identificador único de control.
    * Cada control de una página tendrá un ID único.
    */
   public String getId() 
   {
      return id;
   }

   /**
    * Establece el identificador único del control.
    */
   public void setId(String id) 
   {
      this.id = id;
   }
   
   //==============================================
   // Methods
   //==============================================

   /**
    * Renderiza el control y genera el código XHTML de representación.
    *
    * @return Devuelve una cadena en formato XHTML que representa el control. 
    */
   public abstract String render(HttpSession session, Template template);
   
   //==============================================
   // Static members
   //==============================================
   
   /**
    * Reemplaza un TAG de contenido.
    * Soporta TAGs condicionales.
    * 
    * @param sb Una instancia de {@link StringBuilder} que contiene el texto a evaluar.
    * @param tag Una cadena que contiene el TAG a reemplazar. El TAG se especifica sólo con carácteres alfanuméricos, sin formato final.
    * @param text Texto que debe reemplazar el TAG.
    */
   public static void replaceTag(StringBuilder sb, String tag, String text)
   {
      int idx;
      String cTag = getTag(tag);
      String cTagBegin = getConditionalTagBegin(tag);
      String cTagEnd = getConditionalTagEnd(tag);
      
      idx = sb.indexOf(cTagBegin);
      
      // Evita contenido nulo
      if (text == null)
      {
         text = "";
      }
      
      // Existe condicional y hay contenido
      if (idx >= 0 && !StringUtils.isNullOrEmpty(text))
      {
         sb.delete(idx, idx + cTagBegin.length());
         
         idx = sb.indexOf(cTagEnd);
         sb.delete(idx, idx + cTagEnd.length());
         
         idx = sb.indexOf(cTag);
         sb.replace(idx, idx + cTag.length(), text);
         
         return;
      }
      
      // Existe condicional y NO hay contenido
      if (idx >= 0 && StringUtils.isNullOrEmpty(text))
      {
         sb.delete(idx, idx + cTagBegin.length());
         
         idx = sb.indexOf(cTagEnd);
         sb.delete(idx, idx + cTagEnd.length());
         
         idx = sb.indexOf(cTagEnd);
         sb.delete(idx, idx + cTag.length());
         
         return;
      }
      
      // No existe condicional (el contenido se reemplaza tanto si lo hay como si no)
      idx = sb.indexOf(cTag);
      if (idx < 0)
      {
         sb.replace(idx, idx + cTag.length(), text);
      }
   }
   
   /**
    * Reemplaza un TAG de contenido.
    * Soporta TAGs condicionales.
    * 
    * @param xhtml Una instancia de {@link StringBuilder} que contiene el texto a evaluar.
    * @param tag Una cadena que contiene el TAG a reemplazar. El TAG se especifica sólo con carácteres alfanuméricos, sin formato final.
    * @param text Texto que debe reemplazar el TAG.
    */
   public static String replaceTag(String xhtml, String tag, String text)
   {
      int idx;
      String cTag = Control.getTag(tag);
      String cTagBegin = Control.getConditionalTagBegin(tag);
      String cTagEnd = Control.getConditionalTagEnd(tag);

      StringBuilder sb = new StringBuilder(xhtml);
      
      // Existe condicional y hay contenido
      if (xhtml.contains(cTagBegin) && !StringUtils.isNullOrEmpty(text))
      {
         idx = sb.indexOf(cTagBegin);
         sb.delete(idx, idx + cTagBegin.length());
         
         idx = sb.indexOf(cTagEnd);
         sb.delete(idx, idx + cTagEnd.length());
         
         idx = sb.indexOf(cTag);
         sb.replace(idx, idx + cTag.length(), text);

         return sb.toString();
      }
      
      // Existe condicional y NO hay contenido
      if (xhtml.contains(cTagBegin) && StringUtils.isNullOrEmpty(text))
      {
         sb.delete(sb.indexOf(cTagBegin), sb.indexOf(cTagEnd) + cTagEnd.length());
         return sb.toString();
      }
      
      // No existe condicional (el contenido se reemplaza tanto si lo hay como si no)
      if (xhtml.contains(cTag))
      {
         xhtml = xhtml.replace(cTag, text);
         
         return xhtml;
      }
      
      return xhtml;
   }
   
   //==============================================
   // Private members
   //==============================================

   private static String getTag(String name)
   {
      return "[@" + name.trim().toUpperCase() + "]";
   }
   
   private static String getConditionalTagBegin(String name)
   {
      return "[#IF " + name.trim().toUpperCase() + "]";
   }
   
   private static String getConditionalTagEnd(String name)
   {
      return "[#ENDIF " + name.trim().toUpperCase() + "]";
   }
}
