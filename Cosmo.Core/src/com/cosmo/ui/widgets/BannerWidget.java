package com.cosmo.ui.widgets;

import com.cosmo.Workspace;
import com.cosmo.ui.templates.TemplateUnavailableException;

/**
 *
 * @author Gerard Llort
 */
public class BannerWidget extends Widget
{
   private static final String WIDGET_ID = "CosmoUiWdgtBannerArea";
   
   public BannerWidget(Workspace workspace)
   {
      super(workspace);
   }
   
   @Override
   public String getWidgetTypeId() 
   {
      return BannerWidget.WIDGET_ID;
   }

   @Override
   public String render() throws TemplateUnavailableException 
   {
      throw new UnsupportedOperationException("Not supported yet.");
   }
   
}
