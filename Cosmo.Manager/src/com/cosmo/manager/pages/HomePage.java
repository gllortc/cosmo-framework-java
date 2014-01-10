package com.cosmo.manager.pages;

import java.util.ArrayList;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cosmo.ui.Page;
import com.cosmo.ui.PageContext;
import com.cosmo.ui.annotations.CacheScope;
import com.cosmo.ui.annotations.CacheScope.PageCacheScopes;
import com.cosmo.ui.controls.HeaderControl;
import com.cosmo.ui.controls.Icon;
import com.cosmo.ui.controls.XhtmlControl;

/**
 * Home page.
 * 
 * @author Gerard Llort
 */
@CacheScope(scope = PageCacheScopes.SCOPE_APPLICATION)
@WebServlet(description = "HomePage", urlPatterns = { "/HomePage" })
public class HomePage extends Page
{
   /** Serial Version UID */
   private static final long serialVersionUID = -539209206006431580L;

   @Override
   public PageContext initPageEvent(PageContext pc, HttpServletRequest request, HttpServletResponse response)
   {
      ArrayList<String> ul;

      pc.setLayout(PageContext.PageLayout.TwoColumnsLeft);
      pc.setTitle("Cosmo - Inici");

      HeaderControl header = new HeaderControl(getWorkspace());
      header.setTitle("Benvingut a Cosmo Framework!");
      pc.addContent(header, PageContext.ContentColumns.MAIN);

      ul = new ArrayList<String>();
      ul.add("'''Contingut''': Exemple de pàgina de contingut (text)");
      ul.add("'''Llistes''': Exemple de llistes (entrades de bloc, documents, etc.)");
      ul.add("'''Slider''': Exemple amb un slider o control que de visualitza diapositives HTML o imatges.");
      ul.add("'''Grid / Formularis''': Exemple de manteniment d'una taula que combina dues pàgines, una amb un grid i un formulari per donar d'alta registres.");
      ul.add(Icon.render(Icon.ICON_IMAGE_LOCK)
            + " '''Informació de seguretat''': Mostra la configuració actual dels agents de seguretat.");
      ul.add(Icon.render(Icon.ICON_IMAGE_LOCK)
            + " '''Tests de seguretat''': Executa i mostra els resultats dels diferents tests de seguretat per a l'usuari actual.");
      ul.add(Icon.render(Icon.ICON_IMAGE_LOCK)
            + " '''Gestió d'usuaris''': Exemple de manteniment de comptes d'usuari (només permet crear comptes d'usuari).");

      XhtmlControl xhtml = new XhtmlControl(getWorkspace());
      xhtml.appendHeadder(Icon.render(Icon.ICON_IMAGE_TAG) + " Contingut de l'aplicació demo", 4)
            .appendParagraph("Aquesta aplicació ''demo'' presenta les diferents funcionalitats de '''Cosmo Framework'''.")
            .appendUnorderedList(ul)
            .appendParagraph(
                  "Les pàgines que contenen la icona "
                        + Icon.render(Icon.ICON_IMAGE_LOCK)
                        + " indiquen que només són accessibles per usuaris autenticats i opcionalment amb un rol o permis determinat.");
      pc.addContent(xhtml, PageContext.ContentColumns.MAIN);

      return pc;
   }

   @Override
   public PageContext loadPageEvent(PageContext pc, HttpServletRequest request, HttpServletResponse response)
   {
      return pc;
   }

   @Override
   public PageContext formSendedEvent(PageContext pc, HttpServletRequest request, HttpServletResponse response)
   {
      throw new UnsupportedOperationException();
   }

   @Override
   public PageContext pageException(PageContext pc, Exception exception)
   {
      pc.showException(getWorkspace(), exception);

      return pc;
   }
}
