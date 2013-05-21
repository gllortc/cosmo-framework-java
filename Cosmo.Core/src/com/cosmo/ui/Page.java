package com.cosmo.ui;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.cosmo.Cosmo;
import com.cosmo.Workspace;
import com.cosmo.WorkspaceLoadException;
import com.cosmo.WorkspaceFactory;
import com.cosmo.security.NotAuthorizedException;
import com.cosmo.security.UserNotFoundException;
import com.cosmo.security.UserSession;
import com.cosmo.security.auth.AuthenticationException;
import com.cosmo.security.auth.AuthorizationException;
import com.cosmo.ui.controls.Control;
import com.cosmo.ui.controls.FormControl;
import com.cosmo.ui.render.LoadPageRenderException;
import com.cosmo.ui.render.PageRenderException;
import com.cosmo.ui.render.PageRenderProvider;
import com.cosmo.ui.templates.RulesLoadException;
import com.cosmo.ui.templates.TemplateLoadException;
import com.cosmo.ui.templates.TemplateUnavailableException;
import com.cosmo.ui.widgets.providers.MenuProviderException;

/**
 * Implementa una página de Cosmo.
 * 
 * @author Gerard Llort
 */
public abstract class Page extends HttpServlet implements PageInterface 
{
   /** Serial Version UID */
   private static final long serialVersionUID = -2313025410371254322L;

   private boolean init = false;
   private String charset;
   private String title;
   private PageLayout layout;
   private Workspace workspace;
   private ArrayList<Control> leftContents;
   private ArrayList<Control> centerContents;
   private ArrayList<Control> rightContents;
   private StringBuilder xhtml;
   private PageRenderProvider renderProvider;
   private HttpSession session;

   /**
    * Enumera las distintas regiones dónde se pueden agregar controles en la página.
    */
   public enum ContentColumns
   {
      LEFT,
      MAIN,
      RIGHT
   }
   
   /**
    * Enumera los distintos formatos de página.
    */
   public enum PageLayout
   {
      HomePage,
      OneColumn,
      TwoColumnsLeft,
      TwoColumnsRight,
      ThreeColumns
   }
   
   //==============================================
   // Constructors
   //==============================================
   
   /**
    * Constructor de la clase.
    */
   public Page()
   {
      super();
      initPage();
   }
   
   /**
    * Constructor de la clase.
    * 
    * @param title Título de la página.
    */
   public Page(String title)
   {
      // Inicialización de la instancia
      super();
      initPage();
      
      // Inicializaciones por parámetro
      this.title = title;
   }
   
   /**
    * Constructor de la clase.
    * 
    * @param title Título de la página.
    * @param layout Formato de la página.
    */
   public Page(String title, PageLayout layout)
   {
      super();
      initPage();
      
      this.title = title;
      this.layout = layout;
   }
   
   
   //==============================================
   // Properties
   //==============================================

   /**
    * Devuelve el título de la página.
    */
   public String getTitle() 
   {
      return title;
   }

   /**
    * Establece el título de la página.
    */
   public void setTitle(String title) 
   {
      this.title = title;
   }

   /**
    * Devuelve el <em>layout</em> de la página.
    */
   public PageLayout getLayout() 
   {
      return layout;
   }

   /**
    * Establece el <em>layout</em> de la página.
    */
   public void setLayout(PageLayout layout) 
   {
      this.layout = layout;
   }

   /**
    * Devuelve el juego de carácteres de la página.
    */
   public String getCharset() 
   {
      return charset;
   }

   /**
    * Establece el juego de carácteres de la página.
    */
   public void setCharset(String charset) 
   {
      this.charset = charset;
   }

   /**
    * Devuelve la instancia de {@link Workspace} que representa el workspace actual.
    */
   public Workspace getWorkspace() 
   {
      return workspace;
   }
   
   /**
    * Devuelve la instancia de {@link UserSession} que representa la sessión autenticada actual.
    */
   public UserSession getUserSession()
   {
      return workspace.getUserSession();
   }
   
