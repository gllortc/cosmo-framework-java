package com.cosmo.manager.pages;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cosmo.security.annotations.ActivitiesAllowed;
import com.cosmo.security.auth.AuthenticationFactory;
import com.cosmo.security.auth.impl.PostgreSqlAuthenticationImpl;
import com.cosmo.structures.GridData;
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
import com.cosmo.ui.controls.GridRowAction;
import com.cosmo.ui.controls.HeaderControl;
import com.cosmo.ui.controls.Icon;

/**
 * Página de prova.
 * 
 * @author Gerard Llort
 */
@ActivitiesAllowed( "admin.users.manage" )
@WebServlet( description = "UserManagerPage", urlPatterns = { "/UserManagerPage" } )
public class UserManagerPage extends Page 
{
   /** Serial Version UID */
   private static final long serialVersionUID = -2960537450581221913L;

   private static final String ID_GRID = "grid";
   private static final String ID_MSG = "msg";
   
   @Override
   public PageContext initPageEvent(PageContext pc, HttpServletRequest request, HttpServletResponse response) 
   {
      pc.setLayout(PageLayout.TwoColumnsLeft);
      pc.setTitle("Cosmo - Gestió d'usuaris");
      
      BreadcrumbsControl navbar = new BreadcrumbsControl(getWorkspace());
      navbar.addItem(new BreadcrumbsItem("Home", "HomePage", Icon.ICON_IMAGE_HOME));
      navbar.addItem(new BreadcrumbsItem("Gestió d'usuaris"));
      pc.addContent(navbar, ContentColumns.MAIN);
      
      HeaderControl header = new HeaderControl(getWorkspace());
      header.setTitle("Gestió d'usuaris");
      pc.addContent(header, ContentColumns.MAIN);
      
      DynamicMessageControl msg = new DynamicMessageControl(getWorkspace(), ID_MSG);
      msg.setVisible(false);
      pc.addContent(msg, ContentColumns.MAIN);
      
      ButtonBarControl btnBar = new ButtonBarControl(getWorkspace());
      btnBar.addButton(new ButtonBarItem("Nou compte", "UserRegisterPage", Icon.ICON_IMAGE_PLUS));
      btnBar.addButton(new ButtonBarItem("Refrescar", "UserManagerPage", Icon.ICON_IMAGE_REFRESH));
      pc.addContent(btnBar, ContentColumns.MAIN);
      
      GridControl grid = new GridControl(getWorkspace(), ID_GRID);
      grid.addRowAction(new GridRowAction("", "UserRegisterPage?mode=edit&id=" + GridRowAction.TOKEN_ROW_ID, "icon-pencil"));
      grid.addRowAction(new GridRowAction("", "UserManagerPage?action=delete&id=" + GridRowAction.TOKEN_ROW_ID, "icon-remove-circle"));
      pc.addContent(grid, ContentColumns.MAIN);
      
      return pc;
   }
   
   @Override
   public PageContext loadPageEvent(PageContext pc, HttpServletRequest request, HttpServletResponse response) 
   {
      try 
      {
         PostgreSqlAuthenticationImpl auth = (PostgreSqlAuthenticationImpl) AuthenticationFactory.getInstance(getWorkspace());
      
         GridData gd = new GridData();
         gd.setCells(auth.getUsersList(), true);
         
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
