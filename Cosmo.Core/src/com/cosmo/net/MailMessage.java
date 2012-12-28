package com.cosmo.net;

import java.util.ArrayList;

import javax.mail.internet.InternetAddress;

public class MailMessage 
{
   private String subject;
   private String body;
   private String htmlBody;
   private ArrayList<InternetAddress> to;
   
   public MailMessage() 
   { 
      this.subject = "";
      this.body = "";
      this.htmlBody = "";
      this.to = new ArrayList<InternetAddress>();
   }
   
   public String getSubject() 
   {
      return subject;
   }

   public void setSubject(String subject) 
   {
      this.subject = subject;
   }

   public String getBody() 
   {
      return body;
   }

   public void setBody(String body) 
   {
      this.body = body;
   }

   public String getHtmlBody() 
   {
      return htmlBody;
   }

   public void setHtmlBody(String htmlBody) 
   {
      this.htmlBody = htmlBody;
   }
   
   public ArrayList<InternetAddress> getTo() 
   {
      return to;
   }

   public void addReceipient(InternetAddress address)
   {
      this.to.add(address);
   }
   
}
