package com.cosmo.data.adapter;

/**
 *
 * @author Gerard Llort
 */
public class SqlAdapterProvider 
{
   private SqlAdapterProvider() 
   {
   }
   
   public static SqlAdapterProvider getInstance() 
   {
      return SqlAdapterProviderHolder.INSTANCE;
   }
   
   private static class SqlAdapterProviderHolder 
   {
      private static final SqlAdapterProvider INSTANCE = new SqlAdapterProvider();
   }
   
   private void loadDefinition()
   {
      
   }
}
