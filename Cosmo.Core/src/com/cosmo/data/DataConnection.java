package com.cosmo.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Implementa una classe de gestió de connexions a la base de dades.
 * 
 * @author gllort
 */
public class DataConnection
{
   public static final String CONNECTION_SERVER = "cosmo.server";
   
   private static final String DRIVER_ORACLE = "oracle.jdbc.driver.OracleDriver";

   private static final String XML_TAG_CONNECTION = "connection";
   private static final String XML_ATT_ID = "id";
   private static final String XML_ATT_DRIVER = "driver";
   private static final String XML_ATT_SERVER = "server";
   private static final String XML_ATT_PORT = "port";
   private static final String XML_ATT_SCHEMA = "schema";
   private static final String XML_ATT_USER = "user";
   private static final String XML_ATT_PASSWORD = "pwd";
   
   private Boolean debug;
   private DataSource pds;
   private Connection conn = null;
   private String filePath = "";
   private String lastSqlStatement = "";
   private Boolean autoCommit = true; 
   
   /**
    * Defineix el nom que ha de tenir l'arxiu de configuració de conexions.
    * L'arxiu ha d'estar sempre a la mateixa carpeta que el JAR.
    */
   public static final String FILE_CONFIGURATION = "connections.xml";
   
   //==============================================
   // Constructors
   //==============================================
   
   public DataConnection(DataSource datasource, Boolean logDebug) throws Exception
   {
      this.pds = datasource;
   }
   
   public DataConnection(String dbServer, String dbPort, String dbName, String dbUser, String dbPassword, Boolean logDebug)
   {
      this.pds = new DataSource("custom", DataConnection.DRIVER_ORACLE, dbServer, dbPort, dbName, dbUser, dbPassword);
   }

   //==============================================
   // Properties
   //==============================================
   
   public Boolean getDebug() 
   {
      return debug;
   }

   public void setDebug(Boolean debug) 
   {
      this.debug = debug;
   }

   public Boolean getAutoCommit() 
   {
      return autoCommit;
   }

   public void setAutoCommit(Boolean autoCommit) 
   {
      this.autoCommit = autoCommit;
   }
   
   public String getLastSQLStatement()
   {
      return this.lastSqlStatement;
   }
   
   //==============================================
   // Methods
   //==============================================
   
   /**
    * Obre una connexió amb la base de dades seleccionada.
    * 
    * @throws SQLException
    * @throws ClassNotFoundException
    * @throws DataException 
    */
   public void connect() throws SQLException, ClassNotFoundException, DataException 
   {
      if (pds == null)
      {
         throw new DataException("Impossible connectar a BBDD: No hi ha cap DataSource seleccioant");
      }
      
      try 
      {
         Class.forName(pds.getDriver());
         
         conn = DriverManager.getConnection(pds.getConnectionUrl() + ":" + pds.getPort(), pds.getLogin(), pds.getPassword());
         conn.setAutoCommit(this.autoCommit);
      } 
      catch (SQLException e) 
      {
         throw e;
      }
   }

   /**
    * Tanca qualsevol connexió oberta amb la base de dades activa.
    */
   public void disconnect()
   {
      if (conn != null)
      {
         try 
         {
            if (!conn.isClosed()) 
            {
               conn.close();
            }
         } 
         catch (SQLException e) 
         {
            // Nothing to do here
         }
      }
   }
   
