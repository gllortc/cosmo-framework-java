package com.cosmo.ui.controls;

import com.cosmo.Workspace;

/**
 * Implementa un cuadro de texto representable dentro de un formulario Cosmo.
 * Un grupo (y formulario) sólo puede contener un control {@link FormFieldCaptcha}.
 * 
 * @author Gerard Llort
 */
public class FormFieldCaptcha extends FormField
{
   public static final String CAPTCHA_SERVLET = "CosmoCaptcha";

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
   public FormFieldCaptcha(String name, String label) 
   {
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
    * @param maxLength Número máximo de carácteres que admite el control.
    */
   public FormFieldCaptcha(String name, String label, int maxLength) 
   {
      this.name = name;
      this.label = label;
      this.description = "";
      this.value = "";
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
      
      sb.append("<img src=\"" + FormFieldCaptcha.CAPTCHA_SERVLET + "\" />&nbsp;");
      sb.append("<input type=\"text\" id=\"").append(this.name).append("\" name=\"").append(this.name).append("\" />");
      
      return sb.toString();
   }

   /**
    * Convierte la instancia en una cadena de texto.
    */
   @Override
   public String toString()
   {
      StringBuilder sb = new StringBuilder();
      
      sb.append("<img src=\"" + FormFieldCaptcha.CAPTCHA_SERVLET + "\" />&nbsp;");
      sb.append("<input type=\"text\" id=\"").append(this.name).append("\" name=\"").append(this.name).append("\" />");
      
      return sb.toString();
   }
}
