package com.cosmo.ui.controls;

import java.io.PrintWriter;
import java.io.StringWriter;

import com.cosmo.Workspace;
import com.cosmo.security.NotAuthorizedException;
import com.cosmo.security.auth.AuthenticationException;
import com.cosmo.security.auth.AuthorizationException;
import com.cosmo.ui.templates.TemplateControl;

/**
 * Representa un menú de aplicación Cosmo.
 * 
 * @author Gerard Llort
 */
public class ErrorMessageControl extends Control
{
   /** CTUID - Control Type Unique ID */
   private static final String CONTROL_ID = "CosmoUiCtrlErrMsg";

   private static final String CPART_BODY = "errmsg-body";

   private static final String TAG_TITLE = "TITLE";
   private static final String TAG_ICON = "ICON";
   private static final String TAG_MESSAGE = "MSG";
   private static final String TAG_TRACE = "TRACE";

   // Declaración de variables internas
   private Exception exception;


   //==============================================
   // Constructor
   //==============================================

   /**
    * Constructor de la clase.
    * 
    * @param workspace Una instancia de {@link Workspace} que representa el espacio de aplicación actual.
    * @param exception Una instancia de {@link Exception} que contiene los detalles del error.
    */
   public ErrorMessageControl(Workspace workspace, Exception exception)
   {
      super(workspace);
      
      this.exception = exception;
   }


   //==============================================
   // Properties
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
    * Renderiza el widget y genera el código XHTML de representación.
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

      // Determina el título y el icono a mostrar
      if (this.getException() instanceof NotAuthorizedException ||
          this.getException() instanceof AuthorizationException ||
          this.getException() instanceof AuthenticationException)
      {
         title = "Avís de seguretat (" + this.getException().getClass().getSimpleName() + ")";
         icon = "icon-lock";
      }
      else
      {
         title = "Error de servidor";
         icon = " icon-remove-circle";
      }

      // Determina la traza del error
      StringWriter writer = new StringWriter();
      PrintWriter printWriter = new PrintWriter(writer);
      exception.printStackTrace(printWriter);
      printWriter.flush();
      trace = writer.toString();

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
