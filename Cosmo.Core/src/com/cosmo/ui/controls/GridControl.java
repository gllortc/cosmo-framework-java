package com.cosmo.ui.controls;

import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import com.cosmo.Workspace;
import com.cosmo.data.DataException;
import com.cosmo.data.orm.InvalidMappingException;
import com.cosmo.data.orm.OrmFactory;
import com.cosmo.data.orm.annotations.CormObjectField;
import com.cosmo.structures.GridData;
import com.cosmo.ui.templates.TemplateControl;

/**
 * Implementa un control grid para mostrar datos en forma de tabla.
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
   private String rowActionsCaption;
   private ArrayList<GridRowAction> rowActions;
   private ArrayList<Integer> rowIds;
   private GridData gridData;
   
   //==============================================
   // Contructors
   //==============================================
   
   /**
    * Contructor de la clase.
    * 
    * @param workspace Una instancia de {@link Workspace} que representa el workspace actual.
    * @param id Identificador único del control.
    */
   public GridControl(Workspace workspace, String id)
   {
      super(workspace, id);
      
      this.firstRowTitles = false;
      this.title = "";
      this.description = "";
      this.rowActionsCaption = "Acciones";
      this.rowActions = new ArrayList<GridRowAction>();
      this.gridData = new GridData();
   }
   
   //==============================================
   // Properties
   //==============================================

   /**
    * Devuelve un identificador único del tipo de control.
    */
   @Override
   public String getControlTypeId() 
   {
      return GridControl.CONTROL_ID;
   }

   /**
    * Devuelve el título que se mostrará en el control.
    */
   public String getTitle() 
   {
      return title;
   }

   /**
    * Establece el título que se mostrará en el control.
    */
   public void setTitle(String title) 
   {
      this.title = title;
   }

   /**
    * Devuelve la descripción que se mostrará en el control.
    */
   public String getDescription() 
   {
      return description;
   }

   /**
    * Establece la descripción que se mostrará en el control.
    */
   public void setDescription(String description) 
   {
      this.description = description;
   }

   /**
    * Indica si se muestra la fila de títulos de columna.
    */
   public boolean isFirstRowTitles() 
   {
      return firstRowTitles;
   }

   /**
    * Establece el indicador de muestra de la fila de títulos de columna.
    */
   public void setFirstRowTitles(boolean firstRowTitles) 
   {
      this.firstRowTitles = firstRowTitles;
   }
   
   /**
    * Devuelve el título de la columna de acciones.
    */
   public String getRowActionsCaption()
   {
      return rowActionsCaption;
   }

   /**
    * Establece el título de la columna de acciones.
    */
   public void setRowActionsCaption(String rowActionsCaption)
   {
      this.rowActionsCaption = rowActionsCaption;
   }
   
   /**
    * Devuelve la lista de índices de columnas que contienen los indicadores de clave primaria (base 0).
    */
   public ArrayList<Integer> getRowIds() 
   {
      return rowIds;
   }

   /**
    * Establece la lista de índices de columnas que contienen los indicadores de clave primaria (base 0).
    */
   public void setRowIds(ArrayList<Integer> rowIds)
   {
      this.rowIds = rowIds;
   }
   
   
   //==============================================
   // Methods
   //==============================================

   /**
    * Establece los datos del grid.
    * 
    * @param request Una instancia de {@link HttpServletRequest} que contiene el contexto de la llamada.
    * @param data Una instancia de {@link GridData} que contiene los datos del grid.
    */
   public void setData(GridData data)
   {
      this.gridData = data;
   }
   
   /**
    * Establece los datos del grid a partir de una consulta a una tabla de datos ORM.
    * 
    * @param dataSourceId Identificador de la connexión a datos que se usará para obtener los datos.
    * @param ormClass Una clase Cosmo ORM (CORM) que contiene la definición de la misma.
    * 
    * @throws Exception 
    * @throws DataException 
    * @throws SQLException 
    * @throws InvalidMappingException 
    */
   public void setData(String dataSourceId, Class<?> ormClass, boolean showAllColumns) throws InvalidMappingException, SQLException, DataException, Exception 
   {
      ResultSet rs;

      // Limpia los IDs
      this.rowIds = new ArrayList<Integer>();
      
      // Obtiene el ResultSet
      OrmFactory ormp = new OrmFactory(dataSourceId, getWorkspace());
      rs = ormp.select(ormClass, showAllColumns);
      
      // Establece los datos en el grid
      this.gridData.setCells(rs, true);
      
      // Establece los índices de las columnas que contienen las claves principales
      setGridMetaData(ormClass, showAllColumns);
   }
   
   /**
    * Obtiene los datos de la tabla en función del usuario/caché.
    * 
    * @param request Una instancia de {@link HttpServletRequest} que contiene el contexto de la llamada.
    * 
    * @return Una instancia de {@link GridData}.
    */
   public GridData getData()
   {
      return this.gridData;
   }
   
   /**
    * Agrega una acción a la columna de acciones de fila.
    * 
    * @param action Una instancia de {@link GridRowAction} que representa la acción sobre la fila.
    */
   public void addRowAction(GridRowAction action)
   {
      this.rowActions.add(action);
   }
   
   /**
    * Elimina la columna de acciones de fila.
    */
   public void clearRowActions()
   {
      this.rowActions.clear();
   }
   
   /**
    * Renderiza el control y genera el código XHTML de representación.
    *
    * @return Devuelve una cadena en formato XHTML que representa el control.
    */
   @Override
   public String render()
   {
      String xhtml = "";
      String xitem;
      String actions;
      String xrowtitle, xrow, xrowdata, xcell, xhead;
      TemplateControl ctrl;
      
      // Obtiene la plantilla y la parte del control
      ctrl = getWorkspace().getTemplate().getControl(CONTROL_ID);

      // Genera la fila de títulos
      xrowdata = "";
      xcell = ctrl.getElement(CPART_ROWTITLE_CELL);
      for (int col = 0; col < this.gridData.getColumnCount(); col++)
      {
         xrowdata += Control.replaceTag(xcell, TAG_VALUE, this.gridData.getCell(0, col, "").toString());
      }
      if (!this.rowActions.isEmpty())
      {
         xrowdata += Control.replaceTag(xcell, TAG_VALUE, this.rowActionsCaption);
      }
      xrowtitle = ctrl.getElement(CPART_ROWTITLE_BODY);
      xrowtitle = Control.replaceTag(xrowtitle, TAG_CELLS, xrowdata);

      // Genera las celdas de datos
      xrow = "";
      xcell = ctrl.getElement(CPART_ROW_CELL);
      for (int row = 1; row < this.gridData.getRowCount(); row++)
      {
         xrowdata = "";
         for (int col = 0; col < this.gridData.getColumnCount(); col++)
         {
            xrowdata += Control.replaceTag(xcell, TAG_VALUE, this.gridData.getCell(row, col, "").toString());
         }
         if (!this.rowActions.isEmpty())
         {
            actions = "";
            for (GridRowAction action : this.rowActions)
            {
               // actions += action.render(ctrl, data.getCell(row, this.idColumn, "").toString());
               actions += action.render(ctrl, this.gridData.getRowId(row)); //  getRowId(this.gridData, row));
            }
            xrowdata += Control.replaceTag(xcell, TAG_VALUE, actions);
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
   
   
   //==============================================
   // Methods
   //==============================================

   /**
    * Establece las índices de columna que contienen los identificadores de registro.
    * 
    * @param ormClass Clase CORM que contiene la definición de la misma (anotaciones).
    */
   private void setGridMetaData(Class<?> ormClass, boolean showAllColumns)
   {
      int idx = 0;
      CormObjectField cfg;
      
      this.rowIds = new ArrayList<Integer>();
      
      for (Method method : ormClass.getMethods())
      {
         if (method.isAnnotationPresent(CormObjectField.class))
         {
            cfg = method.getAnnotation(CormObjectField.class);
            
            if (showAllColumns || (!showAllColumns && cfg.showInObjectListGrid()))
            {
               cfg = method.getAnnotation(CormObjectField.class);
               this.gridData.setColumnField(idx, cfg.dbTableColumn());
               this.gridData.setColumnPrimaryKey(idx, cfg.isPrimaryKey());
               idx++;
            }
         }
      }
   }
   
   /**
    * Obtiene un ID compuesto por todos los valores de las columnas identioficador de fila.
    * <br /><br />
    * Formato: {@code id=12|AJC-0|2}
    * <br /><br />
    * Se forma juntando los distintos valores que forman parte de la clave principal mediante el carácter "{@code |}". 
    * 
    * @param data Una instancia de {@link GridData} que contiene los datos del control.
    * @param row Índice de la fila actual.
    * 
    * @return Una cadena que contiene un identificador únioco para la fila.
    */
   /*private String getRowId(GridData data, int row)
   {
      if (this.rowIds.isEmpty())
      {
         return "";
      }

      boolean first = true;
      String id = "";
      
      for (Integer colIdx : this.rowIds)
      {
         id += (first ? "" : "|");
         id += data.getCell(row, colIdx, "").toString();
         first = false;
      }
      
      return id;
   }*/
}
