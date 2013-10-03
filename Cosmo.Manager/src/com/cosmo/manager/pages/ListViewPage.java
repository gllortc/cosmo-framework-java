package com.cosmo.manager.pages;

import com.cosmo.ui.Page;
import com.cosmo.ui.PageContext;
import com.cosmo.ui.PageContext.ContentColumns;
import com.cosmo.ui.PageContext.PageLayout;
import com.cosmo.ui.controls.BreadcrumbsControl;
import com.cosmo.ui.controls.BreadcrumbsItem;
import com.cosmo.ui.controls.HeaderControl;
import com.cosmo.ui.controls.Icon;
import com.cosmo.ui.controls.ListViewControl;
import com.cosmo.ui.controls.ListViewItem;
import com.cosmo.ui.controls.XhtmlControl;

import java.util.Date;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Página de prova.
 * 
 * @author Gerard Llort
 */
@WebServlet( description = "ListViewPage", urlPatterns = { "/ListViewPage" } )
public class ListViewPage extends Page 
{
   /** Serial Version UID */
   private static final long serialVersionUID = 3643631517575757387L;
   
   @Override
   public PageContext initPageEvent(PageContext pc, HttpServletRequest request, HttpServletResponse response) 
   {
      pc.setLayout(PageLayout.TwoColumnsLeft);
      pc.setTitle("Cosmo - Llistes");
      
      BreadcrumbsControl navbar = new BreadcrumbsControl(getWorkspace());
      navbar.addItem(new BreadcrumbsItem("Inici", "HomePage", Icon.ICON_IMAGE_HOME));
      navbar.addItem(new BreadcrumbsItem("Llistes", ""));
      pc.addContent(navbar, ContentColumns.MAIN);
      
      HeaderControl header = new HeaderControl(getWorkspace());
      header.setTitle("ListView Control");
      header.setDescription("Exemple d'ús del control " + XhtmlControl.formatBold("ListViewControl") + " per generar llistats de contingut (tipus bloc).");
      pc.addContent(header, ContentColumns.MAIN);
      
      ListViewControl olc = new ListViewControl(getWorkspace());
      olc.addListItem(new ListViewItem("Cosmos", "<em>Cosmos: un viaje personal</em> es una serie documental de divulgación científica escrita por Carl Sagan, Ann Druyan y Steven Soter (con Sagan como guionista principal), cuyos objetivos fundamentales fueron: difundir la historia de la astronomía y de la ciencia, el origen de la vida, concienciar sobre el lugar que ocupa nuestra especie y nuestro planeta en el universo, las modernas visiones de la cosmología y las últimas noticias de la exploración espacial; en particular, las misiones Voyager.", "#", "img/cosmo_01.png", 55, 55, "Carl Seagan", new Date()));
      olc.addListItem(new ListViewItem("Los Grados del Zodiaco de Charubel", "Charubel fue el seudónimo de un monje y vidente inglés nacido en el año 1826. Su don profético le permitió atribuir a cada grado zodiacal una frase, representativa de una visión, que luego podría ser aplicada para la interpretación simbólica de un tema natal o para aplicar a eventos o hechos que dependan de la interpretación de algún elemento zodiaca. Para su correcta utilización se debe tener en cuenta lo siguiente: en el caso de que un planeta, por ejemplo la luna, se encuentre en el grado 23º15' de un signo, en la tabla de Charubel, se deberá consultar el grado 24, es decir, siempre se deberá redondear hacia arriba, aunque nunca estará demás consultar también el grado anterior.", "#", "img/cosmo_02.png", 55, 55, "Charubel", new Date()));
      pc.addContent(olc, ContentColumns.MAIN);
      
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
      return pc;
   }

   @Override
   public PageContext pageException(PageContext pc, Exception exception) 
   {
      pc.showException(getWorkspace(), exception);
      
      return pc;
   }
}
