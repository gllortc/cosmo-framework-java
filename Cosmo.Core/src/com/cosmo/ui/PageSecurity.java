package com.cosmo.ui;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cosmo.Cosmo;
import com.cosmo.Workspace;
import com.cosmo.security.NotAuthorizedException;
import com.cosmo.security.annotations.ActivitiesAllowed;
import com.cosmo.security.annotations.RolesAllowed;
import com.cosmo.security.annotations.SessionRequired;
import com.cosmo.security.providers.AuthenticationProvider;
import com.cosmo.util.StringUtils;
import com.cosmo.util.URL;

/**
 * Implementa los mecanismos de seguridad de las páginas de Cosmo.
 * 
 * @author Gerard Llort
  */
public class PageSecurity 
{
   /**
    * Chequea la seguridad de una página y activa los mecanismos necesarios.
    * 
    * @param page Una instancia de {@link Page} que representa la página a comprobar.
    * @param workspace Una instancia de {@link Workspace} que representa el workspace actual. 
    * 
    * @throws IOException 
    * @throws NotAuthorizedException 
    */
   public void checkPageSecurity(Page page, Workspace workspace, HttpServletRequest request, HttpServletResponse response) throws IOException, NotAuthorizedException
   {
      //----------------------
      // Autenticación
      //----------------------
      
      // Si no requiere sesión de usuario termina la comprobación si hacer nada
      if (!isSessionRequired(page))
      {
         return;
      }
      
      // Comprueba si existe autenticación (obligatorio)
      if (!workspace.isValidUserSession())
      {
         sendLoginRedirect(workspace, request, response);
         return;
      }
      
      
      
      //----------------------
      // Anotaciones
      //----------------------
      
      // NOTA: No se comprueba la anotación SessionRequired debido a que ya va implícita 
      // a la llamada al método isSessionRequired(page)-
      
      // Comprueba si existe la anotación RolesAllowed y efectúa las comporbaciones de seguridad
      if (isRoleAllowed(page))
      {
         if (!workspace.getUserSession().isInRole(getRolesAllowed(page)))
         {
            throw new NotAuthorizedException("El usuario " + workspace.getUserSession().getCurrentUser().getLogin() + 
                                             " no puede ejecutar la acción solicitada debido a que no tiene ningún rol con permiso.");
         }
      }
      
      // Comprueba si existe la anotación ActivitiesAllowed y efectúa las comporbaciones de seguridad
      if (isActivityAllowed(page))
      {
         if (!workspace.getUserSession().isActivityGranted(getActivitiesAllowed(page)))
         {
            throw new NotAuthorizedException("El usuario " + workspace.getUserSession().getCurrentUser().getLogin() + 
                                             " no puede ejecutar la acción solicitada debido a que no tiene permisos sobre la(s) actividad(es) solicitada(s).");
         }
      }
   }

   /**
    * Envia una redirección hacia el mecanismo de authenticación (login).
    * 
    * @param workspace Una instancia de {@link Workspace} que representa el workspace actual.
    * 
    * @throws IOException 
    */
   private void sendLoginRedirect(Workspace workspace, HttpServletRequest request, HttpServletResponse response) throws IOException
   {
      URL url = new URL(workspace.getProperties().getLoginPage());
      
      // Determina si existe una dirección de origen
      try
      {
         
         AuthenticationProvider auth = AuthenticationProvider.getInstance(workspace);
         
         if (auth.isLoginGatewayRequired() && auth.isLoginGatewayValidated(request))
         {
            workspace.createSession("gllort");
            
            return;
         }
         else if (auth.isLoginGatewayRequired())
         {
            url = new URL(auth.getLoginGatewayUrl());
         }
         else
         {
            url = new URL(workspace.getProperties().getLoginPage());
            
            String urlSource = request.getRequestURL().toString();
            url.addParameter(Cosmo.URL_PARAM_TOURL, urlSource);
         }
         
         // HttpServletRequest request = getWorkspace().getServerRequest();
         String urlSource = request.getRequestURL().toString();
         url.addParameter(Cosmo.URL_PARAM_TOURL, urlSource);
      }
      catch (Exception ex)
      {
         // No lo tiene en cuenta
      }

      // Redirecciona la página al servlet de LOGIN.
      response.sendRedirect(url.toString(workspace.getCharset()));
   }
   
   /**
    * Indica si la página requiere sesión de usuario para ser accedida.
    * 
    *  @param page Una instancia de {@link Page} que representa la página a comprobar.
    *  
    *  @return {@code true} si la página requiere autenticación para ser accedida o {@code false} en cualquier otro caso.
    */
   private boolean isSessionRequired(Page page)
   {
      boolean sessionRequired = false;
      
      sessionRequired = sessionRequired || (page.getClass().isAnnotationPresent(SessionRequired.class));
      sessionRequired = sessionRequired || (page.getClass().isAnnotationPresent(RolesAllowed.class));
      sessionRequired = sessionRequired || (page.getClass().isAnnotationPresent(ActivitiesAllowed.class));
      
      return sessionRequired;
   }
   
   /**
    * Indica si la página requiere uno de los roles especificados para ser accedida. 
    */
   private boolean isRoleAllowed(Page page)
   {
      return page.getClass().isAnnotationPresent(RolesAllowed.class);
   }
   
   /**
    * Obtiene la lista de roles admitidos para ejecutar determinada página.<br />
    * El usuario debe disponer de una de ellas para ejecutar la página.
    * 
    * @return Un array de identificadores de rol.
    */
   private ArrayList<String> getRolesAllowed(Page page)
   {
      RolesAllowed ra;
      ArrayList<String> list;
      
      list = new ArrayList<String>();
      ra = page.getClass().getAnnotation(RolesAllowed.class);
      
      for (String str : ra.value())
      {
         list.add(str);
      }
      
      return list;
   }
   
   /**
    * Indica si la página requiere permisos sobre una o varias actividades para ser accedida. 
    */
   private boolean isActivityAllowed(Page page)
   {
      return page.getClass().isAnnotationPresent(ActivitiesAllowed.class);
   }
   
   /**
    * Obtiene una lista de las actividades requeridas para ejecutar determinada página.<br />
    * El usuario debe disponer de permiso sobre una de ellas para ejecutar la página.
    * 
    * @return Un array de identificadores de actividad.
    */
   private ArrayList<String> getActivitiesAllowed(Page page)
   {
      ActivitiesAllowed aa;
      ArrayList<String> list;
      
      list = new ArrayList<String>();
      aa = page.getClass().getAnnotation(ActivitiesAllowed.class);
      
      for (String str : aa.value())
      {
         list.add(str);
      }
      
      return list;
   }
}
