package com.cosmo.ui.controls;

/**
 *
 * @author gllort
 */
public class FormFieldHidden extends FormField
{
   private String name;
   private String value;
   
   //==============================================
   // Constructors
   //==============================================
   
   /**
    * Constructor de la clase.
    * 
    * @param name Nombre identificativo del control.
    */
   public FormFieldHidden(String name) 
   {
      this.name = name;
      this.value = "";
   }
   
   /**
    * Constructor de la clase.
    * 
    * @param name Nombre identificativo del control.
    * @param value Valor asociado al campo.
    */
   public FormFieldHidden(String name, String value) 
   {
      this.name = name;
      this.value = value;
   }

   //==============================================
   // Properties
   //==============================================
   
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

   public void setValue(String value) 
   {
      this.value = value;
   }
   
   //==============================================
   // Methods
   //==============================================
   
   /**
    * Convierte la instancia en una cadena XHTML que representa el elemento en una página web.
    */
   @Override
   public String render()
   {
      return toString();
   }
   
   @Override
   public String toString()
   {
      StringBuilder sb = new StringBuilder();
      sb.append("    <input type=\"hidden\" id=\"").append(this.name).append("\" name=\"").append(this.name).append("\" value=\"").append(this.value).append("\" />");
      
      return sb.toString();
   }
}
