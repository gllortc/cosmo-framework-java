package com.cosmo.security.providers;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;

import com.cosmo.Workspace;
import com.cosmo.security.Agent;
import com.cosmo.security.User;
import com.cosmo.security.UserNotFoundException;

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

   private static String PARAM_CASSERVICE = "cas-service";
   private static String PARAM_SERVICEURL = "service-url";
   
   private String grantingTicket;
   
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
      grantingTicket = "";
      fClient = new HttpClient();
      
      this.workspace = workspace;
      this.agent = this.workspace.getProperties().getAuthenticationAgent();
   }
   
   
   //==============================================
   // Properties
   //==============================================
   
   /**
    * Devuelve el Ticket Granting Ticket de CAS.
    * 
    * @return Una cadena que contiene el Ticket Granting Ticket de CAS.
    */
   public String getGrantingTicket()
   {
      return this.grantingTicket;
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
         fCasUrl = agent.getParam(PARAM_CASSERVICE);
         
         authenticate(agent.getParam(PARAM_SERVICEURL), login, password);
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
   
   /**
    * Revalida la sesión de usuario.
    */
   @Override
   public void validate() 
   {
      return;   
   };
   
   /**
    * Indica si el servicio usa un gateway para la autenticación de usuarios.
    */
   public boolean isLoginGateway()
   {
      return false;
   }
   
   /**
    * Devuelve la URL usada para la autenticación de usuarios.
    */
   public String getLoginGateway()
   {
      String host = agent.getParam(PARAM_CASSERVICE).trim();
      host += (host.endsWith("/") ? "" : "/") + "login";
      
      com.cosmo.util.URL url = new com.cosmo.util.URL(host);
      url.addParameter("service", workspace.getRequestedUrl());
      
      return url.toString();
   }
   
   //==============================================
   // Private members
   //==============================================
   
   // public static Logger LOG = Logger.getLogger( CasClient.class  );

   public static final String LOGIN_URL_PART = "login";
   public static final String SERVICE_VALIDATE_URL_PART = "serviceValidate";
   public static final String TICKET_BEGIN = "ticket=";
   private static final String LT_BEGIN = "name=\"lt\" value=\"";
   public static final String CAS_USER_BEGIN = "<cas:user>";
   public static final String CAS_USER_END = "</cas:user>";
  
   private HttpClient fClient;
   private String fCasUrl;
  
   /**
    * Construct a new CasClient.
    *
    * @param casUrl The base URL of the CAS service to be used.
    */
   /*public CasClient( String casBaseUrl )
   {
       this( new HttpClient(), casBaseUrl );
   }*/
  
   /**
    * Construct a new CasClient which uses the specified HttpClient
    * for its HTTP calls.
    *
    * @param client
    * @param casBaseUrl
    */
   /*public CasClient( HttpClient client, String casBaseUrl )
   {
       fClient = client;
       fCasUrl = casBaseUrl;
   }*/
  
   /**
    * Authenticate the specified username with the specified password.
    * This will not yield any ticket, as no service is authenticated
    * against. This wil just set the CAS cookie in this client upon
    * successful authentication.
    *
    * @param username
    * @param password
    */
   public void authenticate(String username, String password)
   {
      authenticate(null, username, password);
   }
  
   /**
    * Authenticate the specified user with the specified password against the specified service.
    *
    * @param serviceUrl May be null. If a url is specified, the authentication will happen against this service, yielding a service ticket which can be validated.
    * @param username
    * @param password
    * @return A valid service ticket, if and only if the specified service URL is not null.
    */
   public String authenticate(String serviceUrl, String username, String password)
   {
      String lt = getLt(serviceUrl);
      String result = null;
      PostMethod method = null;
       
      if (lt == null)
      {
         // LOG.error( "Cannot retrieve LT from CAS. Aborting authentication for '" + username + "'" );
         return null;
      }
       
      method = new PostMethod(fCasUrl + LOGIN_URL_PART);

      /*if (serviceUrl != null) // optional
      {
         method.setParameter("service", serviceUrl);
      }
      method.setParameter("_eventId", "submit");
      method.setParameter("username", username);
      method.setParameter("password", password);
      method.setParameter("lt", lt);
      method.setParameter("gateway", "true");*/
      
      method.setRequestBody(new NameValuePair[] 
      {
         new NameValuePair("service", serviceUrl),
         new NameValuePair("_eventId", "submit"),
         new NameValuePair("username", username),
         new NameValuePair("password", password),
         new NameValuePair("lt", lt),
         new NameValuePair("gateway", "true")
      });
      
      try
      {
         fClient.executeMethod(method);
           
         if (serviceUrl == null)
         {
            // if CAS does not return a login page with an LT authentication was successful
            if (extractLt(new String(method.getResponseBody())) != null)
            {
               // LOG.error("Authentication for '" +  username + "' unsuccessful");
               // if (LOG.isDebugEnabled())
               // {
                  // LOG.debug( "Authentication for '" + username + "' unsuccessful." );
               // }
            } 
            else
            {
               // if (LOG.isDebugEnabled())
               // {
               //    LOG.debug( "Authentication for '" + username + "' unsuccessful." );
               // }
            }
         } 
         else
         {
            Header h = method.getResponseHeader("Location");
               
            if (h != null)
            {
               result = extractServiceTicket(h.getValue());
            }
               
            if (result == null)
            {
               // LOG.error( "Authentication for '" + username + "' unsuccessful." );
            }
         }
      } 
      catch (Exception x)
      {
         // LOG.error( "Could not authenticate'" + username + "':" + x.toString () );
      }
      finally
      {
         method.releaseConnection();
      }
       
      return result;
   }
   
   /**
    * Validate the specified service ticket against the specified service.
    * If the ticket is valid, this will yield the clear text user name
    * of the autenticated user.<br>
    * Note that each service ticket issued by CAS can be used exactly once
    * to validate.
    *
    * @param serviceUrl
    * @param serviceTicket
    *
    * @return Clear text username of the authenticated user.
    */
   public String validate(String serviceUrl, String serviceTicket)
   {
      String result = null;
      PostMethod method = new PostMethod(fCasUrl + SERVICE_VALIDATE_URL_PART);
       
      method.setParameter("service", serviceUrl);
      method.setParameter("ticket", serviceTicket);
      
      try
      {
         int statusCode = fClient.executeMethod(method);
         
         if (statusCode != HttpStatus.SC_OK)
         {
            // LOG.error("Could not validate: " + method.getStatusLine());
            method.releaseConnection();
         } 
         else
         {   
            result = extractUser(new String(method.getResponseBody()));
         }
      } 
      catch (Exception x)
      {
         // LOG.error("Could not validate: " + x.toString ());
         x.printStackTrace();
      }
      finally
      {
         method.releaseConnection();
      }
      
      return result;
   }
  
   /**
    * Helper method to extract the user name from a "service validate" call to CAS.
    *
    * @param data Response data.
    * @return The clear text username, if it could be extracted, null otherwise.
    */
   protected String extractUser(String data)
   {
      int start;
      int end;
      String user = null;
      
      start = data.indexOf(CAS_USER_BEGIN);
       
      if (start >= 0)
      {
         start += CAS_USER_BEGIN.length();
         end = data.indexOf(CAS_USER_END);
           
         if (end > start)
         {
            user = data.substring(start, end);
         }
         else
         {
            // LOG.warn("Could not extract username from CAS validation response. Raw data is: '" + data + "'" );
         }
      } 
      else
      {
         // LOG.warn("Could not extract username from CAS validation response. Raw data is: '" + data + "'" );
      }

      return user;
   }
  
   /**
    * Helper method to extract the service ticket from a login call to CAS.
    *
    * @param data Response data.
    * @return The service ticket, if it could be extracted, null otherwise.
    */
   protected String extractServiceTicket(String data)
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
  
   /**
    * This method requests the original login form from CAS.
    * This form contains an LT, an initial token that must be
    * presented to CAS upon sending it an authentication request
    * with credentials.<br>
    * If a service URL is provided (which is optional), this method
    * will post the URL such that CAS authenticates against the
    * specified service when a subsequent authentication request is
    * sent.
    *
    * @param serviceUrl
    * @return The LT token if it could be extracted from the CAS response.
    */
   protected String getLt(String serviceUrl)
   {
       String lt = null;
       HttpMethod method = null;
       
       if (serviceUrl == null)
       {
           method = new GetMethod(fCasUrl + LOGIN_URL_PART);
       }
       else
       {
           method = new PostMethod(fCasUrl + LOGIN_URL_PART);
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
               Object o = method.getResponseHeaders();
               return extractLt(new String(method.getResponseBody()));
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
}
