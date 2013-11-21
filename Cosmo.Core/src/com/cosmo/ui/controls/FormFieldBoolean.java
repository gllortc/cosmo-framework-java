package com.cosmo.ui.controls;

import com.cosmo.Workspace;

/**
 * Implementa un cuadro de texto representable dentro de un formulario Cosmo.
 * 
 * @author Gerard Llort
 */
public class FormFieldBoolean extends FormField
{
   private String name;
   private boolean value;
   private String label;
   private String description;


   //==============================================
   // Contructors
   //==============================================

   /**
    * Contructor de la clase.
    * 
    * @param name Nombre identificativo del elemento dentro de la página.
    * @param label Etiqueta que se mostrará junto el control.
    */
   public FormFieldBoolean(String name, String label) 
   {
      this.name = name;
      this.label = label;
      this.description = "";
      this.value = false;
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

   public boolean getValue() 
   {
      return value;
   }

   @Override
   public void setValue(Object value) 
   {
      this.value = (Boolean) value;
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
      sb.append("<input type=\"checkbox\" id=\"").append(this.name).append("\" name=\"").append(this.name).append("\" value=\"true\" ").append(this.value ? " checked=\"checked\"" : "").append(" />");

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
      sb.append("<label for=\"").append(this.name).append("\">"); 
      sb.append(this.label).append(" ");
      sb.append("<input type=\"checkbox\" id=\"").append(this.name).append("\" name=\"").append(this.name).append("\" value=\"true\" ").append(this.value ? " checked=\"checked\"" : "").append(" />");
      sb.append("</label>").append("\n");
      sb.append("    </div>");

      return sb.toString();
   }
}
