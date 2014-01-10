package com.cosmo.manager.pages;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cosmo.ui.Page;
import com.cosmo.ui.PageContext;
import com.cosmo.ui.PageContext.ContentColumns;
import com.cosmo.ui.PageContext.PageLayout;
import com.cosmo.ui.controls.BreadcrumbsControl;
import com.cosmo.ui.controls.BreadcrumbsItem;
import com.cosmo.ui.controls.HeaderControl;
import com.cosmo.ui.controls.Icon;
import com.cosmo.ui.controls.SliderControl;
import com.cosmo.ui.controls.SliderItem;
import com.cosmo.ui.controls.XhtmlControl;

/**
 * Página de prova.
 * 
 * @author Gerard Llort
 */
@WebServlet(description = "SliderPage", urlPatterns = { "/SliderPage" })
public class SliderPage extends Page
{
   /** Serial Version UID */
   private static final long serialVersionUID = 6834195523514376992L;

   @Override
   public PageContext initPageEvent(PageContext pc, HttpServletRequest request, HttpServletResponse response)
   {
      pc.setLayout(PageLayout.TwoColumnsLeft);
      pc.setTitle("Cosmo - Slider");

      BreadcrumbsControl navbar = new BreadcrumbsControl(getWorkspace());
      navbar.addItem(new BreadcrumbsItem("Inici", "HomePage", Icon.ICON_IMAGE_HOME));
      navbar.addItem(new BreadcrumbsItem("Slider", ""));
      pc.addContent(navbar, ContentColumns.MAIN);

      HeaderControl header = new HeaderControl(getWorkspace());
      header.setTitle("Slider Control");
      header.setDescription("Exemple d'ús del control " + XhtmlControl.formatBold("SliderControl")
            + " per generar presentacions d'imatges i/o contingut XHTML.");
      pc.addContent(header, ContentColumns.MAIN);

      SliderControl slider = new SliderControl(getWorkspace());
      slider.setWidth(550);
      slider.setHeight(413);
      slider.addItem(new SliderItem(
            "img/Cosmos_01.jpg",
            550,
            413,
            "Slider 1",
            "Quisque tristique massa nulla, eget congue lectus varius eget. Mauris eget nisi tincidunt, condimentum orci ut, consequat felis. Pellentesque pretium hendrerit egestas. Interdum et malesuada fames ac ante ipsum primis in faucibus. Vivamus quis ipsum sed diam faucibus iaculis lacinia ac nisi.",
            SliderItem.SlideType.Image));
      slider.addItem(new SliderItem(
            "img/Cosmos_02.jpg",
            550,
            413,
            "Slider 2",
            "Quisque tristique massa nulla, eget congue lectus varius eget. Mauris eget nisi tincidunt, condimentum orci ut, consequat felis. Pellentesque pretium hendrerit egestas. Interdum et malesuada fames ac ante ipsum primis in faucibus. Vivamus quis ipsum sed diam faucibus iaculis lacinia ac nisi.",
            SliderItem.SlideType.Image));
      slider.addItem(new SliderItem(
            "img/Cosmos_03.jpg",
            550,
            413,
            "Slider 3",
            "Quisque tristique massa nulla, eget congue lectus varius eget. Mauris eget nisi tincidunt, condimentum orci ut, consequat felis. Pellentesque pretium hendrerit egestas. Interdum et malesuada fames ac ante ipsum primis in faucibus. Vivamus quis ipsum sed diam faucibus iaculis lacinia ac nisi.",
            SliderItem.SlideType.Image));
      slider.addItem(new SliderItem(
            "img/Cosmos_04.jpg",
            550,
            413,
            "Slider 4",
            "Quisque tristique massa nulla, eget congue lectus varius eget. Mauris eget nisi tincidunt, condimentum orci ut, consequat felis. Pellentesque pretium hendrerit egestas. Interdum et malesuada fames ac ante ipsum primis in faucibus. Vivamus quis ipsum sed diam faucibus iaculis lacinia ac nisi.",
            SliderItem.SlideType.Image));
      slider.addItem(new SliderItem(
            "img/Cosmos_05.jpg",
            550,
            413,
            "Slider 5",
            "Quisque tristique massa nulla, eget congue lectus varius eget. Mauris eget nisi tincidunt, condimentum orci ut, consequat felis. Pellentesque pretium hendrerit egestas. Interdum et malesuada fames ac ante ipsum primis in faucibus. Vivamus quis ipsum sed diam faucibus iaculis lacinia ac nisi.",
            SliderItem.SlideType.Image));
      pc.addContent(slider, ContentColumns.MAIN);

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
