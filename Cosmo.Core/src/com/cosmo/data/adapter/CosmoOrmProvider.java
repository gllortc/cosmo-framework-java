package com.cosmo.data.adapter;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.cosmo.annotations.CosmoField;
import com.cosmo.annotations.CosmoTable;
import com.cosmo.data.DataConnection;
import com.cosmo.data.DataException;

/**
 * Implementa el proveedor de Cosmo ORM (CORM) para PostgreSQL.
 * 
 * @author Gerard Llort
 */
public class CosmoOrmProvider extends CosmoOrm
{
   // Tabla de palabras reservadas por PostgreSQL
   private final static String SQL_INSERT = "INSERT INTO";
   private final static String SQL_INSERT_VALUES = "VALUES";
   
   private final static String PROVIDER_NAME = "PostgreSQL CORM Driver";
   
   //==============================================
   // Constructors
   //==============================================
   
   public CosmoOrmProvider(DataConnection connection)
   {
      super(connection);
   }
   
   //==============================================
   // Properties
   //==============================================
   
   public String getProviderName() 
   {
      return CosmoOrmProvider.PROVIDER_NAME;
   }
   
   //==============================================
   // Methods
   //==============================================

   /**
    * Genera una senténcia INSERT INTO a partir de una instancia.
    * 
    * @param data Clase que contiene los datos a insertar.
    * 
    * @throws InvalidMappingException
    * @throws DataException 
    * @throws SQLException 
    * @throws Exception 
    */
   public void add(Object data) throws InvalidMappingException, SQLException, DataException, Exception
   {
      boolean first;
      StringBuilder sql = new StringBuilder();
      CosmoTable ct;
      CosmoField cf;
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
      
      sql.append(SQL_INSERT);
      sql.append(" ");
      
      // Obtiene el nombre de la tabla
      ct = data.getClass().getAnnotation(CosmoTable.class);
      if (ct == null)
      {
         throw new InvalidMappingException("No CosmoTable annotation detected on POJO class.");
      }
      sql.append(ct.tableName());
      sql.append(" (");

      // Obtiene la lista de campos
      first = true;
      for (Field field : data.getClass().getFields())
      {
         cf = field.getAnnotation(CosmoField.class);
         
         if (cf != null)
         {
            sql.append((first ? "" : ", "));
            sql.append(cf.tableColumnName());
            first = false;
         }
      }
      
      sql.append(") ");
      sql.append(SQL_INSERT_VALUES);
      sql.append("( ");

      // Inserta los valores en la senténcia
      first = true;
      for (Field field : data.getClass().getFields())
      {
         cf = field.getAnnotation(CosmoField.class);
         
         if (cf != null && !cf.readOnly())
         {
            sql.append((first ? "" : ", "));

            if (field.getType() == String.class)
            {
               sql.append("'");
               sql.append(convertToSql((String) field.get(data)));
               sql.append("'");
            }
            else if (field.getType() == Integer.class || field.getType() == int.class)
            {
               sql.append((Integer) field.get(data));
            }
            else if (field.getType() == Date.class)
            {
               sql.append("'");
               sql.append(sdf.format((Date) field.get(data)));
               sql.append("'");
            }

            first = false;
         }
      }
      
      sql.append(")");
      
      // Memoriza la senténcia SQL generada
      this.setLastSqlSentence(sql.toString());
      
      // Ejecuta la senténcia SQL
      this.getConnection().connect();
      this.getConnection().execute(sql.toString());
   }
   
   //==============================================
   // Private members
   //==============================================

   private String convertToSql(String text)
   {
      return text.replace("'", "''");
   }
}
