package com.cosmo.web.pages;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cosmo.ui.Page;
import com.cosmo.ui.controls.BreadcrumbsControl;
import com.cosmo.ui.controls.BreadcrumbsItem;
import com.cosmo.ui.controls.HeaderControl;
import com.cosmo.ui.controls.Icon;
import com.cosmo.ui.controls.SliderControl;
import com.cosmo.ui.controls.SliderItem;

/**
 * Página de prova.
 * 
 * @author Gerard Llort
 */
@WebServlet( description = "SliderPage", urlPatterns = { "/SliderPage" } )
public class SliderPage extends Page 
{
   /** Serial Version UID */
   private static final long serialVersionUID = 6834195523514376992L;

   @Override
   public void initPageEvent(HttpServletRequest request, HttpServletResponse response) 
   {
      this.setLayout(PageLayout.TwoColumnsLeft);
      this.setTitle("Cosmo - Samples - Slider Control");
      
      BreadcrumbsControl navbar = new BreadcrumbsControl(getWorkspace());
      navbar.addItem(new BreadcrumbsItem("Home", "HomePage", Icon.ICON_IMAGE_HOME));
      navbar.addItem(new BreadcrumbsItem("Samples", "SamplesPage"));
      navbar.addItem(new BreadcrumbsItem("Slider sample", ""));
      this.addContent(navbar, ContentColumns.MAIN);
      
      HeaderControl header = new HeaderControl(getWorkspace());
      header.setTitle("Slider Control");
      header.setDescription("Exemple de control slider.");
      this.addContent(header, ContentColumns.MAIN);
      
      SliderControl slider = new SliderControl(getWorkspace());
      slider.setWidth(550);
      slider.setHeight(413);
      slider.addItem(new SliderItem("img/Cosmos_01.jpg", 550, 413, SliderItem.SlideType.Image));
      slider.addItem(new SliderItem("img/Cosmos_02.jpg", 550, 413, SliderItem.SlideType.Image));
      slider.addItem(new SliderItem("img/Cosmos_03.jpg", 550, 413, SliderItem.SlideType.Image));
      slider.addItem(new SliderItem("img/Cosmos_04.jpg", 550, 413, SliderItem.SlideType.Image));
      slider.addItem(new SliderItem("img/Cosmos_05.jpg", 550, 413, SliderItem.SlideType.Image));
      this.addContent(slider, ContentColumns.MAIN);
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
