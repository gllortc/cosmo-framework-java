package com.cosmo.pages;

import com.cosmo.security.User;
import com.cosmo.security.UserNotFoundException;
import com.cosmo.security.providers.UserProviderException;
import com.cosmo.ui.controls.DynamicMessageControl;
import com.cosmo.ui.controls.FormButton;
import com.cosmo.ui.controls.FormControl;
import com.cosmo.ui.controls.FormFieldGroup;
import com.cosmo.ui.controls.FormFieldHidden;
import com.cosmo.ui.controls.FormFieldText;
import com.cosmo.ui.controls.HeaderControl;
import com.cosmo.ui.Page;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Gerard Llort
 */
public class LoginPage extends Page 
{
   /** Serial Version UID */
   private static final long serialVersionUID = 2644276975396335015L;

   private static final String FIELD_LOGIN = "txtLogin";
   private static final String FIELD_PASSWORD = "txtPwd";
   
   String dataReceived = "";
   
   @Override
   public void initPageEvent(HttpServletRequest request, HttpServletResponse response) 
   {
      this.setTitle("Identificació d'usuaris");
      
      HeaderControl header = new HeaderControl();
      header.setTitle(this.getTitle());
      header.setDescription("La pÃ¡gina o opció que ha solÂ·licitat requereix una sessió d'usuari.");
      this.addContent(header, ContentColumns.MAIN);
      
      DynamicMessageControl msg = new DynamicMessageControl("msg");
      msg.setVisible(false);
      this.addContent(msg, ContentColumns.MAIN);

      FormControl form = new FormControl();
      form.setActionUrl("/cosmo/LoginPage");
      form.setName("frmTest");
      form.setTitle("Identificació");
      form.setDescription("Introdueixi les dades d'identificació.");
      form.addHiddenValue(new FormFieldHidden("txtUserId", "0"));
      FormFieldGroup grp = new FormFieldGroup("Usuari");
      grp.addField(new FormFieldText(FIELD_LOGIN, "Login", 20));
      grp.addField(new FormFieldText(FIELD_PASSWORD, "Password", 20));
      form.addGroup(grp);
      form.addButton(new FormButton("cmdSend", "Aceptar", FormButton.ButtonType.Submit));
      form.addButton(new FormButton("cmdReset", "Limpiar", FormButton.ButtonType.Reset));
      this.addContent(form, ContentColumns.MAIN);
   }
   
   @Override
   public void formSendedEvent(HttpServletRequest request, HttpServletResponse response) 
   {
      User user = new User();
      user.setLogin(request.getParameter(FIELD_LOGIN));
      user.setPwd(request.getParameter(FIELD_PASSWORD));
      
      try 
      {
         this.getWorkspace().createSession(user);
         
         // Tot OK -> Redirecció
      } 
      catch (UserNotFoundException ex) 
      {
         DynamicMessageControl msg = (DynamicMessageControl) this.getControl("msg");
         msg.setVisible(true);
         msg.setMessage("No s'ha trobat cap usuari amb aquestes credencials. Si us plau, torni a introduïr les dades.");
      } 
      catch (UserProviderException ex) 
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
