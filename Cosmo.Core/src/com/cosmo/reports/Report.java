package com.cosmo.reports;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.cosmo.Workspace;
import com.cosmo.data.DataQuery;
import com.cosmo.logging.LogFactory;
import com.cosmo.ui.templates.TemplateLoadException;
import com.cosmo.util.XmlUtils;

/**
 * Representa una plantilla de informe e implementa las funcionalidades básicas de carga.
 * 
 * @author Gerard Llort
 */
public class Report
{
   public static final String PATH_REPORTS = "reports";
   public static final String FILENAME_REPORT = "report.xml";

   private static final String XML_DEFINITION_VERSION = "1.0";

   private static final String XML_NODE_REPORT = "report";
   private static final String XML_NODE_NAME = "name";
   private static final String XML_NODE_AUTHOR = "author";
   private static final String XML_NODE_VERSION = "version";
   private static final String XML_NODE_COPYRIGHT = "copyright";
   private static final String XML_NODE_HEADER = "header";
   private static final String XML_NODE_FOOTER = "footer";
   private static final String XML_NODE_DETAIL = "detail";
   private static final String XML_NODE_DETAILHEADER = "detail-header";
   private static final String XML_NODE_DETAILROW = "detail-row";
   private static final String XML_NODE_DETAILFOOTER = "detail-footer";
   private static final String XML_NODE_STATICVALUES = "static-values";

   private static final String XML_ATT_DEFINITIONVER = "cdt-ver";
   private static final String XML_ATT_ID = "id";
   private static final String XML_ATT_DATAQUERY = "dataquery";

   private boolean generated;
   private String id;
   private String name;
   private String author;
   private String version;
   private String copyright;
   private String description;
   private String header;
   private ArrayList<ReportDetailGroup> groups;
   private String footer;
   private HashMap<String, String> staticValues;
   private HashMap<String, DataQuery> dataQueries;
   private String url;
   private String renderedXhtml;

   Logger log = LogFactory.getLogger("Reporting Services");


   //==============================================
   // Constructors
   //==============================================

   /**
    * Constructor de la clase {@link Report}.
    */
   public Report(Workspace workspace)
   {
      initialize();
   }

   /**
    * Constructor de la clase {@link Report}.
    * 
    * @param workspace Una instancia de {@link Workspace} que representa el workspace actual.
    * @param templateId Identificador del informe que se desea cargar.
    * 
    * @throws ReportException 
    */
   public Report(Workspace workspace, String templateId) throws ReportException
   {
      initialize();

      // Carga el informe
      loadTemplate(workspace, templateId);
   }


   //==============================================
   // Properties
   //==============================================

   public boolean isAvailable()
   {
      return this.generated;
   }

   public String getId()
   {
      return id;
   }

   public void setId(String id)
   {
      this.id = id;
   }

   public String getName()
   {
      return name;
   }

   public void setName(String name)
   {
      this.name = name;
   }

   public String getAuthor()
   {
      return author;
   }
   
   public void setAuthor(String author)
   {
      this.author = author;
   }

   public String getVersion()
   {
      return version;
   }

   public void setVersion(String version)
   {
      this.version = version;
   }

   public String getCopyright()
   {
      return copyright;
   }

   public void setCopyright(String copyright)
   {
      this.copyright = copyright;
   }

   public String getDescription()
   {
      return description;
   }

   public void setDescription(String description)
   {
      this.description = description;
   }

   public String getHeader()
   {
      return header;
   }

   public void setHeader(String header)
   {
      this.header = header;
   }

   public String getFooter()
   {
      return footer;
   }

   public void setFooter(String footer)
   {
      this.footer = footer;
   }

   public List<ReportDetailGroup> getDetailGroups()
   {
      return this.groups;
   }

   public String getRenderedXhtml()
   {
      return renderedXhtml;
   }

   public void setRenderedXhtml(String renderedXhtml)
   {
      this.renderedXhtml = renderedXhtml;
   }

   public String getUrl()
   {
      return url;
   }

   public void setUrl(String url)
   {
      this.url = url;
      this.generated = true;
   }


   //==============================================
   // Methods
   //==============================================

   public void addDataQuery(DataQuery query)
   {
      this.dataQueries.put(query.getId(), query);
   }

   public Collection<DataQuery> getDataQueries()
   {
      return this.dataQueries.values();
   }
   
   public DataQuery getDataQuery(String name)
   {
      return this.dataQueries.get(name);
   }

