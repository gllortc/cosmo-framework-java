package com.cosmo;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.cosmo.logging.LogFactory;
import com.cosmo.security.User;
import com.cosmo.security.UserNotFoundException;
import com.cosmo.security.UserSession;
import com.cosmo.security.auth.AuthenticationException;
import com.cosmo.security.auth.AuthorizationException;
import com.cosmo.ui.templates.RulesLoadException;
import com.cosmo.ui.templates.Template;
import com.cosmo.ui.templates.TemplateLoadException;
import com.cosmo.ui.templates.TemplateUnavailableException;
import com.cosmo.ui.widgets.providers.MenuProviderException;
import com.cosmo.util.StringUtils;

/**
 * Representa un espacio de trabajo de Cosmo.<br /> 
 * Los espacios de trabajo de Cosmo ponen a disposici�n de la/s aplicaci�n/es los servicios del framework.
 * 
 * @author Gerard Llort
 */
public class Workspace 
{
   private ServletContext context;
   private Template template;
   private WorkspaceProperties properties;
   private HttpServletRequest srvRequest;
   private HttpServletResponse srvResponse;
   private UserSession usrSession;
   private String requestedUrl;
   private String url;
   private String name;
   private String mail;


   //==============================================
   // Constructors
   //==============================================

   /**
    * Constructor de la clase {@link Workspace}.
    */
   public Workspace()
   {
      initialize();
   }

   /**
    * Constructor de la clase {@link Workspace}.
    * 
    * @param context Una instancia de {@link ServletContext} que representa el contexto del servlet.
    * @param request Una instancia de {@link HttpServletRequest} que representa el contexto de la llamada.
    * @param response Una instancia de {@link HttpServletResponse} que representa el contexto de la respuesta.
    * 
    * @throws WorkspaceLoadException 
    * @throws RulesLoadException
    * @throws TemplateUnavailableException
    * @throws TemplateLoadException
    * @throws MenuProviderException
    */
   public Workspace(ServletContext context, HttpServletRequest request, HttpServletResponse response) throws WorkspaceLoadException, RulesLoadException, TemplateUnavailableException, TemplateLoadException, MenuProviderException
   {
      initialize();

      reloadContext(context, request, response);
   }


   //==============================================
   // Properties
   //==============================================

   /**
    * Devuelve la plantilla de presentaci�n aplicada.
    */
   public Template getTemplate() 
   {
      return template;
   }

   /**
    * Establece la plantilla de presentaci�n aplicada.
    */
   public void setTemplate(Template template) 
   {
      this.template = template;
   }

   /**
    * Devuelve la instancia de {@link ServletContext} usada para generar la instancia de Workspace actual.
    */
   public ServletContext getServerContext() 
   {
      return this.context;
   }

   /**
    * Devuelve la instancia de {@link HttpServletRequest} que corresponde a la llamada.
    */
   public HttpServletRequest getServerRequest()
   {
      return this.srvRequest;
   }

   /**
    * Establece la instancia de {@link HttpServletRequest} que corresponde a la llamada.
    */
   public void setServerRequest(HttpServletRequest request)
   {
      this.srvRequest = request;
   }

   /**
    * Devuelve la instancia de {@link HttpServletResponse} que corresponde a la respuesta.
    */
   public HttpServletResponse getServerResponse()
   {
      return this.srvResponse;
   }

   /**
    * Devuelve la instancia de {@link HttpServletRequest} que corresponde a la llamada.
    */
   public HttpSession getServerSession()
   {
      return this.srvRequest.getSession();
   }

   /**
    * Devuelve la instancia de {@link WorkspaceProperties} que contiene la configuraci�n del workspace.
    */
   public WorkspaceProperties getProperties()
   {
      return this.properties;
   }

   /**
    * Devuelve la URL de inicio del workspace.
    */
   public String getUrl() 
   {
      return this.url;
   }

   /**
    * Devuelve el nombre del workspace.
    */
   public String getName() 
   {
      return this.name;
   }

   /**
    * Devuelve el mail de contacto del workspace.
    */
   public String getMail() 
   {
      return this.mail;
   }

   /**
    * Devuelve el juego de car�cteres usado en el workspace.
    */
   public String getCharset()
   {
      String charSet = this.properties.getString(Cosmo.PROPERTY_WORKSPACE_UI_CHARSET);
      return (StringUtils.isNullOrEmptyTrim(charSet) ? Cosmo.CHARSET_UTF_8 : charSet);
   }

   /**
    * Devuelve la URL correspondiente a l apetici�n HTTP.
    * 
    * @return Una cadena con la URL correspondiente a la llamada.
    */
   public String getRequestedUrl()
   {
      return this.requestedUrl;
   }

   /**
    * Indica si existe una sesi�n de usuario Cosmo v�lida iniciada.
    * 
    * @return {@code true} si existe una sesi�n de usuario v�lida o {@code false} en cualquier otro caso.
    */
   public boolean isValidUserSession()
   {
      if (this.usrSession != null)
      {
         if (this.usrSession.isValidSession())
         {
            return true;
         }
         else
         {
            return false;
         }
      }
      else
      {
         return false;
      }
   }

   /**
    * Devuelve una instancia de {@link UserSession} que corresponde a la sesi�n de usuario iniciada o
    * {@code null} si no hay ninguna sesi�n de usuario iniciada.
    */
   public UserSession getUserSession() 
   {
      return this.usrSession;
   }


