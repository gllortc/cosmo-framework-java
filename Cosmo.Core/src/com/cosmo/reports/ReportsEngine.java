package com.cosmo.reports;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;

import org.apache.log4j.Logger;

import com.cosmo.Workspace;
import com.cosmo.data.DataAgent;
import com.cosmo.data.DataException;
import com.cosmo.data.DataQuery;
import com.cosmo.logging.LogFactory;
import com.cosmo.net.URL;
import com.cosmo.ui.Page;
import com.cosmo.ui.PageContext;
import com.cosmo.util.IOUtils;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;

/**
 * Implementa el generador de informes de Cosmo Framework.
 * 
 * @author Gerard Llort
 */
public abstract class ReportsEngine
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
      STATICVALUE,
      /** Valor del workspace. */
      WORKSPACE
   }
   
   /**
    * Enumera los tipos de sección de un informe.
    */
   public enum ReportSection
   {
      /** Cabecera del informe. */
      HEADER,
      /** Cabecera de grupo de detalle. */
      DETAILGROUPHEADER,
      /** Fila de grupo de detalle. */
      DETAILGROUPROW,
      /** Pie de grupo de detalle. */
      DETAILGROUPFOOTER,
      /** Pie del informe. */
      FOOTER
   }

   // Variables internas
   private Workspace workspace;
   private Report report;

   Logger log = LogFactory.getLogger("Reporting Services");


   //==============================================
   // Constructors
   //==============================================

   /**
    * Constructor de la clase {@link ReportsEngine}.
    * 
    * @param workspace Una instancia de {@link Workspace} que representa el workspace actual.
    */
   public ReportsEngine(Workspace workspace)
   {
      initialize();

      this.workspace = workspace;
   }

   public ReportsEngine(Workspace workspace, String reportId) throws ReportException
   {
      initialize();

      this.workspace = workspace;

      // Carga la plantilla de informe
      loadReport(reportId);
   }


   //==============================================
   // Properties
   //==============================================

   /**
    * Devuelve la instancia de {@link Workspace} usada internamente.
    */
   public Workspace getWorkspace()
   {
      return workspace;
   }

   /**
    * Devuelve la instancia de {@link Report} que representa el informe cargado actualmente.
    */
   public Report getReport()
   {
      return report;
   }

   /**
    * Indica si hay un informe cargado en el generador de informes.
    */
   public boolean isReportLoaded()
   {
      return (this.report != null);
   }


   //==============================================
   // Methods
   //==============================================

   public abstract String generateReport() throws ReportException ;

   /**
    * Carga una plantilla de informe.
    * 
    * @param reportId Una cadena que contiene el identificador del informe.
    * 
    * @throws ReportException 
    */
   public void loadReport(String reportId) throws ReportException
   {
      this.report = new Report(this.workspace, reportId);
   }

   /**
    * Renderiza el informe cargado en el motor de informes.<br />
    * Una vez renderizado el informe este se encuentra accesible en la URL asociada al informe. 
    * 
    * @throws DataException 
    * @throws ReportException 
    */
   public void render() throws DataException, ReportException
   {
      HashMap<String, ResultSet> data;
      StringBuilder xhtml;

      // Inicializaciones
      xhtml = new StringBuilder();
      data = new HashMap<String, ResultSet>();

      log.debug("Start rendering report '" + getReport().getId() + "'...");

      try
      {
         // Ejecuta todas las consultas necesarias para el informe
         for (DataQuery dq : getReport().getDataQueries())
         {
            data.put(dq.getId(), dq.execute(getWorkspace()));
         }

         // Genera el HEADER
         xhtml.append(renderSection(getReport().getHeader(), ReportSection.HEADER, null));

         // Genera los GRUPOS DE DETALLE
         for (ReportDetailGroup group : getReport().getDetailGroups())
         {
            xhtml.append(renderDetailGroup(group));
         }

         // Genera el FOOTER
         xhtml.append(renderSection(getReport().getFooter(), ReportSection.FOOTER, null));

         // Almacena el código XHTML resultante
         getReport().setRenderedXhtml(xhtml.toString());

         /*
         // Genera el nombre del archivo
         String path = "/" + Report.PATH_REPORTS + "/" + "temp" + "/";
         String filename = UUID.randomUUID().toString() + ".pdf";
         String fileNamePath = getWorkspace().getServerContext().getRealPath(path + filename);

         // Asegura la existencia de la carpeta
         IOUtils.ensurePathExists(getWorkspace().getServerContext().getRealPath(File.separator), path);

         // Convierte el código XHTML a PDF
         Document document = new Document();
         InputStream stream = new ByteArrayInputStream(xhtml.toString().getBytes());

         PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(fileNamePath));
         document.open();
         XMLWorkerHelper.getInstance().parseXHtml(writer, document, stream);
         document.close();

         // Actualiza el informe on la URL del nuevo informe generado
         URL url = new URL(getWorkspace().getUrl());
         url.addFolderOrFile("reports");
         url.addFolderOrFile("temp");
         url.addFolderOrFile(filename);
         getReport().setUrl(url.build());
         */
      }
      catch (Exception ex)
      {
         log.error("Error rendering report '" + getReport().getId() + "': " + ex.getMessage(), ex);
         throw new ReportException(ex.getMessage(), ex);
      }

      log.debug("Report '" + getReport().getId() + "' renderized");
   }


   //==============================================
   // Private Members
   //==============================================

   /**
    * Renderiza una determinada sección del informe cargado actualmente.
    * 
    * @param text Texto que corresponde a la sección dentro de la plantilla del informe.
    * @param section Un valor de {@link ReportSection} que indica que tipo de sección se debe renderizar.
    * @param rs Una instancia de {@link ResultSet} que contiene los datos del grupo de detalle actual o {@code null}
    *   si la sección no corresponde a un grupo de detalle.
    *   
    * @return Una cadena que contiene el código XHTML correspondiente a la representación gráfica de la sección.
    * 
    * @throws ReportException
    * @throws DataException
    */
   private String renderSection(String text, ReportSection section, ResultSet rs) throws ReportException, DataException
   {
      int curPos = 0;
      String xhtml = text;
      ReportTag tag = null;

      do 
      {
         tag = getNextTag(xhtml, curPos);

         if (tag != null)
         {
            curPos = tag.getStartPosition() + 1;

            if (tag.getTagType() == ReporTagType.STATICVALUE)
            {
               xhtml = replaceTag(xhtml, tag, this.report.getStaticValue(tag.getValueName()));
            }
            else if (tag.getTagType() == ReporTagType.FIRSTROWVALUE)
            {
               xhtml = replaceTag(xhtml, tag, getFirstRowValue(tag));
            }
            else if (tag.getTagType() == ReporTagType.WORKSPACE)
            {
               xhtml = replaceTag(xhtml, tag, getWorkspaceValue(tag));
            }
            else if (section == ReportSection.DETAILGROUPROW && tag.getTagType() == ReporTagType.ROWVALUE)
            {
               xhtml = replaceTag(xhtml, tag, getRowValue(rs, tag));
            }
            else
            {
               throw new ReportException("Malformed report template (character " + curPos + "): Can't use ROWVALUE TAG outside a detail group");
            }
         }
      } 
      while (tag != null);

      return xhtml;
   }

   /**
    * Renderiza un determinado grupo de detalle del informe.
    * 
    * @param group Una instancia de {@link ReportDetailGroup} que representa el grupo de detalle a renderizar.
    * 
    * @return Una cadena que contiene el código XHTML que representa el grupo de detalle renderizado.
    * 
    * @throws ReportException
    * @throws DataException
    */
   private String renderDetailGroup(ReportDetailGroup group) throws ReportException, DataException
   {
      StringBuilder xhtml = new StringBuilder();
      ResultSet rs = null;

      xhtml.append(renderSection(group.getHeader(), ReportSection.DETAILGROUPHEADER, null));

      try
      {
         // Genera los GRUPOS DE DETALLE
         DataQuery query = this.report.getDataQuery(group.getDataQueryId());
         rs = query.execute(this.workspace);
         while (rs.next())
         {
            xhtml.append(renderSection(group.getDetail(), ReportSection.DETAILGROUPROW, rs));
         }
      }
      catch (SQLException ex)
      {
         throw new DataException(ex.getMessage(), ex);
      }
      finally 
      {
         DataAgent.closeResultSet(rs);
      }

      xhtml.append(renderSection(group.getFooter(), ReportSection.DETAILGROUPFOOTER, null));

      return xhtml.toString();
   }

   /**
    * Obtiene un valor de base de datos que se encuentra en la primera fila de una consulta.
    * 
    * @param tag Una instancia de {@link ReportTag} que representa el TAG a rellenar.
    * 
    * @return El valor a representar.
    * 
    * @throws DataException
    */
   private String getFirstRowValue(ReportTag tag) throws DataException
   {
      String value = null;
      
      try
      {
         ResultSet rs = this.report.getDataQuery(tag.getConnectionId()).getResultSet();
         if (!rs.next())
         {
            throw new DataException("The query has no results.");
         }

         value = rs.getString(tag.getValueName());
      }
      catch (SQLException ex)
      {
         value = null;
         // throw new DataException(ex.getMessage(), ex);
      }

      return (value == null ? "[unknown value]" : value);
   }

   /**
    * Obtiene un valor dependiente a una fila de un grupo de detalle.
    * 
    * @param rs Una instancia de {@link ResultSet} que contiene los datos. Debe estar correctamente posicionado en la fila adecuada.
    * @param tag Una instancia de {@link ReportTag} que representa el TAG a rellenar.
    * 
    * @return El valor a representar.
    * 
    * @throws DataException
    */
   private String getRowValue(ResultSet rs, ReportTag tag) throws DataException
   {
      String value = null;
      
      try
      {
         value = rs.getString(tag.getValueName());
      }
      catch (SQLException ex)
      {
         value = null;
         // throw new DataException(ex.getMessage(), ex);
      }

      return (value == null ? "[unknown value]" : value);
   }

   /**
    * Obtiene el valor de una TAG de workspace.
    * 
    * @param tag Una instancia de {@link ReportTag} que representa el TAG para el que se desea obtener el valor.
    * 
    * @return Una cadena de texto que contiene el valor a mostrar.
    * 
    * @throws DataException
    */
   private String getWorkspaceValue(ReportTag tag) throws DataException
   {
      if (tag.getValueName().equals("name"))
      {
         return this.workspace.getName();
      }
      else if (tag.getValueName().equals("mail"))
      {
         return this.workspace.getMail();
      }
      else if (tag.getValueName().equals("url"))
      {
         return this.workspace.getUrl();
      }
      else if (tag.getValueName().equals("property"))
      {
         return this.workspace.getProperties().getString(tag.getPropertyName());
      }

      return null;
   }

   private String replaceTag(String text, ReportTag tag, String value)
   {
      return text.replace(tag.getOriginalTag(), value);
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
    * Inicializa la instancia.
    */
   private void initialize()
   {
      this.workspace = null;
      this.report = null;
   }


   //==============================================
   // Internal Classes
   //==============================================

   /**
    * Representa un TAG encontrado en un informe.<br />
    * Los TAGs admitidos son los siguientes:
    * <ul>
    * <li>{@code ROWVALUE rsColumnName}</li>
    * <li>{@code WORKSPACE [ name | mail | url | property PropertyName ]}</li>
    * </ul>
    */
   private class ReportTag
   {
      private static final String CMD_ROWVALUE = "ROWVALUE";
      private static final String CMD_FIRSTROWVALUE = "FIRSTROWVALUE";
      private static final String CMD_STATICVALUE = "STATICVALUE";
      private static final String CMD_WORKSPACE = "WORKSPACE";

      private int startPosition;
      private String valueName;
      private String propertyName;
      private String originalTag;
      private String connectionId;
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
         this.connectionId = "";
         this.valueName = "";
         this.propertyName= ""; 

         getTagProperties();
      }

      public String getValueName()
      {
         return valueName;
      }

      public void setValueName(String valueName)
      {
         this.valueName = valueName;
      }

      public String getPropertyName() 
      {
         return propertyName;
      }

      public void setPropertyName(String propertyName) 
      {
         this.propertyName = propertyName;
      }

      public String getConnectionId()
      {
         return connectionId;
      }

      public void setConnectionId(String connectionId)
      {
         this.connectionId = connectionId;
      }

      public String getOriginalTag()
      {
         return originalTag;
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
            this.setConnectionId("");
            this.setValueName(params[1]);
         }
         else if (params[0].equals(ReportTag.CMD_FIRSTROWVALUE))
         {
            this.setTagType(ReporTagType.FIRSTROWVALUE);
            this.setConnectionId(params[1]);
            this.setValueName(params[2]);
         }
         else if (params[0].equals(ReportTag.CMD_ROWVALUE))
         {
            this.setTagType(ReporTagType.ROWVALUE);
            this.setValueName(params[1]);
         }
         else if (params[0].equals(ReportTag.CMD_WORKSPACE))
         {
            this.setTagType(ReporTagType.WORKSPACE);
            this.setConnectionId("");
            this.setValueName(params[1].toLowerCase().trim());

            // Si se trata de una propiedad de configuración, añade el parámetro
            if (this.getValueName().trim().toLowerCase().endsWith("property"))
            {
               this.setPropertyName(params[2].toLowerCase().trim());
            }
         }
         else
         {
            throw new ReportException("Malformed report template: Unknown TAG '" + params[0] + "'");
         }
      }
   }
}
