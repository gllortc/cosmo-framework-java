package com.cosmo.data;

/**
 * Representa un origen de datos (base de datos).
 */
public class DataSource 
{
   private String pid;
   private String pdriver;
   private String pserver;
   private String pport;
   private String pschema;
   private String plogin;
   private String ppwd;
   
   //==============================================
   // Constructors
   //==============================================
   
   /**
    * Constructor de la clase.
    */
   public DataSource()
   {
      this.pid = "";
      this.pdriver = "";
      this.pserver = "";
      this.pport = "";
      this.pschema = "";
      this.plogin = "";
      this.ppwd = "";
   }

   /**
    * Constructor de la clase.
    * 
    * @param id Identioficador único del origen de datos.
    * @param driver Clase (driver) que se usará para la conexión a la base de datos.
    * @param server Host (URL).
    * @param port Puesrto de conexión al servidor.
    * @param schema Esquema.
    * @param user usuario.
    * @param pwd Contraseña.
    */
   public DataSource(String id, String driver, String server, String port, String schema, String user, String pwd)
   {
      this.pid = id;
      this.pdriver = driver;
      this.pserver = server;
      this.pport = port;
      this.pschema = schema;
      this.plogin = user;
      this.ppwd = pwd;
   }

   //==============================================
   // Properties
   //==============================================
   
   public String getId() 
   {
      return pid;
   }

   public void setId(String pid) 
   {
      this.pid = pid;
   }

   public String getDriver() 
   {
      return pdriver;
   }

   public void setDriver(String pdriver) 
   {
      this.pdriver = pdriver;
   }

   public String getServer() 
   {
      return pserver;
   }

   public void setServer(String pserver) 
   {
      this.pserver = pserver;
   }

   public String getPort()
   {
      return pport;
   }

   public void setPort(String pport) 
   {
      this.pport = pport;
   }

   public String getSchema() 
   {
      return pschema;
   }

   public void setSchema(String pschema) 
   {
      this.pschema = pschema;
   }

   public String getLogin() 
   {
      return plogin;
   }

   public void setLogin(String plogin)
   {
      this.plogin = plogin;
   }

   public String getPassword() 
   {
      return ppwd;
   }

   public void setPassword(String ppwd) 
   {
      this.ppwd = ppwd;
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
      return this.getServer() + ":" + this.getPort() + "/" + this.getSchema();
   }
}
