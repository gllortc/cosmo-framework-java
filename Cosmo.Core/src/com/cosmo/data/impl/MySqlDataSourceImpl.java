package com.cosmo.data.impl;

import java.sql.SQLException;

import com.cosmo.data.DataAgent;
import com.cosmo.data.DataException;
import com.cosmo.util.PluginProperties;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

public class MySqlDataSourceImpl extends DataAgent
{
   private static final String DATASOURCE_DB_HOST = "mysql.db.host";
   private static final String DATASOURCE_DB_NAME = "mysql.db.name";
   private static final String DATASOURCE_USER_LOGIN = "mysql.user.login";
   private static final String DATASOURCE_USER_PASSWORD = "mysql.user.password";

   // Declaración de variables locales
   private MysqlDataSource source;


   //==============================================
   // Constructors
   //==============================================

   /**
    * Constructor de la clase {@link MySqlDataSourceImpl}.
    * 
    * @param properties Una instancia de {@link PluginProperties} que contiene los parámetros de configuración.
    */
   public MySqlDataSourceImpl(PluginProperties properties)
   {
      super(properties);

      source.setServerName(getProperties().getParamString(MySqlDataSourceImpl.DATASOURCE_DB_HOST));
      source.setDatabaseName(getProperties().getParamString(MySqlDataSourceImpl.DATASOURCE_DB_NAME));
      source.setUser(getProperties().getParamString(MySqlDataSourceImpl.DATASOURCE_USER_LOGIN));
      source.setPassword(getProperties().getParamString(MySqlDataSourceImpl.DATASOURCE_USER_PASSWORD));
   }


   //==============================================
   // Methods
   //==============================================

   /**
    * Abre una conexión con la base de datos usando el mecanismo implementado por el agente de datos.
    * 
    * @throws DataException 
    */
   @Override
   public void connect() throws DataException
   {
      try
      {
         this.setConnection(source.getConnection());
      }
      catch (SQLException ex)
      {
         this.setConnection(null);

         throw new DataException(ex.getMessage(), ex);
      }
   }

   /**
    * Cierra la conexión con la base de datos.
    */
   @Override
   public void disconnect()
   {
      if (this.getConnection() != null)
      {
         try
         {
            if (!this.getConnection().isClosed())
            {
               this.getConnection().close();
            }
         }
         catch (SQLException e)
         {
            // Nothing to do
         }
         finally
         {
            this.setConnection(null);
         }
      }
   }
}
