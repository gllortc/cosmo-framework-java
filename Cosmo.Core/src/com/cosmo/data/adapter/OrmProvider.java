package com.cosmo.data.adapter;

import java.sql.SQLException;

import com.cosmo.data.DataConnection;
import com.cosmo.data.DataException;

/**
 * Implementa un acceso uniforme a los drivers CORM.<br />
 * Esta clase se encarga de instanciar el driver adecuado (según la configuración) y de manejar las llamadas a métodos.
 * 
 * @author Gerard Llort
 */
public class OrmProvider extends OrmDriver
{

   public OrmProvider(DataConnection connection) 
   {
      super(connection);
   }

   @Override
   public String getProviderName() 
   {
      return null;
   }

   @Override
   public void add(Object data) throws InvalidMappingException, SQLException, DataException, Exception 
   {
      
   }
   
}
