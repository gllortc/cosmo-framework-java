package com.cosmo.ui.controls;

import javax.servlet.http.HttpSession;

import com.cosmo.structures.GridData;
import com.cosmo.ui.templates.Template;
import com.cosmo.ui.templates.TemplateControl;

/**
 * Implementa un grid.
 * 
 * @author Gerard Llort
 */
public class GridControl extends Control
{
   private static final String CONTROL_ID = "CosmoUiCtrlGrid";

   private static final String CPART_TITLE = "grid-title";
   private static final String CPART_BODY = "grid-body";
   private static final String CPART_ROWTITLE_BODY = "grid-rowtitle-body";
   private static final String CPART_ROWTITLE_CELL = "grid-rowtitle-cell";
   private static final String CPART_ROW_BODY = "grid-row-body";
   private static final String CPART_ROW_CELL = "grid-row-cell";
         
   private static final String TAG_TITLE = "FTITLE";
   private static final String TAG_DESCRIPTION = "FDESC";
   private static final String TAG_TITLE_ROW = "TITLEROW";
   private static final String TAG_DATA_ROWS = "DATAROWS";
   private static final String TAG_CELLS = "CELLS";
   private static final String TAG_VALUE = "VALUE";
   
   private String title;
   private String description;
   private boolean firstRowTitles;
   
   //==============================================
   // Contructors
   //==============================================
   
   /**
    * Contructor de la clase.
    */
   public GridControl(String id)
   {
      super(id);
      
      this.firstRowTitles = false;
      this.title = "";
      this.description = "";
   }
   
   //==============================================
   // Properties
   //==============================================

   @Override
   public String getControlTypeId() 
   {
      return GridControl.CONTROL_ID;
   }

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

   public boolean isFirstRowTitles() 
   {
      return firstRowTitles;
   }

   public void setFirstRowTitles(boolean firstRowTitles) 
   {
      this.firstRowTitles = firstRowTitles;
   }
   
   /**
    * Devuelve el número de columnas de la tabla.
    */
   /*public int getColumnCount() 
   {
      return table.getColumnCount();
   }*/

   /**
    * Devuelve el número de filas de la tabla.
    */
   /*public int getRowCount() 
   {
      return table.getRowCount();
   }*/
   
   //==============================================
   // Methods
   //==============================================

   /**
    * Establece el valor de una celda de la tabla.
    * 
    * @param row Índice de la fila (base 0).
    * @param col Índice de la columna (base 0).
    * @param value Valor a establecer.
    */
   /*public void setCell(int row, int col, Object value) throws GridDataLimitsException
   {
      table.setCell(row, col, value);
   }*/
   
   /**
    * Obtiene el contenido de una celda.
    * 
    * @param row Índice de la fila (base 0).
    * @param col Índice de la columna (base 0).
    * @return Un objeto que corresponde al contenido de la celda. En caso de ser una celda vacía, este método devolverá {@code null}.
    */
   /*public Object getCell(int row, int col)
   {
      return table.getCell(row, col);
   }*/
   
   /**
    * Establece los valores de una tabla a partir de una consulta a base de datos.
    * 
    * @param rs Una instancia de {@link ResultSet} que contiene los datos para la tabla.
    */
   /*public void setCells(ResultSet rs) throws SQLException
   {
      table.setCells(rs, true);
   }*/
   
   /**
    * Establece los datos del grid.
    * 
    * @param session
    * @param data
    */
   public void setData(HttpSession session, GridData data)
   {
      session.setAttribute(this.getSessionControlData(), data);
   }
   
   /**
    * Obtiene los datos de la tabla en función del usuario/caché.
    * 
    * @param session Una instancia de {@link HttpSession}.
    * @return Una instancia de {@link GridData}.
    */
   public GridData getData(HttpSession session)
   {
      GridData gd = (GridData) session.getAttribute(this.getSessionControlData());
      
      if (gd != null)
      {
         return gd;
      }
      else
      {
         return new GridData();
      }
   }
   
   /**
    * Vacía la tabla de todos sus datos.
    */
   /*public void clear()
   {
      table.clear();
   }*/
   
   @Override
   public String render(HttpSession session, Template template)
   {
      String xhtml = "";
      String xitem;
      String xrowtitle, xrow, xrowdata, xcell, xhead;
      TemplateControl ctrl;
      
      // Obtiene los datos de la tabla
      GridData data = this.getData(session);
      
      // Obtiene la plantilla y la parte del control
      ctrl = template.getControl(CONTROL_ID);

      // Genera la fila de títulos
      xrowdata = "";
      xcell = ctrl.getElement(CPART_ROWTITLE_CELL);
      for (int col = 0; col < data.getColumnCount(); col++)
      {
         xrowdata += Control.replaceTag(xcell, TAG_VALUE, data.getCell(0, col, "").toString());
      }
      xrowtitle = ctrl.getElement(CPART_ROWTITLE_BODY);
      xrowtitle = Control.replaceTag(xrowtitle, TAG_CELLS, xrowdata);

      // Genera las celdas de datos
      xrow = "";
      xcell = ctrl.getElement(CPART_ROW_CELL);
      for (int row = 1; row < data.getRowCount(); row++)
      {
         xrowdata = "";
         for (int col = 0; col < data.getColumnCount(); col++)
         {
            xrowdata += Control.replaceTag(xcell, TAG_VALUE, data.getCell(row, col, "").toString());
         }
         
         xhead = ctrl.getElement(CPART_ROW_BODY);
         xhead = Control.replaceTag(xhead, TAG_CELLS, xrowdata);
         
         xrow += xhead;
      }
      
      // Genera la cabecera del grid
      xitem = ctrl.getElement(CPART_TITLE);
      xitem = Control.replaceTag(xitem, TAG_TITLE, this.getTitle());
      xitem = Control.replaceTag(xitem, TAG_DESCRIPTION, this.getDescription());
      xhtml += xitem;

      // Forma la tabla
      xitem = ctrl.getElement(CPART_BODY);
      xitem = Control.replaceTag(xitem, TAG_TITLE_ROW, xrowtitle);
      xitem = Control.replaceTag(xitem, TAG_DATA_ROWS, xrow);
      xhtml += xitem;
      
      return xhtml;
   }
}
