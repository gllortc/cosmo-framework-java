package com.cosmo.web.pages;

import com.cosmo.ui.Page.PageLayout;
import com.cosmo.ui.controls.HeaderControl;
import com.cosmo.ui.Page;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Página de prova
 * 
 * @author Gerard Llort
 */
public class HomePage extends Page 
{
   /** Serial Version UID */
   private static final long serialVersionUID = -539209206006431580L;

   @Override
   public void initPageEvent(HttpServletRequest request, HttpServletResponse response) 
   {
      // Nothing to do
   }
   
   @Override
   public void loadPageEvent(HttpServletRequest request, HttpServletResponse response) 
   {
      this.setLayout(PageLayout.TwoColumnsLeft);
      this.setTitle("Página de prova");

      HeaderControl header = new HeaderControl();
      header.setTitle("PROVA DE PÁGINA");
      header.setDescription("Això és una prova de creació de págines amb Cosmo for Java!");
      
      // int a = 1 / 0;
      
      this.addContent(header, ContentColumns.MAIN);
   }
   
   @Override
   public void formSendedEvent(HttpServletRequest request, HttpServletResponse response) 
   {
      throw new UnsupportedOperationException();
   }
   
}
