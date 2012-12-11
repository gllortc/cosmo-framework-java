package com.cosmo.web.pages;

import java.util.Enumeration;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cosmo.structures.GridData;
import com.cosmo.ui.Page;
import com.cosmo.ui.controls.BreadcrumbsControl;
import com.cosmo.ui.controls.BreadcrumbsItem;
import com.cosmo.ui.controls.DynamicMessageControl;
import com.cosmo.ui.controls.GridControl;
import com.cosmo.ui.controls.HeaderControl;
import com.cosmo.ui.controls.Icon;

/**
 * Página de prova.
 * 
 * @author Gerard Llort
 */
@WebServlet(description = "Server information page", urlPatterns = { "/ServerInfoPage" })
public class ServerInfoPage extends Page 
{
   /** Serial Version UID */
   private static final long serialVersionUID = -2960537450581221913L;

   private static final String ID_GRID = "grid";
   private static final String ID_MSG = "msg";
   
   @Override
   public void initPageEvent(HttpServletRequest request, HttpServletResponse response) 
   {
      this.setLayout(PageLayout.TwoColumnsLeft);
      this.setTitle("Cosmo - Samples - Grid Control");
      
      BreadcrumbsControl navbar = new BreadcrumbsControl();
      navbar.addItem(new BreadcrumbsItem("Home", "HomePage", Icon.ICON_IMAGE_HOME));
      navbar.addItem(new BreadcrumbsItem("Samples", "SamplesPage"));
      navbar.addItem(new BreadcrumbsItem("Grid sample", ""));
      this.addContent(navbar, ContentColumns.MAIN);
      
      HeaderControl header = new HeaderControl();
      header.setTitle("Server Status");
      header.setDescription("Cosmo Server status and configuration information.");
      this.addContent(header, ContentColumns.MAIN);
      
      DynamicMessageControl msg = new DynamicMessageControl(ID_MSG);
      msg.setVisible(false);
      this.addContent(msg, ContentColumns.MAIN);
      
      GridControl grid = new GridControl(ID_GRID);
      grid.setTitle("User control data (Session Cache)");
      grid.setFirstRowTitles(false);
      this.addContent(grid, ContentColumns.MAIN);
   }
   
   @Override
   public void loadPageEvent(HttpServletRequest request, HttpServletResponse response) 
   {
      int row = 0;
      String name;
      Enumeration<String> attrs;
      
      try 
      {
         GridData gd = new GridData();

         attrs = request.getSession().getAttributeNames();
         while (attrs.hasMoreElements())
         {
            name = attrs.nextElement();
            
            gd.setCell(row, 0, name);
            gd.setCell(row++, 1, request.getSession().getAttribute(name));
         }
         
         GridControl grid = (GridControl) this.getControl(ID_GRID);
         grid.setData(request.getSession(), gd);
      } 
      catch (Exception ex) 
      {
         DynamicMessageControl msg = (DynamicMessageControl) this.getControl(ID_MSG);
         msg.setVisible(true);
         msg.setType(DynamicMessageControl.MessageTypes.Error);
         msg.setMessage("ERROR: " + ex.getMessage());
      }
   }
   
   @Override
   public void formSendedEvent(HttpServletRequest request, HttpServletResponse response) 
   {
      throw new UnsupportedOperationException();
   }
   
}
