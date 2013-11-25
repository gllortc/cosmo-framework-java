package com.cosmo.data.impl;

import java.sql.Connection;
import java.sql.SQLException;

import org.postgresql.ds.PGPoolingDataSource;

import com.cosmo.structures.PluginProperties;

public class PostgreSqlDataSourceImpl
{
   PGPoolingDataSource source;

   public PostgreSqlDataSourceImpl(PluginProperties properties)
   {
      source = new PGPoolingDataSource();
      source.setDataSourceName("A Data Source");
      source.setServerName("localhost");
      source.setDatabaseName("test");
      source.setUser("testuser");
      source.setPassword("testpassword");
      source.setMaxConnections(10);
   }
   
   public Connection getConnection() throws SQLException
   {
      return source.getConnection();
   }
}
