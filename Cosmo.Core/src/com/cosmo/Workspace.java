package com.cosmo;

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
import java.io.IOException;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

/**
 * Representa un espacio de trabajo de Cosmo.
 */
public class Workspace 
{
   private ServletContext context;
   private Template template;
   private WorkspaceProperties properties;
   private Rules rules;
   private UserSession session;
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
    * @throws IOException 
    * @throws SAXException 
    * @throws ParserConfigurationException 
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
    * 
    * @throws TemplateUnavailableException 
    */
   public Template getTemplate() throws TemplateUnavailableException 
   {
      if (this.template == null)
      {
         throw new TemplateUnavailableException();
      }
      else
      {
         return template;
      }
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
   public ServletContext getContext() 
   {
      return context;
   }
   
   /**
    * Devuelve la instancia de {@link WorkspaceProperties} que contiene la configuración del workspace.
    */
   public WorkspaceProperties getProperties()
   {
      return properties;
   }

   /**
    * Devuelve la URL de inicio del workspace.
    */
   public String getUrl() 
   {
      return url;
   }

   /**
    * Devuelve el nombre del workspace.
    */
   public String getName() 
   {
      return name;
   }

   /**
    * Devuelve el mail de contacto del workspace.
    */
   public String getMail() 
   {
      return mail;
   }
   
   /**
    * Devuelve la sesión de usuario.
    */
   public UserSession getSession() 
   {
      return session;
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
      this.session = new UserSession(this, login, password);
   }
   
   //==============================================
   // Private members
   //==============================================

   /**
    * Recarga el contexto.
    * 
    * @param context Contexto de la llamada al workspace.
    * 
    * @throws WorkspaceLoadException
    * @throws RulesLoadException 
    */
   private void reloadContext(ServletContext context, HttpServletRequest request) throws WorkspaceLoadException, RulesLoadException, TemplateUnavailableException, TemplateLoadException, MenuProviderException
   {
      this.context = context;
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
      
      this.url = "";
      this.name = "";
      this.mail = "";
   }
}
