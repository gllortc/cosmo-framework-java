package com.cosmo.ui.render;

import com.cosmo.Cosmo;
import com.cosmo.Workspace;
import com.cosmo.security.auth.AuthenticationException;
import com.cosmo.ui.Page;
import com.cosmo.ui.templates.TemplateUnavailableException;

/**
 * Singleton que proporciona la instancia del renderizador de paginas Cosmo.
 * 
 * @author Gerard Llort
 */
public abstract class PageRenderProvider 
{
   public static final String TAG_MENU = "[@MENU]";
   public static final String TAG_NAVIGATIONBAR = "[@NAVBAR]";
   public static final String TAG_CONTENT = "[@CONTENT]";
   public static final String TAG_CONTENT_RIGHT = "[@CONTENT-RIGHT]";
   public static final String TAG_BANNERS_LEFT = "[@BANNERS-LEFT]";
   public static final String TAG_BANNERS_RIGHT = "[@BANNERS-RIGHT]";
   public static final String TAG_BANNERS_CENTER_TOP = "[@BANNERS-CENTER-TOP]";
   public static final String TAG_BANNERS_CENTER_BOTTOM = "[@BANNERS-CENTER-BOTTOM]";
   public static final String TAG_STATS = "[@STATS]";
   
   private static PageRenderProvider instance = null;
   
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
   public abstract String render(Page page) throws TemplateUnavailableException, PageRenderException;
   
   //==============================================
   // Static members
   //==============================================
   
   /**
    * Devuelve una instancia de {@link PageRenderProvider} convenientemente instanciada y con
    * el proveedor de renderizado cargado.
    * 
    * @param workspace Una instancia de {@link Workspace} que representa el workspace actual.
    * @return Una instancia única de {@link PageRenderProvider} (sigleton).
    * 
    * @throws LoadPageRenderException 
    */
   public static PageRenderProvider getInstance(Workspace workspace) throws LoadPageRenderException 
   {
      if (instance == null) 
      {
         instance = loadProvider(workspace);
      }

      return instance;
   }
   
   //==============================================
   // Private members
   //==============================================
   
   /**
    * Carga el controlador de usuarios.
    * 
    * @throws AuthenticationException 
    */
   private static PageRenderProvider loadProvider(Workspace workspace) throws LoadPageRenderException
   {
      String className = "-- no render provider defined in proprties --";
      PageRenderProvider provider;
      
      try 
		{
         // Recupera el nombre de la clase
         className = workspace.getProperties().getString(Cosmo.PROPERTY_WORKSPACE_UI_RENDER_PROVIDER);
         
         // Genera una instancia de la clase
         Class<?> cls = Class.forName(className);
         provider = (PageRenderProvider) cls.newInstance();
         
         return provider;
		} 
		catch (ClassNotFoundException ex) 
		{
         throw new LoadPageRenderException("ClassNotFoundException: " + className, ex);
		}
      catch (InstantiationException ex)
      {
         throw new LoadPageRenderException("InstantiationException: " + className, ex);
      }
      catch (IllegalAccessException ex)
      {
         throw new LoadPageRenderException("IllegalAccessException: " + className, ex);
      }
   }
}
