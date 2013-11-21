package com.cosmo.ui.controls;

import com.cosmo.Workspace;

/**
 * Implementa un cuadro de texto representable dentro de un formulario Cosmo.
 * 
 * @author Gerard Llort
 */
public class FormFieldInteger extends FormField
{
   private String name;
   private String label;
   private String description;
   private Integer value;
   private Integer max;
   private Integer min;
   private boolean required;


   //==============================================
   // Contructors
   //==============================================

   /**
    * Contructor de la clase.
    * 
    * @param name Nombre identificativo del elemento dentro de la página.
    * @param label Etiqueta que se mostrará junto el control.
    */
   public FormFieldInteger(String name, String label) 
   {
      this.name = name;
      this.label = label;
      this.description = "";
      this.value = 0;
      this.min = null;
      this.max = null;
      this.required = false;
   }

   /**
    * Contructor de la clase.
    * 
    * @param name Nombre identificativo del elemento dentro de la página.
    * @param label Etiqueta que se mostrará junto el control.
    * @param required Número máximo de carácteres que admite el control.
    */
   public FormFieldInteger(String name, String label, boolean required) 
   {
      this.name = name;
      this.label = label;
      this.description = "";
      this.value = 0;
      this.min = null;
      this.max = null;
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

   public Integer getValue() 
   {
      return value;
   }

   @Override
   public void setValue(Object value) 
   {
      this.value = (Integer) value;
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
   
   public int getMax()
   {
      return max;
   }

   public void setMax(int max)
   {
      this.max = max;
   }

   public int getMin()
   {
      return min;
   }

   public void setMin(int min)
   {
      this.min = min;
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
    * Convierte el campo en un TAG XHTML 5.
    */
   @Override
   public String render(Workspace workspace)
   {
      StringBuilder sb = new StringBuilder();

      sb.append("<input type=\"number\" ").
         append("id=\"").append(this.name).append("\" ").
         append("name=\"").append(this.name).append("\" ").
         append("value=\"").append(this.value).append("\" ").
         append(min != null ? "min=\"" + this.min + "\" " : "").
         append(max != null ? "max=\"" + this.max + "\" " : "").
         append("value=\"").append(this.value).append("\" ").
         append("pattern=\"[0-9]\" ").
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
      sb.append("<input type=\"").append("number").append("\" id=\"").append(this.name).append("\" name=\"").append(this.name).append("\" value=\"").append(this.value).append("\" />");
      sb.append("</label>").append("\n");
      sb.append("    </div>");

      return sb.toString();
   }
}