   /**
    * Indica si la connexió està oberta i disponible.
    * 
    * @return Retorna {@code true} si la connexió està disponible o {@code false} en qualsevol altre cas.
    */
   public boolean isOpened()
   {
      if (conn == null) 
      {
         return false;
      }
      
      try 
      {
         return !conn.isClosed();
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
      conn.commit();
   }
   
   public ResultSet executeSql(String sql) throws SQLException, ClassNotFoundException, DataException
   {
      // Memoritza la sentència SQL
      this.lastSqlStatement = sql;
      
      // Obliga a tener la conexión abierta
      if (conn == null)
      {
         this.connect();
      }
      
      Statement st = conn.createStatement();
      return st.executeQuery(sql);
   }
   
   /**
    * Executa una sentència SQL de la que no s'espera cap resultat (p. ex. INSERT, UPDATE, DELETE).
    * 
    * @param sql Sentència SQL a executar (consulta).
    * 
    * @return {@code True} si l'execució ha tingut èxit o {@code False} en qualsevol altre cas.
    */
   public boolean execute(String sql) throws SQLException, ClassNotFoundException, DataException
   {
      // Memoritza la sentència SQL
      this.lastSqlStatement = sql;
      
      // Obliga a tener la conexión abierta
      if (conn == null)
      {
         this.connect();
      }
      
      Statement st = conn.createStatement();
      return st.execute(sql);
   }
   
   /**
    * Executa una consulta SQL i retorna el valor enter ({@link Integer} de la primera fila i primera columna.
    * 
    * @param sql Sentència SQL a executar (consulta).
    * 
    * @return Un valor enter correspòn al valor de la cel·la 0,0.
    */
   public Integer executeScalar(String sql) throws SQLException, ClassNotFoundException, DataException, Exception 
   {
      // Memoritza la sentència SQL
      this.lastSqlStatement = sql;
      
      // Obliga a tener la conexión abierta
      if (conn == null)
      {
         this.connect();
      }
      
      Statement st = conn.createStatement();
      ResultSet rs = st.executeQuery(sql);
      if (!rs.next())
      {
         throw new Exception("ERROR: No es pot obtenir un ID vàlid per la nova relació.");
      }
      
      return rs.getInt(1);
   }
   
   /**
    * Executa una consulta SQL i retorna la cadena de text (o el valor en format {@link String}) de la primera fila i primera columna.
    * 
    * @param sql Sentència SQL a executar (consulta).
    * 
    * @return Una cadena de text que correspòn al valor de la cel·la 0,0.
    */
   public String executeString(String sql) throws SQLException, ClassNotFoundException, DataException, Exception 
   {
      // Memoritza la sentència SQL
      this.lastSqlStatement = sql;
      
      // Obliga a tener la conexión abierta
      if (conn == null)
      {
         this.connect();
      }
      
      Statement st = conn.createStatement();
      ResultSet rs = st.executeQuery(sql);
      if (!rs.next())
      {
         throw new Exception("ERROR: No es pot obtenir un ID vàlid per la nova relació.");
      }
      
      return rs.getString(1);
   }
   
   //==============================================
   // Static members
   //==============================================
   
   /**
    * Permet recuperar una cadena d'una fila d'un {@link ResultSet} convertint a cadena buida els valors {@code NULL}.
    * 
    * @param rs Una instancia de {@link ResultSet} que la cadena a estreure d'un camp de la fila actual.
    * @param column Índex de la columna (1 per la primera).
    * 
    * @return 
    */
   public static String getNotNullString(ResultSet rs, int column) throws SQLException
   {
      String value = rs.getString(column);
      return (value == null ? "" : value);
   }
   
   /**
    * Formata una cadena de text per a poder-la emprar en qualsevòl sentència SQL.
    * 
    * @param text Cadena de text a formatar.
    * @return Una cadena de text que es pot emprar en qualsevol sentència SQL.
    */
   public static String sqlFormatTextValue(String text)
   {
	   String ftext = text.trim().replace("'", "''");
	   return ftext;
   }

   //==============================================
   // Private members
   //==============================================
   
   /**
    * Carrega l'arxiu de definicions de connexions a dades.
    * @throws Exception 
    */
   /*private void loadConnections() throws Exception
   {
      DataSource ds;

      // Obté el nom + path de l'arxiu de configuració
      
      InputStream is = DataConnection.class.getResourceAsStream(DataConnection.FILE_CONFIGURATION);
      if (is == null)
      {
         throw new Exception("No es troba l'arxiu de definició de connexions.");
      }

         DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
         DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
         Document doc = dBuilder.parse(is);
         doc.getDocumentElement().normalize();
    
         NodeList nList = doc.getElementsByTagName(DataConnection.XML_TAG_CONNECTION);
         for (int temp = 0; temp < nList.getLength(); temp++) 
         {
            Node nNode = nList.item(temp);
            if (nNode.getNodeType() == Node.ELEMENT_NODE)
            {
               Element eElement = (Element) nNode;
    
               ds = new DataSource();
               ds.setId(eElement.getAttribute(DataConnection.XML_ATT_ID));
               ds.setDriver(eElement.getAttribute(DataConnection.XML_ATT_DRIVER));
               ds.setServer(eElement.getAttribute(DataConnection.XML_ATT_SERVER));
               ds.setPort(eElement.getAttribute(DataConnection.XML_ATT_PORT));
               ds.setSchema(eElement.getAttribute(DataConnection.XML_ATT_SCHEMA));
               ds.setLogin(eElement.getAttribute(DataConnection.XML_ATT_USER));
               ds.setPassword(eElement.getAttribute(DataConnection.XML_ATT_PASSWORD));

               this.psources.put(ds.getId(), ds);
            }
         }
         
         // Controla el cas en que no s'hagi detectat cap 
         if (this.psources.values().size() <= 0)
         {
            throw new Exception("No hi ha cap DataSource definit a l'arxiu de definició de connexions: "); // + fXmlFile.getAbsolutePath());
         }
   }*/
   
}
