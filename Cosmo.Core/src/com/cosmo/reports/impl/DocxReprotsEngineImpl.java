package com.cosmo.reports.impl;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import javax.xml.bind.JAXBException;

import org.apache.log4j.Logger;
import org.docx4j.convert.in.xhtml.XHTMLImporterImpl;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.NumberingDefinitionsPart;

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
import com.itextpdf.text.DocumentException;

/**
 * Implementa un motor de informes que permite obtener informes en formato PDF.
 * 
 * @author Gerard Llort
 */
public class DocxReprotsEngineImpl extends ReportsEngine
{
   private static final String FILE_EXTENSION = "docx";

   // Habilita el LOG
   Logger log = LogFactory.getLogger("Reporting Services");

   public DocxReprotsEngineImpl(Workspace workspace, String reportId) throws ReportException
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
         return convertXhtmlToDocx(getWorkspace(), getReport().getRenderedXhtml());
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
         return convertXhtmlToDocx(page.getWorkspace(), sb.toString());
      } 
      catch (Docx4JException ex)
      {
         throw new ReportException("[Docx4JException] " + ex.getMessage(), ex);
      }
      catch (IOException ex)
      {
         throw new ReportException("[IOException] " + ex.getMessage(), ex);
      }
      catch (JAXBException ex)
      {
         throw new ReportException("[JAXBException] " + ex.getMessage(), ex);
      }
   }

   /**
    * Convierte un código XHTML a DOCX (Microsoft Word).
    * 
    * @see <a href="http://www.docx4java.org/trac/docx4j">http://www.docx4java.org/trac/docx4j</a>
    * 
    * @param workspace Una instancia de {@link Workspace} que representa el espacio de trabajo actual.
    * @param xhtml Una cadena XHTML que representa el informe renderizado.
    * 
    * @return Una cadena que representa la URL de acceso al informe generado.
    * 
    * @throws DocumentException
    * @throws IOException
    * @throws JAXBException
    * @throws Docx4JException
    */
   private static String convertXhtmlToDocx(Workspace workspace, String xhtml) throws IOException, JAXBException, Docx4JException
   {
      // Genera el nombre del archivo
      String path = "/" + Report.PATH_REPORTS + "/" + "temp" + "/";
      String filename = UUID.randomUUID().toString() + "." + FILE_EXTENSION;
      String fileNamePath = workspace.getServerContext().getRealPath(path + filename);

      // Asegura la existencia de la carpeta
      IOUtils.ensurePathExists(workspace.getServerContext().getRealPath(File.separator), path);

      // Convierte el código XHTML a DOCX
      WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.createPackage();

      NumberingDefinitionsPart ndp = new NumberingDefinitionsPart();
      wordMLPackage.getMainDocumentPart().addTargetPart(ndp);
      ndp.unmarshalDefaultNumbering(); 

      XHTMLImporterImpl xHTMLImporter = new XHTMLImporterImpl(wordMLPackage);
      xHTMLImporter.setHyperlinkStyle("Hyperlink");

      InputStream stream = new ByteArrayInputStream(xhtml.getBytes());
      wordMLPackage.getMainDocumentPart().getContent().addAll(xHTMLImporter.convert(stream, null));

      wordMLPackage.save(new File(fileNamePath));

      // Construye la URL de acceso al informe PDF generado
      URL url = new URL(workspace.getUrl());
      url.addFolderOrFile("reports");
      url.addFolderOrFile("temp");
      url.addFolderOrFile(filename);

      return url.build();
   }
}
