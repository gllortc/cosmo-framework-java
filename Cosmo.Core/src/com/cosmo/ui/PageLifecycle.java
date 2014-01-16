package com.cosmo.ui;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cosmo.Cosmo;
import com.cosmo.Workspace;
import com.cosmo.WorkspaceFactory;
import com.cosmo.WorkspaceLoadException;
import com.cosmo.security.NotAuthorizedException;
import com.cosmo.security.PageSecurity;
import com.cosmo.security.UserNotFoundException;
import com.cosmo.security.auth.AuthenticationException;
import com.cosmo.security.auth.AuthorizationException;
import com.cosmo.ui.controls.Control;
import com.cosmo.ui.controls.FormControl;
import com.cosmo.ui.templates.RulesLoadException;
import com.cosmo.ui.templates.TemplateLoadException;
import com.cosmo.ui.templates.TemplateUnavailableException;
import com.cosmo.ui.widgets.providers.MenuProviderException;

/**
 * Implementa el ciclo de vida de una página Cosmo.
 * 
 * @author Gerard Llort
 */
public class PageLifecycle 
{

   //==============================================
   // Static members
   //==============================================

   /**
    * Crea la página.<br />
    * El guión de llamadas a eventos es el siguiente:<br /><ul>
    * <li>- {@code initPageEvent()}: Sólo si es la primera vez que se accede a la página.</li>
    * <li>- {@code formSendedEvent()}: Sólo si se reciben datos de un formulario Cosmo.</li>
    * <li>- {@code loadPageEvent()}</li>
    * </ul>
    * 
    * @param page Una instancia de {@link Page} que representa la página para la que se desea ejecutar el ciclo de vida.
    * @param request Una instancia de {@link HttpServletRequest} que corresponde a la llamada actual del contexto.
    * @param response Una instancia de {@link HttpServletResponse} que corresponde a la respuesta actual del contexto.
    * 
    * @return Una instancia de {@link PageContext} que representa el modelo de la página.
    * 
    * @throws ServletException
    * @throws IOException
    * @throws WorkspaceLoadException
    * @throws RulesLoadException
    * @throws TemplateUnavailableException
    * @throws TemplateLoadException
    * @throws MenuProviderException
    * @throws NotAuthorizedException
    * @throws AuthenticationException
    * @throws UserNotFoundException
    * @throws AuthorizationException
    */
   public static PageContext execute(Page page, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, WorkspaceLoadException, RulesLoadException, TemplateUnavailableException, TemplateLoadException, MenuProviderException, NotAuthorizedException, AuthenticationException, UserNotFoundException, AuthorizationException
   {
      PageContext pc = null;
      Workspace workspace = null;

      // Obtiene el workspace
      workspace = WorkspaceFactory.getInstance(page.getServletContext(), request, response);

      try
      {
         // Comprueba si el usuario puede ver la página
         PageSecurity psec = new PageSecurity();
         psec.checkPageSecurity(page, workspace, request, response);

         // Controla el evento initPageEvent:
         // Si no es cacheable o es cacheable y nunca se ha generado, se lanza el evento
         // Si es cacheable y ya está generada, usa la versión cacheada
         if (page.isCacheable())
         {
            pc = getCachedPage(page, request);

            if (pc == null)
            {
               pc = new PageContext();            
               pc = page.initPageEvent(pc, request, response);
               setCachedPage(page, request, pc);
            }
         }
         else
         {
            pc = new PageContext();            
            pc = page.initPageEvent(pc, request, response);
         }

         // Lanza el evento formSendedEvent
         if (isPostback(request))
         {
            formRefreshData(pc, request);
            pc = page.formSendedEvent(pc, request, response);
         }

         // Lanza el evento loadPageEvent
         pc = page.loadPageEvent(pc, request, response);
      }
      catch (Exception ex)
      {
         pc = page.pageException(pc, ex);
      }

      return pc;
   }


   //==============================================
   // Private members
   //==============================================

   /**
    * Indica si la llamada es un envio de un formulario.
    * 
    * @param request Servlet request.
    * 
    * @return {@code true} si el contexto actual corresponde a un envío de formulario o {@code false} en cualquier otro caso.
    */
   private static boolean isPostback(HttpServletRequest request)
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
    * Método que se llama en la respuesta de un formulario enviado y que pone los datos dentro del formulario.
    * 
    * @param pc Una instancia de {@link PageContext} que contiene el contexto de la página actual.
    * @param request Una instancia de {@link HttpServletRequest} que contiene el contexto de la petición actual.
    */
   private static void formRefreshData(PageContext pc, HttpServletRequest request)
   {
      for (Control control : pc.getCenterContents())
      {
         if (control instanceof FormControl)
         {
            ((FormControl) control).setFormValues(request);
         }
      }
   }

   /**
    * Recupera una página de la cache.
    * 
    * @param page Una instancia de {@link Page} que representa la página a obtener de la caché.
    * @param request Una instancia de {@link HttpServletRequest} que contiene el contexto de la petición actual.
    */
   private static PageContext getCachedPage(Page page, HttpServletRequest request)
   {
      Object obj = request.getSession().getAttribute(page.getUuid());

      if (obj instanceof PageContext)
      {
         return (PageContext) obj;
      }

      return null;   
   }

   /**
    * Pone una página en memoria caché.
    * 
    * @param page Una instancia de {@link Page} que representa la página a obtener de la caché.
    * @param request Una instancia de {@link HttpServletRequest} que contiene el contexto de la petición actual.
    * @param pc Una instancia de {@link PageContext} que contiene el contexto de la página actual.
    */
   private static void setCachedPage(Page page, HttpServletRequest request, PageContext pageContext)
   {
      request.getSession().setAttribute(page.getUuid(), pageContext);
   }
}
