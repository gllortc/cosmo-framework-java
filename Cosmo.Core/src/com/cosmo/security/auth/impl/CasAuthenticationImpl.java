package com.cosmo.security.auth.impl;

import java.io.IOException;
import java.io.StringReader;
import java.util.AbstractMap.SimpleEntry;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.cosmo.Workspace;
import com.cosmo.net.HttpRequestUtils;
import com.cosmo.net.URL;
import com.cosmo.security.User;
import com.cosmo.security.UserNotFoundException;
import com.cosmo.security.annotations.LoginGatewayAgent;
import com.cosmo.security.auth.Authentication;
import com.cosmo.security.auth.AuthenticationException;
import com.cosmo.structures.PluginProperties;
import com.cosmo.util.StringUtils;

/**
 * Implementación del agente de autenticación para CAS 2.0.
 * 
 * @version 1.0.0
 * @author Gerard Llort
 */
@LoginGatewayAgent
public class CasAuthenticationImpl implements Authentication
{
   // Parámetros URL
   private static String URL_PARAM_TICKET = "ticket";
   private static String URL_PARAM_SERVICE = "service";
   
   // Llamadas URL
   private static final String SERVICE_VALIDATE_URL_PART = "serviceValidate";
   private static final String LOGIN_URL_PART = "login";
   
   // Tags XML para el parseo de las respuestas de CAS
   private static String TAG_CAS_USER = "user";
   private static String TAG_CAS_PROPERTY = "attribute";
   private static String TAG_CAS_ATTRIBNAME = "name";
   private static String TAG_CAS_ATTRIBVALUE = "value";
   
   // Constantes para obtener las propiedades de configuración del agente
   private static String AGENT_PARAM_CASSERVICE = "cas-service";
   private static String AGENT_PARAM_SERVICEURL = "service-url";
   private static String CAS_ATTRIB_MAIL = "cas-attrib-mail";
   private static String CAS_ATTRIB_COMPLETNAME = "cas-attrib-cname";
   private static String CAS_ATTRIB_NAME = "cas-attrib-name";
   private static String CAS_ATTRIB_SURNAME = "cas-attrib-surname";
   
   private Workspace workspace;
   private PluginProperties agent;
   private String serviceTicket;
   private HttpClient httpClient;
   
   //==============================================
   // Constructors
   //==============================================
   
   /**
    * Constructor de la clase. 
    * 
    * @param workspace Una instancia de {@link Workspace} que representa el workspace al que está conectado actualmente.
    */
   public CasAuthenticationImpl(Workspace workspace)
   {
      this.serviceTicket = "";
      this.httpClient = new HttpClient();
      this.workspace = workspace;
      this.agent = this.workspace.getProperties().getSecurityProperties().getAuthenticationAgent();
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
      return this.serviceTicket;
   }
   
