package com.cosmo.web.pages;

import java.util.ArrayList;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cosmo.security.Activity;
import com.cosmo.security.Role;
import com.cosmo.security.annotations.AuthenticationRequired;
import com.cosmo.security.auth.AuthorizationException;
import com.cosmo.security.auth.AuthorizationFactory;
import com.cosmo.security.auth.impl.PostgreSqlAuthorizationImpl;
import com.cosmo.ui.Page;
import com.cosmo.ui.PageContext;
import com.cosmo.ui.PageContext.ContentColumns;
import com.cosmo.ui.PageContext.PageLayout;
import com.cosmo.ui.controls.BreadcrumbsControl;
import com.cosmo.ui.controls.BreadcrumbsItem;
import com.cosmo.ui.controls.DynamicMessageControl;
import com.cosmo.ui.controls.DynamicMessageControl.MessageTypes;
import com.cosmo.ui.controls.HeaderControl;
import com.cosmo.ui.controls.Icon;
import com.cosmo.ui.controls.XhtmlControl;

/**
 * Página ejecuta todos los tests de seguridad para comprobar el correcto funcionamiento del módulo de seguridad de Cosmo.
 * 
 * @author Gerard Llort
 */
@AuthenticationRequired
@WebServlet( description = "Tests de seguretat", urlPatterns = { "/SecurityTestsPage" } )
public class SecurityTestsPage extends Page 
{
   /** Serial Version UID */
   private static final long serialVersionUID = -1863993648152701220L;
   
   @Override
   public PageContext initPageEvent(PageContext pc, HttpServletRequest request, HttpServletResponse response) 
   {
      pc.setLayout(PageLayout.TwoColumnsLeft);
      pc.setTitle("Cosmo - Tests de seguretat");
      
      BreadcrumbsControl navbar = new BreadcrumbsControl(getWorkspace());
      navbar.addItem(new BreadcrumbsItem("Inici", "HomePage", Icon.ICON_IMAGE_HOME));
      navbar.addItem(new BreadcrumbsItem("Tests de seguretat"));
      pc.addContent(navbar, ContentColumns.MAIN);
      
      HeaderControl header = new HeaderControl(getWorkspace(), "head");
      header.setTitle("Tests de seguretat");
      pc.addContent(header, ContentColumns.MAIN);

      XhtmlControl xhtml = new XhtmlControl(getWorkspace(), "content");
      xhtml.appendParagraph("Aquesta pàgina conté els tests per verificar que el mòdul de seguretat de de '''Cosmo Framework''' està funcionant correctament.");
      pc.addContent(xhtml, ContentColumns.MAIN);
      
      // Informació de l'usuari autenticat
      
      XhtmlControl xUser = new XhtmlControl(getWorkspace(), "xUser");
      pc.addContent(xUser, ContentColumns.MAIN);
      
      // Información sobre roles
      
      XhtmlControl xhtmlRoles = new XhtmlControl(getWorkspace(), "content-roles");
      pc.addContent(xhtmlRoles, ContentColumns.MAIN);
      
      DynamicMessageControl msgRoles = new DynamicMessageControl(getWorkspace(), "msg-roles");
      msgRoles.setType(MessageTypes.Warning);
      pc.addContent(msgRoles, ContentColumns.MAIN);
      
      // Roles efectivos
      
      XhtmlControl xRolList = new XhtmlControl(getWorkspace(), "xRolList");
      pc.addContent(xRolList, ContentColumns.MAIN);
      
      DynamicMessageControl xRolMsg = new DynamicMessageControl(getWorkspace(), "xRolMsg");
      xRolMsg.setType(MessageTypes.Warning);
      pc.addContent(xRolMsg, ContentColumns.MAIN);
      
      // Información sobre actividades
      
      XhtmlControl xhtmlAct = new XhtmlControl(getWorkspace(), "content-act");
      pc.addContent(xhtmlAct, ContentColumns.MAIN);
      
      DynamicMessageControl msgAct = new DynamicMessageControl(getWorkspace(), "msg-act");
      msgAct.setType(MessageTypes.Warning);
      pc.addContent(msgAct, ContentColumns.MAIN);
      
      // Permisos efectivos
      
      XhtmlControl xhtmlActLst = new XhtmlControl(getWorkspace(), "act-list");
      pc.addContent(xhtmlActLst, ContentColumns.MAIN);
      
      DynamicMessageControl xActMsg = new DynamicMessageControl(getWorkspace(), "xActMsg");
      xActMsg.setType(MessageTypes.Warning);
      pc.addContent(xActMsg, ContentColumns.MAIN);
      
      return pc;
   }
   
