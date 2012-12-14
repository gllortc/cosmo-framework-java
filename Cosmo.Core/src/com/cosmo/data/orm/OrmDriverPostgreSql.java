package com.cosmo.data.orm;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import com.cosmo.annotations.CosmoFieldGetter;
import com.cosmo.annotations.CosmoFieldSetter;
import com.cosmo.annotations.CosmoTable;
import com.cosmo.data.DataConnection;
import com.cosmo.data.DataException;
import com.cosmo.net.HttpRequestUtils;

/**
 * Implementa el proveedor de Cosmo ORM (CORM) para PostgreSQL.
 * 
 * @author Gerard Llort
 */
public class OrmDriverPostgreSql extends OrmDriver
{
   // Tabla de palabras reservadas por PostgreSQL
   private final static String SQL_INSERT = "INSERT INTO";
   private final static String SQL_INSERT_VALUES = "VALUES";
   
   private final static String POSTGRESQL_DATE_FORMAT = "yyyy/MM/dd";
   
   private final static String PROVIDER_NAME = "PostgreSQL CORM Driver";
   private final static String JDBC_DRIVER_CLASS = "org.postgresql.Driver";

   //==============================================
   // Constructors
   //==============================================
   
   public OrmDriverPostgreSql(DataConnection connection)
   {
      super(connection);
   }
   
   //==============================================
   // Properties
   //==============================================
   
   @Override
   public String getProviderName() 
   {
      return OrmDriverPostgreSql.PROVIDER_NAME;
   }
   
   @Override
   public String getJdbcDriver() 
   {
      return OrmDriverPostgreSql.JDBC_DRIVER_CLASS;
   }
   
   //==============================================
   // Methods
   //==============================================

   /**
    * Recupera los valores, los coloca en una instancia de la clase y agrega los datos en la base de datos.
    * 
    * @param ormClass
    * @param request
    * 
    * @throws Exception 
    * @throws DataException 
    * @throws SQLException 
    * @throws InvalidMappingException 
    */
   public Object add(Class<?> ormClass, HttpServletRequest request) throws InvalidMappingException, SQLException, DataException, Exception
   {
      Object instance = null;
      CosmoFieldSetter cfs;
      
      // Obtiene el constructor vacio (siempre usará el constructor vacío)  
      Constructor<?>[] ctors = ormClass.getDeclaredConstructors();
      for (Constructor<?> ctor : ctors)
      {
         if (ctor.getGenericParameterTypes().length == 0)
         {
            ctor.setAccessible(true);
            instance = ctor.newInstance();
            
            for (Method method : ormClass.getMethods())
            {
               if (method.isAnnotationPresent(CosmoFieldSetter.class))
               {               
                  method.setAccessible(true);
                  cfs = method.getAnnotation(CosmoFieldSetter.class);
                  
                  if (method.getReturnType() == String.class)
                  {
                     method.invoke(instance, HttpRequestUtils.getValue(request, cfs.name(), ""));
                  }
                  else if (method.getReturnType() == Integer.class)
                  {
                     method.invoke(instance, HttpRequestUtils.getInt(request, cfs.name()));
                  }
               }
            }
         }
      }

      // Agrega el registro a la BBDD
      this.add(instance);
      
      return instance;
   }
   
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
      CosmoFieldGetter cf;
      SimpleDateFormat sdf = new SimpleDateFormat(OrmDriverPostgreSql.POSTGRESQL_DATE_FORMAT);
      
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
         cf = field.getAnnotation(CosmoFieldGetter.class);
         
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
         cf = field.getAnnotation(CosmoFieldGetter.class);
         
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
