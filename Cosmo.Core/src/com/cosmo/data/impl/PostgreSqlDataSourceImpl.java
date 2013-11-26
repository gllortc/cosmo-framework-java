package com.cosmo.data.impl;

import java.sql.Connection;
import java.sql.SQLException;

import org.postgresql.ds.PGPoolingDataSource;

import com.cosmo.util.PluginProperties;

public class PostgreSqlDataSourceImpl
{
   private static final String DATASOURCE_NAME = "pgsql.datasource.name";
   private static final String DATASOURCE_MAXCONN = "pgsql.datasource.maxconnections";
   private static final String DATASOURCE_DB_HOST = "pgsql.db.host";
   private static final String DATASOURCE_DB_NAME = "pgsql.db.name";
   private static final String DATASOURCE_USER_LOGIN = "pgsql.user.login";
   private static final String DATASOURCE_USER_PASSWORD = "pgsql.user.password";

   PGPoolingDataSource source;

   public PostgreSqlDataSourceImpl(PluginProperties properties)
   {
      source = new PGPoolingDataSource();

      source.setDataSourceName(properties.getParamString(PostgreSqlDataSourceImpl.DATASOURCE_NAME));
      source.setServerName(properties.getParamString(PostgreSqlDataSourceImpl.DATASOURCE_DB_HOST));
      source.setDatabaseName(properties.getParamString(PostgreSqlDataSourceImpl.DATASOURCE_DB_NAME));
      source.setUser(properties.getParamString(PostgreSqlDataSourceImpl.DATASOURCE_USER_LOGIN));
      source.setPassword(properties.getParamString(PostgreSqlDataSourceImpl.DATASOURCE_USER_PASSWORD));
      source.setMaxConnections(properties.getParamInteger(PostgreSqlDataSourceImpl.DATASOURCE_MAXCONN, 10));
   }

   public Connection getConnection() throws SQLException
   {
      return source.getConnection();
   }
}
