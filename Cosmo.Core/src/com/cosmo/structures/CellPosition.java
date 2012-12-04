package com.cosmo.structures;

/**
 * Implementa una celda de una tabla.
 * 
 * @see Ver {@link Table}.
 * @author Gerard Llort
 */
public class CellPosition 
{
   private int col;
   private int row;
   
   //==============================================
   // Contructors
   //==============================================
   
   /**
    * Contructor de la clase.
    * 
    * @param row Índice de la fila.
    * @param col Índice de la columna.
    */
   public CellPosition(int row, int col)
   {
      this.col = col;
      this.row = row;
   }

   //==============================================
   // properties
   //==============================================
   
   public int getCol() 
   {
      return col;
   }

   public void setCol(int col) 
   {
      this.col = col;
   }

   public int getRow() 
   {
      return row;
   }

   public void setRow(int row) 
   {
      this.row = row;
   }
}
