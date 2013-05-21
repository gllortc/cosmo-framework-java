package com.cosmo.web.pages;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cosmo.security.annotations.SessionRequired;
import com.cosmo.ui.Page;
import com.cosmo.ui.controls.BreadcrumbsControl;
import com.cosmo.ui.controls.BreadcrumbsItem;
import com.cosmo.ui.controls.DynamicMessageControl;
import com.cosmo.ui.controls.DynamicMessageControl.MessageTypes;
import com.cosmo.ui.controls.HeaderControl;
import com.cosmo.ui.controls.Icon;
import com.cosmo.ui.controls.XhtmlControl;

/**
 * Página de prova.
 * 
 * @author Gerard Llort
 */
@SessionRequired
@WebServlet( description = "Exemple de pàgina privada que requereix sessió d'usuari per accedir-hi", urlPatterns = { "/SecretPage" } )
public class SecretPage extends Page 
{
   /** Serial Version UID */
   private static final long serialVersionUID = -1863993648152701220L;
   
   @Override
   public void initPageEvent(HttpServletRequest request, HttpServletResponse response) 
   {
      this.setLayout(PageLayout.TwoColumnsLeft);
      this.setTitle("Cosmo - Tests de seguretat");
      
      BreadcrumbsControl navbar = new BreadcrumbsControl(getWorkspace());
      navbar.addItem(new BreadcrumbsItem("Inici", "HomePage", Icon.ICON_IMAGE_HOME));
      navbar.addItem(new BreadcrumbsItem("Tests de seguretat"));
      this.addContent(navbar, ContentColumns.MAIN);
      
      HeaderControl header = new HeaderControl(getWorkspace(), "head");
      header.setTitle("Tests de seguretat");
      this.addContent(header, ContentColumns.MAIN);

      XhtmlControl xhtml = new XhtmlControl(getWorkspace(), "content");
      xhtml.appendParagraph("Aquesta pàgina serveix de test per provar totes les funcionalitats de seguretat de ").
            appendBold("Cosmo Framework").
            append("Depenent de l'usuari autenticat s'activen les funcionalitats per les quals té permís.");
      this.addContent(xhtml, ContentColumns.MAIN);
      
      DynamicMessageControl msg = new DynamicMessageControl(getWorkspace(), "msg");
      msg.setType(MessageTypes.Information);
      this.addContent(msg, ContentColumns.MAIN);
      
      // Información sobre roles
      
      XhtmlControl xhtmlRoles = new XhtmlControl(getWorkspace(), "content-roles");
      this.addContent(xhtmlRoles, ContentColumns.MAIN);
      
      DynamicMessageControl msgRoles = new DynamicMessageControl(getWorkspace(), "msg-roles");
      msgRoles.setType(MessageTypes.Warning);
      this.addContent(msgRoles, ContentColumns.MAIN);
      
      // Información sobre actividades
      
      XhtmlControl xhtmlAct = new XhtmlControl(getWorkspace(), "content-act");
      this.addContent(xhtmlAct, ContentColumns.MAIN);
      
      DynamicMessageControl msgAct = new DynamicMessageControl(getWorkspace(), "msg-act");
      msgAct.setType(MessageTypes.Warning);
      this.addContent(msgAct, ContentColumns.MAIN);
   }
   
   @Override
   public void loadPageEvent(HttpServletRequest request, HttpServletResponse response) 
   {
      if (getWorkspace().isValidUserSession())
      {
         DynamicMessageControl msg = (DynamicMessageControl) this.getControl("msg");
         msg.setMessage("Usuari autenticat: " + XhtmlControl.formatBold(getUserSession().getCurrentUser().getLogin()) + " (" + 
                                                XhtmlControl.formatBold(getUserSession().getCurrentUser().getName()) + ")");
         msg.setVisible(true);
         
         // Muestra los roles del usuario
         XhtmlControl xhtmlRoles = (XhtmlControl) this.getControl("content-roles");
         xhtmlRoles.clear();
         xhtmlRoles.appendHeadder("Rols d'usuari", 5).
                    appendParagraph("La següent llista conté els rols que té l'usuari:");
         if (!getUserSession().getRoles().isEmpty())
         {
            xhtmlRoles.appendUnorderedList(getUserSession().getRoles(), "alt");
         }
         else
         {
            DynamicMessageControl msgRoles = (DynamicMessageControl) this.getControl("msg-roles");
            msgRoles.setMessage("L'usuari " + XhtmlControl.formatBold(getUserSession().getCurrentUser().getLogin()) + " no té cap rol associat.");
            msgRoles.setVisible(true);
         }
         
         // Muestra los permisos del usuario
         XhtmlControl xhtmlAct = (XhtmlControl) this.getControl("content-act");
         xhtmlAct.clear();
         xhtmlAct.appendHeadder("Permisos de l'usuari", 5).
                  appendParagraph("La següent llista conté les activitats sobre les que l'usuari té permisos especificats:");
         if (!getUserSession().getPermissions().isEmpty())
         {
            xhtmlAct.appendUnorderedList(getUserSession().getPermissions(), "alt");
         }
         else
         {
            DynamicMessageControl msgAct = (DynamicMessageControl) this.getControl("msg-act");
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
      }
   }
   
   @Override
   public void formSendedEvent(HttpServletRequest request, HttpServletResponse response) 
   {
      throw new UnsupportedOperationException();
   }
   
}
