package com.cosmo.data.impl;

import java.sql.Connection;
import java.sql.SQLException;

import com.cosmo.structures.PluginProperties;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

public class MySqlDataSourceImpl
{
   MysqlDataSource source;

   public MySqlDataSourceImpl(PluginProperties properties)
   {
      source = new MysqlDataSource();
      source.setUser("usuario");
      source.setPassword("password");
      source.setDatabaseName("dedicaciones");
      source.setServerName("localhost");
   }

   public Connection getConnection() throws SQLException
   {
      return source.getConnection();
   }
}
