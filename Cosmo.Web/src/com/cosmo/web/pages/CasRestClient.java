package com.cosmo.web.pages;

import java.io.IOException;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;

/**
 * Servlet implementation class CasRestClient
 */
@WebServlet("/CasRestClient")
public class CasRestClient extends HttpServlet 
{
	private static final long serialVersionUID = 1L;
	
	public static final String LOGIN_URL_PART = "login";
   public static final String SERVICE_VALIDATE_URL_PART = "serviceValidate";
   public static final String TICKET_BEGIN = "ticket=";
   private static final String LT_BEGIN = "name=\"lt\" value=\"";
   public static final String CAS_USER_BEGIN = "<cas:user>";
   public static final String CAS_USER_END = "</cas:user>";
	
   private HttpClient fClient;
   private String fCasUrl;
   
	private static final Logger LOG = Logger.getLogger(CasRestClient.class.getName());
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CasRestClient() 
    {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
	   doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
	   String usuario = "gllort";  
      String pass = "dbag103";  
      String casServer = "http://localhost:8080/cas-server-webapp-3.5.2/";  
      String appServer = "http://localhost:8080/Cosmo.Web";

      authenticate(casServer, appServer, usuario, pass);
      
      /*LOG.info("USUARIO:  " + usuario + " -- CONTRASEÑA: " + pass);  

      String ticketGrantingTicket = getTicketGrantingTicket("https://developsc.intranet.dtgna:8443/cas-dipta/login", usuario, pass);  

      LOG.info("Este es el ticket que recupero: " + ticketGrantingTicket);  

      Cookie cookie = new Cookie("CASTGC", ticketGrantingTicket);  
      cookie.setSecure(false);  
      cookie.setPath("/cas");  
      cookie.setMaxAge(-1);  

      response.addCookie(cookie);*/
	}
	
	private void authenticate(String server, String serviceUrl, String username, String password)
	{
	   // Inicialitzacions
	   fClient = new HttpClient();

	   // Obté el LT
	   String lt = getLoginTicket(server, serviceUrl);
	   
	   // Autentica a l'usuari
	   String st = getServiceTicket(lt, server, serviceUrl, username, password);
	}
	
	private String getServiceTicket(String loginTicket, String server, String serviceUrl, String username, String password)
	{
	   String tgt = null;
      HttpMethod method = null;
      
      if (serviceUrl == null)
      {
          method = new GetMethod(server + LOGIN_URL_PART);
      }
      else
      {
          method = new PostMethod(server + LOGIN_URL_PART);
          ((PostMethod) method).setParameter("username", username);
          ((PostMethod) method).setParameter("password", password);
          ((PostMethod) method).setParameter("lt", loginTicket);
      }

      try
      {
          int statusCode = fClient.executeMethod(method);
          
          if (statusCode != HttpStatus.SC_OK)
          {
              method.releaseConnection();
          } 
          else
          {
              Header[] headers = method.getResponseHeaders();
              return extractSt(new String(method.getResponseBody()));
          }
      } 
      catch (Exception x)
      {
         System.out.print(x.getMessage());
      }
      finally
      {
         method.releaseConnection();
      }
      
      return tgt;
	}
	
	/**
    * Helper method to extract the service ticket from a login call to CAS.
    *
    * @param data Response data.
    * @return The service ticket, if it could be extracted, null otherwise.
    */
   protected String extractSt(String data)
   {
      int start;
      String serviceTicket = null;
      
      start = data.indexOf(TICKET_BEGIN);

      if (start > 0)
      {
         start += TICKET_BEGIN.length();
         serviceTicket = data.substring(start);
      }

      return serviceTicket;
   }
	
	
	
	
	
	
	private String getLoginTicket(String server, String serviceUrl)
	{
	   String lt = null;
      HttpMethod method = null;
      
      if (serviceUrl == null)
      {
          method = new GetMethod(server + LOGIN_URL_PART);
      }
      else
      {
          method = new PostMethod(server + LOGIN_URL_PART);
          ((PostMethod) method).setParameter("service", serviceUrl);
      }

      try
      {
          int statusCode = fClient.executeMethod(method);
          
          if (statusCode != HttpStatus.SC_OK)
          {
              // LOG.error( "Could not obtain LT token from CAS: " + method.getStatusLine() );
              method.releaseConnection();
          } 
          else
          {
              // Object o = method.getResponseHeaders();
              method.getResponseHeaders();
              return extractLt(new String(method.getResponseBody()));
          }
      } 
      catch (Exception x)
      {
         System.out.print(x.getMessage());
      }
      finally
      {
         method.releaseConnection();
      }
      
      return lt;
	}
	
	
	private String getTicketGrantingTicket(final String server, final String username, final String password) 
	{  
     final HttpClient client = new HttpClient();  
     final PostMethod post = new PostMethod(server);  

     post.setRequestBody(new NameValuePair[] 
     {  
        new NameValuePair("username", username),  
        new NameValuePair("password", password) 
     });  

     try 
     {  
         client.executeMethod(post);  
         final String response = post.getResponseBodyAsString();  

         switch (post.getStatusCode()) {  
         case 201: {  
             final Matcher matcher = Pattern.compile(".*action=\".*/(.*?)\".*").matcher(response);  

             if (matcher.matches()){  
                 return matcher.group(1);  
             }  
             LOG.info("Successful ticket granting request, but no ticket found!");  
             LOG.info("Response (1k): " + response.substring(0, Math.min(1024, response.length())));  
             break;  
         }  

         default:  
             LOG.info("Invalid response code (" + post.getStatusCode() + ") from CAS server!");  
             LOG.info("Response (1k): " + response.substring(0,Math.min(1024, response.length())));  
             break;  
         }  
     } catch (final IOException e) {  
         LOG.info(e.getMessage());  
     } finally {  
         post.releaseConnection();  
     }  

     return null;  
 	}
	
	/**
    * Helper method to extract the LT from a login form from CAS.
    *
    * @param data Response data.
    * @return The LT, if it could be extracted, null otherwise.
    */
   protected String extractLt(String data)
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
