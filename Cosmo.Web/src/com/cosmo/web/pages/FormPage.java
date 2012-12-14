package com.cosmo.web.pages;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cosmo.data.DataConnection;
import com.cosmo.net.HttpRequestUtils;
import com.cosmo.ui.Page;
import com.cosmo.ui.controls.BreadcrumbsControl;
import com.cosmo.ui.controls.BreadcrumbsItem;
import com.cosmo.ui.controls.DynamicMessageControl;
import com.cosmo.ui.controls.FormButton;
import com.cosmo.ui.controls.FormButton.ButtonType;
import com.cosmo.ui.controls.FormControl;
import com.cosmo.ui.controls.FormFieldGroup;
import com.cosmo.ui.controls.FormFieldText;
import com.cosmo.ui.controls.HeaderControl;
import com.cosmo.ui.controls.Icon;
import com.cosmo.web.sample.Weather;
import com.cosmo.web.sample.WeatherManager;

/**
 * Servlet implementation class FormPage
 */
@WebServlet(description = "Exemple de formulari Cosmo", urlPatterns = { "/FormPage" })
public class FormPage extends Page 
{
   /** Serial Version UID */
   private static final long serialVersionUID = 8878330001994265668L;
   
   private static final String ID_MSG = "msg";

   @Override
   public void initPageEvent(HttpServletRequest request, HttpServletResponse response) 
   {
      this.setLayout(PageLayout.TwoColumnsLeft);
      this.setTitle("Cosmo - Samples - Form Control");
      
      BreadcrumbsControl navbar = new BreadcrumbsControl(getWorkspace());
      navbar.addItem(new BreadcrumbsItem("Home", "HomePage", Icon.ICON_IMAGE_HOME));
      navbar.addItem(new BreadcrumbsItem("Samples", "SamplesPage"));
      navbar.addItem(new BreadcrumbsItem("Form sample", ""));
      this.addContent(navbar, ContentColumns.MAIN);

      HeaderControl header = new HeaderControl(getWorkspace());
      header.setTitle("Form Control");
      header.setDescription("Exemple de formulari. Per consultar el contingut de les dades que s'introdueixen en aquest formulari, consultar l'exemple de Grid Control.");
      this.addContent(header, ContentColumns.MAIN);
      
      DynamicMessageControl message = new DynamicMessageControl(getWorkspace(), ID_MSG);
      this.addContent(message, ContentColumns.MAIN);
      
      FormControl form = new FormControl(getWorkspace(), "WeatherForm");
      FormFieldGroup group = new FormFieldGroup("Temps meteorològic", "Introdueixi les dades de registre meteorològic.", form);
      group.addField(new FormFieldText("txtName", "Ciutat"));
      group.addField(new FormFieldText("txtTMin", "Temperatura mínima"));
      group.addField(new FormFieldText("txtTMax", "Temperatura màxima"));
      group.addField(new FormFieldText("txtPre", "Precipitació mitja"));
      form.addGroup(group);
      form.addButton(new FormButton("cmdAcceopt", "Enviar", ButtonType.Submit));
      this.addContent(form, ContentColumns.MAIN);
   }

   @Override
   public void formSendedEvent(HttpServletRequest request, HttpServletResponse response) 
   {
      DataConnection conn;
      WeatherManager wm;
      
      Weather weather = new Weather();
      weather.setCityName(HttpRequestUtils.getValue(request, "txtName"));
      weather.setTempMin(HttpRequestUtils.getInt(request, "txtTMin"));
      weather.setTempMax(HttpRequestUtils.getInt(request, "txtTMax"));
      weather.setPrecipitation(HttpRequestUtils.getInt(request, "txtPre"));
      
      try 
      {
         conn = new DataConnection(getWorkspace().getProperties().getDataSource("cosmo.server"));
         wm = new WeatherManager(conn);
         wm.add(weather);

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
