package com.cosmo.ui.controls;

import java.util.UUID;

import com.cosmo.Cosmo;
import com.cosmo.Workspace;
import com.cosmo.util.StringUtils;

/**
 * Clase abstracta que deben implementar los controles de página.
 * 
 * @author Gerard Llort
 */
public abstract class Control
{
   // Declaración de variables internas
   private String id;
   private String uuid;
   private Workspace workspace;


   //==============================================
   // Constructors
   //==============================================

   /**
    * Constructor de la clase.<br />
    * Usar este constructor si el control es estático y no modifica sus valores nunca.
    */
   public Control(Workspace workspace)
   {
      this.id = this.uuid = UUID.randomUUID().toString();
      this.workspace = workspace;
   }

   /**
    * Constructor de la clase.<br />
    * Usar este constructor si el control es dinámico y puede alterar sus valores (por ejemplo, los valores de un GRID).
    * 
    * @param workspace Una instancia de {@link Workspace} que representa el workspace actual.
    * @param id Identificador único del control.
    */
   public Control(Workspace workspace, String id)
   {
      this.id = id;
      this.uuid = UUID.randomUUID().toString();
      this.workspace = workspace;
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

   /**
    * Devuelve un UUID único para el control.
    */
   public String getUuid()
   {
      return this.uuid;
   }

   /**
    * Devuelve la instancia de {@link Workspace} usada para generar el control.
    */
   public Workspace getWorkspace()
   {
      return this.workspace;
   }


   //==============================================
   // Methods
   //==============================================

   /**
    * Renderiza el control y genera el código XHTML de representación.
    *
    * @return Devuelve una cadena en formato XHTML que representa el control. 
    */
   public abstract String render();

   /**
    * Genera una clave para almacenar/recuperar los datos de un control en caché de sessión de usuario.
    * 
    * @return Una cadena de texto que corresponde a la clave de acceso a los datos del control en caché.
    */
   public String getSessionControlDataKey()
   {
      return Cosmo.KEY_CACHE_SESSION_CTRLDATA + this.uuid;
   }


   //==============================================
   // Static members
   //==============================================

   /**
    * Reemplaza un TAG de contenido.
    * Soporta TAGs condicionales.
    * 
    * @param sb Una instancia de {@link StringBuilder} que contiene el texto a evaluar.
    * @param tag Una cadena que contiene el TAG a reemplazar. El TAG se especifica sólo con carácteres alfanuméricos, sin formato final.
    * @param replaceWith Texto que debe reemplazar el TAG.
    */
   public static void replaceTag(StringBuilder sb, String tag, String replaceWith)
   {
      int idx;
      String cTag = getTag(tag);
      String cTagBegin = getConditionalTagBegin(tag);
      String cTagEnd = getConditionalTagEnd(tag);

      idx = sb.indexOf(cTagBegin);

      // Evita contenido nulo
      if (replaceWith == null)
      {
         replaceWith = "";
      }

      // Existe condicional y hay contenido
      if (idx >= 0 && !StringUtils.isNullOrEmpty(replaceWith))
      {
         sb.delete(idx, idx + cTagBegin.length());

         idx = sb.indexOf(cTagEnd);
         if (idx >= 0)
         {
            sb.delete(idx, idx + cTagEnd.length());
         }

         idx = sb.indexOf(cTag);
         {
            sb.replace(idx, idx + cTag.length(), replaceWith);
         }

         return;
      }

      // Existe condicional y NO hay contenido
      if (idx >= 0 && StringUtils.isNullOrEmpty(replaceWith))
      {
         sb.delete(idx, idx + cTagBegin.length());

         idx = sb.indexOf(cTagEnd);
         if (idx >= 0)
         {
            sb.delete(idx, idx + cTagEnd.length());
         }

         idx = sb.indexOf(cTagEnd);
         if (idx >= 0)
         {
            sb.delete(idx, idx + cTag.length());
         }

         return;
      }

      // No existe condicional (el contenido se reemplaza tanto si lo hay como si no)
      idx = sb.indexOf(cTag);
      if (idx >= 0)
      {
         sb.replace(idx, idx + cTag.length(), replaceWith);
      }
   }

   /**
    * Reemplaza un TAG de contenido.
    * Soporta TAGs condicionales.
    * 
    * @param xhtml Una instancia de {@link StringBuilder} que contiene el texto a evaluar.
    * @param tag Una cadena que contiene el TAG a reemplazar. El TAG se especifica sólo con carácteres alfanuméricos, sin formato final.
    * @param replaceWith Texto que debe reemplazar el TAG.
    */
   public static String replaceTag(String xhtml, String tag, String replaceWith)
   {
      int idx;
      String cTag = Control.getTag(tag);
      String cTagBegin = Control.getConditionalTagBegin(tag);
      String cTagEnd = Control.getConditionalTagEnd(tag);

      if (xhtml == null)
      {
         return StringUtils.EMPTY;
      }
      if (tag == null)
      {
         return xhtml;
      }
      if (replaceWith == null)
      {
         replaceWith = StringUtils.EMPTY;
      }

      StringBuilder sb = new StringBuilder(xhtml);

      // Existe condicional y hay contenido
      if (xhtml.contains(cTagBegin) && !StringUtils.isNullOrEmpty(replaceWith))
      {
         idx = sb.indexOf(cTagBegin);
         if (idx >= 0)
         {
            sb.delete(idx, idx + cTagBegin.length());
         }
         
         idx = sb.indexOf(cTagEnd);
         if (idx >= 0)
         {
            sb.delete(idx, idx + cTagEnd.length());
         }
         
         idx = sb.indexOf(cTag);
         if (idx >= 0)
         {
            sb.replace(idx, idx + cTag.length(), replaceWith);
         }

         return sb.toString();
      }

      // Existe condicional y NO hay contenido
      if (xhtml.contains(cTagBegin) && StringUtils.isNullOrEmpty(replaceWith))
      {
         sb.delete(sb.indexOf(cTagBegin), sb.indexOf(cTagEnd) + cTagEnd.length());
         return sb.toString();
      }

      // No existe condicional (el contenido se reemplaza tanto si lo hay como si no)
      if (xhtml.contains(cTag))
      {
         xhtml = xhtml.replace(cTag, replaceWith);
         return xhtml;
      }

      return xhtml;
   }


   //==============================================
   // Private members
   //==============================================

   /**
    * Genera un TAG concreto (formato {@code [@TAG]}).
    * <br /><br />
    * Ejemplo de uso:<br />
    * {@code [#IF TITLE]Este es el [@TITLE] del documento[#ENDIF TITLE]}
    * 
    * @param name Nombre del TAG.
    * 
    * @return Una cadena que contiene el TAG correctamente formateado.
    */
   private static String getTag(String name)
   {
      return "[@" + name.trim().toUpperCase() + "]";
   }

   /**
    * Genera un TAG condicional de inicio (formato {@code [#IF TAG]}).
    * <br /><br />
    * Ejemplo de uso:<br />
    * {@code [#IF TITLE]Este es el [@TITLE] del documento[#ENDIF TITLE]}
    * 
    * @param name Nombre del TAG.
    * 
    * @return Una cadena que contiene el TAG correctamente formateado.
    */
   private static String getConditionalTagBegin(String name)
   {
      return "[#IF " + name.trim().toUpperCase() + "]";
   }

   /**
    * Genera un TAG condicional de cierre (formato {@code [#ENDIF TAG]}).
    * <br /><br />
    * Ejemplo de uso:<br />
    * {@code [#IF TITLE]Este es el [@TITLE] del documento[#ENDIF TITLE]}
    * 
    * @param name Nombre del TAG.
    * 
    * @return Una cadena que contiene el TAG correctamente formateado.
    */
   private static String getConditionalTagEnd(String name)
   {
      return "[#ENDIF " + name.trim().toUpperCase() + "]";
   }
}
