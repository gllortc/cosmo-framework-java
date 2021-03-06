package com.cosmo.ui.render.impl;

import java.util.Iterator;

import com.cosmo.Cosmo;
import com.cosmo.Workspace;
import com.cosmo.ui.PageContext;
import com.cosmo.ui.controls.Control;
import com.cosmo.ui.render.PageRenderException;
import com.cosmo.ui.render.PageRenderer;
import com.cosmo.ui.templates.Template;
import com.cosmo.ui.templates.TemplateControl;
import com.cosmo.ui.templates.TemplateControlException;
import com.cosmo.ui.templates.TemplateLink;
import com.cosmo.ui.templates.TemplateScript;
import com.cosmo.ui.templates.TemplateUnavailableException;
import com.cosmo.ui.widgets.BannerAreaWidget;
import com.cosmo.ui.widgets.LoginWidget;
import com.cosmo.ui.widgets.MenuWidget;
import com.cosmo.ui.widgets.providers.MenuProviderException;

/**
 * Convierte una p�gina en c�digo XHTML para enviar al cliente.<br />
 * Esta implementaci�n de {@link PageRenderer} corresponde al renderizador de p�ginas nativo de Cosmo Framework.
 *  
 * @author Gerard Llort
 */
public class CosmoPageRenderImpl implements PageRenderer
{
   private static final String TAG_SITE_NAME = "SITE.NAME";
   private static final String TAB_SEPARATOR = "  ";


   //==============================================
   // Constructors
   //==============================================

   // No constructors declared


   //==============================================
   // Methods
   //==============================================

   /**
    * Renderiza la p�gina convirtiendo la l�gica de clases en c�digo XHTML.
    * 
    * @param page Una instancia de {@link PageContext} que representa la p�gina a renderizar.
    * @param uuid Identificador �nico de la p�gina (si lo tuviera).
    * 
    * @return Una cadena que contiene el c�digo XHTML.
    * 
    * @throws TemplateUnavailableException
    * @throws PageRenderException 
    */
   @Override
   public String render(Workspace workspace, PageContext page, String uuid) throws TemplateUnavailableException, PageRenderException
   {
      TemplateScript script;
      TemplateLink link;
      TemplateControl tctrl;
      StringBuilder xhtml = new StringBuilder();

      Template template = workspace.getTemplate();

      try
      {
         // Confecciona la cabecera
         xhtml.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">").append("\n");
         xhtml.append("<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"es\" lang=\"es\">").append("\n");
         xhtml.append("<head>").append("\n");
         xhtml.append(TAB_SEPARATOR + "<title>").append(page.getTitle()).append("</title>").append("\n");
         xhtml.append(TAB_SEPARATOR + "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=").append(page.getCharset()).append("\"></meta>").append("\n");
         xhtml.append(TAB_SEPARATOR + "<meta name=\"generator\" content=\"" + Cosmo.COSMO_NAME + "\"></meta>").append("\n");
         xhtml.append(TAB_SEPARATOR + "<meta name=\"uuid\" content=\"" + uuid + "\"></meta>").append("\n");

         // Inserta Scripts y Links de la p�gina
         Iterator<TemplateLink> itl = template.getLinks();
         while (itl.hasNext())
         {
            link = itl.next();
            xhtml.append(TAB_SEPARATOR + link.render());
         }

         Iterator<TemplateScript> its = template.getScripts();
         while (its.hasNext())
         {
            script = its.next();
            xhtml.append(TAB_SEPARATOR + script.render());
         }

         // Inserta Scripts y Links de los controles
         for (Control ctrl : page.getCenterContents())
         {
            tctrl = template.getControl(ctrl.getControlTypeId());

            if (tctrl != null)
            {
               for (TemplateLink tlink : tctrl.getLinks())
               {
                  xhtml.append(TAB_SEPARATOR + tlink.render());
               }

               for (TemplateScript tscript : tctrl.getScripts())
               {
                  xhtml.append(TAB_SEPARATOR + tscript.render());
               }
            }
         }

         // xhtml.append("  <link rel=\"stylesheet\" type=\"text/css\" href=\"templates/1/template.css\"></link>").append("\n");
         xhtml.append("</head>").append("\n");
         xhtml.append("<body>").append("\n");

         // Obtiene la estructura
         xhtml.append(template.getLayout(page.getLayout()));
         Control.replaceTag(xhtml, TAG_SITE_NAME, workspace.getName());

         // Widgets
         renderLogin(workspace, xhtml, page);
         renderMenus(workspace, xhtml, page);
         renderBanners(workspace, xhtml, page);

         // Contenido
         renderContents(xhtml, page);

         // Confecciona el pie
         xhtml.append("</body>").append("\n");
         xhtml.append("</html>");
      }
      catch (TemplateControlException ex)
      {
         throw new PageRenderException(ex.getMessage(), ex);
      }
      catch (MenuProviderException ex)
      {
         throw new PageRenderException(ex.getMessage(), ex);
      }

