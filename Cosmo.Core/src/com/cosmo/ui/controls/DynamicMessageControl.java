package com.cosmo.ui.controls;

import com.cosmo.Workspace;
import com.cosmo.ui.templates.TemplateControl;
import com.cosmo.util.StringUtils;

/** 
 * Implementa un mensaje que se puede hacer aparecer de forma din�mica (JavaScript, etc).
 * 
 * @author Gerard Llort
 */
public class DynamicMessageControl extends Control 
{
   /** Control Type Unique ID */
   private static final String CTUID = "CosmoUiCtrlDynamicMsg";

   private static final String CPART_MSG_INFORMATION = "info-box-info";
   private static final String CPART_MSG_WARNING = "info-box-warning";
   private static final String CPART_MSG_ERROR = "info-box-error";

   private static final String TAG_TEXT = "TEXT";

   // Declaraci�n de variables internas
   private String message;
   private MessageTypes type;
   private boolean visible;

   /**
    * Enumera los distintos tipos de mensaje que se puede mostrar.
    */
   public enum MessageTypes
   {
      /** Mensaje informativo */
      Information,
      /** Mensaje de advertencia */
      Warning,
      /** Mensaje de error */
      Error
   }


   //==============================================
   // Contructors
   //==============================================

   /**
    * Contructor de la clase {@link DynamicMessageControl}.
    * 
    * @param workspace Una instancia de {@link Workspace} que representa el espacio de aplicaci�n actual.
    * @param id Identificador �nico del control en la p�gina.
    */
   public DynamicMessageControl(Workspace workspace, String id)
   {
      super(workspace, id);
      initialize();
   }

   /**
    * Contructor de la clase {@link DynamicMessageControl}.
    * 
    * @param workspace Una instancia de {@link Workspace} que representa el espacio de aplicaci�n actual.
    * @param id Identificador �nico del control en la p�gina.
    * @param message Cadena que contiene el mensaje visible por el usuario.
    * @param type Una opci�n de {@link MessageTypes} que indica que tipo de mensaje representar.
    * @param visible {@code true} si se debe renderizar la etiqueta o {@code false} si se desea no renderizar el elemento.
    */
   public DynamicMessageControl(Workspace workspace, String id, String message, MessageTypes type, boolean visible)
   {
      super(workspace, id);
      initialize();

      this.message = message;
      this.visible = visible;
      this.type = type;
   }


   //==============================================
   // Properties
   //==============================================

   @Override
   public String getControlTypeId() 
   {
      return DynamicMessageControl.CTUID;
   }

   public String getMessage() 
   {
      return message;
   }

   public void setMessage(String message) 
   {
      this.message = (message == null ? "no message specified" : message);
   }

   public MessageTypes getType() 
   {
      return type;
   }

   public void setType(MessageTypes type) 
   {
      this.type = type;
   }
   
   public boolean isVisible() 
   {
      return visible;
   }

   public void setVisible(boolean visible) 
   {
      this.visible = visible;
   }


   //==============================================
   // Methods
   //==============================================

   /**
    * Renderiza el control y genera el c�digo XHTML de representaci�n.
    *
    * @return Devuelve una cadena en formato XHTML que representa el control. 
    */
   @Override
   public String render() 
   {
      String xhtml = StringUtils.EMPTY;
      TemplateControl ctrl = getWorkspace().getTemplate().getControl(DynamicMessageControl.CTUID);

      // Si no es visible, no se renderiza
      if (!this.visible)
      {
         return "<!-- Dynamic label placeholder [" + this.getId() + "] -->";
      }

      switch (this.type)
      {
         case Information:
            xhtml = ctrl.getElement(DynamicMessageControl.CPART_MSG_INFORMATION);
            xhtml = Control.replaceTag(xhtml, DynamicMessageControl.TAG_TEXT, this.message);
            break;

         case Warning:
            xhtml = ctrl.getElement(DynamicMessageControl.CPART_MSG_WARNING);
            xhtml = Control.replaceTag(xhtml, DynamicMessageControl.TAG_TEXT, this.message);
            break;

         case Error:
            xhtml = ctrl.getElement(DynamicMessageControl.CPART_MSG_ERROR);
            xhtml = Control.replaceTag(xhtml, DynamicMessageControl.TAG_TEXT, this.message);
            break;
      }

      return xhtml;
   }

   @Override
   public String toString()
   {
      StringBuilder sb = new StringBuilder();

      if (this.visible)
      {
         sb.append("<p style=\"border:1px;background-color:rgb(175,175,175);\">").append(this.message).append("</p>").append("\n");
      }
      else
      {
         sb.append("<!-- dynamic label hided -->").append("\n");
      }

      return sb.toString();
   }


   //==============================================
   // Private Members
   //==============================================

   /**
    * Inicializa la instancia.
    */
   private void initialize()
   {
      this.message = StringUtils.EMPTY;
      this.visible = false;
      this.type = MessageTypes.Information;
   }
}
