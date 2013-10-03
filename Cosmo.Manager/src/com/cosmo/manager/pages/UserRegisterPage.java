package com.cosmo.manager.pages;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cosmo.net.HttpRequestUtils;
import com.cosmo.security.User;
import com.cosmo.security.UserAlreadyExistsException;
import com.cosmo.security.annotations.ActivitiesAllowed;
import com.cosmo.security.auth.Authentication;
import com.cosmo.security.auth.AuthenticationException;
import com.cosmo.security.auth.AuthenticationFactory;
import com.cosmo.security.auth.impl.PostgreSqlAuthenticationImpl;
import com.cosmo.ui.Page;
import com.cosmo.ui.PageContext;
import com.cosmo.ui.PageContext.ContentColumns;
import com.cosmo.ui.PageContext.PageLayout;
import com.cosmo.ui.controls.DynamicMessageControl;
import com.cosmo.ui.controls.DynamicMessageControl.MessageTypes;
import com.cosmo.ui.controls.FormButton;
import com.cosmo.ui.controls.FormControl;
import com.cosmo.ui.controls.FormFieldset;
import com.cosmo.ui.controls.FormFieldText;
import com.cosmo.ui.controls.HeaderControl;

/**
 * Página estandar de identificación de usuarios.
 * 
 * @author Gerard Llort
 */
@ActivitiesAllowed( "admin.users.manage" )
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
   
   String dataReceived = "";
   
   @Override
   public PageContext initPageEvent(PageContext pc, HttpServletRequest request, HttpServletResponse response) 
   {
      pc.setLayout(PageLayout.TwoColumnsLeft);
      pc.setTitle("Crear compte d'usuari - " + getWorkspace().getName());
      
      try 
      {
         // Comprueba si el agente es el adecuado
         Authentication ap = AuthenticationFactory.getInstance(getWorkspace());
         if (!(ap instanceof PostgreSqlAuthenticationImpl))
         {
            DynamicMessageControl msg = new DynamicMessageControl(getWorkspace(), "msg");
            msg.setType(MessageTypes.Error);
            msg.setMessage("Només es poden gestionar els comptes d'usuari amb el proveïdor natiu de Cosmo (PostgreSqlAuthenticationProvider).");
            msg.setVisible(true);
            pc.addContent(msg, ContentColumns.MAIN);
         }
         else
         {
            HeaderControl header = new HeaderControl(getWorkspace(), "hc");
            header.setTitle("Crear compte d'usuari");
            header.setDescription("Ompleni el formulari per crear un nou compte d'usuari.");
            pc.addContent(header, ContentColumns.MAIN);
            
            DynamicMessageControl msg = new DynamicMessageControl(getWorkspace(), "msg");
            msg.setVisible(false);
            pc.addContent(msg, ContentColumns.MAIN);

            FormControl form = new FormControl(getWorkspace(), "UserRegisterForm");
            form.setName("frmUserAdd");
            form.setTitle("Nou compte d'usuari");

            FormFieldset grpId = new FormFieldset("Identificació");
            grpId.addField(new FormFieldText(FIELD_LOGIN, "Login", 35));
            grpId.addField(new FormFieldText(FIELD_PASSWORD, "Contrassenya", 32, true));
            grpId.addField(new FormFieldText(FIELD_PASSWORDVER, "Verificació", 32, true));
            grpId.addField(new FormFieldText(FIELD_MAIL, "Correu-e", 255));
            form.addGroup(grpId);

            FormFieldset grpDat = new FormFieldset("Dades personals");
            grpDat.addField(new FormFieldText(FIELD_NAME, "Nom complert", 64));
            form.addGroup(grpDat);
            
            form.addButton(new FormButton("cmdSend", "Aceptar", FormButton.ButtonType.Submit));
            form.addButton(new FormButton("cmdCancel", "Cancel·lar", "UserManagerPage"));
            pc.addContent(form, ContentColumns.MAIN);
         }
      } 
      catch (AuthenticationException e) 
      {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      
      return pc;
   }
   
   @Override
   public PageContext loadPageEvent(PageContext pc, HttpServletRequest request, HttpServletResponse response) 
   {
      /*if (HttpRequestUtils.getValue(request, "mode", "").equals("edit"))
      {
         try 
         {
            // Obtiene el usuario
            PostgreSqlAuthenticationImpl auth = (PostgreSqlAuthenticationImpl) AuthenticationFactory.getInstance(pc.getWorkspace());
            User user = auth.add(HttpRequestUtils.getValue(request, "id", ""));
            
            HeaderControl header = (HeaderControl) pc.getControl("hc");
            header.setTitle("Editar compte d'usuari"); 
            
            FormControl form = (FormControl) pc.getControl("UserRegisterForm");
            form.setFieldValue(request, FIELD_LOGIN, user.getLogin());
            
         }
         catch (AuthenticationException e) 
         {
            
         }
      }*/
      
      return pc;
   }
   
   @Override
   public PageContext formSendedEvent(PageContext pc, HttpServletRequest request, HttpServletResponse response) 
   {
      // Comprobación de datos
              
      if (HttpRequestUtils.isNullOrEmpty(request, FIELD_PASSWORD) || !HttpRequestUtils.equals(request, FIELD_PASSWORD, FIELD_PASSWORDVER))
      {
         DynamicMessageControl msg = (DynamicMessageControl) pc.getControl("msg");
         msg.setMessage("La contrassenya i la verificación han de ser exactament iguals.");
         msg.setVisible(true);
         return pc;
      }
      
      try 
      {
         // Recopilación de datos
         User user = new User();
         user.setLogin(HttpRequestUtils.getValue(request, FIELD_LOGIN));
         user.setMail(HttpRequestUtils.getValue(request, FIELD_MAIL));
         user.setName(HttpRequestUtils.getValue(request, FIELD_NAME));

         // Acciones
         PostgreSqlAuthenticationImpl up = (PostgreSqlAuthenticationImpl) AuthenticationFactory.getInstance(getWorkspace());
         up.add(user, HttpRequestUtils.getValue(request, FIELD_PASSWORD));

         response.sendRedirect("UserManagerPage");
         return pc;
      }
      catch (UserAlreadyExistsException ex)
      {
         DynamicMessageControl msg = (DynamicMessageControl) pc.getControl("msg");
         msg.setVisible(true);
         msg.setMessage("Ja s'ha trobat un usuari amb el mateix LOGIN o MAIL. Si us plau, verifiqui les dades proporcionades.");
      }
      catch (Exception ex) 
      {
         DynamicMessageControl msg = (DynamicMessageControl) pc.getControl("msg");
         msg.setVisible(true);
         msg.setMessage("S'ha produït un error: " + ex.getMessage());
      }
      
      return pc;
   }

   @Override
   public PageContext pageException(PageContext pc, Exception exception) 
   {
      pc.showException(getWorkspace(), exception);
      
      return pc;
   }
}