   /**
    * Devuelve un iterador sobre el contenido de la página.
    * 
    * @param column Un valor de {@link ContentColumns} que indica la columna para la que se desea obtener el contenido.
    */
   public Iterator<Control> getPageContent(ContentColumns column)
   {
      switch (column)
      {
         case LEFT:
            return this.leftContents.iterator();
         case RIGHT:
            return this.rightContents.iterator();
         default:
            return this.centerContents.iterator();
      }
   }
   
   //==============================================
   // Methods
   //==============================================
   
   /**
    * Método que es llamado al inicializar la página.
    */
   public abstract void initPageEvent(HttpServletRequest request, HttpServletResponse response);
   
   /**
    * Método que es llamado cuando la página recibe los datos de un formulario.
    */
   public abstract void formSendedEvent(HttpServletRequest request, HttpServletResponse response);
   
   /**
    * Método que es llamado al cargar la página.
    */
   public abstract void loadPageEvent(HttpServletRequest request, HttpServletResponse response);
   
   /**
    * Agrega un control a la página.
    * 
    * @param control Una instancia de {@link Control} que representa el control a añadir.
    * @param situation Posición en la que se debe agregar el control.
    */
   public void addContent(Control control, ContentColumns situation)
   {
      switch (situation)
      {
         case LEFT:  
            leftContents.add(control);
            break;
         case MAIN:  
            centerContents.add(control);  
            break;
         case RIGHT: 
            rightContents.add(control);   
            break;
      }
   }
   
   /**
    * Permite obtener un determinado control para poder modificar alguna de sus propiedades.
    * 
    * @param id Identificador del control.
    * @return Una instancia de {@link Control} que representa el control solicitado.
    */
   public Control getControl(String id)
   {
      for (Control control : this.centerContents)
      {
         if (control instanceof Control)
         {
            if (id.equals(((Control) control).getId()))
            {
               return control;
            }
         }
      }
      
      return null;
   }
   
   /**
    * Renderiza la página actual.
    * 
    * @throws PageRenderException 
    */
   @Override
   public void render()
   {
      try
      {
         // Si no hay renderizador asignado, lo carga
         if (this.renderProvider == null)
         {
            this.renderProvider = PageRenderProvider.getInstance(workspace);
         }
         
         // Invoca la renderización al proveedor
         xhtml = new StringBuilder(this.renderProvider.render(this));
      }
      catch (PageRenderException ex)
      {
         // Si se produce algún error durante el renderizado, se llama a toString() que deja xhtml con el código básico de la página
         toString();
      }
      catch (LoadPageRenderException ex)
      {
         // Si se produce algún error durante el renderizado, se llama a toString() que deja xhtml con el código básico de la página
         toString();
      }
      catch (TemplateUnavailableException ex)
      {
         // Si se produce algún error durante el renderizado, se llama a toString() que deja xhtml con el código básico de la página
         toString();
      }
   }
   
   /**
    * Indica si la llamada es un envio de un formulario.
    * 
    * @param request Servlet request.
    * @return {@code true} si el contexto actual corresponde a un envío de formulario o {@code false} en cualquier otro caso.
    */
   public boolean isPostback(HttpServletRequest request)
   {
      String frmAction = request.getParameter(Cosmo.KEY_UI_FORM_ACTION);
      
      if (frmAction == null)
      {
         return false;
      }
      else if (frmAction.equals(Cosmo.TOKEN_UI_FORM_POSTBACK))
      {
         return true;
      }
      else
      {
         return false;
      }
   }
   
   /**
    * Atiende la petición de la página por GET y POST.
    * 
    * @param request Servlet request.
    * @param response Servlet response.
    * 
    * @throws ServletException 
    * @throws IOException 
	 */
   protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
   {
      try 
      {
         // Assegura que exista la sessión associada a la petición actual
         this.session = request.getSession(true);
         
         createPage(request, response);
      } 
      catch (Exception ex) 
      {
         // Mostrar error sin formato o con formato dependiendo de la excepción
         throw new ServletException(ex.getMessage(), ex);
      }
   }
   
