package com.cosmo.security.providers;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.cosmo.Workspace;
import com.cosmo.security.Agent;
import com.cosmo.security.User;
import com.cosmo.security.UserNotFoundException;
import com.cosmo.util.StringUtils;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;

/**
 * Proveedor de seguridad nativo de Cosmo.<br />
 * Este proveedor requiere conexión a BBDD y tener las tablas de usuarios de Cosmo.
 * 
 * @author Gerard Llort
 */
public class CasAuthenticationProvider extends AuthenticationProvider 
{
   private Workspace workspace;
   private Agent agent;

   // private static String PARAM_CONTEXTFACTORY = "context-factory";
   // private static String PARAM_PROVIDERURL = "provider-url";
   
   //==============================================
   // Constructors
   //==============================================
   
   /**
    * Constructor de la clase. 
    * 
    * @param workspace Una instancia de {@link Workspace} que representa el workspace al que está conectado actualmente.
    */
   public CasAuthenticationProvider(Workspace workspace)
   {
      this.workspace = workspace;
      this.agent = workspace.getProperties().getAuthenticationAgent();
   }
   
   
   //==============================================
   // Methods
   //==============================================
   
   /**
    * Verifica las credenciales de un usuario.
    * 
    * @param login Login del usuario.
    * @param password Contraseña (sin encriptar) del usuario.
    * @return Una instancia de {@link User} que representa el usuario al que corresponden las credenciales proporcionadas.
    * 
    * @throws UserNotFoundException
    * @throws AuthenticationProviderException 
    */
   @Override
   public User login(String login, String password) throws UserNotFoundException, AuthenticationProviderException
   {
      User user = null;
      
      try
      {
         getTicket("server", login, password, "service");
      }
      catch (Exception ex)
      {
         throw new AuthenticationProviderException(ex.getMessage(), ex);
      }
      
      return user;
   }
   
   /**
    * Elimina la información de autenticación actual.
    */
   @Override
   public void logout()
   {
      return;
   }
   
   
   //==============================================
   // Private members
   //==============================================
   
   // private static final Logger LOG = Logger.getLogger(Client.class.getName());
   
   public static String getTicket(final String server, final String username, final String password, final String service)
   {
      notNull(server, "server must not be null");
      notNull(username, "username must not be null");
      notNull(password, "password must not be null");
      notNull(service, "service must not be null");
  
      return getServiceTicket(server, getTicketGrantingTicket(server, username, password), service);
   }
  
   private static String getServiceTicket(final String server, final String ticketGrantingTicket, final String service)
   {
      if (ticketGrantingTicket == null)
      {
         return null;
      }
  
      final HttpClient client = new HttpClient();
      final PostMethod post = new PostMethod(server + "/" + ticketGrantingTicket);
  
      post.setRequestBody(new NameValuePair[] 
      { 
            new NameValuePair("service", service) 
      });
  
      try
      {
         client.executeMethod(post);
  
         final String response = post.getResponseBodyAsString();
  
         switch (post.getStatusCode())
         {
            case 200:
               return response;
  
            default:
               // LOG.warning("Invalid response code (" + post.getStatusCode() + ") from CAS server!");
               // LOG.info("Response (1k): " + response.substring(0, Math.min(1024, response.length())));
               break;
         }
      }
      catch (final IOException e)
      {
         // LOG.warning(e.getMessage());
      }
      finally
      {
         post.releaseConnection();
      }

      return null;
   }
  
   private static String getTicketGrantingTicket(final String server, final String username, final String password)
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
  
         switch (post.getStatusCode())
         {
            case 201:
            {
               final Matcher matcher = Pattern.compile(".*action=\".*/(.*?)\".*").matcher(response);
  
               if (matcher.matches())
               {
                  return matcher.group(1);
               }
  
               // LOG.warning("Successful ticket granting request, but no ticket found!");
               // LOG.info("Response (1k): " + response.substring(0, Math.min(1024, response.length())));
               break;
            }
  
            default:
               // LOG.warning("Invalid response code (" + post.getStatusCode() + ") from CAS server!");
               // LOG.info("Response (1k): " + response.substring(0, Math.min(1024, response.length())));
               break;
         }
      }
      catch (final IOException e)
      {
        // LOG.warning(e.getMessage());
      }
      finally
      {
        post.releaseConnection();
      }
  
      return null;
   }
  
   private static void notNull(final Object object, final String message)
   {
     if (object == null)
     {
        throw new IllegalArgumentException(message);
     }
   }
  
   public static void main(final String[] args)
   {
     final String server = "http://localhost:8080/cas/v1/tickets";
     final String username = "username";
     final String password = "password";
     final String service = "http://localhost:8080/service";
  
     // LOG.info(getTicket(server, username, password, service));
   }
}
