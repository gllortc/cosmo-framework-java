package com.cosmo.ui.controls;

import javax.servlet.http.HttpSession;

import com.cosmo.ui.templates.Template;
import com.cosmo.ui.templates.TemplateControl;

/** 
 * Implementa un mensaje que se puede hacer aparecer de forma dinámica (JavaScript, etc).
 * @author gllort
 */
public class DynamicMessageControl extends IdentificableControl 
{
   private static final String CONTROL_ID = "CosmoUiCtrlDynamicMsg";
   
   private static final String CPART_MSG_INFORMATION = "info-box-info";
   private static final String CPART_MSG_WARNING = "info-box-warning";
   private static final String CPART_MSG_ERROR = "info-box-error";
         
   private static final String TAG_TEXT = "TEXT";
   
   private String message;
   private MessageTypes type;
   private boolean visible;

   /**
    * Enumera los distintos tipos de mensaje que se puede mostrar.
    */
   public enum MessageTypes
   {
      Information,
      Warning,
      Error
   }
   
   //==============================================
   // Contructors
   //==============================================
   
   /**
    * Contructor de la clase.
    */
   public DynamicMessageControl(String id)
   {
      super(id);
      initialize();
   }
   
   /**
    * Contructor de la clase.
    * 
    * @param id El identificador único de este elemento.
    * @param message Cadena que contiene el mensaje visible por el usuario.
    * @param type Una opción de {@link MessageTypes} que indica que tipo de mensaje representar.
    * @param visible {@code true} si se debe renderizar la etiqueta o {@code false} si se desea no renderizar el elemento.
    */
   public DynamicMessageControl(String id, String message, MessageTypes type, boolean visible)
   {
      super(id);
      initialize();
      
      this.message = message;
      this.visible = visible;
      this.type = type;
   }
   
   @Override
   public String getControlTypeId() 
   {
      return DynamicMessageControl.CONTROL_ID;
   }

   public String getMessage() 
   {
      return message;
   }

   public void setMessage(String message) 
   {
      this.message = message;
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
   
   @Override
   public String render(HttpSession session, Template template) 
   {
      String xhtml = "";
      TemplateControl ctrl = template.getControl(DynamicMessageControl.CONTROL_ID);
      
      // Si no es visible, no se renderiza
      if (!this.visible)
      {
         return "<!-- Dynamic label placeholder [" + this.getControlId() + "] -->";
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
   
   private void initialize()
   {
      this.message = "";
      this.visible = false;
      this.type = MessageTypes.Information;
   }
}
