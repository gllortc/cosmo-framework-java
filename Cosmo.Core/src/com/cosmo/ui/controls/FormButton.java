package com.cosmo.ui.controls;

import com.cosmo.Workspace;

/**
 * Implementa un botón de formulario.
 * 
 * @author Gerard Llort
 */
public class FormButton  extends FormField
{
   private String name;
   private String label;
   private String href = "";
   private ButtonType type;

   /**
    * Enumera los distintos tipos de botón de formulario soportados.
    */
   public enum ButtonType
   {
      /** Envia la información */ 
      Submit,
      /** Borra todos los datos del formulario en el cliente */ 
      Reset,
      /** Botón que actúa de enlace */ 
      Link,
      /** NO SOPORTADO: Ejecuta una acción JavaScript */ 
      JsAction
   }


   //==============================================
   // Constructors
   //==============================================

   /**
    * Constructor de la clase.
    * 
    * @param label Cadena de texto que será visible en el botón.
    * @param type Tipo de botón.
    */
   public FormButton(String name, String label, ButtonType type)
   {
      this.name = name;
      this.label = label;
      this.href = "";
      this.type = type;
   }

   /**
    * Constructor de la clase.
    * 
    * @param name Una cadena que contiene el nombre único del control.
    * @param label Cadena de texto que será visible en el botón.
    * @param href Una cadena que contiene la URL de destino para el enlace.
    */
   public FormButton(String name, String label, String href)
   {
      this.name = name;
      this.label = label;
      this.href = href;
      this.type = ButtonType.Link;
   }


   //==============================================
   // Properties
   //==============================================

   @Override
   public String getName() 
   {
      return name;
   }

   public void setName(String name) 
   {
      this.name = name;
   }

   public String getLabel() 
   {
      return label;
   }

   public void setLabel(String label) 
   {
      this.label = label;
   }

   public ButtonType getType() 
   {
      return type;
   }

   public void setType(ButtonType type) 
   {
      this.type = type;
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
    * Establece el valor del campo.
    */
   @Override
   public void setValue(Object value)
   {
      // No aplica en este tipo de control
      // Se omite la llamada a este método
   }

   /**
    * Convierte la instancia en una cadena XHTML que representa el elemento en una página web.
    */
   @Override
   public String render(Workspace workspace) 
   {
      return toString();
   }

   @Override
   public String toString()
   {
      String btnType = "";
      StringBuilder sb = new StringBuilder();

      if (this.type == ButtonType.JsAction || this.type == ButtonType.Submit || this.type == ButtonType.Reset)
      {      
         switch (this.type)
         {
            case Submit:   btnType = "submit";  break;
            case Reset:    btnType = "reset";   break;
            default:       btnType = "button";  break;
         }

         sb.append("<input type=\"").append(btnType).append("\" id=\"").append(this.name).append("\" name=\"").append(this.name).append("\" value=\"").append(this.label).append("\" />");
      }
      else
      {
         sb.append("<a class=\"button\" href=\"").append(this.href).append("\">").append(this.label).append("</a>");
      }

      return sb.toString();
   }
}
