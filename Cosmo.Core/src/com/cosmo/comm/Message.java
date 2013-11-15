package com.cosmo.comm;

import java.util.ArrayList;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;

/**
 * Representa un mensage genérico (puede ser de correo electrónico, SMS, etc.).<br />
 * Lea atentamente las intrucciones de cada proveedor de comunicaciones para saber
 * que campos son reconocidos por los distintos servidores de comunicaciones.
 * 
 * @author Gerard Llort
 */
public class Message
{
   private String subject;
   private String body;
   private String htmlBody;
   private ArrayList<Object> receipients;
   private ArrayList<MimeBodyPart> multiPart;


   //==============================================
   // Constructors
   //==============================================

   /**
    * Constructor de la clase {@link MailMessage}.
    */
   public Message() 
   { 
      this.subject = "";
      this.body = "";
      this.htmlBody = "";
      this.receipients = new ArrayList<Object>();
      this.multiPart = new ArrayList<MimeBodyPart>();
   }


   //==============================================
   // Properties
   //==============================================

   /**
    * Devuelve el asunto del mensaje.
    */
   public String getSubject() 
   {
      return subject;
   }

   /**
    * Establece el asunto del mensaje.
    */
   public void setSubject(String subject) 
   {
      this.subject = subject;
   }

   /**
    * Devuelve el cuerpo del mensaje (sólo texto).
    */
   public String getBody() 
   {
      return body;
   }

   /**
    * Establece el cuerpo del mensaje (sólo texto).
    */
   public void setBody(String body) 
   {
      this.body = body;
   }

   /**
    * Devuelve el cuerpo del mensaje (en formato HTML si es aplicable).
    */
   public String getHtmlBody() 
   {
      return htmlBody;
   }

   /**
    * Establece el cuerpo del mensaje (en formato HTML si es aplicable).
    */
   public void setHtmlBody(String htmlBody) 
   {
      this.htmlBody = htmlBody;
   }

   /**
    * Devuelve la lista de destinatarios.<br />
    * Lea atentamente las instrucciones del servidor concreto para saber que tipos admite esta lista.
    */
   public ArrayList<Object> getReceipients() 
   {
      return receipients;
   }

   /**
    * Agrega un destinatario a la lista de destinatarios.
    * 
    * @param address Un objeto que representa un destinatario del mensaje (lea atentamente las instrucciones del servidor 
    *    concreto para saber que tipos son admitidos como destinatario). 
    */
   public void addReceipient(Object address)
   {
      this.receipients.add(address);
   }
   
   /**
    * Indica si el mensaje contiene archivos adjuntos.
    */
   public boolean hasAttachments()
   {
      return (this.multiPart.size() > 0);
   }

   /**
    * Devuelve la lista de archivos adjuntos del mensaje.
    * 
    * @return Una lista de instancias de {@link MimeBodyPart} que representan los archivos adjuntos al mensaje.
    */
   public ArrayList<MimeBodyPart> getAttachments()
   {
      return this.multiPart;
   }


   //==============================================
   // Methods
   //==============================================

   /**
    * Agrega un archivo adjunto al mensaje.
    * 
    * @param fileName Path + nombre del archivo a adjuntar.
    * 
    * @throws MessagingException
    */
   public void addAttachment(String fileName) throws MessagingException
   {
      DataSource source = new FileDataSource(fileName);
      
      MimeBodyPart attachment = new MimeBodyPart();
      attachment.setFileName("manual.pdf");
      attachment.setDataHandler(new DataHandler(source));
      
      multiPart.add(attachment);
   }
}
