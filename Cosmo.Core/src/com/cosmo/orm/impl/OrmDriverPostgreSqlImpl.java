package com.cosmo.orm.impl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import com.cosmo.data.DataAgent;
import com.cosmo.data.DataException;
import com.cosmo.orm.InvalidMappingException;
import com.cosmo.orm.OrmDriver;
import com.cosmo.orm.OrmFactory;
import com.cosmo.orm.annotations.CormFieldSetter;
import com.cosmo.orm.annotations.CormObjectField;
import com.cosmo.orm.annotations.CormObjectField.FieldSortType;

/**
 * Implementa el proveedor de Cosmo ORM (CORM) para PostgreSQL 9.X.
 * 
 * @author Gerard Llort
 */
public class OrmDriverPostgreSqlImpl extends OrmDriver
{
   private final static String PROVIDER_NAME = "PostgreSQL CORM Driver";
   private final static String JDBC_DRIVER_CLASS = "org.postgresql.Driver";


   //==============================================
   // Constructors
   //==============================================
   
   public OrmDriverPostgreSqlImpl(DataAgent connection)
   {
      super(connection);
   }


   //==============================================
   // Properties
   //==============================================

   @Override
   public String getProviderName() 
   {
      return OrmDriverPostgreSqlImpl.PROVIDER_NAME;
   }

   @Override
   public String getJdbcDriver() 
   {
      return OrmDriverPostgreSqlImpl.JDBC_DRIVER_CLASS;
   }


   //==============================================
   // Methods
   //==============================================

   /**
    * Genera una senténcia SELECT a partir de un objeto CORM.
    * 
    * @param ormObject Una referencia a un objeto CORM (clase POJO que está anotada con anotaciones CORM).
    * 
    * @return Una instancia de {@link ResultSet} que contiene los datos obtenidos en la consulta.
    * 
    * @throws InvalidMappingException
    * @throws ClassNotFoundException 
    * @throws DataException 
    * @throws SQLException 
    * @throws Exception 
    */
   public ResultSet select(Class<?> ormClass, boolean showAllColumns) throws InvalidMappingException, SQLException, DataException, Exception
   {
      boolean first;
      StringBuilder sql = new StringBuilder();
      CormObjectField cf;
      ResultSet rs;

      // Comprueba si el objeto proporcionado es un objeto CORM válido
      if (!OrmFactory.isValidCormObject(ormClass))
      {
         throw new InvalidMappingException(ormClass.getName() + " is not a CORM object.");
      }

      // Configura la cláusula SELECT
      sql.append(SQL_SELECT);
      sql.append(" ");

      first = true;
      for (Method method : ormClass.getMethods())
      {
         cf = method.getAnnotation(CormObjectField.class);
         if (cf != null)
         {
            if ((showAllColumns) || (!showAllColumns && cf.showInObjectListGrid()))
            {
               sql.append((first ? "" : ", "));
               sql.append(cf.dbTableColumn() + " As \"" + cf.label() + "\"");
               first = false;
            }
         }
      }
      sql.append(" ");

      // Configura la cláusula FROM
      sql.append(SQL_FROM);
      sql.append(" ");
      sql.append(OrmFactory.getDbTableName(ormClass));
      sql.append(" ");

      // Configura la cláusula ORDER BY
      if (OrmFactory.haveSortedFields(ormClass))
      {
         sql.append(SQL_ORDERBY);
         sql.append(" ");

         // Obtiene la lista de campos
         first = true;
         for (Method method : ormClass.getMethods())
         {
            cf = method.getAnnotation(CormObjectField.class);
            if (cf != null)
            {
               if (cf.sort() == FieldSortType.Ascending)
               {
                  sql.append((first ? "" : ", "));
                  sql.append(cf.dbTableColumn() + " ");
                  sql.append(SQL_ORDERBY_ASC);

                  first = false;
               }
               else if (cf.sort() == FieldSortType.Descending)
               {
                  sql.append((first ? "" : ", "));
                  sql.append(cf.dbTableColumn() + " ");
                  sql.append(SQL_ORDERBY_DESC);

                  first = false;
               }
            }
         }
      }

      // Memoriza la senténcia SQL generada
      this.setLastSqlSentence(sql.toString());

      // Ejecuta la senténcia SQL
      this.getConnection().connect();
      rs = this.getConnection().executeSql(sql.toString());
      this.getConnection().disconnect();

      return rs;
   }

