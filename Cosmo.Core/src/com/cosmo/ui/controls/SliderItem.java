package com.cosmo.ui.controls;

import com.cosmo.util.StringUtils;

/**
 * Implementa un slide que se puede incorporar a un {@link SliderControl}.
 * 
 * @author Gerard Llort
 */
public class SliderItem 
{
   // Declaración de variables internas
   private SlideType type;
   private String title;
   private String description;
   private String src;
   private int width;
   private int height;

   /**
    * Enumera el tipo de contenido del slide.
    */
   public enum SlideType
   {
      /** Slide basado en una imagen */
      Image,
      /** Slide basado en contenido XHTML */
      Xhtml,
      /** Slide basado en un video */
      Video
   }


   //==============================================
   // Constructors
   //==============================================

   /**
    * Constructor de la clase {@link SliderItem}.
    */
   public SliderItem()
   {
      this.type = SlideType.Xhtml;
      this.title = StringUtils.EMPTY;
      this.description = StringUtils.EMPTY;
      this.src = StringUtils.EMPTY;
      this.width = 0;
      this.height = 0;
   }

   /**
    * Constructor de la clase {@link SliderItem}.
    * 
    * @param src URL (para videos e imágenes) o código XHTML para los slides con contenido web.
    * @param type Tipo de elemento de <em>slider</em>.
    */
   public SliderItem(String src, SlideType type)
   {
      this.type = type;
      this.title = StringUtils.EMPTY;
      this.description = StringUtils.EMPTY;
      this.src = src;
      this.width = 0;
      this.height = 0;
   }

   /**
    * Constructor de la clase {@link SliderItem}.
    * 
    * @param src URL (para videos e imágenes) o código XHTML para los slides con contenido web.
    * @param width Un número que indica el ancho (en píxels) de la imagen o video.
    * @param height Un número que indica la altura (en píxels) de la imagen o video.
    * @param type Tipo de elemento de <em>slider</em>.
    */
   public SliderItem(String src, int width, int height, SlideType type)
   {
      this.type = type;
      this.title = StringUtils.EMPTY;
      this.description = StringUtils.EMPTY;
      this.src = src;
      this.width = width;
      this.height = height;
   }

   /**
    * Constructor de la clase {@link SliderItem}.
    * 
    * @param src URL (para videos e imágenes) o código XHTML para los slides con contenido web.
    * @param width Un número que indica el ancho (en píxels) de la imagen o video.
    * @param height Un número que indica la altura (en píxels) de la imagen o video.
    * @param description Un texto descriptivo que acompañará el <em>slider</em> (p. ej. pie de foto o video).
    * @param type Tipo de elemento de <em>slider</em>.
    */
   public SliderItem(String src, int width, int height, String title, String description, SlideType type)
   {
      this.type = type;
      this.title = title;
      this.description = description;
      this.src = src;
      this.width = width;
      this.height = height;
   }


   //==============================================
   // Properties
   //==============================================

   public String getTitle()
   {
      return title;
   }

   public void setTitle(String title) 
   {
      this.title = title;
   }
   
   public String getDescription() 
   {
      return description;
   }

   public void setDescription(String description) 
   {
      this.description = description;
   }

   public String getSrc() 
   {
      return src;
   }

   public void setSrc(String src) 
   {
      this.src = src;
   }

   public int getWidth() 
   {
      return width;
   }

   public void setWidth(int width) 
   {
      this.width = width;
   }

   public int getHeight() 
   {
      return height;
   }

   public void setHeight(int height) 
   {
      this.height = height;
   }

   public SlideType getType() 
   {
      return type;
   }

   public void setType(SlideType type) 
   {
      this.type = type;
   }


   //==============================================
   // Methods
   //==============================================

   /**
    * Convierte la instancia en una cadena en formato XHTML a partir de la estructura de la plantilla.
    */
   public String render()
   {
      String xhtml = StringUtils.EMPTY;

      switch (this.type)
      {
         case Image:
            xhtml = "<img src=\"" + this.src + "\" width=\"" + this.width + "\" height=\"" + this.height + "\" />";
            break;
         case Video:
            break;
         case Xhtml:
            xhtml = this.src;
            break;
      }

      return xhtml;
   }
}
