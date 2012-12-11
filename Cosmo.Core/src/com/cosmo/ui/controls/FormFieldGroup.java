package com.cosmo.ui.controls;

import java.util.ArrayList;
import java.util.Iterator;

import javax.servlet.http.HttpSession;

/**
 * Implementa un grupo de controles de un formulario web.
 */
public class FormFieldGroup extends FormField
{
   private String name;
   private String title;
   private String description;
   private ArrayList<FormField> fields;
   // private FormControl control;
   private boolean haveCaptcha;

   //==============================================
   // Constructors
   //==============================================
   
   /**
    * Constructor de la clase.
    * 
    * @param title TÃ­tulo visible del grupo de controles.
    * @param control La instancia de {@link FormControl} al que pertenece.
    */
   public FormFieldGroup(String title)
   {
      this.title = title;
      this.description = "";
      this.fields = new ArrayList<FormField>();
      // this.control = control;
      this.haveCaptcha = false;
   }
   
   /**
    * Constructor de la clase.
    * 
    * @param title TÃ­tulo visible del grupo de controles.
    * @param description DescripciÃ³n del contenido del grupo.
    * @param control La instancia de {@link FormControl} al que pertenece.
    */
   public FormFieldGroup(String title, String description, FormControl control)
   {
      this.title = title;
      this.description = description;
      this.fields = new ArrayList<FormField>();
      // this.control = control;
      this.haveCaptcha = false;
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
   
   public String getTitle() 
   {
      return title;
   }

   public void setTitle(String title) 
   {
      this.title = title;
   }

   public String getDescription() 
   {
      return description;
   }

   public void setDescription(String description) 
   {
      this.description = description;
   }
   
   public boolean haveCaptcha()
   {
      return this.haveCaptcha;
   }
   
   /**
    * Devuelve un iterador que permite recorrer los campos de un grupo.
    */
   public Iterator<FormField> getFields()
   {
      return this.fields.iterator();
   }
   
   //==============================================
   // Methods
   //==============================================
   
   /**
    * Establece el valor del campo.
    */
   @Override
   public void setValue(String value)
   {
      // No aplica en este tipo de control
      // Se omite la llamada a este método
   }
   
   /**
    * Agrega un nuevo campo al formulario.<br />
    * NOTA: Un grupo (y formulario) sólo puede contener un control {@link FormFieldCaptcha}.
    * 
    * @param field Una instancia de un objeto que extienda {@link FormField}.
    */
   public void addField(FormField field) // throws MalformedFormException
   {
      /*
      // Evita incorporar más de un campo captcha en un grupo
      if (field instanceof FormFieldCaptcha)
      {
         if (this.haveCaptcha)
         {
            throw new MalformedFormException("Only ONE captcha field is allowen in a form.");
         }
         else
         {
            this.haveCaptcha = true;
         }
      }*/

      fields.add(field);
   }
   
   /**
    * Convierte la instancia en una cadena XHTML que representa el elemento en una página web.
    */
   @Override
   public String render(HttpSession session) 
   {
      return toString();
   }
   
   @Override
   public String toString()
   {
      StringBuilder str = new StringBuilder();
      
      str.append("    <fieldset name=\"").append(this.name).append("\">").append("\n");
      str.append("    <legend>").append(this.title).append("</legend>").append("\n");
      for (FormField field : this.fields)
      {
         if (field instanceof FormFieldHidden)
         {
            str.append(field.render(null)).append("\n");
         }
      }
      for (FormField field : this.fields)
      {
         if (!(field instanceof FormFieldHidden))
         {
            str.append(field.render(null)).append("\n");
         }
      }
      str.append("    </fieldset>");
      
      return str.toString();
   }
}
