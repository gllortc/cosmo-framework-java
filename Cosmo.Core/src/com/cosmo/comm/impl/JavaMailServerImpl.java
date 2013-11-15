package com.cosmo.comm.impl;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.cosmo.Workspace;
import com.cosmo.comm.CommServer;
import com.cosmo.structures.PluginProperties;

/**
 * Implementa un servidor de correo electrónico basado en Java Mail API.
 * 
 * @author Gerard Llort
 */
public class JavaMailServerImpl implements CommServer
{
   private static final String PROPERTY_WORKSPACE_COMM_MAIL_TRANSPORT = "transport.protocol";
   private static final String PROPERTY_WORKSPACE_COMM_SMTP_HOST = "smtp.host";
   private static final String PROPERTY_WORKSPACE_COMM_SMTP_AUTH = "smtp.auth";
   private static final String PROPERTY_WORKSPACE_COMM_SMTP_STARTTLS = "smtp.starttls.enable";
   private static final String PROPERTY_WORKSPACE_COMM_SMTP_LOGIN = "smtp.login";
   private static final String PROPERTY_WORKSPACE_COMM_SMTP_PASSWORD = "smtp.password";
   private static final String PROPERTY_WORKSPACE_COMM_SMTP_FROMNAME = "smtp.from.name";
   private static final String PROPERTY_WORKSPACE_COMM_SMTP_FROMADD = "smtp.from.address";

   // private Workspace workspace;
   private String transportProtocol;
   private String host;
   private String fromName;
   private String fromAddress;
   private String login;
   private String password;
   private boolean authenticated;
   private boolean startTls;
   private PluginProperties properties;


   //==============================================
   // Constructors
   //==============================================

   /**
    * Constructor de la clase {@link JavaMailServerImpl}.
    * 
    * @param workspace Una instancia de {@link Workspace} que representa el workspace actual.
    */
   public JavaMailServerImpl(Workspace workspace)
   {
      // this.workspace = workspace;

      this.transportProtocol = workspace.getProperties().getString(PROPERTY_WORKSPACE_COMM_MAIL_TRANSPORT);
      this.host = workspace.getProperties().getString(PROPERTY_WORKSPACE_COMM_SMTP_HOST);
      this.authenticated = workspace.getProperties().getBoolean(PROPERTY_WORKSPACE_COMM_SMTP_AUTH);
      this.startTls = workspace.getProperties().getBoolean(PROPERTY_WORKSPACE_COMM_SMTP_STARTTLS);
      this.fromName = workspace.getProperties().getString(PROPERTY_WORKSPACE_COMM_SMTP_FROMNAME);
      this.fromAddress = workspace.getProperties().getString(PROPERTY_WORKSPACE_COMM_SMTP_FROMADD);
      this.login = workspace.getProperties().getString(PROPERTY_WORKSPACE_COMM_SMTP_LOGIN);
      this.password = workspace.getProperties().getString(PROPERTY_WORKSPACE_COMM_SMTP_PASSWORD);
   }


   //==============================================
   // Properties
   //==============================================

   @Override
   public PluginProperties getProperties()
   {
      return this.properties;
   }

   public String getTransportProtocol()
   {
      return transportProtocol;
   }

   public void setTransportProtocol(String transportProtocol)
   {
      this.transportProtocol = transportProtocol;
   }

   public String getHost()
   {
      return host;
   }

   public void setHost(String host)
   {
      this.host = host;
   }

   public String getFromName()
   {
      return fromName;
   }

   public void setFromName(String fromName)
   {
      this.fromName = fromName;
   }

   public String getFromAddress()
   {
      return fromAddress;
   }

   public void setFromAddress(String fromAddress)
   {
      this.fromAddress = fromAddress;
   }

   public String getLogin()
   {
      return login;
   }

   public void setLogin(String login)
   {
      this.login = login;
   }

   public String getPassword()
   {
      return password;
   }

   public void setPassword(String password)
   {
      this.password = password;
   }

   public boolean isAuthenticated()
   {
      return authenticated;
   }

   public void setAuthenticated(boolean authenticated)
   {
      this.authenticated = authenticated;
   }

   public boolean isStartTls()
   {
      return startTls;
   }

   public void setStartTls(boolean startTls)
   {
      this.startTls = startTls;
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
   @Override
   public void sendMessage(com.cosmo.comm.Message message) throws Exception
   {
      Properties props = System.getProperties();
      props.put("mail.transport.protocol", transportProtocol);
      props.put("mail.smtp.host", host);
      props.put("mail.smtp.auth", authenticated ? "true" : "false");
      props.put("mail.smtp.starttls.enable", startTls ? "true" : "false");

      Session session = Session.getInstance(props, null);

      Message msg = new MimeMessage(session);
      msg.setFrom(new InternetAddress(this.fromAddress, this.fromName));
      msg.setSubject(message.getSubject());
      msg.setText(message.getBody());
      for (Object add : message.getReceipients())
      {
         msg.addRecipient(Message.RecipientType.TO, (InternetAddress) add);
      }

      Transport transport = session.getTransport();
      transport.connect(host, login, password);
      transport.sendMessage(msg, msg.getAllRecipients());
      transport.close();
   }

}
