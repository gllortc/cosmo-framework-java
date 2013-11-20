package com.cosmo.ui.widgets;

import com.cosmo.Workspace;
import com.cosmo.net.URL;
import com.cosmo.ui.controls.Control;
import com.cosmo.ui.templates.Template;
import com.cosmo.ui.templates.TemplateControl;
import com.cosmo.ui.templates.TemplateUnavailableException;

/**
 * Un <em>widget</em> que implementa el mecanismo de log-in / log-out.
 *  
 * @author Gerard Llort
 */
public class LoginWidget extends Widget
{
   private static final String WIDGET_ID = "CosmoUiWdgtLogin";
   
   private static final String TAG_HREF = "HREF";
   private static final String TAG_USER = "USER";
   
   private static final String WPART_LOGIN = "log-in";
   private static final String WPART_LOGOUT = "log-out";

   
   //==============================================
   // Constructor
   //==============================================
   
   /**
    * Constructor de la clase.
    * 
    * @param workspace Una instancia de {@link Workspace} que representa el workspace actual.
    */
   public LoginWidget(Workspace workspace)
   {
      super(workspace);
   }
   
   
   //==============================================
   // Methods
   //==============================================
   
   /**
    * Devuelve un identificador único del tipo de widget.
    */
   @Override
   public String getWidgetTypeId() 
   {
      return LoginWidget.WIDGET_ID;
   }

   /**
    * Renderiza el widget y genera el código XHTML de representación.
    *
    * @return Devuelve una cadena en formato XHTML que representa el widget.
    * 
    * @throws TemplateUnavailableException
    */
   @Override
   public String render() throws TemplateUnavailableException 
   {
      String xhtml = "";
      TemplateControl ctrl;
      Template template;
      
      // Obtiene la plantilla y la parte del widget
      template = this.getWorkspace().getTemplate();
      ctrl = template.getControl(WIDGET_ID);
      
      if (!getWorkspace().isValidUserSession())
      {
         // Configura la url del login
         URL url = new URL(getWorkspace().getProperties().getSecurityProperties().getLoginPage());
         url.addParameter("tourl", getWorkspace().getRequestedUrl());
         
         // Genera el XHTML del widget
         xhtml += ctrl.getElement(WPART_LOGIN);
         xhtml = Control.replaceTag(xhtml, TAG_HREF, url.build(getWorkspace().getCharset()));
      }
      else
      {
         // Configura la url del login
         URL url = new URL("LogoutPage");
         url.addParameter("tourl", getWorkspace().getRequestedUrl());
         
         // Genera el XHTML del widget
         xhtml += ctrl.getElement(WPART_LOGOUT);
         xhtml = Control.replaceTag(xhtml, TAG_HREF, url.build(getWorkspace().getCharset()));
         xhtml = Control.replaceTag(xhtml, TAG_USER, getWorkspace().getUserSession().getCurrentUser().getLogin());
      }

      return xhtml;
   }
   
}
