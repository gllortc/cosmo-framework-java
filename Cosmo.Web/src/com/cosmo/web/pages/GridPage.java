package com.cosmo.web.pages;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cosmo.data.DataConnection;
import com.cosmo.structures.GridData;
import com.cosmo.ui.Page;
import com.cosmo.ui.controls.BreadcrumbsControl;
import com.cosmo.ui.controls.BreadcrumbsItem;
import com.cosmo.ui.controls.DynamicMessageControl;
import com.cosmo.ui.controls.GridControl;
import com.cosmo.ui.controls.HeaderControl;
import com.cosmo.ui.controls.Icon;
import com.cosmo.web.sample.WeatherManager;

/**
 * Página de prova.
 * 
 * @author Gerard Llort
 */
@WebServlet( description = "GridPage", urlPatterns = { "/GridPage" } )
public class GridPage extends Page 
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
      
      BreadcrumbsControl navbar = new BreadcrumbsControl(getWorkspace());
      navbar.addItem(new BreadcrumbsItem("Home", "HomePage", Icon.ICON_IMAGE_HOME));
      navbar.addItem(new BreadcrumbsItem("Samples", "SamplesPage"));
      navbar.addItem(new BreadcrumbsItem("Grid sample", ""));
      this.addContent(navbar, ContentColumns.MAIN);
      
      HeaderControl header = new HeaderControl(getWorkspace());
      header.setTitle("Grid Control");
      header.setDescription("Exemple de llistat.");
      this.addContent(header, ContentColumns.MAIN);
      
      DynamicMessageControl msg = new DynamicMessageControl(getWorkspace(), ID_MSG);
      msg.setVisible(false);
      this.addContent(msg, ContentColumns.MAIN);
      
      GridControl grid = new GridControl(getWorkspace(), ID_GRID);
      this.addContent(grid, ContentColumns.MAIN);
   }
   
   @Override
   public void loadPageEvent(HttpServletRequest request, HttpServletResponse response) 
   {
      DataConnection conn;
      WeatherManager wm;
      
      try 
      {
         conn = new DataConnection(getWorkspace().getProperties().getDataSource("cosmo.server"));
         wm = new WeatherManager(conn);
      
         GridData gd = new GridData();
         gd.setCells(wm.getAll(), true);
         
         GridControl grid = (GridControl) this.getControl(ID_GRID);
         grid.setData(request, gd);
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
