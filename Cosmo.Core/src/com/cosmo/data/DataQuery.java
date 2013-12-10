package com.cosmo.data;

import java.sql.ResultSet;

import com.cosmo.Workspace;

public class DataQuery
{
   private String name;
   private String datasourceId;
   private String sqlQuery;

   public DataQuery(String name, String datasourceId, String sqlQuery)
   {
      this.name = name;
      this.datasourceId = datasourceId;
      this.sqlQuery = sqlQuery;
   }

   public String getName()
   {
      return name;
   }

   public void setName(String name)
   {
      this.name = name;
   }

   public String getDatasourceId()
   {
      return datasourceId;
   }

   public void setDatasourceId(String id)
   {
      this.datasourceId = id;
   }

   public String getSqlQuery()
   {
      return sqlQuery;
   }

   public void setSqlQuery(String sql)
   {
      this.sqlQuery = sql;
   }
   
   public ResultSet execute(Workspace workspace) throws DataException
   {
      DataAgent da = DataFactory.getInstance(workspace, this.datasourceId);
      return da.executeSql(this.sqlQuery);
   }
}
