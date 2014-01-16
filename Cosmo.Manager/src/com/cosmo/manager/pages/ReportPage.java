package com.cosmo.manager.pages;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cosmo.net.HttpRequestUtils;
import com.cosmo.reports.ReportsEngine;
import com.cosmo.reports.impl.DocxReprotsEngineImpl;
import com.cosmo.reports.impl.PdfReprotsEngineImpl;
import com.cosmo.ui.Page;
import com.cosmo.ui.PageContext;
import com.cosmo.ui.PageContext.ContentColumns;
import com.cosmo.ui.annotations.CacheScope;
import com.cosmo.ui.annotations.CacheScope.PageCacheScopes;
import com.cosmo.ui.controls.DynamicMessageControl;
import com.cosmo.ui.controls.FormButton;
import com.cosmo.ui.controls.FormButton.ButtonType;
import com.cosmo.ui.controls.FormControl;
import com.cosmo.ui.controls.FormFieldList;
import com.cosmo.ui.controls.FormFieldText;
import com.cosmo.ui.controls.FormFieldset;
import com.cosmo.ui.controls.HeaderControl;
import com.cosmo.util.KeyValue;

/**
 * Home page.
 * 
 * @author Gerard Llort
 */
@CacheScope(scope = PageCacheScopes.SCOPE_APPLICATION)
@WebServlet(description = "ReportPage", urlPatterns = { "/ReportPage" })
public class ReportPage extends Page
{
   /** Serial Version UID */
   private static final long serialVersionUID = -8593989858399713969L;

   @Override
   public PageContext initPageEvent(PageContext pc, HttpServletRequest request, HttpServletResponse response)
   {
      pc.setLayout(PageContext.PageLayout.TwoColumnsLeft);
      pc.setTitle("Cosmo - Reporting Service Sample");

      HeaderControl header = new HeaderControl(getWorkspace());
      header.setTitle("Reporting Service Sample");
      pc.addContent(header, PageContext.ContentColumns.MAIN);

      DynamicMessageControl message = new DynamicMessageControl(getWorkspace(), "result_msg");
      pc.addContent(message, ContentColumns.MAIN);

      FormFieldList formatList = new FormFieldList("cboFormat", "Format", request);
      formatList.addListOption(new KeyValue("pdf", "Adobe Acrobat (PDF)", true));
      formatList.addListOption(new KeyValue("docx", "Ms Word (DOCX)"));

      FormControl form = new FormControl(getWorkspace(), "rptForm");
      FormFieldset ffs = new FormFieldset("Report details");
      ffs.addField(new FormFieldText("txtTitle", "Title", 64, request));
      ffs.addField(formatList);
      form.addGroup(ffs);
      form.addButton(new FormButton("cmdReport", "Generate report", ButtonType.Submit));
      pc.addContent(form, ContentColumns.MAIN);

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
      String rptUrl = "";
      DynamicMessageControl msg = (DynamicMessageControl) pc.getControl("result_msg");
      ReportsEngine re;

      try
      {
         if (HttpRequestUtils.getValue(request, "cboFormat").equals("docx"))
         {
            re = new DocxReprotsEngineImpl(getWorkspace(), "weather");
         }
         else
         {
            re = new PdfReprotsEngineImpl(getWorkspace(), "weather");
         }
         re.getReport().addStaticValue("title", HttpRequestUtils.getValue(request, "txtTitle"));
         rptUrl = re.generateReport();

         msg.setVisible(true);
         msg.setType(DynamicMessageControl.MessageTypes.Information);
         msg.setMessage("<a href=\"" + rptUrl + "\" target=\"_blank\">Descargar archivo</a>");
      }
      catch (Exception ex)
      {
         msg.setVisible(true);
         msg.setType(DynamicMessageControl.MessageTypes.Error);
         msg.setMessage("ERROR: " + ex.getMessage());
      }

      return pc;
   }

   @Override
   public PageContext pageException(PageContext pc, Exception exception)
   {
      pc.showException(getWorkspace(), exception);

      return pc;
   }
}