   /**
    * Genera una senténcia SELECT a partir de una instancia de un objeto CORM.
    * 
    * @param ormObject Una referencia a un objeto CORM (clase POJO que está anotada con anotaciones CORM).
    * 
    * @return Una instancia de {@link ResultSet} que contiene los datos obtenidos en la consulta.
    * 
    * @throws InvalidMappingException
    * @throws ClassNotFoundException 
    * @throws DataException 
    * @throws SQLException 
    * @throws Exception 
    */
   public Object get(Object data) throws InvalidMappingException, SQLException, DataException, Exception
   {
      boolean first;
      StringBuilder sql = new StringBuilder();
      CormObjectField cf;
      CormFieldSetter cfs;
      ResultSet rs;

      // Comprueba si el objeto proporcionado es un objeto CORM válido
      if (!OrmFactory.isValidCormObject(data.getClass()))
      {
         throw new InvalidMappingException(data.getClass() + " is not a CORM object.");
      }
      
      // Configura la cláusula SELECT
      sql.append(SQL_SELECT);
      sql.append(" ");

      first = true;
      for (Method method : data.getClass().getMethods())
      {
         cf = method.getAnnotation(CormObjectField.class);
         if (cf != null)
         {
            sql.append((first ? "" : ", "));
            sql.append(cf.dbTableColumn());

            first = false;
         }
      }
      sql.append(" ");
      
      // Configura la cláusula FROM
      sql.append(SQL_FROM);
      sql.append(" ");
      sql.append(OrmFactory.getDbTableName(data.getClass()));
      sql.append(" ");

      // Genera la cláusua WHERE
      sql.append(getFilter(data));
      
      // Memoriza la senténcia SQL generada
      this.setLastSqlSentence(sql.toString());
      
      // Ejecuta la senténcia SQL
      this.getConnection().connect();
      rs = this.getConnection().executeSql(sql.toString());
      this.getConnection().disconnect();
      
      // Recopila los datos en el objeto
      if (rs.next())
      {
         for (Method method : data.getClass().getMethods())
         {
            if (method.isAnnotationPresent(CormFieldSetter.class))
            {
               cfs = method.getAnnotation(CormFieldSetter.class);
               
               Class<?>[] argTypes = method.getParameterTypes();
               if (argTypes.length > 0)
               {
                  // Texto
                  if ((argTypes[0] == String.class) || (argTypes[0] == char.class))
                  {
                     method.invoke(data, rs.getString(cfs.dbTableColumn()));
                  }
                  // Enteros
                  else if (argTypes[0] == Integer.class || argTypes[0] == int.class)
                  {
                     method.invoke(data, rs.getInt(cfs.dbTableColumn()));
                  }
                  else if (argTypes[0] == Long.class || argTypes[0] == long.class)
                  {
                     method.invoke(data, rs.getLong(cfs.dbTableColumn()));
                  }
                  else if (argTypes[0] == Short.class || argTypes[0] == short.class)
                  {
                     method.invoke(data, rs.getShort(cfs.dbTableColumn()));
                  }
                  else if (argTypes[0] == Byte.class || argTypes[0] == byte.class)
                  {
                     method.invoke(data, rs.getByte(cfs.dbTableColumn()));
                  }
                  // Decimales
                  else if (argTypes[0] == Double.class || argTypes[0] == double.class)
                  {
                     method.invoke(data, rs.getDouble(cfs.dbTableColumn()));
                  }
                  else if (argTypes[0] == Float.class || argTypes[0] == float.class)
                  {
                     method.invoke(data, rs.getFloat(cfs.dbTableColumn()));
                  }
                  else if (argTypes[0] == BigDecimal.class)
                  {
                     method.invoke(data, rs.getBigDecimal(cfs.dbTableColumn()));
                  }
                  // Fechas y horas
                  else if (argTypes[0] == Date.class)
                  {
                     method.invoke(data, rs.getDate(cfs.dbTableColumn()));
                  }
                  else if (argTypes[0] == Time.class)
                  {
                     method.invoke(data, rs.getTime(cfs.dbTableColumn()));
                  }
                  else if (argTypes[0] == Timestamp.class)
                  {
                     method.invoke(data, rs.getTimestamp(cfs.dbTableColumn()));
                  }
                  // Booleanos
                  else if (argTypes[0] == boolean.class || argTypes[0] == Boolean.class)
                  {
                     method.invoke(data, rs.getBoolean(cfs.dbTableColumn()));
                  }
               }
            }
         }
         
         return data;
      }
      else
      {
         return null;
      }
   }
   
