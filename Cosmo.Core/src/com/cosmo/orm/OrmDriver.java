package com.cosmo.orm;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.cosmo.data.DataAgent;
import com.cosmo.data.DataException;

/**
 * Declara una clase abstracta que debe servir como base para la implementación de drivers ORM para Cosmo (CORM).
 * 
 * @author Gerard Llort
 */
public abstract class OrmDriver 
{
   // Declaración de variables locales
   private String lastSqlSentence;
   private DataAgent connection;


   //==============================================
   // Constructors
   //==============================================

   /**
    * Constructor de la clase {@link OrmDriver}.
    * 
    * @param connection Un objeto de conexión a datos.
    */
   public OrmDriver(DataAgent connection)
   {
      this.lastSqlSentence = "";
      this.connection = connection;
   }


   //==============================================
   // Abstract members
   //==============================================

   /**
    * Devuelve el nombre del proveedor.<br />
    * Debe especificar claramente para qué gestor de BBDD está implementado (nombre del producto).
    */
   public abstract String getProviderName();

   /**
    * Devuelve el nombre cualificado de la clase que actúa de driver para la conexión JDBC.
    */
   public abstract String getJdbcDriver();

   /**
    * Genera una senténcia SELECT a partir de un objeto CORM que selecciona todos los registros.
    * 
    * @param ormObject Una referencia a un objeto CORM (clase POJO que está anotada con anotaciones CORM).
    * @param showAllColumns {@code true} muestra todas las columnas y {@code false} sólo muestra las columnas correspondientes a un listado de objetos. 
    * 
    * @return Una instancia de {@link ResultSet} que contiene los datos obtenidos en la consulta.
    * 
    * @throws InvalidMappingException
    * @throws ClassNotFoundException 
    * @throws DataException 
    * @throws SQLException 
    * @throws Exception 
    */
   public abstract ResultSet select(Class<?> ormObject, boolean showAllColumns) throws InvalidMappingException, SQLException, DataException, Exception;

   /**
    * Genera una senténcia SELECT que devuelve un listado de objetos del tipo apuntado por el objeto CORM proporcionado.
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
   public ResultSet select(Class<?> ormObject) throws InvalidMappingException, SQLException, DataException, Exception
   {
      return select(ormObject, true);
   }

   /**
    * Obtiene un registro a partir de una instancia de un objeto CORM.
    * 
    * @param ormObject Clase que contiene los datos de consulta (todos los campos que forman parte de la clave principal establecidos).
    * 
    * @return Una instancia del objeto CORM con los datos obtenidos.
    * 
    * @throws InvalidMappingException
    * @throws ClassNotFoundException 
    * @throws DataException 
    * @throws SQLException 
    * @throws Exception 
    */
   public abstract Object get(Object data) throws InvalidMappingException, SQLException, DataException, Exception;

   /**
    * Genera una senténcia INSERT INTO a partir de una instancian de un objeto CORM.
    * 
    * @param data Clase que contiene los datos a insertar.
    * 
    * @throws InvalidMappingException
    * @throws DataException 
    * @throws SQLException 
    * @throws Exception 
    */
   public abstract void insert(Object data) throws InvalidMappingException, SQLException, DataException, Exception;

   /**
    * Elimina el registro de la tabla de datos que indica el valor asociado a/los campo/s identificador/es.
    * 
    * @param data Una instancia de un objeto CORM que contiene los datos para el borrado (todos los campos de la clave principal establecidos).
    * 
    * @throws Exception 
    * @throws DataException 
    * @throws SQLException 
    * @throws InvalidMappingException 
    */
   public abstract void delete(Object data) throws InvalidMappingException, SQLException, DataException, Exception;

   /**
    * Actualiza la información del registro de la tabla de datos que indica el valor asociado a/los campo/s identificador/es.
    * 
    * @param data Una instancia de un objeto CORM que contiene los datos para la actualización.
    * 
    * @throws Exception 
    * @throws DataException 
    * @throws SQLException 
    * @throws InvalidMappingException 
    */
   public abstract void update(Object data) throws InvalidMappingException, SQLException, DataException, Exception;


   //==============================================
   // Properties
   //==============================================

   /**
    * Devuelve la última senténcia SQL generada y ejecutada.
    */
   public String getLastSqlSentence() 
   {
      return lastSqlSentence;
   }

   /**
    * Establece la última senténcia SQL generada y ejecutada.<br />
    * Este método sólo será usado por las implementaciones de los distintos ORM.
    */
   public void setLastSqlSentence(String sql)
   {
      this.lastSqlSentence = sql;
   }

   /**
    * Devuelve la conexión a BBDD usada por la instancia.
    */
   public DataAgent getConnection() 
   {
      return connection;
   }
}
