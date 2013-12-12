package com.cosmo.data;

import java.sql.ResultSet;

import com.cosmo.Workspace;

/**
 * Representa una consulta de datos ejecutable mediante Cosmo Data Services.
 * 
 * @author Gerard Llort
 */
public class DataQuery
{
   private String id;
   private String datasourceId;
   private String sqlQuery;


   //==============================================
   // Constructors
   //==============================================

   /**
    * Constructor de la clase {@link DataQuery}.
    * 
    * @param id Identificador único de la consulta.
    * @param connectionId Identificador de la conexión de datos de Data Services.
    * @param sqlQuery Una cadena que contiene la senténcia SQL de consulta.
    */
   public DataQuery(String id, String connectionId, String sqlQuery)
   {
      this.id = id;
      this.datasourceId = connectionId;
      this.sqlQuery = sqlQuery;
   }


   //==============================================
   // Private Members
   //==============================================

   public String getId()
   {
      return id;
   }

   public void setId(String id)
   {
      this.id = id;
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


   //==============================================
   // Methods
   //==============================================

   /**
    * Ejecuta la consulta.
    * 
    * @param workspace Una instancia de {@link Workspace} que representa el workspace actual.
    * 
    * @return Una instancia de {@link ResultSet} que contiene los datos devueltos por la consulta.
    * 
    * @throws DataException
    */
   public ResultSet execute(Workspace workspace) throws DataException
   {
      DataAgent da = DataFactory.getInstance(workspace, this.datasourceId);
      return da.executeSql(this.sqlQuery);
   }
}