   @Override
   public PageContext loadPageEvent(PageContext pc, HttpServletRequest request, HttpServletResponse response) 
   {
      ArrayList<String> list;
      
      if (getWorkspace().isValidUserSession())
      {
         // Muestra los detalles del usuario actual
         list = new ArrayList<String>();
         list.add("Login: " + getUserSession().getCurrentUser().getLogin());
         list.add("Mail: " + getUserSession().getCurrentUser().getMail());
         list.add("Nom: " + getUserSession().getCurrentUser().getName());

         XhtmlControl xUser = (XhtmlControl) pc.getControl("xUser");
         xUser.clear();
         xUser.appendHeadder(Icon.render(Icon.ICON_IMAGE_USER) + " usuari autenticat", 4).
               appendParagraph("A continuació es mostren les dades de l'usuari autenticat actualment.").
               appendUnorderedList(list, "alt");
         
         // Muestra los roles del usuario
         XhtmlControl xhtmlRoles = (XhtmlControl) pc.getControl("content-roles");
         xhtmlRoles.clear();
         xhtmlRoles.appendHeadder(Icon.render(Icon.ICON_IMAGE_GROUP) + " Rols d'usuari", 4).
                    appendParagraph("La següent llista conté els rols que té l'usuari:");
         if (!getUserSession().getRoles().isEmpty())
         {
            xhtmlRoles.appendUnorderedList(getUserSession().getRoles(), "alt");
         }
         else
         {
            DynamicMessageControl msgRoles = (DynamicMessageControl) pc.getControl("msg-roles");
            msgRoles.setMessage("L'usuari " + XhtmlControl.formatBold(getUserSession().getCurrentUser().getLogin()) + " no té cap rol associat.");
            msgRoles.setVisible(true);
         }
         
         // Muestra los roles efectivos del usuario
         try 
         {
            // Se instancia sólo para disponer de la lista completa de roles
            PostgreSqlAuthorizationImpl auth = (PostgreSqlAuthorizationImpl) AuthorizationFactory.getInstance(getWorkspace());
            
            // Se recorre la lista completa de actividades y se comprueba si el usuario dispone o no de permiso sobre cada una de ellas
            ArrayList<String> lst = new ArrayList<String>();
            for (Role role : auth.getRoles())
            {
               // Comprueba si el usuario dispone de permisos para la actividad
               lst.add((getUserSession().isInRole(role.getId()) ? Icon.render(Icon.ICON_IMAGE_OK_SIGN, Icon.ICON_SIZE_SMALL, Icon.ICON_COLOR_GREEN) : Icon.render(Icon.ICON_IMAGE_REMOVE_SIGN, Icon.ICON_SIZE_SMALL, Icon.ICON_COLOR_RED)) + " " +
                        role.getId() + " (" + role.getDescription() + ")");
            }
            
            XhtmlControl xRolList = (XhtmlControl) pc.getControl("xRolList");
            xRolList.clear();
            xRolList.appendHeadder(Icon.render(Icon.ICON_IMAGE_CHECK) + " Rols efectius d'usuari", 4).
                     appendParagraph("La següent llista mostra els rols efectius de l'usuari. S'agafa la llista complerta de rols i un per un es comprova per l'usuari a través la API de seguretat.").
                     appendUnorderedList(lst, "alt");
         } 
         catch (AuthorizationException ex) 
         {
            DynamicMessageControl xRolMsg = (DynamicMessageControl) pc.getControl("xRolMsg");
            xRolMsg.setMessage(ex.getMessage());
            xRolMsg.setVisible(true);
         }
         
         // Muestra los permisos del usuario
         XhtmlControl xhtmlAct = (XhtmlControl) pc.getControl("content-act");
         xhtmlAct.clear();
         xhtmlAct.appendHeadder(Icon.render(Icon.ICON_IMAGE_SHARE) + " Permisos de l'usuari", 4).
                  appendParagraph("La següent llista conté les activitats sobre les que l'usuari té permisos especificats:");
         if (!getUserSession().getPermissions().isEmpty())
         {
            xhtmlAct.appendUnorderedList(getUserSession().getPermissions(), "alt");
         }
         else
         {
            DynamicMessageControl msgAct = (DynamicMessageControl) pc.getControl("msg-act");
            msgAct.setVisible(true);
            
            if (!getUserSession().isSuperUser())
            {
               msgAct.setMessage("L'usuari " + XhtmlControl.formatBold(getUserSession().getCurrentUser().getLogin()) + " no té permís d'execució per cap activitat.");
            }
            else
            {
               msgAct.setMessage("L'usuari " + XhtmlControl.formatBold(getUserSession().getCurrentUser().getLogin()) + " té permís d'execució per totes les activitats (SuperUser).");
            }
         }
         
         // Muestra los permisos efectivos del usuario
         try 
         {
            // Se instancia sólo para disponer de la lista completa de permisos
            PostgreSqlAuthorizationImpl auth = (PostgreSqlAuthorizationImpl) AuthorizationFactory.getInstance(getWorkspace());
            
            // Se recorre la lista completa de actividades y se comprueba si el usuario dispone o no de permiso sobre cada una de ellas
            ArrayList<String> lst = new ArrayList<String>();
            for (Activity activity : auth.getActivities())
            {
               // Comprueba si el usuario dispone de permisos para la actividad
               lst.add((getUserSession().isActivityAllowed(activity.getId()) ? Icon.render(Icon.ICON_IMAGE_OK_SIGN, Icon.ICON_SIZE_SMALL, Icon.ICON_COLOR_GREEN) : Icon.render(Icon.ICON_IMAGE_REMOVE_SIGN, Icon.ICON_SIZE_SMALL, Icon.ICON_COLOR_RED)) + " " +
                        activity.getId() + " (" + activity.getDescription() + ")");
            }
            
            XhtmlControl xhtmlActLst = (XhtmlControl) pc.getControl("act-list");
            xhtmlActLst.clear();
            xhtmlActLst.appendHeadder(Icon.render(Icon.ICON_IMAGE_CHECK) + " Permisos efectius d'usuari", 4).
                        appendParagraph("La següent llista mostra els permisos efectius de l'usuari. S'agafa la llista complerta d'activitats i una per una es comprova per l'usuari a través la API de seguretat.").
                        appendUnorderedList(lst, "alt");
         } 
         catch (AuthorizationException ex) 
         {
            DynamicMessageControl xActMsg = (DynamicMessageControl) pc.getControl("xActMsg");
            xActMsg.setMessage(ex.getMessage());
            xActMsg.setVisible(true);
         }
      }
      
      return pc;
   }
   
   @Override
   public PageContext formSendedEvent(PageContext pc, HttpServletRequest request, HttpServletResponse response) 
   {
      return pc;
   }
   
   @Override
   public PageContext pageException(PageContext pc, Exception exception) 
   {
      pc.showException(getWorkspace(), exception);
      
      return pc;
   }
}
