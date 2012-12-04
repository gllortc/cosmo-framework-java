package com.cosmo.ui.controls;

/**
 * Implementa un slide que se puede incorporar a un {@link SliderControl}.
 * 
 * @author Gerard Llort
 */
public class SliderItem 
{
   // variables privadas
   private SlideType type;
   private String src;
   private int width;
   private int height;
   
   /**
    * Enumera el tipo de contenido del slide.
    */
   public enum SlideType
   {
      Image,
      Xhtml,
      Video
   }
   
   //==============================================
   // Constructors
   //==============================================
   
   /**
    * Constructor de la clase.
    */
   public SliderItem()
   {
      this.type = SlideType.Xhtml;
      this.src = "";
      this.width = 0;
      this.height = 0;
   }
   
   /**
    * Constructor de la clase.
    * 
    * @param src URL (para videos e imágenes) o código XHTML para los slides con contenido web.
    */
   public SliderItem(String src, SlideType type)
   {
      this.type = type;
      this.src = src;
      this.width = 0;
      this.height = 0;
   }
   
   /**
    * Constructor de la clase.
    * 
    * @param src URL (para videos e imágenes) o código XHTML para los slides con contenido web.
    * @param width Un número que indica el ancho (en píxels) de la imagen o video.
    * @param height Un número que indica la altura (en píxels) de la imagen o video. 
    */
   public SliderItem(String src, int width, int height, SlideType type)
   {
      this.type = type;
      this.src = src;
      this.width = width;
      this.height = height;
   }

   //==============================================
   // Properties
   //==============================================

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
   
   public String render()
   {
      String xhtml = "";
      
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
