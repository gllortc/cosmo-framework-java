package com.cosmo.manager.pages;

import java.util.Enumeration;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cosmo.ui.Page;
import com.cosmo.ui.PageContext;
import com.cosmo.ui.PageContext.ContentColumns;
import com.cosmo.ui.PageContext.PageLayout;
import com.cosmo.ui.controls.BreadcrumbsControl;
import com.cosmo.ui.controls.BreadcrumbsItem;
import com.cosmo.ui.controls.DynamicMessageControl;
import com.cosmo.ui.controls.GridControl;
import com.cosmo.ui.controls.HeaderControl;
import com.cosmo.ui.controls.Icon;
import com.cosmo.util.GridData;

/**
 * Página de prova.
 * 
 * @author Gerard Llort
 */
@WebServlet(description = "Server information page", urlPatterns = { "/ServerInfoPage" })
public class ServerInfoPage extends Page
{
   /** Serial Version UID */
   private static final long   serialVersionUID = -2960537450581221913L;

   private static final String ID_GRID          = "grid";
   private static final String ID_MSG           = "msg";

   @Override
   public PageContext initPageEvent(PageContext pc, HttpServletRequest request, HttpServletResponse response)
   {
      pc.setLayout(PageLayout.TwoColumnsLeft);
      pc.setTitle("Cosmo - Samples - Grid Control");

      BreadcrumbsControl navbar = new BreadcrumbsControl(getWorkspace());
      navbar.addItem(new BreadcrumbsItem("Home", "HomePage", Icon.ICON_IMAGE_HOME));
      navbar.addItem(new BreadcrumbsItem("Samples", "SamplesPage"));
      navbar.addItem(new BreadcrumbsItem("Grid sample", ""));
      pc.addContent(navbar, ContentColumns.MAIN);

      HeaderControl header = new HeaderControl(getWorkspace());
      header.setTitle("Server Status");
      header.setDescription("Cosmo Server status and configuration information.");
      pc.addContent(header, ContentColumns.MAIN);

      DynamicMessageControl msg = new DynamicMessageControl(getWorkspace(), ID_MSG);
      msg.setVisible(false);
      pc.addContent(msg, ContentColumns.MAIN);

      GridControl grid = new GridControl(getWorkspace(), ID_GRID);
      grid.setTitle("User control data (Session Cache)");
      grid.setFirstRowTitles(true);
      pc.addContent(grid, ContentColumns.MAIN);

      return pc;
   }

   @Override
   public PageContext loadPageEvent(PageContext pc, HttpServletRequest request, HttpServletResponse response)
   {
      int row = 0;
      String name;
      Enumeration<String> attrs;

      try
      {
         GridData gd = new GridData();
         gd.setCell(row, 0, "Variable");
         gd.setCell(row++, 1, "Value");

         attrs = request.getSession().getAttributeNames();
         while (attrs.hasMoreElements())
         {
            name = attrs.nextElement();

            gd.setCell(row, 0, name);
            gd.setCell(row++, 1, request.getSession().getAttribute(name));
         }

         GridControl grid = (GridControl) pc.getControl(ID_GRID);
         grid.setData(gd);
         // grid.setData(request, gd);
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
