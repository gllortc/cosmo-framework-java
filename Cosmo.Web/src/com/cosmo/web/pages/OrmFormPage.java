package com.cosmo.web.pages;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cosmo.data.DataConnection;
import com.cosmo.data.adapter.CosmoOrmProvider;
import com.cosmo.data.adapter.InvalidMappingException;
import com.cosmo.net.HttpRequestUtils;
import com.cosmo.ui.Page;
import com.cosmo.ui.controls.BreadcrumbsControl;
import com.cosmo.ui.controls.BreadcrumbsItem;
import com.cosmo.ui.controls.DynamicMessageControl;
import com.cosmo.ui.controls.FormControl;
import com.cosmo.ui.controls.HeaderControl;
import com.cosmo.ui.controls.Icon;
import com.cosmo.ui.controls.XhtmlControl;
import com.cosmo.web.sample.Weather;

/**
 * Servlet implementation class FormPage
 */
@WebServlet(description = "Exemple de formulari Cosmo fent us de CORM", urlPatterns = { "/OrmFormPage" })
public class OrmFormPage extends Page 
{
   /** Serial Version UID */
   private static final long serialVersionUID = 8878330001994265668L;
   
   private static final String ID_MSG = "msg";

   @Override
   public void initPageEvent(HttpServletRequest request, HttpServletResponse response) 
   {
      this.setLayout(PageLayout.TwoColumnsLeft);
      this.setTitle("Cosmo - Samples - Form Control");
      
      BreadcrumbsControl navbar = new BreadcrumbsControl();
      navbar.addItem(new BreadcrumbsItem("Home", "HomePage", Icon.ICON_IMAGE_HOME));
      navbar.addItem(new BreadcrumbsItem("Samples", "SamplesPage"));
      navbar.addItem(new BreadcrumbsItem("ORM Form sample", ""));
      this.addContent(navbar, ContentColumns.MAIN);

      HeaderControl header = new HeaderControl();
      header.setTitle("ORM Form Control");
      header.setDescription("Exemple de formulari que fa us de CORM (Cormo ORM).");
      this.addContent(header, ContentColumns.MAIN);

      XhtmlControl xhtml = new XhtmlControl();
      xhtml.append("Per consultar el contingut de les dades que s'introdueixen en aquest formulari, consultar l'exemple de Grid Control.");
      this.addContent(xhtml, ContentColumns.MAIN);

      DynamicMessageControl message = new DynamicMessageControl(ID_MSG);
      this.addContent(message, ContentColumns.MAIN);
      
      try 
      {
         FormControl form = new FormControl();
         form.addGroup(Weather.class);
         this.addContent(form, ContentColumns.MAIN);
      } 
      catch (InvalidMappingException e) 
      {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      
      /*FormFieldGroup group = new FormFieldGroup("Temps meteorològic", "Introdueixi les dades de registre meteorològic.", form);
      group.addField(new FormFieldText("txtName", "Ciutat"));
      group.addField(new FormFieldText("txtTMin", "Temperatura mínima"));
      group.addField(new FormFieldText("txtTMax", "Temperatura màxima"));
      group.addField(new FormFieldText("txtPre", "Precipitació mitja"));
      form.addGroup(group);
      form.addButton(new FormButton("cmdAcceopt", "Enviar", ButtonType.Submit));*/
      
   }

   @Override
   public void formSendedEvent(HttpServletRequest request, HttpServletResponse response)
   {
      DataConnection conn;
      CosmoOrmProvider cop;
      
      Weather weather = new Weather();
      weather.setCityName(HttpRequestUtils.getValue(request, "txtName"));
      weather.setTempMin(HttpRequestUtils.getInt(request, "txtTMin"));
      weather.setTempMax(HttpRequestUtils.getInt(request, "txtTMax"));
      weather.setPrecipitation(HttpRequestUtils.getInt(request, "txtPre"));
      
      try 
      {
         conn = new DataConnection(getWorkspace().getProperties().getDataSource("cosmo.server"), false);
         cop = new CosmoOrmProvider(conn);
         cop.add(weather);

         response.sendRedirect("GridPage");
      } 
      catch (Exception ex) 
      {
         DynamicMessageControl msg = (DynamicMessageControl) this.getControl(ID_MSG);
         msg.setVisible(true);
         msg.setType(DynamicMessageControl.MessageTypes.Error);
         msg.setMessage("ERROR: " + ex.getMessage());
      }
   }

   @Override
   public void loadPageEvent(HttpServletRequest request, HttpServletResponse response) 
   {
      // TODO Auto-generated method stub
   }
   
}
