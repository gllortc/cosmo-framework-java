package com.cosmo;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.cosmo.security.User;
import com.cosmo.security.UserNotFoundException;
import com.cosmo.security.UserSession;
import com.cosmo.security.providers.UserProviderException;
import com.cosmo.ui.templates.Rules;
import com.cosmo.ui.templates.RulesLoadException;
import com.cosmo.ui.templates.Template;
import com.cosmo.ui.templates.TemplateLoadException;
import com.cosmo.ui.templates.TemplateUnavailableException;
import com.cosmo.ui.widgets.providers.MenuProviderException;

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
   private UserSession usrSession;
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
    * 
    * @throws WorkspaceLoadException 
    * @throws RulesLoadException
    * @throws TemplateUnavailableException
    * @throws TemplateLoadException
    * @throws MenuProviderException
    */
   public Workspace(ServletContext context, HttpServletRequest request) throws WorkspaceLoadException, RulesLoadException, TemplateUnavailableException, TemplateLoadException, MenuProviderException
   {
      initialize();

      reloadContext(context, request);
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
    * Devuelve una instancia de {@link UserSession} que corresponde a la sesión de usuario iniciada o
    * {@code null} si no hay ninguna sesión de usuario iniciada.
    */
   public UserSession getUserSession() 
   {
      return this.usrSession;
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
    * @throws UserProviderException 
    */
   public void createSession(User user) throws UserNotFoundException, UserProviderException
   {
      createSession(user.getLogin(), user.getPwd());
   }

   /**
    * Crea una sesión de usuario.
    * 
    * @param login Login del usuario a autenticar.
    * @param password Contraseña del usuario a autenticar.
    * 
    * @throws UserNotFoundException
    * @throws UserProviderException 
    */
   public void createSession(String login, String password) throws UserNotFoundException, UserProviderException
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
    * 
    * @throws WorkspaceLoadException
    * @throws RulesLoadException
    * @throws TemplateUnavailableException
    * @throws TemplateLoadException
    * @throws MenuProviderException
    */
   private void reloadContext(ServletContext context, HttpServletRequest request) throws WorkspaceLoadException, RulesLoadException, TemplateUnavailableException, TemplateLoadException, MenuProviderException
   {
      this.context = context;
      this.srvRequest = request;
      this.properties = new WorkspaceProperties(context);
      this.rules = new Rules(context);
      this.template = this.rules.checkRules(request.getHeader("User-Agent"));

      this.url = this.properties.getWorkspaceProperty(Cosmo.PROPERTY_WORKSPACE_URL);
      this.name = this.properties.getWorkspaceProperty(Cosmo.PROPERTY_WORKSPACE_TITLE);
      this.mail = this.properties.getWorkspaceProperty(Cosmo.PROPERTY_WORKSPACE_MAIL);
   }

   /**
    * Inicializa la instancia de la clase.
    */
   private void initialize()
   {
      this.template = null;
      this.context = null;
      this.properties = null;
      this.usrSession = null;
      this.srvRequest = null;
      
      this.url = "";
      this.name = "";
      this.mail = "";
   }
}
