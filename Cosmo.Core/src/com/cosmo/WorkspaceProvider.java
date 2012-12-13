package com.cosmo;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cosmo.ui.templates.RulesLoadException;
import com.cosmo.ui.templates.TemplateLoadException;
import com.cosmo.ui.templates.TemplateUnavailableException;
import com.cosmo.ui.widgets.providers.MenuProviderException;

/**
 * Implementa un proveedor para workspaces.
 * 
 * @author Gerard Llort
 */
public class WorkspaceProvider 
{
   /**
    * Obtiene una instancia de {@link Workspace}.
    * 
    * @param context Una instancia de {@link ServletContext} que permite acceder al contexto.
    * @param request Una instancia de {@link HttpServletRequest} que permite acceder al contexto de la llamada.
    * @param response Una instancia de {@link HttpServletResponse} que permite acceder al contexto de la respuesta.
    * @return Una instancia de {@link Workspace} para el usuario actual.
    * 
    * @throws WorkspaceLoadException 
    */
   public static Workspace getWorkspace(ServletContext context, HttpServletRequest request, HttpServletResponse response) throws WorkspaceLoadException, RulesLoadException, TemplateUnavailableException, TemplateLoadException, MenuProviderException
   {
      Workspace ws;
      
      // Obtiene el workspace de la cache
      ws = (Workspace) request.getSession().getAttribute(Cosmo.KEY_CACHE_SESSION_WORKSPACE);

      // Si existe en la cache, devuelve el workspace
      if (ws != null)
      {
         return ws;
      }

      // Inicializa el workspace y lo guarda en la sesión
      ws = new Workspace(context, request, response);
      request.getSession().setAttribute(Cosmo.KEY_CACHE_SESSION_WORKSPACE, ws);
      
      return ws;
   }
}
