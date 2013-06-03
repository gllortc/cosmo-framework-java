package com.cosmo.web.pages;

import java.io.IOException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cosmo.ui.Page;
import com.cosmo.ui.PageContext;

/**
 * Servlet implementation class FormPage
 */
@WebServlet( description = "Logout", urlPatterns = { "/LogoutPage" } )
public class LogoutPage extends Page 
{
   /** Serial Version UID */
   private static final long serialVersionUID = -104852712419784967L;

   @Override
   public PageContext loadPageEvent(PageContext pc, HttpServletRequest request, HttpServletResponse response) 
   {
      if (getWorkspace().isValidUserSession())
      {
         // Realiza la acción de logout
         getWorkspace().closeUserSession();
      }
      
      try 
      {
         // Redirige a la página de inicio
         response.sendRedirect("HomePage");

         return pc;
      } 
      catch (IOException ex) 
      {
         pc.showException(getWorkspace(), ex);
      }
      
      return pc;
   }
   
   @Override
   public PageContext initPageEvent(PageContext pc, HttpServletRequest request, HttpServletResponse response) 
   {
      return pc;
   }

   @Override
   public PageContext formSendedEvent(PageContext pc, HttpServletRequest request, HttpServletResponse response) 
   {
      return pc;
   }
   
   @Override
   public PageContext pageException(PageContext pc, Exception exception) 
   {
      pc.showException(getWorkspace(), exception);
      
      return pc;
   }
}
