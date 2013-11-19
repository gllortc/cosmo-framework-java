package com.cosmo.comm.impl;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.cosmo.comm.CommAgent;
import com.cosmo.comm.CommunicationsException;
import com.cosmo.structures.PluginProperties;
import com.cosmo.util.StringUtils;

/**
 * Implementa un servidor de correo electrónico basado en Java Mail API.
 * 
 * @author Gerard Llort
 */
public class JavaMailAgentImpl extends CommAgent
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


   //==============================================
   // Constructors
   //==============================================

   /**
    * Constructor de la clase {@link JavaMailAgentImpl}.
    * 
    * @param properties Una instancia de {@link PluginProperties} que contiene los parámetros de configuración del agente.
    */
   public JavaMailAgentImpl(PluginProperties properties)
   {
      super(properties);

      // Obtiene las propiedades y las almacena en variables locales
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

   /**
    * Devuelve el tipo de transporte (por defecto, smtp).
    */
   public String getTransportProtocol()
   {
      return transportProtocol;
   }

   /**
    * Devuelve el <em>host</em>.
    */
   public String getHost()
   {
      return host;
   }

   /**
    * Devuelve el puerto TCP/IP del <em>host</em>.
    */
   public int getPort()
   {
      return port;
   }

   /**
    * Devuelve el nombre del remitente por defecto que se usará para enviar los mensajes.
    */
   public String getFromName()
   {
      return fromName;
   }

   /**
    * Devuelve la dirección de correo electrónico (según <a href="http://www.ietf.org/rfc/rfc822.txt">RFC822</a>) del remitente por 
    * defecto que se usará para enviar los mensajes.
    */
   public String getFromAddress()
   {
      return fromAddress;
   }

   /**
    * Devuelve el login del usuario que se usa para la autenticación en el <em>host</em>.
    */
   public String getLogin()
   {
      return login;
   }

   /**
    * Devuelve la contraseña del usuario que se usa para la autenticación en el <em>host</em>.
    */
   public String getPassword()
   {
      return password;
   }

   /**
    * Indica si el servidor de correo electrónico usa autenticación.
    */
   public boolean isAuthenticated()
   {
      return authenticated;
   }

   /**
    * Indica si el servidor de correo electrónico usa Start TLS.
    */
   public boolean isStartTls()
   {
      return startTls;
   }


   //==============================================
   // Methods
   //==============================================

   /**
    * Envia un mensaje de correo electrónico.
    * 
    * @param message Una instancia de {@link MailMessage} que representa el mensaje de correo.
    * 
    * @throws CommunicationsException 
    */
   @Override
   public void sendMessage(com.cosmo.comm.Message message) throws CommunicationsException 
   {
      boolean isMultipart = false;
      Multipart multiPart = new MimeMultipart();

      // Configura el servidor de correo electrónico
      Properties props = System.getProperties();
      props.put("mail.transport.protocol", this.transportProtocol);
      props.put("mail.smtp.starttls.enable", this.startTls ? "true" : "false");
      props.put("mail.smtp.host", this.host);
      props.put("mail.smtp.port", this.port);
      props.put("mail.smtp.user", this.login);
      props.put("mail.smtp.password", this.password);
      props.put("mail.smtp.auth", this.authenticated ? "true" : "false");

      Session session = Session.getInstance(props, null);

      try
      {
         // Crea y configura la instancia para el correo electrónico a partir del mensaje proporcionado
         Message msg = new MimeMessage(session);
         msg.setFrom(new InternetAddress(this.fromAddress, this.fromName));
         for (Object add : message.getReceipients())
         {
            msg.addRecipient(Message.RecipientType.TO, (InternetAddress) add);
         }
         msg.setSubject(message.getSubject());
         msg.setText(message.getBody());
   
         // Si el mensaje tiene cuerpo en formato HTML, lo agrega como adjunto al correo electrónico
         if (!StringUtils.isNullOrEmptyTrim(message.getHtmlBody()))
         {
            MimeBodyPart attachment = new MimeBodyPart();
            attachment.setContent(message.getHtmlBody(), "text/html");
            multiPart.addBodyPart(attachment);
   
            isMultipart = true;
         }
         
         // Si el mensaje tiene adjuntos, los agrega al correo electrónico
         for (MimeBodyPart attachment : message.getAttachments())
         {
            multiPart.addBodyPart(attachment);
   
            isMultipart = true;
         }
   
         // Establece el contenido adjunto al correo electrónico
         if (isMultipart)
         {
            msg.setContent(multiPart);
         }
   
         // Realiza el envio del correo electrónico
         Transport transport = session.getTransport(transportProtocol);
         transport.connect(host, login, password);
         transport.sendMessage(msg, msg.getAllRecipients());
         transport.close();
      }
      catch (MessagingException ex)
      {
         throw new CommunicationsException("ERROR sending email message. " + ex.getMessage(), ex);
      }
      catch (UnsupportedEncodingException ex) 
      {
         throw new CommunicationsException("ERROR encoding mail address (mail: " + this.fromAddress + ", name: " + this.fromName + "). " + ex.getMessage(), ex);
      }
   }
}
