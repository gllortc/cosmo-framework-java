package com.cosmo.manager.pages;

import com.cosmo.ui.Page;
import com.cosmo.ui.PageContext;
import com.cosmo.ui.PageContext.ContentColumns;
import com.cosmo.ui.PageContext.PageLayout;
import com.cosmo.ui.controls.BreadcrumbsControl;
import com.cosmo.ui.controls.BreadcrumbsItem;
import com.cosmo.ui.controls.HeaderControl;
import com.cosmo.ui.controls.Icon;
import com.cosmo.ui.controls.XhtmlControl;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Página de prova.
 * 
 * @author Gerard Llort
 */
@WebServlet( description = "ContentPage", urlPatterns = { "/ContentPage" } )
public class ContentPage extends Page 
{
   /** Serial Version UID */
   private static final long serialVersionUID = -1863993648152701220L;

   @Override
   public PageContext initPageEvent(PageContext pc, HttpServletRequest request, HttpServletResponse response) 
   {
      pc.setLayout(PageLayout.TwoColumnsLeft);
      pc.setTitle("Cosmo - Contingut");
      
      BreadcrumbsControl navbar = new BreadcrumbsControl(getWorkspace());
      navbar.addItem(new BreadcrumbsItem("Inici", "HomePage", Icon.ICON_IMAGE_HOME));
      navbar.addItem(new BreadcrumbsItem("Contingut", ""));
      pc.addContent(navbar, ContentColumns.MAIN);
      
      HeaderControl header = new HeaderControl(getWorkspace());
      header.setTitle("XHTML Control");
      header.setDescription("Exemple d'ús del control " + XhtmlControl.formatBold("XhtmlControl") + " per generar contingut estàtic");
      pc.addContent(header, ContentColumns.MAIN);
      
      XhtmlControl xhtml = new XhtmlControl(getWorkspace());
      xhtml.append("<h3>Headings</h3>");
      xhtml.append("<h1>Heading 1</h1>");
      xhtml.append("<h2>Heading 2</h2>");
      xhtml.append("<h3>Heading 3</h3>");
      xhtml.append("<h4>Heading 4</h4>");
      xhtml.append("<h5>Heading 5</h5>");
      xhtml.append("<h6>Heading 6</h6>");
      xhtml.append("<hr />");
      
      xhtml.append("<h3>Paragraphs</h3>");
      xhtml.append("<p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla tincidunt ornare nisl non cursus. Proin fermentum interdum pulvinar. Morbi sed odio quis dui ornare dignissim vel vel nulla. Proin augue ligula, convallis at lobortis mattis, laoreet in mauris. Praesent posuere hendrerit dolor vel pellentesque. Fusce sollicitudin volutpat elit ut venenatis. Vivamus ultrices neque nec dui pellentesque a ornare enim vehicula. Sed sit amet eros turpis, ac viverra arcu. Cras ullamcorper volutpat faucibus. Integer mi odio, bibendum vel malesuada porttitor, aliquam quis lectus. Integer a nibh at velit congue ornare eu quis erat. Pellentesque odio justo, tempus eu eleifend vel, sollicitudin et lacus. Fusce consectetur massa id ipsum ornare vestibulum. Donec lacinia nibh vel libero rhoncus imperdiet.</p>");
      xhtml.append("<hr />");
      
      xhtml.append("<h3>Blockquote</h3>");
      xhtml.append("<blockquote>");
      xhtml.append("<p>lobortis nisl ut aliquip ex ea commodo consequat. Duis autem vel eum iriure dolor in hendrerit in vulputate velit esse molestie consequat, vel illum dolore eu feugiat nulla facilisis at vero eros et accumsan et iusto odio");
      xhtml.append("<span>Someone Important</span></p>");
      xhtml.append("</blockquote>");
      xhtml.append("<blockquote class=\"small\">");
      xhtml.append("<p>lobortis nisl ut aliquip ex ea commodo consequat. Duis autem vel eum iriure dolor in hendrerit in vulputate velit esse molestie consequat, vel illum dolore eu feugiat nulla facilisis at vero eros et accumsan et iusto odio");
      xhtml.append("<span>Someone Important</span></p>");
      xhtml.append("</blockquote>");
      xhtml.append("<hr />");
      
      xhtml.append("<h3>Inline Styles</h3>");
      xhtml.append("<p><strong>Strong</strong></p>");
      xhtml.append("<p><em>Emphasis</em></p>");
      xhtml.append("<p><a href=\"\">Inline Link</a></p>");
      xhtml.append("<p><strike>Strike</strike></p>");
      xhtml.append("<p>Inline " + Icon.render(Icon.ICON_IMAGE_HOME, Icon.ICON_SIZE_DEFAULT) + " Icons</p>");
      xhtml.append("<p><code>&lt;h1&gt;Sample Code&lt;/h1&gt;</code></p>");
      xhtml.append("<hr />");

      xhtml.append("<h3>Lists</h3>");
      xhtml.append("<ul>");
      xhtml.append("<li>tation ullamcorper suscipit lobortis</li>");
      xhtml.append("<li>Nam liber tempor cum soluta nobis</li>");
      xhtml.append("<li>imperdiet doming id quod mazim</li>");
      xhtml.append("<li>suscipit lobortis nisl ut aliquip ex</li>");
      xhtml.append("</ul>");
      xhtml.append("<ol>");
      xhtml.append("<li>tation ullamcorper suscipit lobortis</li>");
      xhtml.append("<li>Nam liber tempor cum soluta nobis</li>");
      xhtml.append("<li>imperdiet doming id quod mazim</li>");
      xhtml.append("<li>suscipit lobortis nisl ut aliquip ex</li>");
      xhtml.append("</ol>");
      xhtml.append("<ul class=\"checks\">");
      xhtml.append("<li>tation ullamcorper suscipit lobortis</li>");
      xhtml.append("<li>Nam liber tempor cum soluta nobis</li>");
      xhtml.append("<li>imperdiet doming id quod mazim</li>");
      xhtml.append("<li>suscipit lobortis nisl ut aliquip ex</li>");
      xhtml.append("</ul>");
      
      pc.addContent(xhtml, ContentColumns.MAIN);
      
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
