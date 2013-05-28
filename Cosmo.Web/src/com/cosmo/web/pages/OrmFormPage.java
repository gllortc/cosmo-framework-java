package com.cosmo.web.pages;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cosmo.data.orm.InvalidMappingException;
import com.cosmo.data.orm.OrmProvider;
import com.cosmo.ui.Page;
import com.cosmo.ui.PageContext;
import com.cosmo.ui.PageContext.ContentColumns;
import com.cosmo.ui.PageContext.PageLayout;
import com.cosmo.ui.controls.BreadcrumbsControl;
import com.cosmo.ui.controls.BreadcrumbsItem;
import com.cosmo.ui.controls.DynamicMessageControl;
import com.cosmo.ui.controls.FormControl;
import com.cosmo.ui.controls.HeaderControl;
import com.cosmo.ui.controls.Icon;
import com.cosmo.ui.controls.XhtmlControl;
import com.cosmo.web.sample.Weather;

/**
 * Servlet implementation class FormPage
 */
@WebServlet(description = "Exemple de formulari Cosmo fent us de CORM", urlPatterns = { "/OrmFormPage" })
public class OrmFormPage extends Page 
{
   /** Serial Version UID */
   private static final long serialVersionUID = 8878330001994265668L;
   
   private static final String ID_MSG = "msg";

   @Override
   public PageContext initPageEvent(PageContext pc, HttpServletRequest request, HttpServletResponse response) 
   {
      pc.setLayout(PageLayout.TwoColumnsLeft);
      pc.setTitle("Cosmo - Samples - Form Control");
      
      BreadcrumbsControl navbar = new BreadcrumbsControl(getWorkspace());
      navbar.addItem(new BreadcrumbsItem("Home", "HomePage", Icon.ICON_IMAGE_HOME));
      navbar.addItem(new BreadcrumbsItem("Samples", "SamplesPage"));
      navbar.addItem(new BreadcrumbsItem("ORM Form sample", ""));
      pc.addContent(navbar, ContentColumns.MAIN);

      HeaderControl header = new HeaderControl(getWorkspace());
      header.setTitle("ORM Form Control");
      header.setDescription("Exemple de formulari que fa us de CORM (Cormo ORM).");
      pc.addContent(header, ContentColumns.MAIN);

      XhtmlControl xhtml = new XhtmlControl(getWorkspace());
      xhtml.append("Per consultar el contingut de les dades que s'introdueixen en aquest formulari, consultar l'exemple de Grid Control.");
      pc.addContent(xhtml, ContentColumns.MAIN);

      DynamicMessageControl message = new DynamicMessageControl(getWorkspace(), ID_MSG);
      pc.addContent(message, ContentColumns.MAIN);
      
      try 
      {
         FormControl form = new FormControl(getWorkspace(), "OrmWeatherForm");
         form.addGroup(Weather.class);
         pc.addContent(form, ContentColumns.MAIN);
      } 
      catch (InvalidMappingException ex) 
      {
         DynamicMessageControl msg = (DynamicMessageControl) pc.getControl(ID_MSG);
         msg.setVisible(true);
         msg.setType(DynamicMessageControl.MessageTypes.Error);
         msg.setMessage("ERROR: " + ex.getMessage());
      }
      
      return pc;
   }

   @Override
   public PageContext formSendedEvent(PageContext pc, HttpServletRequest request, HttpServletResponse response)
   {
      OrmProvider ormp;
      
      try 
      {
         ormp = new OrmProvider("cosmo.server", getWorkspace());
         ormp.add(Weather.class, request);

         response.sendRedirect("GridPage");
      } 
      catch (Exception ex) 
      {
         DynamicMessageControl msg = (DynamicMessageControl) pc.getControl(ID_MSG);
         msg.setVisible(true);
         msg.setType(DynamicMessageControl.MessageTypes.Error);
         msg.setMessage("ERROR: " + ex.getMessage());
      }
      
      return pc;
   }

   @Override
   public PageContext loadPageEvent(PageContext pc, HttpServletRequest request, HttpServletResponse response) 
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
