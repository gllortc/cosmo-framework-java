package com.cosmo.reports;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.util.HashMap;

import com.cosmo.Workspace;
import com.cosmo.data.DataException;
import com.cosmo.data.DataQuery;
import com.cosmo.ui.Page;
import com.cosmo.ui.PageContext;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;

public class ReportsEngine
{
   private static final String CMD_FOREACHROW = "FOREACHROW";
   private static final String CMD_ROWVAL = "ROWVAL";
   private static final String CMD_FIRSTROWVAL = "FIRSTROWVAL";
   private static final String CMD_STATICVAL = "STATICVAL";

   private static final String PARAM_BEGIN = "BEGIN";
   private static final String PARAM_END = "END";
   
   /**
    * Renderiza un informe.
    * 
    * @param workspace Una instancia de {@link Workspace} que representa el workspace actual.
    * @param report Una instancia de {@link Report} que contiene todos los datos necesarios para elaborar el informe.
    * 
    * @throws DataException 
    */
   public void render(Workspace workspace, Report report) throws DataException
   {
      boolean isTag;
      String chr;
      String line;
      String tag;
      StringBuilder template;
      StringBuilder xhtml;
      BufferedReader br = null;
      HashMap<String, ResultSet> data;

      // Inicializaciones
      template = new StringBuilder();
      xhtml = new StringBuilder();
      data = new HashMap<String, ResultSet>();

      try
      {
         // Ejecuta todas las consultas necesarias para el informe
         for (DataQuery dq : report.getDataQueries())
         {
            data.put(dq.getName(), dq.execute(workspace));
         }

         // Obtiene el código del archivo
         template = readTemplate("file.txt");

         // Empieza el análisis carácter a carácter
         isTag = false;
         tag = "";
         
      }
      catch (Exception ex)
      {

      }
      finally
      {
         try
         {
            br.close();
         }
         catch (IOException e)
         {
            // Nothing to do
         }
      }
   }

   private StringBuilder readTemplate(String templateFileName) throws IOException
   {
      String line;
      StringBuilder template = new StringBuilder();
      BufferedReader br;

      // Obtiene el código del archivo
      br = new BufferedReader(new FileReader(templateFileName));
      line = br.readLine();
      while (line != null)
      {
         template.append(line);
         template.append("\n");
         line = br.readLine();
      }
      br.close();

      return template;
   }

   private void extractTags(StringBuilder text)
   {
      boolean isTag;
      String tag;
      String chr;

      isTag = false;
      tag = "";
      for (int idx = 0; idx < text.length(); idx++)
      {
         chr = text.substring(idx, 1);

         if (isTag)
         {
            if (!chr.equals("]"))
            {
               tag = tag + chr;
            }
            else
            {
               isTag = false;
               tag = "";
            }
         }
         else if (chr.equals("[") && !isTag)
         {
            if (text.substring(idx + 1, 1).equals("@"))
            {
               isTag = true;
            }
         }
      }
   }

   public static void printPage(Page page, PageContext pc) throws ReportException
   {
      try
      {
         StringBuilder sb = page.render(pc);
         convertXhtmlToPdf(sb.toString());
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
   private static void convertXhtmlToPdf(String xhtml) throws DocumentException, IOException
   {
       Document document = new Document();
       InputStream stream = new ByteArrayInputStream(xhtml.getBytes());
       
       PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("pdf.pdf"));
       document.open();
       XMLWorkerHelper.getInstance().parseXHtml(writer, document, stream);
       document.close();

       System.out.println( "PDF Created!" );
   }
}
