package com.cosmo.web.pages;

import java.util.ArrayList;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cosmo.logging.LogManager;
import com.cosmo.logging.Logger;
import com.cosmo.ui.Page;
import com.cosmo.ui.controls.HeaderControl;
import com.cosmo.ui.controls.Icon;
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
      ArrayList<String> ul;
      
      logger = LogManager.getLogger(getWorkspace(), SecurityInfoPage.class);
      
      logger.info("Iniciant pàgina...");
      
      this.setLayout(PageLayout.TwoColumnsLeft);
      this.setTitle("Cosmo - Inici");

      HeaderControl header = new HeaderControl(getWorkspace());
      header.setTitle("Benvingut a Cosmo Framework!");
      this.addContent(header, ContentColumns.MAIN);
      
      ul = new ArrayList<String>();
      ul.add("'''Contingut''': Exemple de pàgina de contingut (text)");
      ul.add("'''Llistes''': Exemple de llistes (entrades de bloc, documents, etc.)");
      ul.add("'''Slider''': Exemple amb un slider o control que de visualitza diapositives HTML o imatges.");
      ul.add("'''Grid / Formularis''': Exemple de manteniment d'una taula que combina dues pàgines, una amb un grid i un formulari per donar d'alta registres.");
      ul.add(Icon.render(Icon.ICON_IMAGE_LOCK) + " '''Informació de seguretat''': Mostra la configuració actual dels agents de seguretat.");
      ul.add(Icon.render(Icon.ICON_IMAGE_LOCK) + " '''Tests de seguretat''': Executa i mostra els resultats dels diferents tests de seguretat per a l'usuari actual.");
      ul.add(Icon.render(Icon.ICON_IMAGE_LOCK) + " '''Gestió d'usuaris''': Exemple de manteniment de comptes d'usuari (només permet crear comptes d'usuari).");
      
      XhtmlControl xhtml = new XhtmlControl(getWorkspace());
      xhtml.appendHeadder(Icon.render(Icon.ICON_IMAGE_TAG) + " Contingut de l'aplicació demo", 4).
            appendParagraph("Aquesta aplicació ''demo'' presenta les diferents funcionalitats de '''Cosmo Framework'''.").
            appendUnorderedList(ul).
            appendParagraph("Les pàgines que contenen la icona " + Icon.render(Icon.ICON_IMAGE_LOCK) + " indiquen que només són accessibles per usuaris autenticats i opcionalment amb un rol o permis determinat.");
      this.addContent(xhtml, ContentColumns.MAIN);
      
      ul = new ArrayList<String>();
      ul.add("[BUG] S'ha de refer el model de dades en controls amb dades de sessió (formularis, grids). Això provoca que no es mostrin algunes dades en controls quan hi ha dos o més usuaris concurrents.");
      ul.add("[TASK] S'ha de refer el model de tractament de formularis ja que precisen encara de massa codi Java per controlar els modes (crear/editar/eliminar).");
      ul.add("[TASK] S'ha de crear una segona plantilla basada en Bootstrap (Twitter) per poder avaluar les funcionalitats de presentació de forma correcte.");
      
      XhtmlControl xBugs = new XhtmlControl(getWorkspace());
      xBugs.appendHeadder(Icon.render(Icon.ICON_IMAGE_TAG) + " Situació actual de Cosmo", 4).
            appendParagraph("Tingueu en compte que '''Cosmo''' és un framework encara en construcció i té actualment bastantes mancances i BUGs no resolts:").
            appendUnorderedList(ul);
      this.addContent(xBugs, ContentColumns.MAIN);
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

   @Override
   public void pageException(Exception exception) 
   {
      showException(exception);
   }
}
