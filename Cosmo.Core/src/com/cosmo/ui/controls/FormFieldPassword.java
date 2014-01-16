package com.cosmo.ui.controls;

import com.cosmo.Workspace;
import com.cosmo.util.StringUtils;

/**
 * Implementa un cuadro de texto oculto (p. ej. contraseñas) representable dentro de un formulario Cosmo.
 * 
 * @author Gerard Llort
 */
public class FormFieldPassword extends FormField
{
   // Declaraci�n de variables internas
   private String name;
   private String value;
   private String label;
   private int maxLength;


   //==============================================
   // Contructors
   //==============================================

   /**
    * Contructor de la clase {@link FormFieldPassword}.
    * 
    * @param name Nombre identificativo del elemento dentro de la página.
    * @param label Etiqueta que se mostrará junto el control.
    */
   public FormFieldPassword(String name, String label) 
   {
      this.name = name;
      this.label = label;
      this.value = StringUtils.EMPTY;
      this.maxLength = -1;
   }

   /**
    * Contructor de la clase {@link FormFieldPassword}.
    * 
    * @param name Nombre identificativo del elemento dentro de la página.
    * @param label Etiqueta que se mostrará junto el control.
    * @param maxLength Número máximo de carácteres que admite el control.
    */
   public FormFieldPassword(String name, String label, int maxLength) 
   {
      this.name = name;
      this.label = label;
      this.value = StringUtils.EMPTY;
      this.maxLength = maxLength;
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

   public int getMaxLength() 
   {
      return maxLength;
   }

   public void setMaxLength(int maxLength) 
   {
      this.maxLength = maxLength;
   }


   //==============================================
   // Methods
   //==============================================

   @Override
   public String render(Workspace workspace)
   {
      return toString();
   }

   @Override
   public String toString()
   {
      StringBuilder sb = new StringBuilder();

      sb.append("    <div>").append("\n");
      sb.append("      <label for=\"").append(this.name).append("\">");
      sb.append(this.label).append(" ");
      sb.append("<input type=\"password\" id=\"").append(this.name).append("\" name=\"").append(this.name).append("\" value=\"").append(this.value).append("\" />");
      sb.append("</label>").append("\n");
      sb.append("    </div>");

      return sb.toString();
   }
}
