package com.cosmo;

import com.cosmo.ui.templates.TemplateUnavailableException;
import com.cosmo.ui.templates.RulesLoadException;
import com.cosmo.ui.templates.TemplateLoadException;
import com.cosmo.ui.widgets.providers.MenuProviderException;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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
    * @param session Una instancia de {@link HttpSession} que permite acceder a la cachÃ© de sesión.
    * @return Una instancia de {@link Workspace} para el usuario actual.
    * 
    * @throws WorkspaceLoadException 
    */
   public static Workspace getWorkspace(ServletContext context, HttpServletRequest request, HttpSession session) throws WorkspaceLoadException, RulesLoadException, TemplateUnavailableException, TemplateLoadException, MenuProviderException
   {
      Workspace ws;
      
      // Obtiene el workspace de la cache
      ws = (Workspace) session.getAttribute(Cosmo.KEY_CACHE_SESSION_WORKSPACE);

      // Si existe en la cache, devuelve el workspace
      if (ws != null)
      {
         return ws;
      }

      // Inicializa el workspace y lo guarda en la sesión
      ws = new Workspace(context, request);
      session.setAttribute(Cosmo.KEY_CACHE_SESSION_WORKSPACE, ws);
      
      return ws;
   }
}
