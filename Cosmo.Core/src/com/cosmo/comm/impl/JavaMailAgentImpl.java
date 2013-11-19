package com.cosmo.comm.impl;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.cosmo.Workspace;
import com.cosmo.comm.CommServer;
import com.cosmo.structures.PluginProperties;
import com.cosmo.util.StringUtils;

/**
 * Implementa un servidor de correo electr�nico basado en Java Mail API.
 * 
 * @author Gerard Llort
 */
public class JavaMailAgentImpl implements CommServer
{
   private static final String PROPERTY_WORKSPACE_COMM_MAIL_TRANSPORT = "transport.protocol";
   private static final String PROPERTY_WORKSPACE_COMM_SMTP_HOST = "smtp.host";
   private static final String PROPERTY_WORKSPACE_COMM_SMTP_PORT = "smtp.port";
   private static final String PROPERTY_WORKSPACE_COMM_SMTP_AUTH = "smtp.auth";
   private static final String PROPERTY_WORKSPACE_COMM_SMTP_STARTTLS = "smtp.starttls.enable";
   private static final String PROPERTY_WORKSPACE_COMM_SMTP_LOGIN = "smtp.login";
   private static final String PROPERTY_WORKSPACE_COMM_SMTP_PASSWORD = "smtp.password";
   private static final String PROPERTY_WORKSPACE_COMM_SMTP_FROMNAME = "smtp.from.name";
   private static final String PROPERTY_WORKSPACE_COMM_SMTP_FROMADD = "smtp.from.address";

   private static final int DEFAULT_SMTP_PORT = 25;

   // private Workspace workspace;
   private String transportProtocol;
   private String host;
   private int port;
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
    * Constructor de la clase {@link JavaMailAgentImpl}.
    * 
    * @param workspace Una instancia de {@link Workspace} que representa el workspace actual.
    */
   public JavaMailAgentImpl(PluginProperties properties)
   {
      this.transportProtocol = properties.getParamString(PROPERTY_WORKSPACE_COMM_MAIL_TRANSPORT);
      this.host = properties.getParamString(PROPERTY_WORKSPACE_COMM_SMTP_HOST);
      this.port = properties.getParamInteger(PROPERTY_WORKSPACE_COMM_SMTP_PORT, DEFAULT_SMTP_PORT);
      this.authenticated = properties.getParamBoolean(PROPERTY_WORKSPACE_COMM_SMTP_AUTH, false);
      this.startTls = properties.getParamBoolean(PROPERTY_WORKSPACE_COMM_SMTP_STARTTLS, false);
      this.fromName = properties.getParamString(PROPERTY_WORKSPACE_COMM_SMTP_FROMNAME);
      this.fromAddress = properties.getParamString(PROPERTY_WORKSPACE_COMM_SMTP_FROMADD);
      this.login = properties.getParamString(PROPERTY_WORKSPACE_COMM_SMTP_LOGIN);
      this.password = properties.getParamString(PROPERTY_WORKSPACE_COMM_SMTP_PASSWORD);
   }


   //==============================================
   // Properties
   //==============================================

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
    * Devuelve los par�metros de configuraci�n del agente de comunicaciones actual.
    * 
    * @return Una instancia de {@link PluginProperties} que contiene los par�metros de configuraci�n.
    */
   @Override
   public PluginProperties getProperties()
   {
      return this.properties;
   }

   /**
    * Envia un mensaje de correo electr�nico.
    * 
    * @param message Una instancia de {@link MailMessage} que representa el mensaje de correo.
    * 
    * @throws Exception
    */
   @Override
   public void sendMessage(com.cosmo.comm.Message message) throws Exception
   {
      boolean isMultipart = false;
      Multipart multiPart = new MimeMultipart();

      // Configura el servidor de correo electr�nico
      Properties props = System.getProperties();
      props.put("mail.transport.protocol", this.transportProtocol);
      props.put("mail.smtp.starttls.enable", this.startTls ? "true" : "false");
      props.put("mail.smtp.host", this.host);
      props.put("mail.smtp.port", this.port);
      props.put("mail.smtp.user", this.login);
      props.put("mail.smtp.password", this.password);
      props.put("mail.smtp.auth", this.authenticated ? "true" : "false");
      

      Session session = Session.getInstance(props, null);

      // Crea y configura la instancia para el correo electr�nico a partir del mensaje proporcionado
      Message msg = new MimeMessage(session);
      msg.setFrom(new InternetAddress(this.fromAddress, this.fromName));
      for (Object add : message.getReceipients())
      {
         msg.addRecipient(Message.RecipientType.TO, (InternetAddress) add);
      }
      msg.setSubject(message.getSubject());
      msg.setText(message.getBody());

      // Si el mensaje tiene cuerpo en formato HTML, lo agrega como adjunto al correo electr�nico
      if (!StringUtils.isNullOrEmptyTrim(message.getHtmlBody()))
      {
         MimeBodyPart attachment = new MimeBodyPart();
         attachment.setContent(message.getHtmlBody(), "text/html");
         multiPart.addBodyPart(attachment);

         isMultipart = true;
      }
      
      // Si el mensaje tiene adjuntos, los agrega al correo electr�nico
      for (MimeBodyPart attachment : message.getAttachments())
      {
         multiPart.addBodyPart(attachment);

         isMultipart = true;
      }

      // Establece el contenido adjunto al correo electr�nico
      if (isMultipart)
      {
         msg.setContent(multiPart);
      }

      // Realiza el envio del correo electr�nico
      Transport transport = session.getTransport(transportProtocol);
      transport.connect(host, login, password);
      transport.sendMessage(msg, msg.getAllRecipients());
      transport.close();
   }

}