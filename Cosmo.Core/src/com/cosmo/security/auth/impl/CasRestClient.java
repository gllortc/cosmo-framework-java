package com.cosmo.security.auth.impl;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;

import com.cosmo.security.User;

/**
 * https://wiki.jasig.org/display/CASUM/RESTful+API
 * @author gllort
 *
 */
public class CasRestClient 
{
   private static final String LOGIN_URL_PART = "login";
   // private static final String SERVICE_VALIDATE_URL_PART = "serviceValidate";
   // private static final String TICKET_BEGIN = "ticket=";
   private static final String LT_BEGIN = "name=\"lt\" value=\"";
   // private static final String CAS_USER_BEGIN = "<cas:user>";
   // private static final String CAS_USER_END = "</cas:user>";
   
   private HttpClient httpClient;
   private String loginTicket;
   private String serviceTicket;
   private String casUrl;
   private String appUrl;
   
   public static void main(String [] args)
   {
      CasRestClient cas = new CasRestClient("https://localhost:8443/cas-server-webapp-3.5.2/", "http://localhost:8080/Cosmo.Web");
      cas.authenticate("gllort", "dbag103");
   }
   
   public CasRestClient(String casUrl, String appUrl)
   {
      this.httpClient = new HttpClient();
      
      this.casUrl = casUrl;
      this.appUrl = appUrl;
   }
   
   public User authenticate(String login, String password)
   {
      this.loginTicket = getLoginTicket();
      
      System.out.println("Login Ticket: " + this.loginTicket);
      
      this.serviceTicket = getGrantingTicket(login, password);
      
      System.out.println("Service Ticket: " + this.serviceTicket);
      
      return null;
   }
   
   //============================================
   // PRIVATE MEMBERS
   //============================================

   private String getGrantingTicket(String login, String password)
   {
      String lt = null;
      HttpMethod method = null;
      
      method = new PostMethod(this.casUrl + LOGIN_URL_PART);
      ((PostMethod) method).setParameter("username", login);
      ((PostMethod) method).setParameter("password", password);
      ((PostMethod) method).setParameter("lt", this.appUrl);

      try
      {
          int statusCode = httpClient.executeMethod(method);
          
          if (statusCode != HttpStatus.SC_OK)
          {
              method.releaseConnection();
          } 
          else
          {
              method.getResponseHeaders();
              
              String body = new String(method.getResponseBody());
              return extractLoginTicket(body);
          }
      } 
      catch (Exception x)
      {
          // LOG.error( "Could not obtain LT token from CAS: " + x.toString ());
      }
      finally
      {
         method.releaseConnection();
      }
      
      return lt;
   }
   
   private String getLoginTicket()
   {
       String lt = null;
       HttpMethod method = null;
       
       method = new PostMethod(this.casUrl + LOGIN_URL_PART);
       ((PostMethod) method).setParameter("service", this.appUrl);
       
       try
       {
           int statusCode = httpClient.executeMethod(method);
           
           if (statusCode != HttpStatus.SC_OK)
           {
               method.releaseConnection();
           } 
           else
           {
               method.getResponseHeaders();
               
               String body = new String(method.getResponseBody());
               return extractLoginTicket(body);
           }
       } 
       catch (Exception x)
       {
           // LOG.error( "Could not obtain LT token from CAS: " + x.toString ());
       }
       finally
       {
          method.releaseConnection();
       }
       
       return lt;
   }
   
   private String extractLoginTicket(String data)
   {
      int start;
      int end;
      String token = null;
      
      start = data.indexOf(LT_BEGIN);
       
      if ( start < 0 )
      {
         // LOG.error( "Could not obtain LT token from CAS: LT Token not found in response." );
      } 
      else
      {
         start += LT_BEGIN.length();
         end = data.indexOf("\"", start);
         token = data.substring(start, end);
      }

      return token;
   }
   
}
