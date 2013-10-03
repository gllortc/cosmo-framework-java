package com.cosmo.manager.pages;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cosmo.Cosmo;
import com.cosmo.net.HttpRequestUtils;
import com.cosmo.security.UserNotFoundException;
import com.cosmo.security.annotations.AuthenticationForm;
import com.cosmo.ui.Page;
import com.cosmo.ui.PageContext;
import com.cosmo.ui.PageContext.ContentColumns;
import com.cosmo.ui.PageContext.PageLayout;
import com.cosmo.ui.controls.BreadcrumbsControl;
import com.cosmo.ui.controls.BreadcrumbsItem;
import com.cosmo.ui.controls.DynamicMessageControl;
import com.cosmo.ui.controls.FormButton;
import com.cosmo.ui.controls.FormButton.ButtonType;
import com.cosmo.ui.controls.FormControl;
import com.cosmo.ui.controls.FormFieldset;
import com.cosmo.ui.controls.FormFieldHidden;
import com.cosmo.ui.controls.FormFieldText;
import com.cosmo.ui.controls.HeaderControl;
import com.cosmo.ui.controls.Icon;

/**
 * Servlet implementation class FormPage
 */
@AuthenticationForm
@WebServlet( description = "Formulari d'autenticació (login)", urlPatterns = { "/LoginPage" } )
public class LoginPage extends Page 
{
   /** Serial Version UID */
   private static final long serialVersionUID = 4174215166551407407L;   
   
   private static final String ID_MSG = "msg";
   
   private static final String FIELD_LOGIN = "txtLogin";
   private static final String FIELD_PASSWORD = "txtPwd";
   // private static final String FIELD_TOURL = "toUrl";

   @Override
   public PageContext initPageEvent(PageContext pc, HttpServletRequest request, HttpServletResponse response) 
   {
      pc.setLayout(PageLayout.TwoColumnsLeft);
      pc.setTitle(this.getWorkspace().getProperties().getString(Cosmo.PROPERTY_WORKSPACE_TITLE) + " - Login");
      
      BreadcrumbsControl navbar = new BreadcrumbsControl(getWorkspace(), "bc");
      navbar.addItem(new BreadcrumbsItem("Home", "HomePage", Icon.ICON_IMAGE_HOME));
      navbar.addItem(new BreadcrumbsItem("Login", ""));
      pc.addContent(navbar, ContentColumns.MAIN);

      HeaderControl header = new HeaderControl(getWorkspace(), "hc");
      header.setTitle("Login");
      header.setDescription("Para acceder a este contenido debe proporcionar sus credenciales de usuario.");
      pc.addContent(header, ContentColumns.MAIN);
      
      DynamicMessageControl message = new DynamicMessageControl(getWorkspace(), ID_MSG);
      pc.addContent(message, ContentColumns.MAIN);
      
      FormControl form = new FormControl(getWorkspace(), "LoginForm");
      form.addHiddenValue(new FormFieldHidden(Cosmo.URL_PARAM_TOURL, HttpRequestUtils.getValue(request, Cosmo.URL_PARAM_TOURL)));
      FormFieldset group = new FormFieldset("Datos de identificación");
      group.addField(new FormFieldText(LoginPage.FIELD_LOGIN, "Login"));
      group.addField(new FormFieldText(LoginPage.FIELD_PASSWORD, "Password", true));
      form.addGroup(group);
      form.addButton(new FormButton("cmdAcceopt", "Enviar", ButtonType.Submit));
      pc.addContent(form, ContentColumns.MAIN);
      
      return pc;
   }

   @Override
   public PageContext formSendedEvent(PageContext pc, HttpServletRequest request, HttpServletResponse response) 
   {
      try 
      {
         getWorkspace().createUserSession(HttpRequestUtils.getValue(request, LoginPage.FIELD_LOGIN), 
                                          HttpRequestUtils.getValue(request, LoginPage.FIELD_PASSWORD));

         response.sendRedirect(HttpRequestUtils.getValue(request, Cosmo.URL_PARAM_TOURL));
      } 
      catch (UserNotFoundException ex)
      {
         DynamicMessageControl msg = (DynamicMessageControl) pc.getControl(ID_MSG);
         msg.setVisible(true);
         msg.setType(DynamicMessageControl.MessageTypes.Error);
         msg.setMessage("Les credencials proporcionades no corresponen a cap usuari. És possible que l'usuari no existeixi o bé que la contrassenya proporcionada sigui incorrecte.");
      }
      catch (Exception ex) 
      {
         DynamicMessageControl msg = (DynamicMessageControl) pc.getControl(ID_MSG);
         msg.setVisible(true);
         msg.setType(DynamicMessageControl.MessageTypes.Error);
         msg.setMessage("ERROR: " + ex.getMessage());
      }
      
      return pc;
   }

   @Override
   public PageContext loadPageEvent(PageContext pc, HttpServletRequest request, HttpServletResponse response) 
   {
      return pc;
   }

   @Override
   public PageContext pageException(PageContext pc, Exception exception) 
   {
      pc.showException(getWorkspace(), exception);
      
      return pc;
   }
}
