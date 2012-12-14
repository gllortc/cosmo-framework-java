package com.cosmo.data.orm;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

import com.cosmo.data.DataConnection;
import com.cosmo.data.DataException;
import com.cosmo.data.adapter.InvalidMappingException;

/**
 * Declara una clase abstracta que debe servir como base para la implementación de drivers ORM para Cosmo.
 * 
 * @author Gerard Llort
 */
public abstract class OrmDriver 
{
   private String lastSqlSentence;
   private DataConnection connection;
   
   //==============================================
   // Constructors
   //==============================================
   
   /**
    * Constructor de la clase.
    * 
    * @param connection Un objeto de conexión a datos.
    */
   public OrmDriver(DataConnection connection)
   {
      this.lastSqlSentence = "";
      this.connection = connection;
   }
   
   //==============================================
   // Properties
   //==============================================
   
   /**
    * Devuelve el nombre del proveedor.<br />
    * Debe especificar claramente para qué gestor de BBDD está implementado (nombre del producto).
    */
   public abstract String getProviderName(); 
   
   /**
    * Devuelve el nombre cualificado de la clase que actúa de driver para la conexión JDBC.
    * Debe especificar claramente para qué BBDD está implementado.
    */
   public abstract String getJdbcDriver(); 
   
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
   public DataConnection getConnection() 
   {
      return connection;
   }   
   
   //==============================================
   // Methods
   //==============================================

   /**
    * Recupera los valores, los coloca en una instancia de la clase y agrega los datos en la base de datos.
    * 
    * @param ormObject
    * @param request
    * 
    * @throws Exception 
    * @throws DataException 
    * @throws SQLException 
    * @throws InvalidMappingException 
    */
   public abstract Object add(Class<?> ormObject, HttpServletRequest request) throws InvalidMappingException, SQLException, DataException, Exception;
   
   /**
    * Genera una senténcia INSERT INTO a partir de una instancian de clase.
    * 
    * @param data Clase que contiene los datos a insertar.
    * 
    * @throws InvalidMappingException
    * @throws DataException 
    * @throws SQLException 
    * @throws Exception 
    */
   public abstract void add(Object data) throws InvalidMappingException, SQLException, DataException, Exception;
}