      return xhtml.toString();
   }

   /**
    * Renderiza el contenido proncipal (columna central)
    */
   private void renderContents(StringBuilder xhtml, PageContext page) throws TemplateUnavailableException
   {
      Control control;
      Iterator<Control> it = page.getPageContent(PageContext.ContentColumns.MAIN);
      StringBuilder ctrl = new StringBuilder();

      while (it.hasNext())
      {
         control = it.next();
         ctrl.append(control.render()).append("\n");
      }

      int index = xhtml.indexOf(PageRenderer.TAG_WIDGET_CONTENT);
      xhtml.replace(index, index + PageRenderer.TAG_WIDGET_CONTENT.length(), ctrl.toString());
   }

   /**
    * Renderiza el widget de Login.
    * 
    * @param xhtml Una instancia de {@link StringBuilder} que se actualizar� con el contenido del elemento renderizado.
    * @param page Una instancia de {@link PageContext} que representa la p�gina a renderizar.
    */
   private void renderLogin(Workspace workspace, StringBuilder xhtml, PageContext page) throws MenuProviderException, TemplateUnavailableException
   {
      int index = xhtml.indexOf(PageRenderer.TAG_WIDGET_LOGIN);

      if (index >= 0)
      {
         LoginWidget login = new LoginWidget(workspace);
         xhtml.replace(index, index + PageRenderer.TAG_WIDGET_LOGIN.length(), login.render());
      }
   }

   /**
    * Renderiza el men�.
    * 
    * @param xhtml Una instancia de {@link StringBuilder} que se actualizar� con el contenido del elemento renderizado.
    * @param page Una instancia de {@link PageContext} que representa la p�gina a renderizar.
    */
   private void renderMenus(Workspace workspace, StringBuilder xhtml, PageContext page) throws MenuProviderException, TemplateUnavailableException
   {
      int index = xhtml.indexOf(PageRenderer.TAG_WIDGET_MENU);

      if (index >= 0)
      {
         MenuWidget menu = new MenuWidget(workspace, MenuWidget.MenuTypes.Lateral);
         xhtml.replace(index, index + PageRenderer.TAG_WIDGET_MENU.length(), menu.render());
      }
   }

   /**
    * Renderiza el men�.
    * 
    * @param xhtml Una instancia de {@link StringBuilder} que se actualizar� con el contenido del elemento renderizado.
    * @param page Una instancia de {@link PageContext} que representa la p�gina a renderizar.
    */
   private void renderBanners(Workspace workspace, StringBuilder xhtml, PageContext page) throws TemplateUnavailableException
   {
      int index;
      BannerAreaWidget baw;

      index = xhtml.indexOf(PageRenderer.TAG_WIDGET_BANNERS_LEFT);
      if (index >= 0)
      {
         baw = new BannerAreaWidget(workspace, BannerAreaWidget.BannerAreas.Left);
         xhtml.replace(index, index + PageRenderer.TAG_WIDGET_BANNERS_LEFT.length(), baw.render());
      }

      index = xhtml.indexOf(PageRenderer.TAG_WIDGET_BANNERS_RIGHT);
      if (index >= 0)
      {
         baw = new BannerAreaWidget(workspace, BannerAreaWidget.BannerAreas.Left);
         xhtml.replace(index, index + PageRenderer.TAG_WIDGET_BANNERS_RIGHT.length(), baw.render());
      }

      index = xhtml.indexOf(PageRenderer.TAG_WIDGET_BANNERS_CENTER_TOP);
      if (index >= 0)
      {
         baw = new BannerAreaWidget(workspace, BannerAreaWidget.BannerAreas.Left);
         xhtml.replace(index, index + PageRenderer.TAG_WIDGET_BANNERS_CENTER_TOP.length(), baw.render());
      }

      index = xhtml.indexOf(PageRenderer.TAG_WIDGET_BANNERS_CENTER_BOTTOM);
      if (index >= 0)
      {
         baw = new BannerAreaWidget(workspace, BannerAreaWidget.BannerAreas.Left);
         xhtml.replace(index, index + PageRenderer.TAG_WIDGET_BANNERS_CENTER_BOTTOM.length(), baw.render());
      }
   }
}
