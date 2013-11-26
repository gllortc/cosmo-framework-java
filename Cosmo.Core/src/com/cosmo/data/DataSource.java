package com.cosmo.data;

import java.util.UUID;

/**
 * Representa un origen de datos (base de datos).
 */
public class DataSource 
{
   private String id;
   private String jdbcDriver;
   private String cormDriver;
   private String host;
   private String port;
   private String schema;
   private String login;
   private String pwd;
   
   //==============================================
   // Constructors
   //==============================================
   
   /**
    * Constructor de la clase.
    */
   public DataSource()
   {
      this.id = UUID.randomUUID().toString();
      this.jdbcDriver = "";
      this.cormDriver = "";
      this.host = "";
      this.port = "";
      this.schema = "";
      this.login = "";
      this.pwd = "";
   }

   /**
    * Constructor de la clase.<br />
    * Este constructar se usa para crear una conexión JDBC puntual que no se deba almacenar y que no use CORM.
    * 
    * @param host Host (URL).
    * @param port Puesrto de conexión al servidor.
    * @param schema Esquema.
    * @param user usuario.
    * @param pwd Contraseña.
    * @param jdbcDriver Clase (driver) que se usará para la conexión a la base de datos.
    */
   public DataSource(String host, String port, String schema, String user, String pwd, String jdbcDriver)
   {
      this.id = UUID.randomUUID().toString();
      this.host = host;
      this.port = port;
      this.schema = schema;
      this.login = user;
      this.pwd = pwd;
      this.jdbcDriver = jdbcDriver;
      this.cormDriver = "";
   }
   
   /**
    * Constructor de la clase.
    * 
    * @param id Identioficador único del origen de datos.
    * @param host Host (URL).
    * @param port Puesrto de conexión al servidor.
    * @param schema Esquema.
    * @param user usuario.
    * @param pwd Contraseña.
    * @param jdbcDriver Clase (driver) que se usará para la conexión a la base de datos.
    * @param cormDriver Clase (driver) que se usará para la gestión de ORM.
    */
   public DataSource(String id, String host, String port, String schema, String user, String pwd, String jdbcDriver, String cormDriver)
   {
      this.id = id;
      this.host = host;
      this.port = port;
      this.schema = schema;
      this.login = user;
      this.pwd = pwd;
      this.jdbcDriver = jdbcDriver;
      this.cormDriver = cormDriver;
   }

   //==============================================
   // Properties
   //==============================================
   
   public String getId() 
   {
      return id;
   }

   public void setId(String id) 
   {
      this.id = id;
   }

   public String getHost() 
   {
      return host;
   }

   public void setHost(String host) 
   {
      this.host = host;
   }

   public String getPort()
   {
      return port;
   }

   public void setPort(String port) 
   {
      this.port = port;
   }

   public String getSchema() 
   {
      return schema;
   }

   public void setSchema(String schema) 
   {
      this.schema = schema;
   }

   public String getLogin() 
   {
      return login;
   }

   public void setLogin(String login)
   {
      this.login = login;
   }

   public String getPassword() 
   {
      return pwd;
   }

   public void setPassword(String password) 
   {
      this.pwd = password;
   }
   
   public String getJdbcDriver() 
   {
      return jdbcDriver;
   }

   public void setJdbcDriver(String jdbcDriver) 
   {
      this.jdbcDriver = jdbcDriver;
   }
   
   public String getCormDriver() 
   {
      return cormDriver;
   }

   public void setCormDriver(String cormDriver) 
   {
      this.cormDriver = cormDriver;
   }   


   //==============================================
   // Methods
   //==============================================   

   /**
    * Obtiene la URL de conexión a la base de datos.
    * 
    * @return Una cadena que representa la URL de conexión a la base de datos.
    */
   public String getConnectionUrl()
   {
      return this.getHost() + ":" + this.getPort() + "/" + this.getSchema();
   }
}