   /**
    * Genera una senténcia INSERT INTO a partir de una instancia de un objeto CORM.
    * 
    * @param data Clase que contiene los datos a insertar.
    * 
    * @throws InvalidMappingException
    * @throws DataException 
    * @throws SQLException 
    * @throws Exception 
    */
   public void insert(Object data) throws InvalidMappingException, SQLException, DataException, Exception
   {
      boolean first;
      StringBuilder sql = new StringBuilder();
      CormObjectField cf;
      
      // Comprueba si el objeto proporcionado es un objeto CORM válido
      if (!OrmFactory.isValidCormObject(data.getClass()))
      {
         throw new InvalidMappingException(data.getClass().getName() + " is not a CORM object.");
      }
      
      // Genera la cláusula INSERT
      sql.append(SQL_INSERT);
      sql.append(" ");
      sql.append(OrmFactory.getDbTableName(data.getClass()));
      sql.append(" (");

      first = true;
      for (Method method : data.getClass().getMethods())
      {
         cf = method.getAnnotation(CormObjectField.class);
         
         if (cf != null && !cf.readOnly() && !cf.isAutogenerated())
         {
            sql.append((first ? "" : ", "));
            sql.append(cf.dbTableColumn());
            first = false;
         }
      }
      
      sql.append(") ");
      
      // Genera la cláusula VALUES
      sql.append(SQL_INSERT_VALUES);
      sql.append(" (");

      first = true;
      for (Method method : data.getClass().getMethods())
      {
         cf = method.getAnnotation(CormObjectField.class);
         
         if (cf != null && !cf.readOnly() && !cf.isAutogenerated())
         {
            sql.append((first ? "" : ", "));
            sql.append(getFormatedValue(method, data));

            first = false;
         }
      }
      
      sql.append(")");
      
      // Memoriza la senténcia SQL generada
      this.setLastSqlSentence(sql.toString());
      
      // Ejecuta la senténcia SQL
      this.getConnection().connect();
      this.getConnection().execute(sql.toString());
      this.getConnection().disconnect();
   }
   
