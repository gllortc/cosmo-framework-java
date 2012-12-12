package com.cosmo.web.pages;

import com.cosmo.ui.Page;
import com.cosmo.ui.controls.BreadcrumbsControl;
import com.cosmo.ui.controls.BreadcrumbsItem;
import com.cosmo.ui.controls.HeaderControl;
import com.cosmo.ui.controls.Icon;
import com.cosmo.ui.controls.ListViewControl;
import com.cosmo.ui.controls.ListViewItem;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * P�gina de prova.
 * 
 * @author Gerard Llort
 */
public class ListViewPage extends Page 
{
   /** Serial Version UID */
   private static final long serialVersionUID = 3643631517575757387L;
   
   @Override
   public void initPageEvent(HttpServletRequest request, HttpServletResponse response) 
   {
      this.setLayout(PageLayout.TwoColumnsLeft);
      this.setTitle("Cosmo - Samples - ListView Control");
      
      BreadcrumbsControl navbar = new BreadcrumbsControl(getWorkspace());
      navbar.addItem(new BreadcrumbsItem("Home", "HomePage", Icon.ICON_IMAGE_HOME));
      navbar.addItem(new BreadcrumbsItem("Samples", "SamplesPage"));
      navbar.addItem(new BreadcrumbsItem("ListView sample", ""));
      this.addContent(navbar, ContentColumns.MAIN);
      
      HeaderControl header = new HeaderControl(getWorkspace());
      header.setTitle("ListView Control");
      header.setDescription("Exemple de llista ListView.");
      this.addContent(header, ContentColumns.MAIN);
      
      ListViewControl olc = new ListViewControl(getWorkspace());
      olc.addListItem(new ListViewItem("Cosmos", "<em>Cosmos: un viaje personal</em> es una serie documental de divulgación científica escrita por Carl Sagan, Ann Druyan y Steven Soter (con Sagan como guionista principal), cuyos objetivos fundamentales fueron: difundir la historia de la astronomía y de la ciencia, el origen de la vida, concienciar sobre el lugar que ocupa nuestra especie y nuestro planeta en el universo, las modernas visiones de la cosmología y las últimas noticias de la exploración espacial; en particular, las misiones Voyager.", "#", "img/cosmo_01.png", 55, 55, "Carl Seagan", new Date()));
      olc.addListItem(new ListViewItem("Los Grados del Zodiaco de Charubel", "Charubel fue el seudónimo de un monje y vidente inglés nacido en el año 1826. Su don profético le permitió atribuir a cada grado zodiacal una frase, representativa de una visión, que luego podría ser aplicada para la interpretación simbólica de un tema natal o para aplicar a eventos o hechos que dependan de la interpretación de algún elemento zodiaca. Para su correcta utilización se debe tener en cuenta lo siguiente: en el caso de que un planeta, por ejemplo la luna, se encuentre en el grado 23°15´ de un signo, en la tabla de Charubel, se deberá consultar el grado 24, es decir.. siempre se deberá redondear hacia arriba, aunque nunca estará demás consultar también el grado anterior.", "#", "img/cosmo_02.png", 55, 55, "Charubel", new Date()));
      this.addContent(olc, ContentColumns.MAIN);
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
