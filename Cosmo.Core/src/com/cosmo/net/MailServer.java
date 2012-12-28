package com.cosmo.net;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.cosmo.Cosmo;
import com.cosmo.Workspace;

/**
 * Implementa el servidor de correo electrónico.
 * 
 * @author Gerard Llort
 */
public class MailServer 
{
   private String transport;
   private String host;
   private String fromName;
   private String fromAddress;
   private String login;
   private String password;
   private boolean auth;
   private boolean starttls;

   //==============================================
   // Constructors
   //==============================================
   
   /**
    * Constructor de la clase {@link MailServer}.
    * 
    * @param workspace Una instancia de {@link Workspace} que representa el workspace actual.
    */
   public MailServer(Workspace workspace)
   {
      this.transport = workspace.getProperties().getString(Cosmo.PROPERTY_WORKSPACE_COMM_MAIL_TRANSPORT);
      this.host = workspace.getProperties().getString(Cosmo.PROPERTY_WORKSPACE_COMM_SMTP_HOST);
      this.auth = workspace.getProperties().getBoolean(Cosmo.PROPERTY_WORKSPACE_COMM_SMTP_AUTH);
      this.starttls = workspace.getProperties().getBoolean(Cosmo.PROPERTY_WORKSPACE_COMM_SMTP_STARTTLS);
      this.fromName = workspace.getProperties().getString(Cosmo.PROPERTY_WORKSPACE_COMM_SMTP_FROMNAME);
      this.fromAddress = workspace.getProperties().getString(Cosmo.PROPERTY_WORKSPACE_COMM_SMTP_FROMADD);
      this.login = workspace.getProperties().getString(Cosmo.PROPERTY_WORKSPACE_COMM_SMTP_LOGIN);
      this.password = workspace.getProperties().getString(Cosmo.PROPERTY_WORKSPACE_COMM_SMTP_PASSWORD);
   }
   
   //==============================================
   // Methods
   //==============================================
   
   /**
    * Envia un mensaje de correo electrónico.
    * 
    * @param message Una instancia de {@link MailMessage} que representa el mensaje de correo.
    * 
    * @throws UnsupportedEncodingException
    * @throws MessagingException
    */
   public void sendMail(MailMessage message) throws UnsupportedEncodingException, MessagingException
   {
      Properties props = System.getProperties();
      props.put("mail.transport.protocol", transport);
      props.put("mail.smtp.host", host);
      props.put("mail.smtp.auth", auth ? "true" : "false");
      props.put("mail.smtp.starttls.enable", starttls ? "true" : "false"); 
         
      Session session = Session.getInstance(props, null);
         
      Message msg = new MimeMessage(session);
      msg.setFrom(new InternetAddress(this.fromAddress, this.fromName));
      msg.setSubject(message.getSubject());
      msg.setText(message.getBody());
      for (InternetAddress add : message.getTo())
      {
         msg.addRecipient(Message.RecipientType.TO, add);
      }
         
      Transport transport = session.getTransport();
      transport.connect(host, login, password);
      transport.sendMessage(msg, msg.getAllRecipients());
      transport.close();
   }
}