   /**
    * Elimina el registro de la tabla de datos que indica el valor asociado a/los campo/s identificador/es.
    * 
    * @param ormClass Una referencia a un objeto CORM (clase POJO que está anotada con anotaciones CORM).
    * @param request
    * 
    * @throws Exception 
    * @throws DataException 
    * @throws SQLException 
    * @throws InvalidMappingException 
    */
   public void delete(Object data) throws InvalidMappingException, SQLException, DataException, Exception
   {
      // boolean first;
      StringBuilder sql = new StringBuilder();

      // Comprueba si el objeto proporcionado es un objeto CORM válido
      if (!OrmFactory.isValidCormObject(data.getClass()))
      {
         throw new InvalidMappingException(data.getClass().getName() + " is not a CORM object.");
      }
      else if (!OrmFactory.havePrimaryKey(data.getClass()))
      {
         throw new InvalidMappingException(data.getClass().getName() + " does not have declared primary key.");
      }
      
      // Genera la cláusula INSERT
      sql.append(SQL_DELETE);
      sql.append(" ");
      sql.append(OrmFactory.getDbTableName(data.getClass()));
      sql.append(" ");

      // Genera la cláusua WHERE
      sql.append(getFilter(data));
      
      // Memoriza la senténcia SQL generada
      this.setLastSqlSentence(sql.toString());
      
      // Ejecuta la senténcia SQL
      this.getConnection().connect();
      this.getConnection().execute(sql.toString());
      this.getConnection().disconnect();
   }
   
   /**
    * Actualiza la información del registro de la tabla de datos que indica el valor asociado a/los campo/s identificador/es.
    * 
    * @param ormClass Una referencia a un objeto CORM (clase POJO que está anotada con anotaciones CORM).
    * @param request Una instancia de {@link HttpServletRequest} que contiene los datos (parámetros) usados para ejecutar la senténcia UPDATE.
    * 
    * @throws Exception 
    * @throws DataException 
    * @throws SQLException 
    * @throws InvalidMappingException 
    */
   public void update(Object data) throws InvalidMappingException, SQLException, DataException, Exception
   {
      boolean first;
      StringBuilder sql = new StringBuilder();
      CormObjectField cfg;
      
      // Comprueba si el objeto proporcionado es un objeto CORM válido
      if (!OrmFactory.isValidCormObject(data.getClass()))
      {
         throw new InvalidMappingException(data.getClass().getName() + " is not a CORM object.");
      }
      
      // Genera la cláusula INSERT
      sql.append(SQL_UPDATE);
      sql.append(" ");
      sql.append(OrmFactory.getDbTableName(data.getClass()));
      sql.append(" ");

      // Genera la cláusula SET
      sql.append(SQL_SET);
      sql.append(" ");

      first = true;
      for (Method method : data.getClass().getMethods())
      {
         cfg = method.getAnnotation(CormObjectField.class);
         
         if (cfg != null)
         {
            if (!cfg.isPrimaryKey())
            {
               sql.append((first ? "" : ", "));
               sql.append(method.getAnnotation(CormObjectField.class).dbTableColumn());
               sql.append(" = ");
               sql.append(getFormatedValue(method, data));
               
               first = false;
            }
         }
      }
      
      sql.append(" ");
      
      // Genera la cláusua WHERE
      sql.append(getFilter(data));
      
      // Memoriza la senténcia SQL generada
      this.setLastSqlSentence(sql.toString());
      
      // Ejecuta la senténcia SQL
      this.getConnection().connect();
      this.getConnection().execute(sql.toString());
      this.getConnection().disconnect();
   }
   
   
   //==============================================
   // Database specific section: POSTGRESQL 9.X
   //==============================================

   // Tabla de palabras reservadas por PostgreSQL
   private final static String SQL_SELECT = "SELECT";
   private final static String SQL_FROM = "FROM";
   private final static String SQL_ORDERBY = "ORDER BY";
   private final static String SQL_ORDERBY_ASC = "Asc";
   private final static String SQL_ORDERBY_DESC = "Desc";
   private final static String SQL_INSERT = "INSERT INTO";
   private final static String SQL_INSERT_VALUES = "VALUES";
   private final static String SQL_DELETE = "DELETE FROM";
   private final static String SQL_WHERE = "WHERE";
   private final static String SQL_OPERATOR_AND = "And";
   private final static String SQL_UPDATE = "UPDATE";
   private final static String SQL_SET = "SET";
   
   private final static String POSTGRESQL_DATE_FORMAT = "yyyy/MM/dd";
   private final static String POSTGRESQL_DECIMAL_FORMAT = "#.#############################################";
   
