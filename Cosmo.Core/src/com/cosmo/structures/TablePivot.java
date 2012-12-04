package com.cosmo.structures;

/**
 *
 * @author gllort
 */
public class TablePivot 
{
   private int col;
   private int row;
   
   public TablePivot(int row, int col)
   {
      this.col = col;
      this.row = row;
   }

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
