package com.cosmo.structures;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * Implementa una estructura de tabla.
 * 
 * @author Gerard llort
 */
public class GridData 
{
   private int cols;
   private int rows;
   private int width;
   private int height;
   private Object[][] map; // row, col
   
   //==============================================
   // Contructors
   //==============================================
   
   /**
    * Contructor de la clase.
    */
   public GridData()
   {
      this.rows = 0;
      this.cols = 0;
      this.height = 10;
      this.width = 10;
      this.map = new Object[10][10];
   }
   
   /**
    * Contructor de la clase.
    */
   public GridData(int rows, int cols)
   {
      this.rows = 0;
      this.cols = 0;
      this.height = rows;
      this.width = cols;
      this.map = new Object[rows][cols];
   }
   
   //==============================================
   // Properties
   //==============================================

   /**
    * Devuelve el n�mero de columnas de la tabla.
    */
   public int getColumnCount() 
   {
      return cols + 1;
   }

   /**
    * Devuelve el n�mero de filas de la tabla.
    */
   public int getRowCount() 
   {
      return rows + 1;
   }
   
   //==============================================
   // Methods
   //==============================================
   
   /**
    * Establece el valor de una celda de la tabla.
    * 
    * @param row �ndice de la fila (base 0).
    * @param col �ndice de la columna (base 0).
    * @param value Valor a establecer.
    */
   public void setCell(int row, int col, Object value) throws GridDataLimitsException
   {
      // Actualiza fila/columna de datos
      if (this.rows < row)
      {
         this.rows = row;
      }
      if (this.cols < col)
      {
         this.cols = col;
      }
      
      if (this.width < col || this.height < row)
      {
         this.width = (this.width < col ? col + 10 : this.width);
         this.height = (this.height < row ? row + 10 : this.height);

         resize(this.height, this.width);
      }

      map[row][col] = value;
   }
   
   /**
    * Obtiene el contenido de una celda.
    * 
    * @param row �ndice de la fila (base 0).
    * @param col �ndice de la columna (base 0).
    * @return Un objeto que corresponde al contenido de la celda. En caso de ser una celda vac�a, este m�todo devolver� {@code null}.
    */
   public Object getCell(int row, int col)
   {
      return map[row][col];
   }
   
   /**
    * Obtiene el contenido de una celda.
    * 
    * @param row �ndice de la fila (base 0).
    * @param col �ndice de la columna (base 0).
    * @param defaultValue Valor por defecto que devolver� la llamada si el valor obtenido est� vac�o.
    * @return Un objeto que corresponde al contenido de la celda. En caso de ser una celda vac�a, este m�todo devolver� {@code null}.
    */
   public Object getCell(int row, int col, Object defaultValue)
   {
      if (map[row][col] == null)
      {
         return defaultValue;
      }
      else
      {
         return map[row][col];
      }
   }
   
   /**
    * Establece los valores de una tabla a partir de una consulta a base de datos.
    * 
    * @param useColNameAsHeadTitle Indica si se deben usar los nombres de columna para la fila de t�tulos.
    * @param rs Una instancia de {@link ResultSet} que contiene los datos para la tabla.
    */
   public void setCells(ResultSet rs, boolean useColNameAsHeadTitle) throws SQLException
   {
      int row;
      
      // Obtiene los metadatos de la estructura
      ResultSetMetaData md = rs.getMetaData();
      
      // Agrega la fila de t�tulos
      if (useColNameAsHeadTitle)
      {
         for (int col = 0; col < md.getColumnCount(); col++)
         {
            try
            {
               setCell(0, col, md.getColumnName(col + 1));
            }
            catch (GridDataLimitsException ex)
            {
               // Nothing to do
            }
         }
      }

      // Agrega los datos
      row = (useColNameAsHeadTitle ? 1 : 0);
      while (rs.next())
      {
         for (int col = 0; col < md.getColumnCount(); col++)
         {
            try
            {
               setCell(row, col, rs.getObject(col + 1));
            }
            catch (GridDataLimitsException ex)
            {
               // Nothing to do
            }
         }
         row++;
      }
   }
   
   /**
    * Vac�a la tabla de todos sus datos.
    */
   public void clear()
   {
      initialize();
   }
   
   @Override
   public String toString()
   {
      String str = "";
      
      str += "[";
      for (int row = 0; row <= this.rows; row++)
      {
         str += "[";
         for (int col = 0; col <= this.cols; col++)
         {
            str += map[row][col].toString() + ", ";
         }
         str += "]";
      }
      str += "]";
      
      return str;
   }

   //==============================================
   // Private members
   //==============================================
   
   /**
    * Inicializa la instancia.
    */
   private void initialize()
   {
      this.rows = 10;
      this.cols = 10;
      map = new Object[10][10];
   }
   
   private void resize(int rows, int cols)
   {
      map = (Object[][]) resizeArray(map, rows);
      for (int i = 0; i < map.length; i++) 
      {
         if (map[i] == null) 
         {
            map[i] = new Object[cols];
         }
         else 
         {
            map[i] = (Object[]) resizeArray(map[i], cols);
         } 
      }
   }

   /**
   * Reallocates an array with a new size, and copies the contents of the old array to the new array.
   * 
   * @param oldArray  the old array, to be reallocated.
   * @param newSize   the new array size.
   * @return          A new array with the same contents.
   */
   @SuppressWarnings("rawtypes")
   private static Object resizeArray(Object oldArray, int newSize) 
   {
      int oldSize = java.lang.reflect.Array.getLength(oldArray);
      Class elementType = oldArray.getClass().getComponentType();
      Object newArray = java.lang.reflect.Array.newInstance(elementType, newSize);
      int preserveLength = Math.min(oldSize, newSize);
      
      if (preserveLength > 0)
      {
         System.arraycopy(oldArray, 0, newArray, 0, preserveLength);
      }
      
      return newArray; 
   }
}