   private final static String POSTGRESQL_NULL = "NULL";
   
   /**
    * Convierte un valor String en una cadena que lo representa.
    */
   private String convertToSql(String text)
   {
      return addStringDelimiters(text.replace("'", "''"));
   }
   
   /**
    * Convierte un valor Date en una cadena que lo representa.
    */
   private String convertToSql(Integer value)
   {
      return value.toString();
   }
   
   /**
    * Convierte un valor Date en una cadena que lo representa.
    */
   private String convertToSql(Date date)
   {
      SimpleDateFormat dt = new SimpleDateFormat(POSTGRESQL_DATE_FORMAT); 
      return addStringDelimiters(dt.format(date));
   }
   
   /**
    * Convierte un valor Float en una cadena que lo representa.
    */
   private String convertToSql(Float value)
   {
      DecimalFormat df = new DecimalFormat(OrmDriverPostgreSqlImpl.POSTGRESQL_DECIMAL_FORMAT);
      return df.format(value);
   }
   
   /**
    * Convierte un valor Double en una cadena que lo representa.
    */
   private String convertToSql(Double value)
   {
      DecimalFormat df = new DecimalFormat(OrmDriverPostgreSqlImpl.POSTGRESQL_DECIMAL_FORMAT);
      return df.format(value);
   }

   /**
    * Convierte un valor Boolean en una cadena que lo representa.
    */
   private String convertToSql(Boolean value)
   {
      return addStringDelimiters(value ? "1" : "0");
   }
   
   /**
    * Agrega delimitadores de String a una cadena.
    */
   private String addStringDelimiters(String text)
   {
      return "'" + text + "'";
   }
   
   private String getFormatedValue(Method method, Object data) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException
   {
      Object retData;
      
      // Invoca el método para obtener el valor
      retData = method.invoke(data);
      
      // Si es NULL devuelve el valor NULL dependiente del motor de BBDD
      if (retData == null)
      {
         return POSTGRESQL_NULL;
      }
      // Texto
      else if ((method.getReturnType() == String.class) || (method.getReturnType() == char.class))
      {
         return convertToSql((String) retData);
      }
      // Enteros
      else if (method.getReturnType() == Integer.class || method.getReturnType() == int.class ||
               method.getReturnType() == Long.class || method.getReturnType() == long.class ||
               method.getReturnType() == Short.class || method.getReturnType() == short.class ||
               method.getReturnType() == Byte.class || method.getReturnType() == byte.class)
      {
         return convertToSql((Integer) retData);
      }
      // Decimales
      else if (method.getReturnType() == Double.class || method.getReturnType() == double.class)
      {
         return convertToSql((Double) retData);
      }
      else if (method.getReturnType() == Float.class || method.getReturnType() == float.class)
      {
         return convertToSql((Float) retData);
      }
      // Fechas y horas
      else if (method.getReturnType() == Date.class)
      {
         return convertToSql((Date) retData);
      }
      // Booleanos
      else if (method.getReturnType() == boolean.class || method.getReturnType() == Boolean.class)
      {
         return convertToSql((Boolean) retData);
      }

      return "";
   }

   private String getFilter(Object data) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException
   {
      boolean first;
      StringBuilder sql = new StringBuilder();

      sql.append(SQL_WHERE);
      sql.append(" ");

      first = true;
      for (Method method : data.getClass().getMethods())
      {
         if (method.isAnnotationPresent(CormObjectField.class))
         {
            if (method.getAnnotation(CormObjectField.class).isPrimaryKey())
            {
               sql.append((first ? "" : " " + SQL_OPERATOR_AND + " "));
               sql.append(method.getAnnotation(CormObjectField.class).dbTableColumn());
               sql.append(" = ");
               sql.append(getFormatedValue(method, data));
               
               first = false;
            }
         }
      }

      return sql.toString();
   }
}
