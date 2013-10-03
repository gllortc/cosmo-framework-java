package com.cosmo.manager.pages;

import java.util.ArrayList;
import java.util.Map.Entry;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cosmo.security.annotations.AuthenticationRequired;
import com.cosmo.security.auth.Authentication;
import com.cosmo.security.auth.AuthenticationFactory;
import com.cosmo.security.auth.Authorization;
import com.cosmo.security.auth.AuthorizationFactory;
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
 * Página que muestra la información referente a la configuración de los agentes de seguridad.
 * 
 * @author Gerard Llort
 */
@AuthenticationRequired
@WebServlet( description = "Informació de seguretat", urlPatterns = { "/SecurityInfoPage" } )
public class SecurityInfoPage extends Page 
{
   /** Serial Version UID */
   private static final long serialVersionUID = -1863993648152701220L;
   
   @Override
   public PageContext initPageEvent(PageContext pc, HttpServletRequest request, HttpServletResponse response) 
   {
      pc.setLayout(PageLayout.TwoColumnsLeft);
      pc.setTitle("Cosmo - Informació de seguretat");
      
      BreadcrumbsControl navbar = new BreadcrumbsControl(getWorkspace());
      navbar.addItem(new BreadcrumbsItem("Inici", "HomePage", Icon.ICON_IMAGE_HOME));
      navbar.addItem(new BreadcrumbsItem("Informació de seguretat"));
      pc.addContent(navbar, ContentColumns.MAIN);
      
      HeaderControl header = new HeaderControl(getWorkspace(), "head");
      header.setTitle("Informació de seguretat");
      pc.addContent(header, ContentColumns.MAIN);
      
      XhtmlControl xInfo = new XhtmlControl(getWorkspace(), "info");
      xInfo.appendParagraph("La següent pàgina mostra les configuracions actuals dels agents de seguretat que s'han llegit a l'arxiu //cosmo.config.xml//, que conté la configuració de '''Cosmo Framnework'''.");
      pc.addContent(xInfo, ContentColumns.MAIN);
      
      XhtmlControl xAuthent = new XhtmlControl(getWorkspace(), "xAuthent");
      pc.addContent(xAuthent, ContentColumns.MAIN);
      
      DynamicMessageControl xMsgAuthent = new DynamicMessageControl(getWorkspace(), "xMsgAuthent");
      pc.addContent(xMsgAuthent, ContentColumns.MAIN);
      
      XhtmlControl xAutho = new XhtmlControl(getWorkspace(), "xAutho");
      pc.addContent(xAutho, ContentColumns.MAIN);

      DynamicMessageControl xMsgAutho = new DynamicMessageControl(getWorkspace(), "xMsgAutho");
      pc.addContent(xMsgAutho, ContentColumns.MAIN);
      
      return pc;
   }
   
   @Override
   public PageContext loadPageEvent(PageContext pc, HttpServletRequest request, HttpServletResponse response) 
   {
      ArrayList<String> lst;
      
      try
      {
         if (getWorkspace().isValidUserSession())
         {
            // Obtiene el agente de autenticación
            Authentication authent = AuthenticationFactory.getInstance(getWorkspace());
            
            lst = new ArrayList<String>();
            lst.add("Classe: //" + authent.getClass().getName() + "//");
            for (Entry<String, String> entry : authent.getParameters().entrySet())
            {
               lst.add(entry.getKey() + ": '''" + entry.getValue() + "'''");
            }
            
            XhtmlControl xAuthent = (XhtmlControl) pc.getControl("xAuthent");
            xAuthent.clear();
            xAuthent.appendHeadder(Icon.render(Icon.ICON_IMAGE_COG) + " Agent d'autenticació", 4).
                     appendParagraph("La següent informació fa referència a l'agent d'autenticació configurat actualment:").
                     appendUnorderedList(lst, "alt");
         }
      }
      catch (Exception ex)
      {
         DynamicMessageControl xMsgAuthent = (DynamicMessageControl) pc.getControl("xMsgAuthent");
         xMsgAuthent.setMessage(ex.getMessage());
         xMsgAuthent.setType(MessageTypes.Error);
         xMsgAuthent.setVisible(true);
      }
      
      try
      {
         if (getWorkspace().isValidUserSession())
         {
            // Obtiene el agente de autenticación
            Authorization autho = AuthorizationFactory.getInstance(getWorkspace());
            
            lst = new ArrayList<String>();
            lst.add("Classe: //" + autho.getClass().getName() + "//");
            for (Entry<String, String> entry : autho.getParameters().entrySet())
            {
               lst.add(entry.getKey() + ": '''" + entry.getValue() + "'''");
            }
            
            XhtmlControl xAutho = (XhtmlControl) pc.getControl("xAutho");
            xAutho.clear();
            xAutho.appendHeadder(Icon.render(Icon.ICON_IMAGE_COG) + " Agent d'autorització", 4).
                   appendParagraph("La següent informació fa referència a l'agent d'autorització configurat actualment:").
                   appendUnorderedList(lst, "alt");
         }
      }
      catch (Exception ex)
      {
         DynamicMessageControl xMsgAutho = (DynamicMessageControl) pc.getControl("xMsgAutho");
         xMsgAutho.setMessage(ex.getMessage());
         xMsgAutho.setType(MessageTypes.Error);
         xMsgAutho.setVisible(true);
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
