package com.cosmo.ui.controls;

import java.util.ArrayList;

import com.cosmo.Workspace;
import com.cosmo.util.KeyValue;

/**
 * Clase abstracta que deben implementar los controles de formulario.
 * 
 * @author Gerard Llort
 */
public abstract class FormField 
{
   /**
    * Enumera los tipos de presentación del control.
    */
   public enum FieldType
   {
      /** Campo editable por el usuario */
      Editable,
      /** Lista desplegable de opciones */
      ComboBox,
      /** Lista de opciones */
      ListBox
   }

   private ArrayList<KeyValue> listOptions = new ArrayList<KeyValue>();
   private FieldType fieldType = FieldType.Editable;


   //==============================================
   // Constructors
   //==============================================

   /**
    * Constructor de la clase.
    */
   public FormField()
   {
      this.listOptions = new ArrayList<KeyValue>();
      this.fieldType = FieldType.Editable;
   }


   //==============================================
   // Abstract members
   //==============================================

   /**
    * Obtiene el nombre (identificador Ãºnico) del campo.
    */
   public abstract String getName();

   /**
    * Establece el valor del campo.
    */
   public abstract void setValue(Object value);

   /**
    * Convierte la instancia en una cadena XHTML que representa el elemento en una pÃ¡gina web.
    */
   public abstract String render(Workspace workspace);


   //==============================================
   // Properties
   //==============================================

   /**
    * Devuelve el tipo de representación que se usa para representar el control.
    */
   public FieldType getFieldType() 
   {
      return fieldType;
   }

   /**
    * Establece el tipo de representación que se usa para representar el control.
    */
   public void setFieldType(FieldType fieldType) 
   {
      this.fieldType = fieldType;
   }


   //==============================================
   // Methods
   //==============================================

   /**
    * Agrega un valor a la lista de valores posibles que puede adoptar el campo.
    * 
    * @param keyvalue Una instancia de {@link KeyValue} que contiene el par clave/valor a agregar.
    */
   public void addListOption(KeyValue keyvalue)
   {
      listOptions.add(keyvalue);
   }

   /**
    * Elimina todos los elementos de la lista de valores posibles que puede adoptar el campo.
    */
   public void clearList()
   {
      listOptions.clear();
   }
}
