package com.cosmo.data.orm.apps;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cosmo.data.orm.OrmFactory;
import com.cosmo.net.HttpRequestUtils;
import com.cosmo.net.URL;
import com.cosmo.ui.Page;
import com.cosmo.ui.PageContext;
import com.cosmo.ui.PageContext.ContentColumns;
import com.cosmo.ui.PageContext.PageLayout;
import com.cosmo.ui.controls.BreadcrumbsControl;
import com.cosmo.ui.controls.BreadcrumbsItem;
import com.cosmo.ui.controls.ButtonBarControl;
import com.cosmo.ui.controls.ButtonBarItem;
import com.cosmo.ui.controls.DynamicMessageControl;
import com.cosmo.ui.controls.FormControl;
import com.cosmo.ui.controls.FormFieldHidden;
import com.cosmo.ui.controls.GridControl;
import com.cosmo.ui.controls.GridRowAction;
import com.cosmo.ui.controls.HeaderControl;
import com.cosmo.ui.controls.Icon;

/**
 * Servlet implementation class OrmDataService
 */
@WebServlet(description = "Servicio back-end para Cosmo ORM (CORM)", urlPatterns = { "/OrmDataService" })
public class OrmDataService extends Page
{
   /** Serial Version UUID */
   private static final long serialVersionUID = 769955795551205157L;

   /** Nombre/URL de la página del servicio **/
   public static final String URL_BASE = "OrmDataService";
   
   public final String PARAMETER_COMMAND = "cmd";
   public final String PARAMETER_HOSTPAGE = "hp";
   public final String PARAMETER_APPID = "appid";
   
   public final String COMMAND_DELETE = "del";
   public final String COMMAND_EDIT = "edit";
   public final String COMMAND_CREATE = "create";
   public final String COMMAND_REPORT = "rpt";
   
   private final String CTRL_HEADER = "head";
   private final String CTRL_MESSAGE = "msg";
   private final String CTRL_GRID = "grd";
   
   @Override
   public PageContext initPageEvent(PageContext pc, HttpServletRequest request, HttpServletResponse response) 
   {
      OrmApplication app;
      
      try
      {      
         // Obtiene la definición de la aplicación y genera la estructura de página
         app = getWorkspace().getProperties().getOrmApplication(getApplicationId(request));
         
         pc.setLayout(PageLayout.TwoColumnsLeft);
         pc.setTitle(getWorkspace().getName() + " - " + app.getTitle());
            
         BreadcrumbsControl navbar = new BreadcrumbsControl(getWorkspace());
         navbar.addItem(new BreadcrumbsItem("Home", "HomePage", Icon.ICON_IMAGE_HOME));
         navbar.addItem(new BreadcrumbsItem("Grid / Formularis", ""));
         pc.addContent(navbar, ContentColumns.MAIN);
            
         HeaderControl header = new HeaderControl(getWorkspace(), CTRL_HEADER);
         header.setTitle(app.getTitle());
         header.setDescription(app.getDescription());
         pc.addContent(header, ContentColumns.MAIN);
         
         DynamicMessageControl msg = new DynamicMessageControl(getWorkspace(), CTRL_MESSAGE);
         msg.setVisible(false);
         pc.addContent(msg, ContentColumns.MAIN);
      }
      catch (Exception ex)
      {
         pc.showException(getWorkspace(), ex);
      }
      
      return pc;
   }

   @Override
   public PageContext formSendedEvent(PageContext pc, HttpServletRequest request, HttpServletResponse response) 
   {
      OrmFactory ormp;
      OrmApplication app;
      Class<?> cls;

      try 
      {
         // Obtiene la definición de la aplicación y genera la estructura de página
         app = getWorkspace().getProperties().getOrmApplication(getApplicationId(request));
         
         // Obtiene la referencia a la clase CORM objeto de la aplicación
         cls = Class.forName(app.getClassName());
         
         // Obtiene la instancia del Request
         Object instance = OrmFactory.getObjectFromRequest(cls, request);

         ormp = new OrmFactory(app.getConnectionId(), getWorkspace());
         if (HttpRequestUtils.getValue(request, PARAMETER_COMMAND, "").equals(COMMAND_CREATE))
         {
            ormp.insert(instance);
         }
         else if (HttpRequestUtils.getValue(request, PARAMETER_COMMAND, "").equals(COMMAND_EDIT))
         {
            ormp.update(instance);
         }
         else if (HttpRequestUtils.getValue(request, PARAMETER_COMMAND, "").equals(COMMAND_DELETE))
         {
            ormp.delete(instance);
         }
         
         // Genera la URL para mostrar el listado
         URL url = new URL(URL_BASE);
         url.addParameter(PARAMETER_APPID, HttpRequestUtils.getValue(request, PARAMETER_APPID));
         url.addParameter(PARAMETER_COMMAND, COMMAND_REPORT);
         
         // Redirige el flujo de la aplicació al listado
         response.sendRedirect(url.toString());
         return null;
      } 
      catch (Exception ex) 
      {
         DynamicMessageControl msg = (DynamicMessageControl) pc.getControl(CTRL_MESSAGE);
         msg.setVisible(true);
         msg.setType(DynamicMessageControl.MessageTypes.Error);
         msg.setMessage("ERROR: " + ex.getMessage());
      }
      
      return pc;
   }