   /**
    * Atiende la petición de la página por GET.
    * 
    * @throws ServletException
    * @throws IOException
	 */
   @Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
      processRequest(request, response);
	}

	/**
    * Atiende la petición de la página por POST.
    * 
    * @throws ServletException
    * @throws IOException
	 */
   @Override
   protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
   {
      processRequest(request, response);
   }
   
   /**
    * Convierte la página en una cadena XHTML (sin aplicar la plantilla).
    */
   @Override
   public String toString()
   {
      xhtml = new StringBuilder();

      // Cabecera
      xhtml.append("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 Transitional//EN\">").append("\n");
      xhtml.append("<html>").append("\n");
      xhtml.append("<head>").append("\n");
      xhtml.append("  <title>").append(this.title).append("</title>").append("\n");
      xhtml.append("  <meta http-equiv=\"Content-Type\" content=\"text/html; charset=").append(this.charset).append("\" />").append("\n");
      xhtml.append("  <meta name=\"generator\" content=\"" + Cosmo.COSMO_NAME + "\" />").append("\n");
      xhtml.append("</head>").append("\n");
      xhtml.append("<body>").append("\n");
      
      try   
      {
         // Contenido (controles)
         for (Control control : this.centerContents)
         {
            xhtml.append(control.render()).append("\n");
         }
      }
      catch (Exception ex)
      {
         xhtml.append("<p><strong>FATAL ERROR</strong>: ").append(ex.getMessage()).append("</p>");
      }
      
      // Pie
      xhtml.append("</body>").append("\n");
      xhtml.append("</html>");
      
      return xhtml.toString();
   }
   
   //==============================================
   // Private members
   //==============================================
   
   /**
    * Inicializa la instancia.
    * TODO: Valores por defecto de la configuración.
    */
   private void initPage()
   {
      this.leftContents = new ArrayList<Control>();
      this.centerContents = new ArrayList<Control>();
      this.rightContents = new ArrayList<Control>();

      this.xhtml = new StringBuilder();
      this.workspace = null;
      this.renderProvider = null;
      this.title = "";
      this.charset = Cosmo.CHARSET_ISO_8859_1;
      this.layout = PageLayout.OneColumn;
   }
   
   /**
    * Método que se llama en la respuesta de un formulario enviado y que pone los datos dentro del formulario.
    */
   private void formRefreshData(HttpServletRequest request)
   {
      for (Control control : this.centerContents)
      {
         if (control instanceof FormControl)
         {
            ((FormControl) control).setFormValues(request);
         }
      }
   }
   
   /**
    * Comprueba si la página dispone de seguridad y si existe sesión autenticada.<br />
    * En caso de requerir autenticación y no existir autenticación, redirige a la página de login.
    * 
    * @param response Una instancia de {@link HttpServletResponse} que corresponde a la respuesta actual del contexto.
    * 
    * @throws IOException 
    */
   /*private boolean checkSecurity(HttpServletRequest request, HttpServletResponse response) throws IOException
   {
      String toUrl;
      
      if (!this.isSessionRequired())
      {
         return false;
      }
      
      if (!getWorkspace().isValidUserSession())
      {
         try
         {
            AuthenticationProvider auth = AuthenticationProvider.getInstance(workspace);
            
            if (auth.isLoginGatewayRequired()) // && auth.isLoginGatewayValidated(request))
            {
               if (auth.isLoginGatewayResponse(request))
               {
                  User user = auth.getLoginGatewayUser(request);
                  workspace.createSession(user);

                  return true;
               }
               else
               {
                  toUrl = auth.getLoginGatewayUrl();
               }
            }
            / *else if (auth.isLoginGatewayRequired())
            {
               toUrl = auth.getLoginGatewayUrl();
            }* /
            else
            {
               URL url = new URL(getWorkspace().getProperties().getLoginPage());
               
               // Determina si existe una dirección de origen
               try
               {
                  // HttpServletRequest request = getWorkspace().getServerRequest();
                  String urlSource = request.getRequestURL().toString();
                  url.addParameter(Cosmo.URL_PARAM_TOURL, urlSource);
               }
               catch (Exception ex)
               {
                  // No lo tiene en cuenta
               }
   
               // Redirecciona la página al servlet de LOGIN.
               String charSet = getWorkspace().getProperties().getString(Cosmo.PROPERTY_WORKSPACE_UI_CHARSET);
               charSet = (StringUtils.isNullOrEmptyTrim(charSet) ? Cosmo.CHARSET_UTF_8 : charSet);
               toUrl = url.toString(charSet);
            }
            
            response.sendRedirect(toUrl);
            return true;
         } 
         catch (Exception e) 
         {
            // TODO: Aquí se debe generar una excepción ya que de lo contrario se puede acceder al recurso
            
            // throw new Exception(e.getMessage());
         }
      }
      
      return false;
   } */
   
   /**
    * Crea la página.<br />
    * El guión de llamadas a eventos es el siguiente:<br /><ul>
    * <li>- {@code initPageEvent()}: Sólo si es la primera vez que se accede a la página.</li>
    * <li>- {@code formSendedEvent()}: Sólo si se reciben datos de un formulario Cosmo.</li>
    * <li>- {@code loadPageEvent()}</li>
    * </ul>
    * 
    * @param request Una instancia de {@link HttpServletRequest} que corresponde a la llamada actual del contexto.
    * @param response Una instancia de {@link HttpServletResponse} que corresponde a la respuesta actual del contexto.
    * 
    * @throws ServletException
    * @throws IOException
    * @throws WorkspaceLoadException
    * @throws RulesLoadException
    * @throws TemplateUnavailableException
    * @throws TemplateLoadException
    * @throws MenuProviderException
    * @throws AuthorizationException 
    * @throws UserNotFoundException 
    * @throws AuthenticationException 
    * @throws NotAuthorizedException 
    */
   private void createPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, WorkspaceLoadException, RulesLoadException, TemplateUnavailableException, TemplateLoadException, MenuProviderException, NotAuthorizedException, AuthenticationException, UserNotFoundException, AuthorizationException
   {
      long startTime = System.currentTimeMillis();
      
      // Obtiene el workspace
      this.workspace = WorkspaceFactory.getInstance(getServletContext(), request, response);

      // Comprueba si el usuario puede ver la página
      PageSecurity psec = new PageSecurity();
      psec.checkPageSecurity(this, workspace, request, response);
      
      /*if (checkSecurity(request, response))
      {
         return;  // Evita la excepción java.lang.IllegalStateException
      }*/
      
      // Lanza el evento initPageEvent sólo si es la primera vez que se accede a la página
      if (!init)
      {
         initPageEvent(request, response);
         init = true;
      }
      
      // Lanza el evento formSendedEvent
      if (isPostback(request))
      {
         formRefreshData(request);
         formSendedEvent(request, response);
      }

      // Lanza el evento loadPageEvent
      loadPageEvent(request, response);
      
      // Renderiza la página
      render();
      
      // Manda el resultado de la renderización al cliente
      response.setContentType("text/html");
      PrintWriter out = response.getWriter();
      out.println(xhtml.toString());
      out.print("<!-- Page generated in " + (System.currentTimeMillis() - startTime) + " mSec using " + Cosmo.COSMO_NAME + " -->\n");
   }
   
   /**
    * Indica si la página requiere sesión de usuario para ser accedida. 
    */
   /*private boolean isSessionRequired()
   {
      return this.getClass().isAnnotationPresent(SessionRequired.class);
   }*/
   
   
}
