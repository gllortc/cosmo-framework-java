package com.cosmo.web.pages;

import java.util.ArrayList;
import java.util.Map.Entry;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cosmo.security.annotations.SessionRequired;
import com.cosmo.security.auth.Authentication;
import com.cosmo.security.auth.AuthenticationFactory;
import com.cosmo.security.auth.Authorization;
import com.cosmo.security.auth.AuthorizationFactory;
import com.cosmo.ui.Page;
import com.cosmo.ui.controls.BreadcrumbsControl;
import com.cosmo.ui.controls.BreadcrumbsItem;
import com.cosmo.ui.controls.HeaderControl;
import com.cosmo.ui.controls.Icon;
import com.cosmo.ui.controls.XhtmlControl;

/**
 * Página de prova.
 * 
 * @author Gerard Llort
 */
@SessionRequired
@WebServlet( description = "Informació de seguretat", urlPatterns = { "/SecurityInfoPage" } )
public class SecurityInfoPage extends Page 
{
   /** Serial Version UID */
   private static final long serialVersionUID = -1863993648152701220L;
   
   @Override
   public void initPageEvent(HttpServletRequest request, HttpServletResponse response) 
   {
      this.setLayout(PageLayout.TwoColumnsLeft);
      this.setTitle("Cosmo - Informació de seguretat");
      
      BreadcrumbsControl navbar = new BreadcrumbsControl(getWorkspace());
      navbar.addItem(new BreadcrumbsItem("Inici", "HomePage", Icon.ICON_IMAGE_HOME));
      navbar.addItem(new BreadcrumbsItem("Informació de seguretat"));
      this.addContent(navbar, ContentColumns.MAIN);
      
      HeaderControl header = new HeaderControl(getWorkspace(), "head");
      header.setTitle("Informació de seguretat");
      this.addContent(header, ContentColumns.MAIN);

      XhtmlControl xInfo = new XhtmlControl(getWorkspace(), "info");
      xInfo.appendParagraph("La següent pàgina mostra les configuracions actuals dels agents de seguretat que s'han llegit a l'arxiu //cosmo.config.xml//, que conté la configuració de '''Cosmo Framnework'''.");
      this.addContent(xInfo, ContentColumns.MAIN);
      
      XhtmlControl xAuthent = new XhtmlControl(getWorkspace(), "xAuthent");
      this.addContent(xAuthent, ContentColumns.MAIN);
      
      XhtmlControl xAutho = new XhtmlControl(getWorkspace(), "xAutho");
      this.addContent(xAutho, ContentColumns.MAIN);
   }
   
   @Override
   public void loadPageEvent(HttpServletRequest request, HttpServletResponse response) 
   {
      ArrayList<String> lst;
      
      if (getWorkspace().isValidUserSession())
      {
         try
         {
            // Obtiene el agente de autenticación
            Authentication authent = AuthenticationFactory.getInstance(getWorkspace());
            lst = new ArrayList<String>();
            lst.add("Classe: //" + authent.getClass().getName() + "//");
            for (Entry<String, String> entry : authent.getParameters().entrySet())
            {
               lst.add(entry.getKey() + ": '''" + entry.getValue() + "'''");
            }
            
            XhtmlControl xAuthent = (XhtmlControl) this.getControl("xAuthent");
            xAuthent.clear();
            xAuthent.appendHeadder(Icon.render(Icon.ICON_IMAGE_COG) + " Agent d'autenticació", 4).
                     appendParagraph("La següent informació fa referència a l'agent d'autenticació configurat actualment:").
                     appendUnorderedList(lst, "alt");
            
            // Obtiene el agente de autenticación
            Authorization autho = AuthorizationFactory.getInstance(getWorkspace());
            lst = new ArrayList<String>();
            lst.add("Classe: //" + autho.getClass().getName() + "//");
            for (Entry<String, String> entry : autho.getParameters().entrySet())
            {
               lst.add(entry.getKey() + ": '''" + entry.getValue() + "'''");
            }
            
            XhtmlControl xAutho = (XhtmlControl) this.getControl("xAutho");
            xAutho.clear();
            xAutho.appendHeadder(Icon.render(Icon.ICON_IMAGE_COG) + " Agent d'autorització", 4).
                   appendParagraph("La següent informació fa referència a l'agent d'autorització configurat actualment:").
                   appendUnorderedList(lst, "alt");
         }
         catch (Exception ex)
         {
            showException(ex);
         }
      }
   }
   
   @Override
   public void formSendedEvent(HttpServletRequest request, HttpServletResponse response) 
   {
      throw new UnsupportedOperationException();
   }
   
}
