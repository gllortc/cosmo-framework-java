package com.cosmo.data.lists;

/**
 * Implementa un elemento de lista.
 * 
 * @author Gerard Llort
 */
public class ListItem 
{
   // Declaraci�n de variables locales
   private String value;
   private String caption;
   private boolean isDefault;


   //==============================================
   // Constructors
   //==============================================

   /**
    * Constructor de la clase {@link ListItem}.
    */
   public ListItem()
   {
      initialize();
   }

   /**
    * Constructor de la clase {@link ListItem}.
    * 
    * @param value Valor asociado al elemento de la lista.
    * @param caption Texto que aparecer� asociado al valor.
    */
   public ListItem(String value, String caption)
   {
      initialize();

      this.value = value;
      this.caption = caption;
   }

   /**
    * Constructor de la clase {@link ListItem}.
    * 
    * @param value Valor asociado al elemento de la lista.
    * @param caption Texto que aparecer� asociado al valor.
    * @param isDefault Indica si el elemento estar� seleccionado por defecto.
    */
   public ListItem(String value, String caption, boolean isDefault)
   {
      initialize();

      this.value = value;
      this.caption = caption;
      this.isDefault = isDefault;
   }


   //==============================================
   // Properties
   //==============================================

   public String getValue() 
   {
      return value;
   }

   public void setValue(String value) 
   {
      this.value = value;
   }

   public String getCaption() 
   {
      return caption;
   }

   public void setCaption(String caption) 
   {
      this.caption = caption;
   }

   public boolean isDefault() 
   {
      return isDefault;
   }

   public void setDefault(boolean isDefault) 
   {
      this.isDefault = isDefault;
   }


   //==============================================
   // Private members
   //==============================================

   /**
    * Convierte la instancia en una cadena de texto.
    */
   @Override
   public String toString()
   {
      return "[value: " + this.value + "\n" +
             " caption: " + this.caption + "\n" +
             " isDefault=" + (this.isDefault ? "true" : "false") + "]";
   }


   //==============================================
   // Private members
   //==============================================

   /**
    * Inicializa la instancia.
    */
   private void initialize()
   {
      value = "";
      caption = "";
      isDefault = false;
   }
}
