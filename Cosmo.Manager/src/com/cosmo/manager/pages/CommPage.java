package com.cosmo.manager.pages;

import javax.mail.internet.InternetAddress;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cosmo.Cosmo;
import com.cosmo.comm.CommunicationsFactory;
import com.cosmo.comm.Message;
import com.cosmo.net.HttpRequestUtils;
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
import com.cosmo.ui.controls.FormFieldText;
import com.cosmo.ui.controls.FormFieldTextArea;
import com.cosmo.ui.controls.FormFieldset;
import com.cosmo.ui.controls.Icon;

/**
 * Servlet implementation class FormPage
 */
@WebServlet(description = "Cosmo Communications Service Sample", urlPatterns = { "/CommPage" })
public class CommPage extends Page 
{
   /** Serial Version UID */
   private static final long serialVersionUID = 8878330001994265668L;
   
   private static final String ID_MSG = "msg";

   @Override
   public PageContext initPageEvent(PageContext pc, HttpServletRequest request, HttpServletResponse response) 
   {
      pc.setLayout(PageLayout.TwoColumnsLeft);
      pc.setTitle("Cosmo - Samples - Communications Service");

      BreadcrumbsControl navbar = new BreadcrumbsControl(getWorkspace());
      navbar.addItem(new BreadcrumbsItem("Home", "HomePage", Icon.ICON_IMAGE_HOME));
      navbar.addItem(new BreadcrumbsItem("Samples", "SamplesPage"));
      navbar.addItem(new BreadcrumbsItem("Communications Service", ""));
      pc.addContent(navbar, ContentColumns.MAIN);

      /*HeaderControl header = new HeaderControl(getWorkspace());
      header.setTitle("Form Control");
      header.setDescription("Exemple de formulari. Per consultar el contingut de les dades que s'introdueixen en aquest formulari, consultar l'exemple de Grid Control.");
      pc.addContent(header, ContentColumns.MAIN);*/

      DynamicMessageControl message = new DynamicMessageControl(getWorkspace(), ID_MSG);
      pc.addContent(message, ContentColumns.MAIN);

      FormControl form = new FormControl(getWorkspace(), "SendMessageForm");
      FormFieldset group = new FormFieldset("Enviar Missatge", "Introdueixi les dades del missatge que desitja enviar.", form);
      group.addField(new FormFieldText("txtName", "Nom del destinatari"));
      group.addField(new FormFieldText("txtMail", "Correu electrònic"));
      group.addField(new FormFieldTextArea("txtBody", "Missatge"));
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
         // Genera el mensaje
         Message message = new Message();
         message.setFrom(getWorkspace().getProperties().getString(Cosmo.PROPERTY_WORKSPACE_MAIL));
         message.addReceipient(new InternetAddress(HttpRequestUtils.getValue(request, "txtMail"), 
                                                   HttpRequestUtils.getValue(request, "txtName")));
         message.setHtmlBody(HttpRequestUtils.getValue(request, "txtBody"));

         // Envia el mensaje
         CommunicationsFactory.sendMessage(getWorkspace(), message);

         DynamicMessageControl msg = (DynamicMessageControl) pc.getControl(ID_MSG);
         msg.setVisible(true);
         msg.setType(DynamicMessageControl.MessageTypes.Information);
         msg.setMessage("Missatge enviat correctament.");
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