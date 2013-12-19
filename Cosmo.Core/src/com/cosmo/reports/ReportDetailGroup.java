package com.cosmo.reports;

/**
 * Representa un grupo de detalle de un informe.
 * 
 * @author Gerard Llort
 */
public class ReportDetailGroup
{
   private String id;
   private String header;
   private String detail;
   private String footer;
   private String dataQueryId;


   //==============================================
   // Constructors
   //==============================================

   /**
    * Constructor de la clase {@link ReportDetailGroup}.
    */
   public ReportDetailGroup()
   {
      this.id          = "";
      this.header      = "";
      this.detail      = "";
      this.footer      = "";
      this.dataQueryId = "";
   }


   //==============================================
   // Properties
   //==============================================

   /**
    * Devuelve el identificador único del grupo dentro del informe.
    */
   public String getId()
   {
      return id;
   }

   /**
    * Establece el identificador único del grupo dentro del informe.
    */
   public void setId(String id)
   {
      this.id = id;
   }

   /**
    * Devuelve el identificador de la consulta ({@link DataQuery}) que proporciona los datos al grupo de detalle.
    */
   public String getDataQueryId()
   {
      return dataQueryId;
   }

   /**
    * Establece el identificador de la consulta ({@link DataQuery}) que proporciona los datos al grupo de detalle.
    */
   public void setDataQueryId(String dataQueryId)
   {
      this.dataQueryId = dataQueryId;
   }

   /**
    * Devuelve el código XHTML que forma la cabecera del grupo (puede contener <em>tags</em> {@code STATICVALUE} y {@code FIRSTROWVALUE}).
    */
   public String getHeader()
   {
      return header;
   }

   /**
    * Establece el código XHTML que forma la cabecera del grupo (puede contener <em>tags</em> {@code STATICVALUE} y {@code FIRSTROWVALUE}).
    */
   public void setHeader(String header)
   {
      this.header = header;
   }

   /**
    * Devuelve el código XHTML que forma el cuerpo de cada fila del grupo (puede contener <em>tags</em> {@code STATICVALUE}, {@code FIRSTROWVALUE} y {@code ROWVALUE}).
    */
   public String getDetail()
   {
      return detail;
   }

   /**
    * Establece el código XHTML que forma el cuerpo de cada fila del grupo (puede contener <em>tags</em> {@code STATICVALUE}, {@code FIRSTROWVALUE} y {@code ROWVALUE}).
    */
   public void setDetail(String detail)
   {
      this.detail = detail;
   }

   /**
    * Devuelve el código XHTML que forma el pie del grupo (puede contener <em>tags</em> {@code STATICVALUE} y {@code FIRSTROWVALUE}).
    */
   public String getFooter()
   {
      return footer;
   }

   /**
    * Establece el código XHTML que forma el pie del grupo (puede contener <em>tags</em> {@code STATICVALUE} y {@code FIRSTROWVALUE}).
    */
   public void setFooter(String footer)
   {
      this.footer = footer;
   }
}
