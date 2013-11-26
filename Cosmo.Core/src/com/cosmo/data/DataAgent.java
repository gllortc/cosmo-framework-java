package com.cosmo.data;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.cosmo.structures.PluginProperties;

/**
 * Implementa la estructura abstracta de un agente de conexión a datos.<br />
 * Todas las implementaciones de agentes de conexión deberán extender de esta clase.
 * 
 * @author Gerard Llort
 */
public abstract class DataAgent
{
   // Declaración de variables locales
   private boolean autoCommit;
   private String lastSqlStatement;
   private PluginProperties properties;
   private Connection conn;


   //==============================================
   // Constructors
   //==============================================

   /**
    * Constructor de la clase {@link DataAgent}.
    * 
    * @param properties Una instancia de {@link PluginProperties} que contiene los parámetros de configuración.
    */
   public DataAgent(PluginProperties properties)
   {
      this.conn = null;
      this.properties = properties;
      this.autoCommit = true;
      this.lastSqlStatement = "";
   }


   //==============================================
   // Properties
   //==============================================

   /**
    * Obtiene las propiedades de configuración de la conexión.
    * 
    * @return Una instancia de {@link PluginProperties} que contiene todos los parámetros de configuración de la conexión.
    */
   public PluginProperties getProperties()
   {
      return this.properties;
   }

   public boolean isAutoCommit()
   {
      return autoCommit;
   }

   public void setAutoCommit(boolean autoCommit)
   {
      this.autoCommit = autoCommit;
   }

   /**
    * Devuelve una cadena que contiene la última senténcia SQL ejecutada por el agente.
    */
   public String getLastSQLStatement()
   {
      return this.lastSqlStatement;
   }


   //==============================================
   // Methods
   //==============================================

   /**
    * Permite ontener la conexión JDBC a datos.
    * 
    * @return Una instancia de {@link Connection} que permite usar la conexión a base de datos.
    */
   public Connection getConnection()
   {
      return this.conn;
   }

   /**
    * Establece la conexión JDBC que se usará para operar con la base de datos.
    */
   public void setConnection(Connection connection)
   {
      this.conn = connection;
   }

   /**
    * Abre una conexión con la base de datos usando el mecanismo implementado por el agente de datos.
    * 
    * @throws DataException 
    */
   public abstract void connect() throws DataException; 

   /**
    * Cierra la conexión con la base de datos.
    */
   public abstract void disconnect();

   /**
    * Indica si la connexión está abierta y disponible.
    * 
    * @return Retorna {@code true} si la connexió está disponible o {@code false} en qualsevol altre cas.
    */
   public boolean isConnected()
   {
      if (this.conn == null)
      {
         return false;
      }

      try
      {
         return !(this.conn.isClosed());
      } 
      catch (SQLException e)
      {
         return false;
      }
   }

   /**
    * Envia els canvis a la base de dades.
    * 
    * @throws SQLException 
    */
   public void commit() throws SQLException
   {
      this.conn.commit();
   }

   /**
    * Ejecuta una senténcia SQL que puede devolver un conjunto de datos o no.
    * 
    * @param sql Una cadena que contiene la senténcia SQL a ejecutar.
    * 
    * @return Una instancia de {@link ResultSet} con los datos obtenidos (sólo si la tenténcia es de consulta).
    * 
    * @throws DataException
    */
   public ResultSet executeSql(String sql) throws DataException
   {
      // Memoriza la senténcia SQL
      this.lastSqlStatement = sql;

      // Obliga a tener la conexión abierta
      if (this.conn == null)
      {
         this.connect();
      }

      try
      {
         Statement st = conn.createStatement();

         return st.executeQuery(sql);
      }
      catch (SQLException ex)
      {
         throw new DataException(ex.getMessage(), ex);
      }
   }

   /**
    * Ejecuta una senténcia SQL de la que no se espera ningún resultado (p. ej. INSERT, UPDATE, DELETE).
    * 
    * @param sql Una cadena que contiene la senténcia SQL a ejecutar.
    * 
    * @return {@code True} si la ejecución ha tenido éxito o {@code false} en cualquier otro caso.
    * 
    * @throws DataException
    */
   public boolean execute(String sql) throws DataException
   {
      // Memoriza la senténcia SQL
      this.lastSqlStatement = sql;

      // Obliga a tener la conexión abierta
      if (this.conn == null)
      {
         this.connect();
      }

      Statement st;
      try
      {
         st = this.conn.createStatement();
         return st.execute(sql);
      }
      catch (SQLException ex)
      {
         throw new DataException(ex.getMessage(), ex);
      }
      
   }

   /**
    * Executa una consulta SQL i retorna el valor enter ({@link Integer} de la primera fila i primera columna.
    * 
    * @param sql Una cadena que contiene la senténcia SQL a ejecutar.
    * 
    * @return Un valor entero que corresponde al valor de la posición fila 0 y columna 1.
    * 
    * @throws DataException
    */
   public Integer executeScalar(String sql) throws DataException 
   {
      // Memoritza la senténcia SQL
      this.lastSqlStatement = sql;

      // Obliga a tener la conexión abierta
      if (this.conn == null)
      {
         this.connect();
      }

      try
      {
         Statement st = this.conn.createStatement();
         ResultSet rs = st.executeQuery(sql);
         if (!rs.next())
         {
            throw new DataException("The query has no results.");
         }

         return rs.getInt(1);
      }
      catch (SQLException ex)
      {
         throw new DataException(ex.getMessage(), ex);
      }
   }

   /**
    * Ejecuta una consulta SQL y devuelve la cadena de texto (o el valor en formato {@link String}) de la primera fila y primera columna.
    * 
    * @param sql Una cadena que contiene la senténcia SQL a ejecutar.
    * 
    * @return Una cadena de texto que corresponde al valor de la posición fila 1 y columna 1.
    * 
    * @throws DataException
    */
   public String executeString(String sql) throws DataException
   {
      // Memoritza la senténcia SQL
      this.lastSqlStatement = sql;

      // Obliga a tener la conexión abierta
      if (this.conn == null)
      {
         this.connect();
      }

      try
      {
         Statement st = this.conn.createStatement();
         ResultSet rs = st.executeQuery(sql);
         if (!rs.next())
         {
            throw new DataException("The query has no results.");
         }
   
         return rs.getString(1);
      }
      catch (SQLException ex)
      {
         throw new DataException(ex.getMessage(), ex);
      }
   }


   //==============================================
   // Static members
   //==============================================

   /**
    * Permite recuperar una cadena de una columna en un {@link ResultSet} convirtiendo en una cadena vacía los valores {@code null} (en la fila actual).
    * 
    * @param rs La instancia de {@link ResultSet} que contiene los valores. 
    * @param column Índice de la columna (1 para la primera).
    * 
    * @return Una cadena que contiene el valor de la columna indicada por {@code column} en la fila actual.
    */
   public static String getNotNullString(ResultSet rs, int column) throws SQLException
   {
      String value = rs.getString(column);

      return (value == null ? "" : value);
   }

   /**
    * Formatea una cadena de texto pera ser usada en una senténcia SQL.
    * 
    * @param text Cadena de texto a formatear.
    * 
    * @return Una cadena de texto que se puede usar en cualquier senténcia SQL.
    */
   public static String sqlFormatTextValue(String text)
   {
      String ftext = text.trim().replace("'", "''");

      return ftext;
   }
}
