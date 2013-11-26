package com.cosmo.data.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.cosmo.data.DataAgent;
import com.cosmo.data.DataException;
import com.cosmo.util.PluginProperties;

public class JdbcDataSourceImpl extends DataAgent
{
   // private static final String DATASOURCE_CORM_DRIVER = "corm.driver";
   private static final String DATASOURCE_JDBC_DRIVER = "db.jdbc.driver";
   private static final String DATASOURCE_DB_HOST = "db.server";
   private static final String DATASOURCE_DB_PORT = "db.port";
   private static final String DATASOURCE_DB_SCHEMA = "db.schema";
   private static final String DATASOURCE_DB_LOGIN = "db.user.login";
   private static final String DATASOURCE_DB_PASSWORD = "db.user.password";

   private String jdbcDriver;
   private String host;
   private String port;
   private String schema;
   private String login;
   private String pwd;


   //==============================================
   // Constructors
   //==============================================

   /**
    * Constructor de la clase {@link DataAgent}.
    * 
    * @param properties Una instancia de {@link PluginProperties} que contiene los parámetros de configuración.
    */
   public JdbcDataSourceImpl(PluginProperties properties)
   {
      super(properties);

      this.jdbcDriver = getProperties().getParamString(DATASOURCE_JDBC_DRIVER);
      // this.cormDriver = getProperties().getParamString(DATASOURCE_CORM_DRIVER);
      this.host = getProperties().getParamString(DATASOURCE_DB_HOST);
      this.port = getProperties().getParamString(DATASOURCE_DB_PORT);
      this.schema = getProperties().getParamString(DATASOURCE_DB_SCHEMA);
      this.login = getProperties().getParamString(DATASOURCE_DB_LOGIN);
      this.pwd = getProperties().getParamString(DATASOURCE_DB_PASSWORD);
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
      Connection conn;
      
      try 
      {
         Class.forName(this.jdbcDriver);

         conn = DriverManager.getConnection(getConnectionUrl(), login, pwd);
         conn.setAutoCommit(this.isAutoCommit());

         this.setConnection(conn);
      } 
      catch (ClassNotFoundException ex)
      {
         throw new DataException("ClassNotFoundException: " + ex.getMessage(), ex);
      }
      catch (SQLException ex)
      {
         throw new DataException("SQLException: " + ex.getMessage(), ex);
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


   //==============================================
   // Private Members
   //==============================================   

   /**
    * Obtiene la URL de conexión a la base de datos.
    * 
    * @return Una cadena que representa la URL de conexión a la base de datos.
    */
   private String getConnectionUrl()
   {
      return this.host + ":" + this.port + "/" + this.schema;
   }
}
