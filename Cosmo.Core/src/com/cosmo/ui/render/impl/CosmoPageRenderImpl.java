package com.cosmo.ui.render.impl;

import java.util.Iterator;

import com.cosmo.Cosmo;
import com.cosmo.ui.Page;
import com.cosmo.ui.controls.Control;
import com.cosmo.ui.render.PageRender;
import com.cosmo.ui.render.PageRenderException;
import com.cosmo.ui.templates.Template;
import com.cosmo.ui.templates.TemplateControlException;
import com.cosmo.ui.templates.TemplateLink;
import com.cosmo.ui.templates.TemplateScript;
import com.cosmo.ui.templates.TemplateUnavailableException;
import com.cosmo.ui.widgets.BannerAreaWidget;
import com.cosmo.ui.widgets.LoginWidget;
import com.cosmo.ui.widgets.MenuWidget;
import com.cosmo.ui.widgets.providers.MenuProviderException;

/**
 * Convierte una página en código XHTML para enviar al cliente.<br />
 * Esta implementación de {@link PageRender} corresponde al renderizador de páginas nativo de Cosmo Framework.
 *  
 * @author Gerard Llort
 */
public class CosmoPageRenderImpl implements PageRender
{
   private static final String TAG_SITE_NAME = "SITE.NAME";
   
   //==============================================
   // Constructors
   //==============================================
   
   // No constructors declared
   
   //==============================================
   // Methods
   //==============================================
   
   /**
    * Renderiza la página convirtiendo la lógica de clases en código XHTML.
    * 
    * @param page Una instancia de {@link Page} que representa la página a renderizar.
    * @return Una cadena que contiene el código XHTML.
    * 
    * @throws TemplateUnavailableException
    * @throws PageRenderException 
    */
   @Override
   public String render(Page page) throws TemplateUnavailableException, PageRenderException
   {
      TemplateScript script;
      TemplateLink link;
      StringBuilder xhtml = new StringBuilder();
      
      Template template = page.getWorkspace().getTemplate();

      try
      {      
         // Confecciona la cabecera
         xhtml.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">").append("\n");
         xhtml.append("<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"es\" lang=\"es\">").append("\n");
         xhtml.append("<head>").append("\n");
         xhtml.append("  <title>").append(page.getTitle()).append("</title>").append("\n");
         xhtml.append("  <meta http-equiv=\"Content-Type\" content=\"text/html; charset=").append(page.getCharset()).append("\"></meta>").append("\n");
         xhtml.append("  <meta name=\"generator\" content=\"" + Cosmo.COSMO_NAME + "\"></meta>").append("\n");
         
         Iterator<TemplateLink> itl = template.getLinks();
         while (itl.hasNext())
         {
            link = itl.next();
            xhtml.append(link.render());
         }
         
         Iterator<TemplateScript> its = template.getScripts();
         while (its.hasNext())
         {
            script = its.next();
            xhtml.append(script.render());
         }
         
         // xhtml.append("  <link rel=\"stylesheet\" type=\"text/css\" href=\"templates/1/template.css\"></link>").append("\n");
         xhtml.append("</head>").append("\n");
         xhtml.append("<body>").append("\n");

         // Obtiene la estructura
         xhtml.append(template.getLayout(page.getLayout()));
         Control.replaceTag(xhtml, TAG_SITE_NAME, page.getWorkspace().getName());

         // Widgets
         renderLogin(xhtml, page);
         renderMenus(xhtml, page);
         renderBanners(xhtml, page);

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
   private void renderContents(StringBuilder xhtml, Page page) throws TemplateUnavailableException
   {
      Control control;
      Iterator<Control> it = page.getPageContent(Page.ContentColumns.MAIN);
      StringBuilder ctrl = new StringBuilder();
      
      while (it.hasNext())
      {
         control = it.next();
         ctrl.append(control.render()).append("\n");
      }

      int index = xhtml.indexOf(PageRender.TAG_WIDGET_CONTENT);
      xhtml.replace(index, index + PageRender.TAG_WIDGET_CONTENT.length(), ctrl.toString());
   }
   
   /**
    * Renderiza el widget de Login.
    * 
    * @param xhtml Una instancia de {@link StringBuilder} que se actualizará con el contenido del elemento renderizado.
    * @param page Una instancia de {@link Page} que representa la página a renderizar.
    */
   private void renderLogin(StringBuilder xhtml, Page page) throws MenuProviderException, TemplateUnavailableException
   {
      int index = xhtml.indexOf(PageRender.TAG_WIDGET_LOGIN);
      
      if (index >= 0)
      {
         LoginWidget login = new LoginWidget(page.getWorkspace());
         xhtml.replace(index, index + PageRender.TAG_WIDGET_LOGIN.length(), login.render());
      }
   }
   
   /**
    * Renderiza el menú.
    * 
    * @param xhtml Una instancia de {@link StringBuilder} que se actualizará con el contenido del elemento renderizado.
    * @param page Una instancia de {@link Page} que representa la página a renderizar.
    */
   private void renderMenus(StringBuilder xhtml, Page page) throws MenuProviderException, TemplateUnavailableException
   {
      int index = xhtml.indexOf(PageRender.TAG_WIDGET_MENU);
      
      if (index >= 0)
      {
         MenuWidget menu = new MenuWidget(page.getWorkspace(), MenuWidget.MenuTypes.Lateral);
         xhtml.replace(index, index + PageRender.TAG_WIDGET_MENU.length(), menu.render());
      }
   }
   
   /**
    * Renderiza el menú.
    * 
    * @param xhtml Una instancia de {@link StringBuilder} que se actualizará con el contenido del elemento renderizado.
    * @param page Una instancia de {@link Page} que representa la página a renderizar.
    */
   private void renderBanners(StringBuilder xhtml, Page page) throws TemplateUnavailableException
   {
      int index;
      BannerAreaWidget baw;
      
      index = xhtml.indexOf(PageRender.TAG_WIDGET_BANNERS_LEFT);
      if (index >= 0)
      {
         baw = new BannerAreaWidget(page.getWorkspace(), BannerAreaWidget.BannerAreas.Left);
         xhtml.replace(index, index + PageRender.TAG_WIDGET_BANNERS_LEFT.length(), baw.render());
      }
      
      index = xhtml.indexOf(PageRender.TAG_WIDGET_BANNERS_RIGHT);
      if (index >= 0)
      {
         baw = new BannerAreaWidget(page.getWorkspace(), BannerAreaWidget.BannerAreas.Left);
         xhtml.replace(index, index + PageRender.TAG_WIDGET_BANNERS_RIGHT.length(), baw.render());
      }
      
      index = xhtml.indexOf(PageRender.TAG_WIDGET_BANNERS_CENTER_TOP);
      if (index >= 0)
      {
         baw = new BannerAreaWidget(page.getWorkspace(), BannerAreaWidget.BannerAreas.Left);
         xhtml.replace(index, index + PageRender.TAG_WIDGET_BANNERS_CENTER_TOP.length(), baw.render());
      }
      
      index = xhtml.indexOf(PageRender.TAG_WIDGET_BANNERS_CENTER_BOTTOM);
      if (index >= 0)
      {
         baw = new BannerAreaWidget(page.getWorkspace(), BannerAreaWidget.BannerAreas.Left);
         xhtml.replace(index, index + PageRender.TAG_WIDGET_BANNERS_CENTER_BOTTOM.length(), baw.render());
      }
   }
}
