package com.cosmo.reports.impl;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import org.apache.log4j.Logger;

import com.cosmo.Workspace;
import com.cosmo.data.DataException;
import com.cosmo.logging.LogFactory;
import com.cosmo.net.URL;
import com.cosmo.reports.Report;
import com.cosmo.reports.ReportException;
import com.cosmo.reports.ReportsEngine;
import com.cosmo.ui.Page;
import com.cosmo.ui.PageContext;
import com.cosmo.util.IOUtils;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;

/**
 * Implementa un motor de informes que permite obtener informes en formato PDF.
 * 
 * @author Gerard Llort
 */
public class PdfReprotsEngineImpl extends ReportsEngine
{
   // Habilita el LOG
   Logger log = LogFactory.getLogger("Reporting Services");

   public PdfReprotsEngineImpl(Workspace workspace, String reportId) throws ReportException
   {
      super(workspace, reportId);
   }

   /**
    * Renderiza el informe cargado en el motor de informes.<br />
    * Una vez renderizado el informe este se encuentra accesible en la URL asociada al informe. 
    * 
    * @throws DataException 
    * @throws ReportException 
    */
   public String generateReport() throws ReportException 
   {
      try
      {
         // Renderiza el informe
         this.render();

         // Transforma el informe a PDF
         return convertXhtmlToPdf(getWorkspace(), getReport().getRenderedXhtml());
      }
      catch (Exception ex)
      {
         log.error("Error rendering report '" + getReport().getId() + "': " + ex.getMessage(), ex);
         throw new ReportException(ex.getMessage(), ex);
      }
   }

   /**
    * Imprime una página generada mediante UI Services.
    * 
    * @param page Una instancia de {@link Page} que representa la página actual.
    * @param pc Una instancia de {@link PageContext} que representa el contexto de la página actual.
    * 
    * @throws ReportException
    */
   public static String printPage(Page page, PageContext pc) throws ReportException
   {
      try
      {
         StringBuilder sb = page.render(pc);
         return convertXhtmlToPdf(page.getWorkspace(), sb.toString());
      } 
      catch (DocumentException ex)
      {
         throw new ReportException("[DocumentException] " + ex.getMessage(), ex);
      } 
      catch (IOException ex)
      {
         throw new ReportException("[IOException] " + ex.getMessage(), ex);
      }
   }

   /**
    * Convierte un código XHTML a PDF.
    * 
    * @see <a href="http://hmkcode.com/itext-html-to-pdf-using-java/">http://hmkcode.com/itext-html-to-pdf-using-java/</a>
    * 
    * @throws DocumentException
    * @throws IOException
    */
   
   
   /**
    * Convierte un código XHTML a PDF.
    * 
    * @param workspace Una instancia de {@link Workspace} que representa el espacio de trabajo actual.
    * @param xhtml Una cadena XHTML que representa el informe renderizado.
    * 
    * @return Una cadena que representa la URL de acceso al informe generado.
    * 
    * @throws DocumentException
    * @throws IOException
    */
   private static String convertXhtmlToPdf(Workspace workspace, String xhtml) throws DocumentException, IOException
   {
      // Genera el nombre del archivo
      String path = "/" + Report.PATH_REPORTS + "/" + "temp" + "/";
      String filename = UUID.randomUUID().toString() + ".pdf";
      String fileNamePath = workspace.getServerContext().getRealPath(path + filename);

      // Asegura la existencia de la carpeta
      IOUtils.ensurePathExists(workspace.getServerContext().getRealPath(File.separator), path);

      // Convierte el código XHTML a PDF
      Document document = new Document();
      InputStream stream = new ByteArrayInputStream(xhtml.getBytes());

      PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(fileNamePath));
      document.open();
      XMLWorkerHelper.getInstance().parseXHtml(writer, document, stream);
      document.close();

      // Construye la URL de acceso al informe PDF generado
      URL url = new URL(workspace.getUrl());
      url.addFolderOrFile("reports");
      url.addFolderOrFile("temp");
      url.addFolderOrFile(filename);

      return url.build();
   }

}
