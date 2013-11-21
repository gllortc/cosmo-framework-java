package com.cosmo.ui;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cosmo.Cosmo;
import com.cosmo.Workspace;
import com.cosmo.WorkspaceFactory;
import com.cosmo.security.UserSession;
import com.cosmo.ui.annotations.CacheScope;
import com.cosmo.ui.render.LoadPageRenderException;
import com.cosmo.ui.render.PageRenderer;
import com.cosmo.ui.render.PageRenderException;
import com.cosmo.ui.render.PageRendererFactory;
import com.cosmo.ui.templates.TemplateUnavailableException;

// Possibles modificacions:
//
// Per simplificar els mètodes abstractes:
// http://stackoverflow.com/questions/1068760/can-i-pass-parameters-by-reference-in-java

/**
 * Implementa una página de Cosmo.
 * 
 * @author Gerard Llort
 */
public abstract class Page extends HttpServlet
{
   /** Serial Version UID */
   private static final long serialVersionUID = -2313025410371254322L;

   private String uuid;
   private Workspace workspace;
   private PageRenderer renderProvider;


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


   //==============================================
   // Properties
   //==============================================

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
    * Devuelve un UUID único para la página.
    */
   public String getUuid()
   {
      return this.uuid;
   }


   //==============================================
   // Abstract members
   //==============================================

   /**
    * Método que es llamado al inicializar la página.
    * 
    * @param pc Una instancia de {@link PageContext} que representa el contenido de la página.
    * @param request Una instancia de {@link HttpServletRequest} que representa el contexto de la llamada.
    * @param response Una instancia de {@link HttpServletResponse} que representa el contexto de la respuesta.
    */
   public abstract PageContext initPageEvent(PageContext pc, HttpServletRequest request, HttpServletResponse response);

   /**
    * Método que es llamado cuando la página recibe los datos de un formulario.
    * 
    * @param pc Una instancia de {@link PageContext} que representa el contenido de la página.
    * @param request Una instancia de {@link HttpServletRequest} que representa el contexto de la llamada.
    * @param response Una instancia de {@link HttpServletResponse} que representa el contexto de la respuesta.
    */
   public abstract PageContext formSendedEvent(PageContext pc, HttpServletRequest request, HttpServletResponse response);

   /**
    * Método que es llamado al cargar la página.
    * 
    * @param pc Una instancia de {@link PageContext} que representa el contenido de la página.
    * @param request Una instancia de {@link HttpServletRequest} que representa el contexto de la llamada.
    * @param response Una instancia de {@link HttpServletResponse} que representa el contexto de la respuesta.
    */
   public abstract PageContext loadPageEvent(PageContext pc, HttpServletRequest request, HttpServletResponse response);

   /**
    * Método que es llamado si se produce un error interno durante la carga (antes del renderizado).
    * 
    * @param pc Una instancia de {@link PageContext} que representa el contenido de la página.
    * @param exception Una instancia de {@link Exception} que representa la excepción capturada durante la construcción de la página.
    */
   public abstract PageContext pageException(PageContext pc, Exception exception);


   //==============================================
   // Methods
   //==============================================

   /**
    * Renderiza la página actual.
    * 
    * @param pc Una instancia de {@link PageContext} que representa el contenido de la página.
    * 
    * @return Una instancia de {@link StringBuilder} que contiene el código XHTML de la página renderizada.
    * 
    * @throws PageRenderException 
    */
   public StringBuilder render(PageContext pc)
   {
      StringBuilder xhtml = null;

      try
      {
         // Si no hay renderizador asignado, lo carga
         if (this.renderProvider == null)
         {
            this.renderProvider = PageRendererFactory.getInstance(workspace);
         }

         // Invoca la renderización al proveedor
         xhtml = new StringBuilder(this.renderProvider.render(getWorkspace(), pc, this.uuid));
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

      return xhtml;
   }

   /**
    * Indica si la llamada es un envio de un formulario.
    * 
    * @param request Servlet request.
    * 
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
    * Atiende la petición de la página por GET y POST.
    * 
    * @param request Servlet request.
    * @param response Servlet response.
    * 
    * @throws ServletException 
    * @throws IOException 
    */
   protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException //, IOException 
   {
      long startTime;
      long totalTime;
      StringBuilder xhtml;
      PageContext pc;

      // Inica el cronometraje de generación de la página
      startTime = System.currentTimeMillis();

      try 
      {
         // Obtiene el workspace
         this.workspace = WorkspaceFactory.getInstance(getServletContext(), request, response);

         // Ejecuta el ciclo de vida de la página y obtiene el modelo de página
         pc = PageLifecycle.execute(this, request, response);

         // Si se ha cancelado la carga (usualmente por redirección de página) termina
         if (pc == null) 
         {
            return;
         }

         // Renderiza la página
         xhtml = render(pc);

         // Finaliza el cronometraje de generación de la página
         totalTime = System.currentTimeMillis() - startTime;
         xhtml.append("\n\n");
         xhtml.append("<!-- Page generated in " + totalTime + " mSec using " + Cosmo.COSMO_NAME + " -->\n");

         // Manda el resultado de la renderización al cliente
         response.setContentType("text/html");
         PrintWriter out = response.getWriter();
         out.println(xhtml.toString());
      } 
      catch (Exception ex) 
      {
         throw new ServletException(ex.getMessage(), ex);
      }
   }

   /**
    * Convierte la página en una cadena XHTML (sin aplicar la plantilla).
    */
   @Override
   public String toString()
   {
      /*StringBuilder xhtml = new StringBuilder();

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
      
      return xhtml.toString();*/
      
      return "";
   }


   //==============================================
   // Private members
   //==============================================

   /**
    * Inicializa la instancia.
    */
   private void initPage()
   {
      this.workspace = null;
      this.renderProvider = null;

      this.uuid = UUID.randomUUID().toString();
   }

   public boolean isCacheable()
   {
      return this.getClass().isAnnotationPresent(CacheScope.class);
   }
}
