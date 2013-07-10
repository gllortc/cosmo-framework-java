package com.cosmo.ui.controls;

import com.cosmo.ui.templates.TemplateControl;

public class GridRowAction 
{
   public static final String TOKEN_ROW_ID = "[@ID]";

   private static final String CPART_ROW_ACTION_ITEM = "grid-row-action-item";
   
   private static final String TAG_HREF = "IHREF";
   private static final String TAG_ICON = "ICON";
   private static final String TAG_TITLE = "FTITLE";
   
   private String caption;
   private String href;
   private String icon;

   public GridRowAction(String caption, String href, String icon)
   {
      this.caption = caption;
      this.href = href;
      this.icon = icon;
   }

   public String getCaption()
   {
      return caption;
   }

   public void setCaption(String caption) 
   {
      this.caption = caption;
   }

   public String getHref()
   {
      return href;
   }

   public void setHref(String href)
   {
      this.href = href;
   }

   public String getIcon()
   {
      return icon;
   }

   public void setIcon(String icon)
   {
      this.icon = icon;
   }
   
   /**
    * Renderiza el elemento y lo convierte a una cadena en formato XHTML.
    * 
    * @param tc Una instancia de {@link TemplateControl} que representa el control al que pertenece el elemento.
    * @param rowId Valor del identificador de la fila.
    * 
    * @return Una cadena en formato XHTML representable en un navegador web.
    */
   public String render(TemplateControl tc, String rowId)
   {
      String xhtml;
      
      xhtml = tc.getElement(CPART_ROW_ACTION_ITEM);
      xhtml = Control.replaceTag(xhtml, TAG_HREF, this.getHref());
      xhtml = Control.replaceTag(xhtml, TAG_TITLE, this.getCaption());
      xhtml = Control.replaceTag(xhtml, TAG_ICON, this.getIcon());
      xhtml = xhtml.replace(GridRowAction.TOKEN_ROW_ID, rowId);
      
      return xhtml;
   }
}
