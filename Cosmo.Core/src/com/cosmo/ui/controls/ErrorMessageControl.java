package com.cosmo.ui.controls;

import com.cosmo.Workspace;
import com.cosmo.security.NotAuthorizedException;
import com.cosmo.ui.templates.TemplateControl;

/**
 * Representa un men� de aplicaci�n Cosmo.
 * 
 * @author Gerard Llort
 */
public class ErrorMessageControl extends Control
{
   private static final String CONTROL_ID = "CosmoUiCtrlErrMsg";
   
   private static final String CPART_BODY = "errmsg-body";
   
   private static final String TAG_TITLE = "TITLE";
   private static final String TAG_ICON = "ICON";
   private static final String TAG_MESSAGE = "MSG";
   private static final String TAG_TRACE = "TRACE";
   
   private Exception exception;
   
   //==============================================
   // Constructor
   //==============================================
   
   /**
    * Constructor de la clase.
    * 
    * @param workspace Una instancia de {@link Workspace} que representa el espacio de aplicaci�n actual.
    * @param exception Una instancia de {@link Exception} que contiene los detalles del error.
    */
   public ErrorMessageControl(Workspace workspace, Exception exception)
   {
      super(workspace);
      
      this.exception = exception;
   }
   
   //==============================================
   // properties
   //==============================================

   @Override
   public String getControlTypeId() 
   {
      return ErrorMessageControl.CONTROL_ID;
   }

   public Exception getException() 
   {
      return exception;
   }

   public void setException(Exception exception) 
   {
      this.exception = exception;
   }
   
   
   //==============================================
   // Methods
   //==============================================
   
   /**
    * Renderiza el widget y genera el c�digo XHTML de representaci�n.
    *
    * @return Devuelve una cadena en formato XHTML que representa el widget. 
    */
   @Override
   public String render()
   {
      String xhtml;
      String icon;
      String title;
      String trace;
      TemplateControl ctrl;
      
      // Determina el t�tulo y el icono a mostrar
      if (this.getException() instanceof NotAuthorizedException)
      {
         title = "Av�s de seguretat (NotAuthorizedException)";
         icon = "icon-lock";
      }
      else
      {
         title = "Error de servidor";
         icon = " icon-remove-circle";
      }
      
      // Determina la traza del error
      if (this.getException().getStackTrace().length > 0)
      {
         trace = this.getException().getStackTrace().toString();
      }
      else
      {
         trace = "";
      }
      
      // Obtiene la plantilla y la parte del control
      ctrl = getWorkspace().getTemplate().getControl(ErrorMessageControl.CONTROL_ID);
      
      // Obtiene el cuerpo del mensaje
      xhtml = ctrl.getElement(CPART_BODY);
      xhtml = Control.replaceTag(xhtml, TAG_TITLE, title);
      xhtml = Control.replaceTag(xhtml, TAG_ICON, icon);
      xhtml = Control.replaceTag(xhtml, TAG_MESSAGE, this.getException().getMessage());
      xhtml = Control.replaceTag(xhtml, TAG_TRACE, trace);
      
      return xhtml;
   }
}