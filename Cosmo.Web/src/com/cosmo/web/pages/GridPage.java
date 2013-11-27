package com.cosmo.web.pages;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cosmo.data.DataAgent;
import com.cosmo.data.DataFactory;
import com.cosmo.ui.Page;
import com.cosmo.ui.PageContext;
import com.cosmo.ui.PageContext.ContentColumns;
import com.cosmo.ui.PageContext.PageLayout;
import com.cosmo.ui.controls.BreadcrumbsControl;
import com.cosmo.ui.controls.BreadcrumbsItem;
import com.cosmo.ui.controls.ButtonBarControl;
import com.cosmo.ui.controls.ButtonBarItem;
import com.cosmo.ui.controls.DynamicMessageControl;
import com.cosmo.ui.controls.GridControl;
import com.cosmo.ui.controls.HeaderControl;
import com.cosmo.ui.controls.Icon;
import com.cosmo.util.GridData;
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
   public PageContext initPageEvent(PageContext pc, HttpServletRequest request, HttpServletResponse response) 
   {
      pc.setLayout(PageLayout.TwoColumnsLeft);
      pc.setTitle("Cosmo - Grid / Formularis");
      
      BreadcrumbsControl navbar = new BreadcrumbsControl(getWorkspace());
      navbar.addItem(new BreadcrumbsItem("Home", "HomePage", Icon.ICON_IMAGE_HOME));
      navbar.addItem(new BreadcrumbsItem("Grid / Formularis", ""));
      pc.addContent(navbar, ContentColumns.MAIN);
      
      HeaderControl header = new HeaderControl(getWorkspace());
      header.setTitle("Grid / Formularis");
      header.setDescription("Exemple d'utilització combinada del control GridControl i FormControl.");
      pc.addContent(header, ContentColumns.MAIN);
      
      DynamicMessageControl msg = new DynamicMessageControl(getWorkspace(), ID_MSG);
      msg.setVisible(false);
      pc.addContent(msg, ContentColumns.MAIN);
      
      ButtonBarControl btnBar = new ButtonBarControl(getWorkspace());
      btnBar.addButton(new ButtonBarItem("Nou registre", "FormPage", Icon.ICON_IMAGE_PLUS));
      btnBar.addButton(new ButtonBarItem("Refrescar", "GridPage", Icon.ICON_IMAGE_REFRESH));
      pc.addContent(btnBar, ContentColumns.MAIN);
      
      GridControl grid = new GridControl(getWorkspace(), ID_GRID);
      pc.addContent(grid, ContentColumns.MAIN);
      
      return pc;
   }
   
   @Override
   public PageContext loadPageEvent(PageContext pc, HttpServletRequest request, HttpServletResponse response) 
   {
      DataAgent conn;
      WeatherManager wm;
      
      try 
      {
         conn = DataFactory.getInstance(getWorkspace(), "cosmo.server");
         wm = new WeatherManager(conn);
      
         GridData gd = new GridData();
         gd.setCells(wm.getAll(), true);
         
         GridControl grid = (GridControl) pc.getControl(ID_GRID);
         // grid.setData(request, gd);
         grid.setData(gd);
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
      throw new UnsupportedOperationException();
   }
   
   @Override
   public PageContext pageException(PageContext pc, Exception exception) 
   {
      pc.showException(getWorkspace(), exception);
      
      return pc;
   }
}