   //==============================================
   // Methods
   //==============================================

   /**
    * Crea una sesi�n de usuario.
    * 
    * @param user Una instancia de {@link User} que contiene el login y password para autenticar al usuario.
    * 
    * @throws UserNotFoundException
    * @throws AuthenticationException 
    * @throws AuthorizationException 
    */
   public void createUserSession(User user) throws UserNotFoundException, AuthenticationException, AuthorizationException
   {
      this.usrSession = new UserSession(this, user);
   }

   /**
    * Crea una sesi�n de usuario.
    * 
    * @param login Login del usuario a autenticar.
    * @param password Contrase�a del usuario a autenticar.
    * 
    * @throws UserNotFoundException
    * @throws AuthenticationException 
    * @throws AuthorizationException 
    */
   public void createUserSession(String login, String password) throws UserNotFoundException, AuthenticationException, AuthorizationException
   {
      this.usrSession = new UserSession(this, login, password);
   }

   /**
    * Cierra la sesi�n de usuario abierta.
    */
   public void closeUserSession()
   {
      this.usrSession.destroy();
      this.usrSession = null;
   }

   /**
    * Convierte la instancia en una cadena de texto.
    */
   @Override
   public String toString()
   {
      return "[Cosmo Workspace]\n" +
             " Name    : " + this.name + "\n" +
             " Services:\n" +
             "  - Logging        -> " + getProperties().getLogProperties().getLoggingAgentsCount() + " appender(s) listening\n" +
             "                      status: running\n" +
             "  - UI             -> template: " + this.template.getName() + "\n" +
             "                      status: running\n" +
             "  - Data           -> " + getProperties().getDataProperties().getDataAgentCount() + " datasource(s) loaded\n" +
             "                      " + getProperties().getDataProperties().getDataListCount() + " datalist(s) loaded\n" +
             "                      status: running\n" +
             "  - Communications -> " + getProperties().getCommProperties().getCommunicationsAgentCount() + " agent(s) loaded\n" +
             "                      status: running\n" +
             "  - Security       -> authentication module: " + getProperties().getSecurityProperties().getAuthenticationAgent().getId() + " / " + getProperties().getSecurityProperties().getAuthenticationAgent().getModuleClass() + "\n" +
             "                      authorization module : " + getProperties().getSecurityProperties().getAuthorizationAgent().getId() + " / " + getProperties().getSecurityProperties().getAuthorizationAgent().getModuleClass() + "\n" +
             "                      status: running\n";
   }


   //==============================================
   // Private members
   //==============================================

   /**
    * Recarga el contexto.
    * 
    * @param context Una instancia de {@link ServletContext} que representa el contexto del servlet.
    * @param request Una instancia de {@link HttpServletRequest} que representa el contexto de la llamada.
    * @param response Una instancia de {@link HttpServletResponse} que representa el contexto de la respuesta.
    * 
    * @throws WorkspaceLoadException
    * @throws RulesLoadException
    * @throws TemplateUnavailableException
    * @throws TemplateLoadException
    * @throws MenuProviderException
    */
   private void reloadContext(ServletContext context, HttpServletRequest request, HttpServletResponse response) throws WorkspaceLoadException, RulesLoadException, TemplateUnavailableException, TemplateLoadException, MenuProviderException
   {
      Logger log = LogFactory.getLogger("reloadContext");

      this.context = context;
      this.srvRequest = request;
      this.srvResponse = response;

      // Lee la configuraci�n del workspace
      this.properties = new WorkspaceProperties(context);

      LogFactory.initialize(this);
      log.info("Loading workspace context (session ID: " + request.getSession().getId() + ")...");
      log.info("Logging Services: running");

      this.template = this.properties.getUiProperties().checkRules(context, request.getHeader("User-Agent"));
      log.info("UI Services: running (template: " + this.template.getName() + ")");

      this.requestedUrl = getRequestedUrl(request);
      this.url = this.properties.getString(Cosmo.PROPERTY_WORKSPACE_URL);
      this.name = this.properties.getString(Cosmo.PROPERTY_WORKSPACE_TITLE);
      this.mail = this.properties.getString(Cosmo.PROPERTY_WORKSPACE_MAIL);

      log.info("Context reloaded for workspace " + this.name);
      
      log.info(this.toString());
   }

   /**
    * Inicializa la instancia de la clase.
    */
   private void initialize()
   {
      this.template = null;
      this.properties = null;
      this.usrSession = null;

      this.context = null;
      this.srvRequest = null;
      this.srvResponse = null;

      this.requestedUrl = "";
      this.url = "";
      this.name = "";
      this.mail = "";
   }

   /**
    * Obtiene la URL correspondiente a la petici�n.
    * 
    * @param request Una instancia de {@link HttpServletRequest} que representa el contexto de la llamada.
    * 
    * @return Una cadena que contiene la URL correspondiente a la petici�n.
    */
   private String getRequestedUrl(HttpServletRequest request) 
   {
      StringBuffer requestURL = request.getRequestURL();
      String queryString = request.getQueryString();

      if (queryString == null) 
      {
          return requestURL.toString();
      }
      else 
      {
          return requestURL.append('?').append(queryString).toString();
      }
  }
}
