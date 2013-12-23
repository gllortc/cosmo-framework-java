package com.cosmo.ui.widgets;

/**
 * Representa un elemento insertable en una área de banners.
 *
 * @author Gerard Llort
 */
public class BannerAreaItem 
{
   private String id;
   private String title;
   private String src;
   private String href;


   //==============================================
   // Constructor
   //==============================================

   /**
    * Constructor de la clase {@link BannerAreaItem}.
    */
   public BannerAreaItem()
   {
      this.id = "";
      this.title = "";
      this.src = "";
      this.href = "";
   }

   /**
    * Constructor de la clase {@link BannerAreaItem}.
    * 
    * @param id Identificador único del elemento.
    * @param title Título del banner.
    * @param src Nombre (y ruta) del recurso.
    * @param href URL de destino para el enlace asociado.
    */
   public BannerAreaItem(String id, String title, String src, String href)
   {
      this.id = id;
      this.title = "";
      this.src = "";
      this.href = "";
   }


   //==============================================
   // Properties
   //==============================================

   public String getId() 
   {
      return id;
   }

   public void setId(String id) 
   {
      this.id = id;
   }
   
   public String getTitle() 
   {
      return title;
   }

   public void setTitle(String title) 
   {
      this.title = title;
   }

   public String getSrc() 
   {
      return src;
   }

   public void setSrc(String src) 
   {
      this.src = src;
   }

   public String getHref() 
   {
      return href;
   }

   public void setHref(String href) 
   {
      this.href = href;
   }


   //==============================================
   // Methods
   //==============================================

   public String render()
   {
      StringBuilder sb = new StringBuilder();

      sb.append("<a href=\"").append(this.href).append("\" target=\"_blank\">");
      sb.append("<img src=\"").append(this.src).append("\" alt=\"").append(this.title).append("\" />");
      sb.append("</a>");
      sb.append("\n");

      return sb.toString();
   }
}
