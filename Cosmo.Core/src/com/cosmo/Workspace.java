package com.cosmo;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.cosmo.security.User;
import com.cosmo.security.UserNotFoundException;
import com.cosmo.security.UserSession;
import com.cosmo.security.providers.AuthenticationException;
import com.cosmo.security.providers.AuthorizationException;
import com.cosmo.ui.templates.Rules;
import com.cosmo.ui.templates.RulesLoadException;
import com.cosmo.ui.templates.Template;
import com.cosmo.ui.templates.TemplateLoadException;
import com.cosmo.ui.templates.TemplateUnavailableException;
import com.cosmo.ui.widgets.providers.MenuProviderException;
import com.cosmo.util.StringUtils;

/**
 * Representa un espacio de trabajo de Cosmo.
 */
public class Workspace 
{
   private ServletContext context;
   private Template template;
   private WorkspaceProperties properties;
   private Rules rules;
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
    * Constructor de la clase.
    */
   public Workspace()
   {
      initialize();
   }
   
   /**
    * Constructor de la clase.
    * 
    * @param context Contexto de la llamada al workspace.
    * @param request
    * @param response
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
    * Devuelve la plantilla de presentación aplicada.
    */
   public Template getTemplate() 
   {
      return template;
   }

   /**
    * Establece la plantilla de presentación aplicada.
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
    * Devuelve la instancia de {@link WorkspaceProperties} que contiene la configuración del workspace.
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
    * Devuelve el juego de carácteres usado en el workspace.
    */
   public String getCharset()
   {
      String charSet = this.properties.getString(Cosmo.PROPERTY_WORKSPACE_UI_CHARSET);
      return (StringUtils.isNullOrEmptyTrim(charSet) ? Cosmo.CHARSET_UTF_8 : charSet);
   }
   
   /**
    * Indica si existe una sesión de usuario Cosmo válida iniciada.
    * @return {@code true} si existe una sesión de usuario válida o {@code false} en cualquier otro caso.
    */
   public boolean isValidUserSession()
   {
      if (this.usrSession == null)
      {
         return false;
      }
      
      return true;
   }
   
   /**
    * Devuelve una instancia de {@link UserSession} que corresponde a la sesión de usuario iniciada o
    * {@code null} si no hay ninguna sesión de usuario iniciada.
    */
   public UserSession getUserSession() 
   {
      return this.usrSession;
   }   
   
   /**
    * Devuelve la URL pedida.
    * 
    * @return Una cadena con la URL correspondiente a la llamada.
    */
   public String getRequestedUrl()
   {
      return this.requestedUrl;
   }
   
   //==============================================
   // Methods
   //==============================================

   /**
    * Crea una sesión de usuario.
    * 
    * @param user Una instancia de {@link User} que contiene el login y password para autenticar al usuario.
    * 
    * @throws UserNotFoundException
    * @throws AuthenticationException 
    * @throws AuthorizationException 
    */
   public void createSession(User user) throws UserNotFoundException, AuthenticationException, AuthorizationException
   {
      this.usrSession = new UserSession(this, user);
   }

   /**
    * Crea una sesión de usuario.
    * 
    * @param login Login del usuario a autenticar.
    * @param password Contraseña del usuario a autenticar.
    * 
    * @throws UserNotFoundException
    * @throws AuthenticationException 
    * @throws AuthorizationException 
    */
   public void createSession(String login, String password) throws UserNotFoundException, AuthenticationException, AuthorizationException
   {
      this.usrSession = new UserSession(this, login, password);
   }


   //==============================================
   // Private members
   //==============================================

   /**
    * Recarga el contexto.
    * 
    * @param context Contexto del servidor.
    * @param request Contexto de la llamada.
    * @param response Contexto de la respuesta.
    * 
    * @throws WorkspaceLoadException
    * @throws RulesLoadException
    * @throws TemplateUnavailableException
    * @throws TemplateLoadException
    * @throws MenuProviderException
    */
   private void reloadContext(ServletContext context, HttpServletRequest request, HttpServletResponse response) throws WorkspaceLoadException, RulesLoadException, TemplateUnavailableException, TemplateLoadException, MenuProviderException
   {
      this.context = context;
      this.srvRequest = request;
      this.srvResponse = response;
      this.properties = new WorkspaceProperties(context);
      this.rules = new Rules(context);
      this.template = this.rules.checkRules(request.getHeader("User-Agent"));

      this.requestedUrl = getRequestedUrl(request);
      
      this.url = this.properties.getString(Cosmo.PROPERTY_WORKSPACE_URL);
      this.name = this.properties.getString(Cosmo.PROPERTY_WORKSPACE_TITLE);
      this.mail = this.properties.getString(Cosmo.PROPERTY_WORKSPACE_MAIL);
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
