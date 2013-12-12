package com.cosmo.reports;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
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
   /**
    * Enumera los tipos de TAG que pueden contener los informes para indicar valores.
    */
   public enum ReporTagType 
   {
      /** Valor de la primera fila de una consulta. */
      FIRSTROWVALUE,
      /** Valor de la fila actual (en un grupo de detalle) de una consulta. */
      ROWVALUE,
      /** Valor estático. */
      STATICVALUE
   }


   //==============================================
   // Constructors
   //==============================================


   //==============================================
   // Properties
   //==============================================


   //==============================================
   // Methods
   //==============================================

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
      HashMap<String, ResultSet> data;
      StringBuilder xhtml;

      // Inicializaciones
      xhtml = new StringBuilder();
      data = new HashMap<String, ResultSet>();

      try
      {
         // Ejecuta todas las consultas necesarias para el informe
         for (DataQuery dq : report.getDataQueries())
         {
            data.put(dq.getId(), dq.execute(workspace));
         }

         // Genera el HEADER
         xhtml.append(renderHeader(report));

         // Genera los GRUPOS DE DETALLE
         
         // Genera el FOOTER
      }
      catch (Exception ex)
      {
      }
      finally
      {
      }
   }

   /**
    * Imprime una página generada mediante UI Services.
    * 
    * @param page
    * @param pc
    * 
    * @throws ReportException
    */
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


   //==============================================
   // Private Members
   //==============================================

   private String renderHeader(Report report) throws ReportException
   {
      int curPos = 0;
      String xhtml = report.getHeader();
      ReportTag rt = null;

      do 
      {
         rt = getNextTag(xhtml, curPos);
         curPos = rt.getStartPosition() + 1;

         switch (rt.getTagType())
         {
            case STATICVALUE:
               break;

            case FIRSTROWVALUE:
               break;

            case ROWVALUE:
               break;

            default:
               throw new ReportException("Malformed report template: Unknown TAG type '" + rt.getTagType() + "'");
         }
      } 
      while (rt != null);

      return xhtml;
   }

   /**
    * Obtiene el siguiente TAG a partir de una determinada posición.
    * 
    * @param text Texto que contiene los TAGs.
    * @param startPos Índice del primer carácter a partir del que se debe buscar.
    * 
    * @return Una instancia de {@link ReportTag} que describe el TAG encontrado.
    * 
    * @throws ReportException 
    */
   private ReportTag getNextTag(String text, int startPos) throws ReportException
   {
      String chr;
      String tag;

      int idxStart = text.indexOf("[@", startPos);

      // Si no encuentra ningún TAG termina
      if (idxStart < 0)
      {
         return null;
      }

      // Busca el cierre del TAG
      tag = "";
      for (int i = idxStart; i < text.length(); i++)
      {
         chr = "" + text.charAt(i);
         tag += chr;
         if (chr.equals("]"))
         {
            return new ReportTag(tag, idxStart);
         }
      }

      // Detectado un TAG no cerrado
      throw new ReportException("Malformed report template: unclosed TAG.");
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


   //==============================================
   // Internal Classes
   //==============================================

   /**
    * Representa un TAG encontrado en un informe.
    */
   private class ReportTag
   {
      private static final String CMD_ROWVALUE = "ROWVALUE";
      private static final String CMD_FIRSTROWVALUE = "FIRSTROWVALUE";
      private static final String CMD_STATICVALUE = "STATICVALUE";

      private int startPosition;
      private String originalTag;
      private ReporTagType tagType;
      
      /**
       * Constructor de la clase {@link ReportTag}.
       * 
       * @param tag Una cadena que contiene el TAG leido desde el informe.
       * @param position Índice del primer carácter del TAG.
       * 
       * @throws ReportException 
       */
      public ReportTag(String tag, int position) throws ReportException
      {
         setStartPosition(position);
         this.originalTag = tag;

         getTagProperties();
      }

      public int getStartPosition()
      {
         return startPosition;
      }

      public void setStartPosition(int startPosition)
      {
         this.startPosition = startPosition;
      }

      public ReporTagType getTagType()
      {
         return tagType;
      }

      public void setTagType(ReporTagType tagType)
      {
         this.tagType = tagType;
      }

      /**
       * Analiza el TAG y obtiene sus propiedades.
       */
      private void getTagProperties() throws ReportException
      {
         String[] params = this.originalTag.replace("[@", "").replace("]", "") .split(" ");

         if (params[0].equals(ReportTag.CMD_STATICVALUE))
         {
            this.setTagType(ReporTagType.STATICVALUE);
         }
         else if (params[0].equals(ReportTag.CMD_FIRSTROWVALUE))
         {
            this.setTagType(ReporTagType.FIRSTROWVALUE);
         }
         else if (params[0].equals(ReportTag.CMD_ROWVALUE))
         {
            this.setTagType(ReporTagType.ROWVALUE);
         }
         else
         {
            throw new ReportException("Malformed report template: Unknown TAG '" + params[0] + "'");
         }
      }
   }
}
