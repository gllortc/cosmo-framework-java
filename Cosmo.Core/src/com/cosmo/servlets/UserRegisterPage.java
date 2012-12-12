package com.cosmo.servlets;

import com.cosmo.net.HttpRequestUtils;
import com.cosmo.security.User;
import com.cosmo.security.UserAlreadyExistsException;
import com.cosmo.security.providers.UserProvider;
import com.cosmo.ui.Page;
import com.cosmo.ui.controls.DynamicMessageControl;
import com.cosmo.ui.controls.FormButton;
import com.cosmo.ui.controls.FormControl;
import com.cosmo.ui.controls.FormFieldGroup;
import com.cosmo.ui.controls.FormFieldText;
import com.cosmo.ui.controls.HeaderControl;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Página estandar de identificación de usuarios.
 * 
 * @author Gerard Llort
 */
@WebServlet( description = "User register page", urlPatterns = { "/UserRegisterPage" } )
public class UserRegisterPage extends Page 
{
   /** Serial version UID */
   private static final long serialVersionUID = -611541787527541887L;

   private static final String FIELD_LOGIN = "txtLogin";
   private static final String FIELD_PASSWORD = "txtPwd";
   private static final String FIELD_PASSWORDVER = "txtPwdV";
   private static final String FIELD_MAIL = "txtMail";
   private static final String FIELD_NAME = "txtName";
   private static final String FIELD_CITY = "txtCity";
   
   String dataReceived = "";
   
   @Override
   public void initPageEvent(HttpServletRequest request, HttpServletResponse response) 
   {
      this.setTitle("Crear compte d'usuari - " + this.getWorkspace().getName());
      
      HeaderControl header = new HeaderControl(getWorkspace(), "hc");
      header.setTitle("Crear compte d'usuari");
      header.setDescription("Ompleni el formulari per crear un nou compte d'usuari.");
      this.addContent(header, Page.ContentColumns.MAIN);
      
      DynamicMessageControl msg = new DynamicMessageControl(getWorkspace(), "msg");
      msg.setVisible(false);
      this.addContent(msg, Page.ContentColumns.MAIN);

      FormControl form = new FormControl(getWorkspace(), "UserRegisterForm");
      form.setName("frmUserAdd");
      form.setTitle("Nou compte d'usuari");

      FormFieldGroup grpId = new FormFieldGroup("Identificació");
      grpId.addField(new FormFieldText(FIELD_LOGIN, "Login", 35));
      grpId.addField(new FormFieldText(FIELD_PASSWORD, "ContraseÃ±a", 32, true));
      grpId.addField(new FormFieldText(FIELD_PASSWORDVER, "Verificació", 32, true));
      grpId.addField(new FormFieldText(FIELD_MAIL, "Correu-e", 255));
      form.addGroup(grpId);

      FormFieldGroup grpDat = new FormFieldGroup("Dades personals");
      grpDat.addField(new FormFieldText(FIELD_NAME, "Nom complert", 64));
      grpDat.addField(new FormFieldText(FIELD_CITY, "Ciutat", 64));
      form.addGroup(grpDat);
      
      form.addButton(new FormButton("cmdSend", "Aceptar", FormButton.ButtonType.Submit));
      form.addButton(new FormButton("cmdReset", "Limpiar", FormButton.ButtonType.Reset));
      this.addContent(form, Page.ContentColumns.MAIN);
   }
   
   @Override
   public void formSendedEvent(HttpServletRequest request, HttpServletResponse response) 
   {
      // Comprobación de datos
              
      if (HttpRequestUtils.isNullOrEmpty(request, FIELD_PASSWORD) || !HttpRequestUtils.equals(request, FIELD_PASSWORD, FIELD_PASSWORDVER))
      {
         DynamicMessageControl msg = (DynamicMessageControl) this.getControl("msg");
         msg.setMessage("La contrassenya i la verificación han de ser exactament iguals.");
         msg.setVisible(true);
         return;
      }
      
      try 
      {
         // Recopilación de datos
      
         User user = new User();
         user.setLogin(HttpRequestUtils.getValue(request, FIELD_LOGIN));
         user.setPwd(HttpRequestUtils.getValue(request, FIELD_PASSWORD));
         user.setMail(HttpRequestUtils.getValue(request, FIELD_MAIL));
         user.setName(HttpRequestUtils.getValue(request, FIELD_NAME));
         user.setCity(HttpRequestUtils.getValue(request, FIELD_CITY));
         user.setStatus(User.UserStates.Active);

         // Acciones

         UserProvider up = UserProvider.getInstance(this.getWorkspace());
         up.add(user);

         // Redirección a destino
         
         response.sendRedirect("/LoginPage");
      }
      catch (UserAlreadyExistsException ex)
      {
         DynamicMessageControl msg = (DynamicMessageControl) this.getControl("msg");
         msg.setVisible(true);
         msg.setMessage("Ja s'ha trobat un usuari amb el mateix LOGIN o MAIL. Si us plau, verifiqui les dades proporcionades.");
      }
      catch (Exception ex) 
      {
         DynamicMessageControl msg = (DynamicMessageControl) this.getControl("msg");
         msg.setVisible(true);
         msg.setMessage("S'ha produït un error: " + ex.getMessage());
      }
   }
   
   @Override
   public void loadPageEvent(HttpServletRequest request, HttpServletResponse response) 
   {
      // Nothing to do
   }

}
