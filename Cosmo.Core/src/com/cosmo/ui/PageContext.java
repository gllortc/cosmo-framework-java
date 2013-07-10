package com.cosmo.ui;

import java.util.ArrayList;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import com.cosmo.Cosmo;
import com.cosmo.Workspace;
import com.cosmo.ui.controls.Control;
import com.cosmo.ui.controls.ErrorMessageControl;
import com.cosmo.ui.render.PageRenderer;

/**
 * Implementa un contenedor de datos de la página (contexto) específico para:
 * <ul>
 *   <li>Sessión ({@code HttpSession}, para cada usuario)</li>
 *   <li>Página</li>
 * </ul>
 * 
 * @author Gerard Llort
 */
public class PageContext 
{
   private boolean init = false;
   private String charset;
   private String title;
   private PageLayout layout;
   private ArrayList<Control> leftContents;
   private ArrayList<Control> centerContents;
   private ArrayList<Control> rightContents;
   private StringBuilder xhtml;
   private PageRenderer renderProvider;
   
   /**
    * Enumera las distintas regiones dónde se pueden agregar controles en la página.
    */
   public enum ContentColumns
   {
      LEFT,
      MAIN,
      RIGHT
   }
   
   /**
    * Enumera los distintos formatos de página.
    */
   public enum PageLayout
   {
      HomePage,
      OneColumn,
      TwoColumnsLeft,
      TwoColumnsRight,
      ThreeColumns
   }
   
   
   //==============================================
   // Constructors
   //==============================================
   
   /**
    * Constructor de la clase.
    */
   public PageContext()
   {
      clear();
   }

   
   //==============================================
   // Properties
   //==============================================
   
   public boolean isInit() 
   {
      return init;
   }

   public void setInit(boolean init) 
   {
      this.init = init;
   }

   public String getCharset() 
   {
      return charset;
   }

   public void setCharset(String charset) 
   {
      this.charset = charset;
   }

   public String getTitle() 
   {
      return title;
   }

   public void setTitle(String title) 
   {
      this.title = title;
   }

   public PageLayout getLayout() 
   {
      return layout;
   }

   public void setLayout(PageLayout layout) 
   {
      this.layout = layout;
   }

   public ArrayList<Control> getLeftContents()
   {
      return leftContents;
   }

   public void setLeftContents(ArrayList<Control> leftContents) 
   {
      this.leftContents = leftContents;
   }

   public ArrayList<Control> getCenterContents() 
   {
      return centerContents;
   }

   public void setCenterContents(ArrayList<Control> centerContents) 
   {
      this.centerContents = centerContents;
   }

   public ArrayList<Control> getRightContents() 
   {
      return rightContents;
   }

   public void setRightContents(ArrayList<Control> rightContents) 
   {
      this.rightContents = rightContents;
   }

   public StringBuilder getXhtml() 
   {
      return xhtml;
   }

   public void setXhtml(StringBuilder xhtml) 
   {
      this.xhtml = xhtml;
   }

   public PageRenderer getRenderProvider() 
   {
      return renderProvider;
   }

   public void setRenderProvider(PageRenderer renderProvider) 
   {
      this.renderProvider = renderProvider;
   }

   
   //==============================================
   // Methods
   //==============================================
   
   /**
    * Agrega un control a la página.
    * 
    * @param control Una instancia de {@link Control} que representa el control a añadir.
    * @param situation Posición en la que se debe agregar el control.
    */
   public void addContent(Control control, ContentColumns situation)
   {
      switch (situation)
      {
         case LEFT:  
            leftContents.add(control);
            break;
         case MAIN:  
            centerContents.add(control);  
            break;
         case RIGHT: 
            rightContents.add(control);   
            break;
      }
   }
   
   /**
    * Devuelve un iterador sobre el contenido de la página.
    * 
    * @param column Un valor de {@link ContentColumns} que indica la columna para la que se desea obtener el contenido.
    */
   public Iterator<Control> getPageContent(ContentColumns column)
   {
      switch (column)
      {
         case LEFT:
            return this.leftContents.iterator();
         case RIGHT:
            return this.rightContents.iterator();
         default:
            return this.centerContents.iterator();
      }
   }
   
   /**
    * Permite obtener un determinado control para poder modificar alguna de sus propiedades.
    * 
    * @param id Identificador del control.
    * @return Una instancia de {@link Control} que representa el control solicitado.
    */
   public Control getControl(String id)
   {
      for (Control control : this.centerContents)
      {
         if (control instanceof Control)
         {
            if (id.equals(((Control) control).getId()))
            {
               return control;
            }
         }
      }
      
      return null;
   }
   
   /**
    * Representa un error (excepción).
    * 
    * @param ex Una instancia de {@link Exception} que contiene los detalles del error.
    */
   public void showException(Workspace workspace, Exception ex)
   {
      // Representa un error
      this.layout = PageLayout.TwoColumnsLeft;
      this.centerContents.clear();
      this.centerContents.add(new ErrorMessageControl(workspace, ex));
   }
   
   /**
    * Limpia el contenido de la página.<br />
    * Equivale a dejar la página en blanco.
    */
   public void clear()
   {
      this.leftContents = new ArrayList<Control>();
      this.centerContents = new ArrayList<Control>();
      this.rightContents = new ArrayList<Control>();

      this.xhtml = new StringBuilder();
      this.renderProvider = null;
      this.title = "";
      this.charset = Cosmo.CHARSET_ISO_8859_1;
      this.layout = PageLayout.OneColumn;
   }
   
   
   //==============================================
   // Static members
   //==============================================
   
   /**
    * Devuelve el contexto actual de la página para la sesión actual.
    * 
    * @param request Una instancia de {@link HttpServletRequest} que representa el contexto de la llamada.
    * @param page Una instancia de {@link Page} que representa la página actual.
    * 
    * @return Una instancia de {@link PageContext} que representa el contexto.
    */
   public static PageContext getPageContext(HttpServletRequest request, Page page)
   {
      String id = getPageContextId(request, page);
      
      PageContext pc = (PageContext) request.getAttribute(id);
      if (pc == null)
      {
         return new PageContext();
      }
      else
      {
         return pc;
      }
   }

   /**
    * Obtiene un identificador único que depende de la sesión actual de servidor y de la página.
    * 
    * @param request Una instancia de {@link HttpServletRequest} que representa el contexto de la llamada.
    * @param page Una instancia de {@link Page} que representa la página actual.
    * 
    * @return Una cadena que es única para la página y la sesión.
    */
   public static String getPageContextId(HttpServletRequest request, Page page)
   {
      return request.getSession().getId() + "." + page.getServletName();
   }
}
