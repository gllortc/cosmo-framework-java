package com.cosmo.web.pages;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cosmo.logging.LogManager;
import com.cosmo.logging.Logger;
import com.cosmo.ui.Page;
import com.cosmo.ui.controls.HeaderControl;
import com.cosmo.ui.controls.XhtmlControl;

/**
 * Home page.
 * 
 * @author Gerard Llort
 */
@WebServlet( description = "HomePage", urlPatterns = { "/HomePage" } )
public class HomePage extends Page 
{
   /** Serial Version UID */
   private static final long serialVersionUID = -539209206006431580L;

   // Habilita el sistema de LOG
   Logger logger; 
   
   @Override
   public void initPageEvent(HttpServletRequest request, HttpServletResponse response) 
   {
      logger = LogManager.getLogger(getWorkspace(), SecretPage.class);
      
      logger.info("Iniciant pàgina...");
      
      this.setLayout(PageLayout.TwoColumnsLeft);
      this.setTitle("Página de prova");

      HeaderControl header = new HeaderControl(getWorkspace());
      header.setTitle("Benvingut!");
      header.setDescription("Benvingut al site de demostració de <strong>Cosmo Framework</strong>.");
      
      this.addContent(header, ContentColumns.MAIN);
      
      XhtmlControl xhtml = new XhtmlControl(getWorkspace());
      xhtml.appendParagraph("Aquest site presenta les diferents funcionalitats de " + 
                            XhtmlControl.formatBold("Cosmo Framework") + ".");
      
      this.addContent(xhtml, ContentColumns.MAIN);
   }
   
   @Override
   public void loadPageEvent(HttpServletRequest request, HttpServletResponse response) 
   {
      // Nothing to do
   }
   
   @Override
   public void formSendedEvent(HttpServletRequest request, HttpServletResponse response) 
   {
      throw new UnsupportedOperationException();
   }
   
}
