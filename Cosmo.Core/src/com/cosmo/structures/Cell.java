package com.cosmo.structures;

/**
 *
 * @author gllort
 */
public class Cell 
{
   private CellPosition pos;
   private Object value;
   
   public Cell(int row, int col, Object value)
   {
      this.pos = new CellPosition(row, col);
      this.value = value;
   }

   public int getCol() 
   {
      return pos.getCol();
   }

   public int getRow() 
   {
      return pos.getRow();
   }

   public Object getValue() 
   {
      return value;
   }

   public void setValue(Object value) 
   {
      this.value = value;
   }
   
   public CellPosition getPosition()
   {
      return pos;
   }
}
