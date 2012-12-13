package com.cosmo.data.adapter;

import java.sql.SQLException;

import com.cosmo.data.DataConnection;
import com.cosmo.data.DataException;

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
    * Debe especificar claramente para qué BBDD está implementado.
    */
   public abstract String getProviderName(); 
   
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
