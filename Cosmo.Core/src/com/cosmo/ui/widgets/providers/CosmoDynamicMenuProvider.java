package com.cosmo.ui.widgets.providers;

import com.cosmo.Workspace;
import com.cosmo.ui.widgets.MenuWidget.MenuTypes;
import com.cosmo.ui.widgets.MenuItem;
import java.util.ArrayList;

/**
 * Implementa un proveedor de menús que accede a base de datos para obtener la estructura de la web.
 * 
 * @author Gerard Llort
 */
public class CosmoDynamicMenuProvider extends MenuProvider
{

   @Override
   public ArrayList<MenuItem> loadMenu(Workspace workspace, MenuTypes type) throws MenuProviderException 
   {
      throw new UnsupportedOperationException("Not supported yet.");
   }
   
}