   /**
    * Devuelve un {@code hash} que contiene los parámetros de configuración del agente de seguridad.
    */
   public HashMap<String, String> getParameters()
   {
      return agent.getParams();
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
    * @throws AuthenticationException 
    */
   @Override
   public User login(String login, String password) throws UserNotFoundException, AuthenticationException
   {
      // Este agente usa Login Gateway por lo que este método no se invocará nunca.
      return null;
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
    * Indica si una respuesta corresponde al retorno de la acción de login.
    * 
    * @param request Una instancia de {@link HttpServletRequest} que cotniene el contexto de la llamada.
    * 
    * @return {@code true} si la petición corresponde al retorno de la pantalla de login o {@code false} en cualquier otro caso.
    */
   public boolean isLoginGatewayResponse(HttpServletRequest request)
   {
      String st = HttpRequestUtils.getValue(request, URL_PARAM_TICKET);
      return (st != null && !st.isEmpty());
   }
   
   /**
    * Devuelve la URL usada para la autenticación de usuarios.
    */
   public String getLoginGatewayUrl()
   {
      URL url = new URL(agent.getParamString(AGENT_PARAM_CASSERVICE).trim());
      url.addFolder(LOGIN_URL_PART);
      url.addParameter(URL_PARAM_SERVICE, agent.getParamString(AGENT_PARAM_SERVICEURL)); 
      
      return url.toString();
   }
   
   /**
    * Detecta si una autenticación delegada (Login Gateway) ha sido exitosa.<br />
    * Las clases que extiendan a {@link Authentication} serán responsables de obtener los datos del usuario 
    * autenticado en el sistema externo, ya sea mediante servicios REST u otros mecanismos.
    * 
    * @param request Una instancia de {@link HttpServletRequest} que cotniene el contexto de la llamada.
    * 
    * @return Una instancia de {@link User} que contiene las propiedades del usuario autenticado o {@code null} en cualquier otro caso. 
    */
   @Override
   public User getLoginGatewayUser(HttpServletRequest request)
   {
      try
      {
         // Obtiene el ServiceTicket
         serviceTicket = HttpRequestUtils.getValue(request, URL_PARAM_TICKET);
   
         if (!StringUtils.isNullOrEmpty(serviceTicket))
         {
            // Valida el ticket y obtiene una instancia con las propiedades del usuario
            return validate(agent.getParamString(AGENT_PARAM_SERVICEURL), serviceTicket);
         }
         else
         {
            return null;
         }
      }
      catch (AuthenticationException ex)
      {
         return null;
      }
   }
   
   
   //==============================================
   // Private members
   //==============================================
   
   /**
    * Valida el ticket obtenido del servidor CAS para la URL del servicio (aplicación).
    * 
    * @param serviceUrl Una cadena que representa la URL del servicio (aplicación).
    * @param serviceTicket Una cadena que contiene el Service Ticket obtenido en el retorno de la pantalla de Login. 
    * 
    * @return Una instancia de {@link User} si la validación es correcta o {@code null} en cualquier otro caso.
    * 
    * @throws AuthenticationException
    */
   private User validate(String serviceUrl, String serviceTicket) throws AuthenticationException
   {
      User user = null;
      
      URL url = new URL(agent.getParamString(AGENT_PARAM_CASSERVICE));
      url.addFolder(SERVICE_VALIDATE_URL_PART);
      
      String toUrl = url.toString();
      
      PostMethod method = new PostMethod(toUrl);
      method.setParameter(URL_PARAM_SERVICE, serviceUrl);
      method.setParameter(URL_PARAM_TICKET, serviceTicket);
      
      try
      {
         int statusCode = httpClient.executeMethod(method);
         
         if (statusCode != HttpStatus.SC_OK)
         {
            method.releaseConnection();
            throw new AuthenticationException("El servidor de CAS no ha respondido correctamente a la llamada de validación de la autenticación (CAS ticket=" + serviceTicket + ").");
         } 
         else
         {  
            String resp = new String(method.getResponseBody());            
            user = getUserDataFromValidation(resp);

            // result = extractUser(new String(method.getResponseBody()));
         }
      }
      catch (IOException ex)
      {
         throw new AuthenticationException(ex.getMessage(), ex);
      }
      catch (AuthenticationException ex)
      {
         throw ex;
      }
      finally
      {
         method.releaseConnection();
      }
      
      return user;
   }
   
   /**
    * Parsea la respuesta de la validación de ticket de CAS y extrae la información del usuario.
    * 
    * @param responseData Una cadena que contiene la respuesta del servidor CAS (en formato XML).
    * 
    * @return Una instancia de {@link User} que contiene los datos del usuario.
    * 
    * @throws AuthenticationException 
    */
   public User getUserDataFromValidation(String responseData) throws AuthenticationException
   {
      Node nNode;
      NodeList nList;
      SimpleEntry<String, String> attrib = null;
      User user = null;
      
      try
      {
         DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
         factory.setNamespaceAware(true);

         DocumentBuilder builder = factory.newDocumentBuilder();
         Document doc = builder.parse(new InputSource(new StringReader(responseData)));
         doc.getDocumentElement().normalize();
         
         // Obtiene el LOGIN del usuario
         nList = doc.getElementsByTagNameNS("*", TAG_CAS_USER);
         for (int temp = 0; temp < nList.getLength(); temp++)
         {
            nNode = nList.item(0);
            if (nNode.getNodeType() == Node.ELEMENT_NODE)
            {
               user = new User();
               user.setLogin(nNode.getFirstChild().getNodeValue());
               break;
            }
         }
         
         // Si no se ha podido extraer el usuario, se devuelve null 
         if (user == null)
         {
            return user;
         }
         
         // Obtiene las PROPIEDADES del usuario
         nList = doc.getElementsByTagNameNS("*", TAG_CAS_PROPERTY);
         for (int temp = 0; temp < nList.getLength(); temp++)
         {
            nNode = nList.item(temp);
            if (nNode.getNodeType() == Node.ELEMENT_NODE)
            {
               attrib = getResponseAttribute((Element) nNode);
               setUserAttribute(user, attrib.getKey(), attrib.getValue());
            }
         }
         
         return user;
      }
      catch (ParserConfigurationException ex)
      {
         throw new AuthenticationException(ex.getMessage(), ex);
      }
      catch (SAXException ex)
      {
         throw new AuthenticationException(ex.getMessage(), ex);
      }
      catch (IOException ex)
      {
         throw new AuthenticationException(ex.getMessage(), ex);
      }
   }
   
   /**
    * Obtiene los valores (nombre y valor) de un atributo de usuario en la respuesta del servidor CAS.
    * 
    * @param attribNode Una instancia de {@link Element} que representa el nodo raíz del atributo a leer.
    * @param name La cadena que se actualizará con el nombre del atributo.
    * @param value La cadena que se actualizará con el valor del atributo.
    */
   private SimpleEntry<String, String> getResponseAttribute(Element attribNode)
   {
      String name = "";
      String value = "";
      Node node;
      NodeList nList;
      
      nList = attribNode.getElementsByTagNameNS("*", TAG_CAS_ATTRIBNAME);
      if (nList.getLength() > 0)
      {
         node = nList.item(0);
         name = node.getFirstChild().getNodeValue();
      }
      
      nList = attribNode.getElementsByTagNameNS("*", TAG_CAS_ATTRIBVALUE);
      if (nList.getLength() > 0)
      {
         node = nList.item(0);
         value = node.getFirstChild().getNodeValue();
      }

      if (!name.isEmpty())
      {
         return new SimpleEntry<String, String>(name, value);
      }
      else
      {
         return null;
      }
   }
   
   /**
    * Establece un atributo del usuario.<br />
    * Si el nombre de la propiedad es desconocida, se omite cualquier acción.
    * 
    * @param user La instancia de {@link User} que va a ser actualizada.
    * @param name Nombre del atributo a actualizar (constantes {@code CAS_ATTRIB_XXXX}).
    * @param value Valor que va a tomar el atributo.
    */
   private void setUserAttribute(User user, String name, String value)
   {
      if (agent.getParamString(CAS_ATTRIB_MAIL) != null && 
          agent.getParamString(CAS_ATTRIB_MAIL).equalsIgnoreCase(name))
      {
         user.setMail(value);
         return;
      }

      if (agent.getParamString(CAS_ATTRIB_NAME) != null && 
          agent.getParamString(CAS_ATTRIB_NAME).equalsIgnoreCase(name))
      {
         user.setName(value);
         return;
      }
      
      if (agent.getParamString(CAS_ATTRIB_SURNAME) != null && 
          agent.getParamString(CAS_ATTRIB_SURNAME).equalsIgnoreCase(name))
      {
         user.setName(user.getName() + " " + value);
         return;
      }
      
      if (agent.getParamString(CAS_ATTRIB_COMPLETNAME) != null && 
          agent.getParamString(CAS_ATTRIB_COMPLETNAME).equalsIgnoreCase(name))
      {
         user.setName(value);
         return;
      }
   }
   
/*   
   
   // public static Logger LOG = Logger.getLogger( CasClient.class  );
   
   public static final String TICKET_BEGIN = "ticket=";
   private static final String LT_BEGIN = "name=\"lt\" value=\"";
   public static final String CAS_USER_BEGIN = "<cas:user>";
   public static final String CAS_USER_END = "</cas:user>";
  
   private HttpClient fClient;
   private String fCasUrl;
  
   // public String 
  
   / **
    * Authenticate the specified username with the specified password.
    * This will not yield any ticket, as no service is authenticated
    * against. This wil just set the CAS cookie in this client upon
    * successful authentication.
    *
    * @param username
    * @param password
    * /
   public void authenticate(String username, String password)
   {
      authenticate(null, username, password);
   }
  
   / **
    * Authenticate the specified user with the specified password against the specified service.
    *
    * @param serviceUrl May be null. If a url is specified, the authentication will happen against this service, yielding a service ticket which can be validated.
    * @param username
    * @param password
    * @return A valid service ticket, if and only if the specified service URL is not null.
    * /
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

      / *if (serviceUrl != null) // optional
      {
         method.setParameter("service", serviceUrl);
      }
      method.setParameter("_eventId", "submit");
      method.setParameter("username", username);
      method.setParameter("password", password);
      method.setParameter("lt", lt);
      method.setParameter("gateway", "true");* /
      
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
   
   
  
   / **
    * Helper method to extract the user name from a "service validate" call to CAS.
    *
    * @param data Response data.
    * @return The clear text username, if it could be extracted, null otherwise.
    * /
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
  
   / **
    * Helper method to extract the service ticket from a login call to CAS.
    *
    * @param data Response data.
    * @return The service ticket, if it could be extracted, null otherwise.
    * /
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
  
   / **
    * Helper method to extract the LT from a login form from CAS.
    *
    * @param data Response data.
    * @return The LT, if it could be extracted, null otherwise.
    * /
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
  
   / **
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
    * /
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
               // Object o = method.getResponseHeaders();
               method.getResponseHeaders();
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
   
   
   
   
   
   public static void main(String[] args)
   {
      
      
      String str = "<cas:serviceResponse xmlns:cas='http://www.yale.edu/tp/cas'>\n" +
                   "<cas:authenticationSuccess>\n" +
                   "  <cas:user>gllort</cas:user>\n" +
                   "  <!-- Begin Ldap Attributes -->\n" +
                   "    <cas:attributes>\n" +
                   "      <cas:attribute>\n" +
                   "        <cas:name>mail</cas:name>\n" +
                   "        <cas:value>gllort@altanet.org</cas:value>\n" +
                   "      </cas:attribute>\n" +
                   "      <cas:attribute>\n" +
                   "        <cas:name>nif</cas:name>\n" +
                   "        <cas:value>46725607A</cas:value>\n" +
                   "      </cas:attribute>\n" +
                   "      <cas:attribute>\n" +
                   "        <cas:name>nomComplet</cas:name>\n" +
                   "        <cas:value>Gerard Llort Casanova</cas:value>\n" +
                   "      </cas:attribute>\n" +
                   "    </cas:attributes>\n" +
                   "  </cas:authenticationSuccess>\n" +
                   "</cas:serviceResponse>";
      
      try 
      {
         System.out.print(getUserDataFromValidation(str));
      } 
      catch (Exception e) 
      {
         e.printStackTrace();
      }
   }
   
   */
}
