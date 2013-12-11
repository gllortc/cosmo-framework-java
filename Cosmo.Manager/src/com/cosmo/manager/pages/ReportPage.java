package com.cosmo.manager.pages;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cosmo.data.DataQuery;
import com.cosmo.reports.Report;
import com.cosmo.reports.ReportsEngine;
import com.cosmo.ui.Page;
import com.cosmo.ui.PageContext;
import com.cosmo.ui.PageContext.ContentColumns;
import com.cosmo.ui.annotations.CacheScope;
import com.cosmo.ui.annotations.CacheScope.PageCacheScopes;
import com.cosmo.ui.controls.FormButton;
import com.cosmo.ui.controls.FormButton.ButtonType;
import com.cosmo.ui.controls.FormControl;
import com.cosmo.ui.controls.FormFieldText;
import com.cosmo.ui.controls.FormFieldset;
import com.cosmo.ui.controls.HeaderControl;

/**
 * Home page.
 * 
 * @author Gerard Llort
 */
@CacheScope( scope = PageCacheScopes.SCOPE_APPLICATION )
@WebServlet( description = "ReportPage", urlPatterns = { "/ReportPage" } )
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

      FormControl form = new FormControl(getWorkspace(), "rptForm");
      FormFieldset ffs = new FormFieldset("Report details");
      ffs.addField(new FormFieldText("txtTitle", "Title", 64));
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
      String sql = "SELECT id As \"PRODUCTS.REFID\", " +
                   "       city As \"PRODUCTS.DESCRIPTION\", " +
                   "       temp_lo As \"INVOICEDET.NUMITEMS\", " +
                   "       temp_hi As \"INVOICEDET.RATE\", " + 
                   "temp_lo + (temp_lo * temp_hi / 100) As \"INVOICEDET.AMOUNT\" " +
                   "FROM weather " + 
                   "ORDER BY city";

      Report rpt = new Report(getWorkspace(), "invoice");
      rpt.addDataQuery(new DataQuery("weather", "cosmo.server", sql));
      
      try
      {
         ReportsEngine re = new ReportsEngine();
         re.render(getWorkspace(), rpt);
      }
      catch (Exception ex)
      {
         
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
