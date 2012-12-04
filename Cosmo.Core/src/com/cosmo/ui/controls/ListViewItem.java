package com.cosmo.ui.controls;

import com.cosmo.ui.templates.TemplateControl;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Implementa un elemento para las listas de objetos.
 * 
 * @author Gerard llort
 */
public class ListViewItem 
{
   private static final String CPART_ITEM = "object-list-item";
   
   private static final String TAG_CAPTION = "CAPTION";
   private static final String TAG_DESCRIPTION = "DESCRIPTION";
   private static final String TAG_HREF = "HREF";
   private static final String TAG_THUMB = "THUMB";
   private static final String TAG_DATE = "DATE";
   private static final String TAG_AUTHOR = "AUTHOR";
   private static final String TAG_WIDTH = "WIDTH";
   private static final String TAG_HEIGHT = "HEIGHT";
   
   private String caption;
   private String description;
   private String href;
   private String thumbSrc;
   private String author;
   private Date date;
   private Integer width;
   private Integer height;
   
   //==============================================
   // Contructors
   //==============================================
   
   /**
    * Contructor de la clase.
    */
   public ListViewItem()
   {
      initialize();
   }
   
   /**
    * Contructor de la clase.
    * 
    * @param caption
    * @param description
    * @param href 
    */
   public ListViewItem(String caption, String description, String href, String thumbSrc, Integer width, Integer height)
   {
      initialize();
      
      this.caption = caption;
      this.description = description;
      this.href = href;
      this.thumbSrc = thumbSrc;
      this.width = width;
      this.height = height;
   }
   
   /**
    * Contructor de la clase.
    * 
    * @param caption
    * @param description
    * @param href
    * @param author
    * @param date 
    */
   public ListViewItem(String caption, String description, String href, String thumbSrc, Integer width, Integer height, String author, Date date)
   {
      initialize();
      
      this.caption = caption;
      this.description = description;
      this.href = href;
      this.thumbSrc = thumbSrc;
      this.width = width;
      this.height = height;
      this.author = author;
      this.date = date;
   }

   //==============================================
   // Properties
   //==============================================
   
   public String getCaption()
   {
      return caption;
   }

   public void setCaption(String caption)
   {
      this.caption = caption;
   }

   public String getDescription()
   {
      return description;
   }

   public void setDescription(String description) 
   {
      this.description = description;
   }

   public String getHref()
   {
      return href;
   }

   public void setHref(String href) 
   {
      this.href = href;
   }

   public String getThumbSrc() 
   {
      return thumbSrc;
   }

   public void setThumbSrc(String thumbSrc) 
   {
      this.thumbSrc = thumbSrc;
   }

   public Integer getWidth()
   {
      return width;
   }

   public void setWidth(Integer width) 
   {
      this.width = width;
   }

   public Integer getHeight() 
   {
      return height;
   }

   public void setHeight(Integer height) 
   {
      this.height = height;
   }
   
   public String getAuthor()
   {
      return author;
   }

   public void setAuthor(String author)
   {
      this.author = author;
   }

   public Date getDate()
   {
      return date;
   }

   public void setDate(Date date)
   {
      this.date = date;
   }
   
   //==============================================
   // Methods
   //==============================================
   
   /**
    * Convierte la instancia en una cadena en formato XHTML a partir de la estructura de la plantilla.
    */
   public String render(TemplateControl tc)
   {
      String xhtml;
      SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
      
      xhtml = tc.getElement(CPART_ITEM);
      xhtml = Control.replaceTag(xhtml, TAG_CAPTION, this.caption);
      xhtml = Control.replaceTag(xhtml, TAG_DESCRIPTION, this.description);
      xhtml = Control.replaceTag(xhtml, TAG_HREF, this.href);
      xhtml = Control.replaceTag(xhtml, TAG_THUMB, this.thumbSrc);
      xhtml = Control.replaceTag(xhtml, TAG_WIDTH, this.width.toString());
      xhtml = Control.replaceTag(xhtml, TAG_HEIGHT, this.height.toString());
      xhtml = Control.replaceTag(xhtml, TAG_DATE, sdf.format(this.date));
      xhtml = Control.replaceTag(xhtml, TAG_AUTHOR, this.author);
      
      return xhtml;
   }
   
   //==============================================
   // Private members
   //==============================================
   
   /**
    * Inicializa la instancia.
    */
   private void initialize()
   {
      this.caption = "";
      this.description = "";
      this.href = "";
      this.thumbSrc = "";
      this.width = 0;
      this.height = 0;
      this.author = "";
      this.date = null;
   }
}
