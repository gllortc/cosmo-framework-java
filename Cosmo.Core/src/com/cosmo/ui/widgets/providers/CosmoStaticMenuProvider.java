package com.cosmo.ui.widgets.providers;

import com.cosmo.Workspace;
import com.cosmo.WorkspaceProperties;
import com.cosmo.orm.apps.OrmApplication;
import com.cosmo.ui.widgets.MenuWidget;
import com.cosmo.ui.widgets.MenuWidget.MenuTypes;
import com.cosmo.ui.widgets.MenuItem;
import com.cosmo.util.DataTypeException;
import com.cosmo.util.IOUtils;
import com.cosmo.util.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Implementa un proveedor de menú que coge las opciones de menú del archivo de configuración de Cosmo.
 * 
 * @author Gerard Llort
 */
public class CosmoStaticMenuProvider extends MenuProvider
{
   private static final String XML_NODE_MENU = "menu";
   private static final String XML_NODE_MENUITEM = "menuitem";
   private static final String XML_ATT_ID = "id";
   private static final String XML_ATT_NAME = "name";
   private static final String XML_ATT_HREF = "href";
   private static final String XML_ATT_ICON = "icon";
   private static final String XML_ATT_PARENT = "parent";
   private static final String XML_ATT_TYPE = "type";
   private static final String XML_NODE_MENUSECTION = "menusection";
   private static final String XML_ATT_SOURCE = "source";


   //==============================================
   // Methods
   //==============================================

   /**
    * Obtiene los elementos de menú del workspace.
    * 
    * @param workspace Una instancia de {@link Workspace} que representa el workspace para el que se desea obtener el menú.
    * @param type Un elemento de {@link MenuTypes} que indica qué tipo de menú se desea obtener.
    * @return Una lista de instancias de {@link MenuItem} que representan los elementos de menú.
    * 
    * @throws MenuProviderException 
    */
   @Override
   public ArrayList<MenuItem> loadMenu(Workspace workspace, MenuTypes type) throws MenuProviderException
   {
      String fileName;
      InputStream is = null;
      MenuTypes mtype; 
      Node nNode;
      Node menuNode;
      Element eElement;
      Element menuElement;
      NodeList menuList;
      NodeList nList;
      MenuItem menuitem;
      ArrayList<MenuItem> menuItems = new ArrayList<MenuItem>();

      try
      {
         fileName = File.separator + "WEB-INF" + File.separator + WorkspaceProperties.PROPERTIES_FILENAME;
         is = new FileInputStream(workspace.getServerContext().getRealPath(fileName));

         DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
         DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
         Document doc = dBuilder.parse(is);
         doc.getDocumentElement().normalize();

         // Obtiene los elementos de menú
         menuList = doc.getElementsByTagName(CosmoStaticMenuProvider.XML_NODE_MENU);
         for (int menuId = 0; menuId < menuList.getLength(); menuId++) 
         {
            menuNode = menuList.item(menuId);
            if (menuNode.getNodeType() == Node.ELEMENT_NODE)
            {
               menuElement = (Element) menuNode;
               if (MenuWidget.convertToMenuType(menuElement.getAttribute(CosmoStaticMenuProvider.XML_ATT_TYPE)) == type)
               {
                  mtype = MenuWidget.convertToMenuType(menuElement.getAttribute(CosmoStaticMenuProvider.XML_ATT_TYPE));
                  if (mtype == type)
                  {
                     // Obtiene los elementos de menú estáticos
                     nList = menuElement.getElementsByTagName(CosmoStaticMenuProvider.XML_NODE_MENUITEM);
                     for (int temp = 0; temp < nList.getLength(); temp++) 
                     {
                        nNode = nList.item(temp);
                        if (nNode.getNodeType() == Node.ELEMENT_NODE)
                        {
                           eElement = (Element) nNode;
                           menuitem = new MenuItem(eElement.getAttribute(CosmoStaticMenuProvider.XML_ATT_ID),
                                                   eElement.getAttribute(CosmoStaticMenuProvider.XML_ATT_NAME), 
                                                   eElement.getAttribute(CosmoStaticMenuProvider.XML_ATT_HREF),
                                                   eElement.getAttribute(CosmoStaticMenuProvider.XML_ATT_PARENT));

                           // Si existe icono especificado, lo agrega
                           if (eElement.getAttribute(CosmoStaticMenuProvider.XML_ATT_ICON) != null)
                           {
                              menuitem.setIcon(eElement.getAttribute(CosmoStaticMenuProvider.XML_ATT_ICON));
                           }

                           menuItems.add(menuitem);
                        }
                     }

                     // Obtiene las secciones dinámicas
                     String source;
                     nList = menuElement.getElementsByTagName(CosmoStaticMenuProvider.XML_NODE_MENUSECTION);
                     for (int temp = 0; temp < nList.getLength(); temp++) 
                     {
                        nNode = nList.item(temp);
                        if (nNode.getNodeType() == Node.ELEMENT_NODE)
                        {
                           eElement = (Element) nNode;
                           source = eElement.getAttribute(CosmoStaticMenuProvider.XML_ATT_SOURCE);

                           if (!StringUtils.isNullOrEmptyTrim(source))
                           {
                              if (source.equalsIgnoreCase("corm-apps"))
                              {
                                 for (OrmApplication app : workspace.getProperties().getOrmProperties().getOrmApplications())
                                 {
                                    menuitem = new MenuItem(app.getId(),
                                                            app.getTitle(), 
                                                            app.getApplicationUrl(workspace),
                                                            "");

                                    // Si existe icono especificado, lo agrega
                                    if (!StringUtils.isNullOrEmptyTrim(eElement.getAttribute(CosmoStaticMenuProvider.XML_ATT_ICON)))
                                    {
                                       menuitem.setIcon(eElement.getAttribute(CosmoStaticMenuProvider.XML_ATT_ICON));
                                    }

                                    menuItems.add(menuitem);
                                 }
                              }
                           }
                        }
                     }
                  }
               }
            }
         }

         return menuItems;
      }
      catch (FileNotFoundException ex)
      {
         throw new MenuProviderException(ex.getMessage(), ex);
      }
      catch (IOException ex)
      {
         throw new MenuProviderException(ex.getMessage(), ex);
      }
      catch (ParserConfigurationException ex)
      {
         throw new MenuProviderException(ex.getMessage(), ex);
      }
      catch (SAXException ex)
      {
         throw new MenuProviderException(ex.getMessage(), ex);
      }
      catch (DataTypeException ex)
      {
         throw new MenuProviderException(ex.getMessage(), ex);
      }
      finally
      {
         IOUtils.closeStream(is);
      }
   }
}
