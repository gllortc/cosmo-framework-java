package com.cosmo.servlets;

import com.cosmo.Cosmo;
import com.cosmo.ui.controls.HeaderControl;
import com.cosmo.ui.Page;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Implementa una página de error estandarizada.
 * 
 * @author Gerard Llort
 */
public class ErrorPage extends Page
{

   /** Serial Version UID */
   private static final long serialVersionUID = -206701370055938291L;

   @Override
   public void initPageEvent(HttpServletRequest request, HttpServletResponse response) 
   {
      // Obtiene la excepción almacenada
      Exception ex = (Exception) request.getSession().getAttribute(Cosmo.KEY_CACHE_SESSION_LASTEXCEPTION);

      StringBuilder sb = new StringBuilder();
      sb.append("An internal error ocurred in this server:");
      sb.append("<br /><br />");
      sb.append("<strong>Description</strong>: ").append(ex.getMessage());

      this.setTitle(this.getWorkspace().getName() + " - SERVER ERROR");

      HeaderControl header = new HeaderControl(getWorkspace(), "head");
      header.setTitle("SERVER ERROR");
      header.setDescription(sb.toString());

      this.addContent(header, ContentColumns.MAIN);
   }

   @Override
   public void formSendedEvent(HttpServletRequest request, HttpServletResponse response) 
   {
      // Nothing to do here
   }

   @Override
   public void loadPageEvent(HttpServletRequest request, HttpServletResponse response) 
   {
      // Nothing to do here
   }
   
}
