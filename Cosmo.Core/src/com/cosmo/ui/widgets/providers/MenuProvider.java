package com.cosmo.ui.widgets.providers;

import com.cosmo.Cosmo;
import com.cosmo.Workspace;
import com.cosmo.ui.widgets.MenuWidget.MenuTypes;
import com.cosmo.ui.widgets.MenuItem;
import java.util.ArrayList;

/**
 * Singleton que proporciona la instancia del generador de menús de Cosmo.
 * 
 * @author Gerard Llort
 */
public abstract class MenuProvider 
{
   private static MenuProvider instance = null;
   
   //==============================================
   // Methods
   //==============================================
   
   /**
    * Obtiene los elementos de menú del workspace.
    * 
    * @param workspace Una instancia de {@link Workspace} que representa el workspace para el que se desea obtener el menú.
    * @return Una lista de instancias de {@link MenuItem} que representan los elementos de menú.
    * 
    * @throws MenuProviderException 
    */
   public abstract ArrayList<MenuItem> loadMenu(Workspace workspace, MenuTypes type) throws MenuProviderException;
   
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
   public static MenuProvider getInstance(Workspace workspace) throws MenuProviderException 
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
    * @throws UserProviderException 
    */
   private static MenuProvider loadProvider(Workspace workspace) throws MenuProviderException 
   {
      String className = "-- no render provider defined in proprties --";
      MenuProvider provider;
      
      try 
		{
         // Recupera el nombre de la clase
         className = workspace.getProperties().getString(Cosmo.PROPERTY_WORKSPACE_MENU_PROVIDER);
         
         // Genera una instancia de la clase
         Class<?> cls = Class.forName(className);
         provider = (MenuProvider) cls.newInstance();
         
         return provider;
		} 
		catch (ClassNotFoundException ex) 
		{
         throw new MenuProviderException("ClassNotFoundException: " + className, ex);
		}
      catch (InstantiationException ex)
      {
         throw new MenuProviderException("InstantiationException: " + className, ex);
      }
      catch (IllegalAccessException ex)
      {
         throw new MenuProviderException("IllegalAccessException: " + className, ex);
      }
   }
}
