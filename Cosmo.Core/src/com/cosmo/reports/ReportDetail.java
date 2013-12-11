package com.cosmo.reports;

public class ReportDetail
{
   private String id;
   private String header;
   private String detail;
   private String footer;
   
   public String getId()
   {
      return id;
   }

   public void setId(String id)
   {
      this.id = id;
   }

   public String getHeader()
   {
      return header;
   }

   public void setHeader(String header)
   {
      this.header = header;
   }

   public String getDetail()
   {
      return detail;
   }

   public void setDetail(String detail)
   {
      this.detail = detail;
   }

   public String getFooter()
   {
      return footer;
   }

   public void setFooter(String footer)
   {
      this.footer = footer;
   }
}