   public void addStaticValue(String key, String value)
   {
      this.staticValues.put(key, value);
   }

   public String getStaticValue(String key)
   {
      String value = this.staticValues.get(key);
      
      return (value == null ? "[unknown value]" : value);
   }

   /**
    * Carga un informe a partir de una plantilla definida en XML.
    * 
    * @param workspace Una instancia de {@link Workspace} que representa el workspace actual.
    * @param templateId Una cadena que contiene el identificador de la plantilla que se desea cargar.
    * 
    * @throws ReportException
    */
   public void loadTemplate(Workspace workspace, String templateId) throws ReportException
   {
      Node nNode;
      NodeList nList;
      Element eElement;
      FileInputStream is;
      ReportDetailGroup rptDet;

      this.id = templateId;

      log.debug("Loading report template '" + templateId + "'...");

      try
      {
         String filename = "/" + Report.PATH_REPORTS + "/" + templateId + "/" + Report.FILENAME_REPORT;
         filename = workspace.getServerContext().getRealPath(filename);

         is = new FileInputStream(filename);

         DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
         DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
         Document doc = dBuilder.parse(is);
         doc.getDocumentElement().normalize();

         // Comprueba que el archivo sea el correcto y la versión compatible
         nList = doc.getElementsByTagName(Report.XML_NODE_REPORT);
         nNode = nList.item(0);
         if (nNode.getNodeType() == Node.ELEMENT_NODE && nNode.getNodeName().equals(Report.XML_NODE_REPORT))
         {
            eElement = (Element) nNode;
            if (eElement.getAttribute(Report.XML_ATT_DEFINITIONVER).compareTo(Report.XML_DEFINITION_VERSION) < 0)
            {
               throw new TemplateLoadException("File defining report must be compatible with version " + Report.XML_DEFINITION_VERSION + " or lower");
            }
         }

         // Obtiene las propiedades del informe
         Element root = doc.getDocumentElement();
         this.name = XmlUtils.getTextValue(root, Report.XML_NODE_NAME);
         this.version = XmlUtils.getTextValue(root, Report.XML_NODE_VERSION);
         this.author = XmlUtils.getTextValue(root, Report.XML_NODE_AUTHOR);
         this.copyright = XmlUtils.getTextValue(root, Report.XML_NODE_COPYRIGHT);

         // Obtiene las definiciones de consulta (si las tuviése definidas)
         this.dataQueries = XmlUtils.readDataQueries(doc);

         // Obtiene los valores estáticos (si los tuviése definidos)
         this.staticValues = XmlUtils.readParameterMap(doc, Report.XML_NODE_STATICVALUES);

         // Obtiene la cabecera y el pie del informe
         this.header = XmlUtils.getTextValue(root, Report.XML_NODE_HEADER);
         this.footer = XmlUtils.getTextValue(root, Report.XML_NODE_FOOTER);

         // Obtiene los grupos de detalle
         nList = doc.getElementsByTagName(Report.XML_NODE_DETAIL);
         for (int temp = 0; temp < nList.getLength(); temp++) 
         {
            nNode = nList.item(temp);
            if (nNode.getNodeType() == Node.ELEMENT_NODE)
            {
               eElement = (Element) nNode;

               rptDet = new ReportDetailGroup();
               rptDet.setId(eElement.getAttribute(Report.XML_ATT_ID));
               rptDet.setDataQueryId(eElement.getAttribute(Report.XML_ATT_DATAQUERY));
               rptDet.setHeader(XmlUtils.getTextValue(eElement, Report.XML_NODE_DETAILHEADER));
               rptDet.setDetail(XmlUtils.getTextValue(eElement, Report.XML_NODE_DETAILROW));
               rptDet.setFooter(XmlUtils.getTextValue(eElement, Report.XML_NODE_DETAILFOOTER));
               this.groups.add(rptDet);
            }
         }
      }
      catch (Exception ex)
      {
         throw new ReportException(ex.getMessage(), ex);
      }
   }


   //==============================================
   // Private Members
   //==============================================

   /**
    * Inicializa la instancia.
    */
   private void initialize()
   {
      this.generated = false;
      this.id = "";
      this.name = "";
      this.author = "";
      this.version = "";
      this.copyright = "";
      this.description = "";
      this.header = "";
      this.groups = new ArrayList<ReportDetailGroup>();
      this.footer = "";
      this.staticValues = new HashMap<String, String>();
      this.dataQueries = new HashMap<String, DataQuery>();
      this.url = "";
      this.renderedXhtml = "";
   }
}
