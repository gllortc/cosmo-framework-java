package com.cosmo.ui.controls;

import com.cosmo.Workspace;

/**
 * Implementa un cuadro de texto representable dentro de un formulario Cosmo.
 * 
 * @author Gerard Llort
 */
public class FormFieldEMail extends FormField
{
   private boolean required;
   private String name;
   private String value;
   private String label;
   private String description;
   private int maxLength;
   
   //==============================================
   // Contructors
   //==============================================
   
   /**
    * Contructor de la clase.
    * 
    * @param name Nombre identificativo del elemento dentro de la página.
    * @param label Etiqueta que se mostrará junto el control.
    */
   public FormFieldEMail(String name, String label) 
   {
      this.required = false;
      this.name = name;
      this.label = label;
      this.description = "";
      this.value = "";
      this.maxLength = -1;
   }
   
   /**
    * Contructor de la clase.
    * 
    * @param name Nombre identificativo del elemento dentro de la página.
    * @param label Etiqueta que se mostrará junto el control.
    */
   public FormFieldEMail(String name, String label, boolean required) 
   {
      this.name = name;
      this.label = label;
      this.description = "";
      this.value = "";
      this.maxLength = -1;
      this.required = required;
   }
   
   /**
    * Contructor de la clase.
    * 
    * @param name Nombre identificativo del elemento dentro de la página.
    * @param label Etiqueta que se mostrará junto el control.
    * @param maxLength Número máximo de carácteres que admite el control.
    */
   public FormFieldEMail(String name, String label, int maxLength) 
   {
      this.name = name;
      this.label = label;
      this.description = "";
      this.value = "";
      this.maxLength = maxLength;
      this.required = false;
   }
   
   /**
    * Contructor de la clase.
    * 
    * @param name Nombre identificativo del elemento dentro de la página.
    * @param label Etiqueta que se mostrará junto el control.
    * @param maxLength Número máximo de carácteres que admite el control.
    * @param required Indica si el campo será de texto oculto.
    */
   public FormFieldEMail(String name, String label, int maxLength, boolean required) 
   {
      this.name = name;
      this.label = label;
      this.description = "";
      this.value = "";
      this.maxLength = maxLength;
      this.required = required;
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

   public String getValue() 
   {
      return value;
   }

   @Override
   public void setValue(Object value) 
   {
      this.value = (String) value;
   }

   public String getLabel() 
   {
      return label;
   }

   public void setLabel(String label) 
   {
      this.label = label;
   }

   public String getDescription() 
   {
      return description;
   }

   public void setDescription(String description) 
   {
      this.description = description;
   }
   
   public int getMaxLength() 
   {
      return maxLength;
   }

   public void setMaxLength(int maxLength) 
   {
      this.maxLength = maxLength;
   }

   public boolean isRequired() 
   {
      return required;
   }

   public void setRequired(boolean required) 
   {
      this.required = required;
   }
   
   //==============================================
   // Methods
   //==============================================
   
   /**
    * Convierte el campo en un TAG XHTML.
    */
   @Override
   public String render(Workspace workspace)
   {
      StringBuilder sb = new StringBuilder();

      sb.append("<input type=\"email\" ").
         append("id=\"").append(this.name).append("\" ").
         append("name=\"").append(this.name).append("\" ").
         append("value=\"").append(this.value).append("\" ").
         append(required ? "required" : "").
         append("/>");

      return sb.toString();
   }
   
   /**
    * Convierte la instancia en una cadena de texto.
    */
   @Override
   public String toString()
   {
      StringBuilder sb = new StringBuilder();
      
      sb.append("    <div>").append("\n");
      sb.append("      <label for=\"").append(this.name).append("\">"); 
      sb.append(this.label).append(" ");
      sb.append("<input type=\"email\" id=\"").append(this.name).append("\" name=\"").append(this.name).append("\" value=\"").append(this.value).append("\" />");
      sb.append("</label>").append("\n");
      sb.append("    </div>");
      
      return sb.toString();
   }
}