   @Override
   public PageContext loadPageEvent(PageContext pc, HttpServletRequest request, HttpServletResponse response) 
   {
      OrmApplication app;
      Class<?> cls;
      
      try
      {
         // Obtiene la definición de la aplicación y genera la estructura de página
         app = getWorkspace().getProperties().getOrmApplication(getApplicationId(request));
         
         // Obtiene la referencia a la clase CORM objeto de la aplicación
         cls = Class.forName(app.getClassName());

         // Si se trata de eliminar o editar, se actúa previamente
         if (HttpRequestUtils.getValue(request, PARAMETER_COMMAND, "").equals(COMMAND_DELETE))
         {
            Object instance = OrmFactory.getObjectFromRequest(cls, request);

            OrmFactory ormp = new OrmFactory(app.getConnectionId(), getWorkspace());
            ormp.delete(instance);            
         }
         
         // Según el comando, realiza una acción. Si no hay comando, muestra el grid
         if (HttpRequestUtils.getValue(request, PARAMETER_COMMAND, "").equals(COMMAND_CREATE))
         {
            FormControl form = new FormControl(getWorkspace(), app.getId());
            form.addGroup(cls);
            form.addHiddenValue(new FormFieldHidden(PARAMETER_APPID, HttpRequestUtils.getValue(request, PARAMETER_APPID)));
            form.addHiddenValue(new FormFieldHidden(PARAMETER_COMMAND, HttpRequestUtils.getValue(request, PARAMETER_COMMAND)));
            pc.addContent(form, ContentColumns.MAIN);
         }
         else if (HttpRequestUtils.getValue(request, PARAMETER_COMMAND, "").equals(COMMAND_EDIT))
         {
            Object instance = OrmFactory.getObjectFromRequest(cls, request);
            
            OrmFactory ormp = new OrmFactory(app.getConnectionId(), getWorkspace());
            instance = ormp.get(instance);
            
            FormControl form = new FormControl(getWorkspace(), app.getId());
            form.addGroup(instance);
            form.addHiddenValue(new FormFieldHidden(PARAMETER_APPID, HttpRequestUtils.getValue(request, PARAMETER_APPID)));
            form.addHiddenValue(new FormFieldHidden(PARAMETER_COMMAND, HttpRequestUtils.getValue(request, PARAMETER_COMMAND)));
            pc.addContent(form, ContentColumns.MAIN);
         }
         else
         {
            // Muestra el GRID de la aplicación
            ButtonBarControl btnBar = new ButtonBarControl(getWorkspace());
            btnBar.addButton(new ButtonBarItem("Nou registre", createActionUrl(app, cls, COMMAND_CREATE, false), Icon.ICON_IMAGE_PLUS));
            btnBar.addButton(new ButtonBarItem("Refrescar", createActionUrl(app, cls, COMMAND_REPORT, false), Icon.ICON_IMAGE_REFRESH));
            pc.addContent(btnBar, ContentColumns.MAIN);
            
            GridControl grid = new GridControl(getWorkspace(), CTRL_GRID);
            grid.addRowAction(new GridRowAction("", createActionUrl(app, cls, COMMAND_EDIT, true), "icon-pencil"));
            grid.addRowAction(new GridRowAction("", createActionUrl(app, cls, COMMAND_DELETE, true), "icon-remove-circle"));
            grid.setData(app.getConnectionId(), cls, false);
            pc.addContent(grid, ContentColumns.MAIN);
         }
      }
      catch (Exception ex)
      {
         pc.showException(getWorkspace(), ex);
      }
      
      return pc;
   }

   @Override
   public PageContext pageException(PageContext pc, Exception exception) 
   {
      return pc;
   }
   
   
   //==============================================
   // Private members
   //==============================================
   
   /**
    * Obtiene el identificador de la aplicación
    */
   private String getApplicationId(HttpServletRequest request)
   {
      String appId = HttpRequestUtils.getValue(request, PARAMETER_APPID);
      
      if (appId == null || appId.trim().isEmpty())
      {
         return "";
      }
      else
      {
         return appId;
      }
   }
   
   /**
    * Genera una URL de comando para una determinada acción y registro.
    */
   private String createActionUrl(OrmApplication app, Class<?> ormClass, String command, boolean idNeeded)
   {
      String url = "";
      
      url = URL_BASE + "?";
      url += PARAMETER_APPID + "=" + app.getId() + "&";
      url += PARAMETER_COMMAND + "=" + command;
      
      if (idNeeded)
      {
         url += "&" + GridRowAction.TOKEN_ROW_ID;
      }
      
      if (command.equals(COMMAND_DELETE))
      {
         url = "javascript:deleteConfirm('" + url + "');";
      }
      
      return url;
   }

}
