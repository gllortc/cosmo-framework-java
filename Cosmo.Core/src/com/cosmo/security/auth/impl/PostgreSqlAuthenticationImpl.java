package com.cosmo.security.auth.impl;

import java.security.GeneralSecurityException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import com.cosmo.Workspace;
import com.cosmo.data.DataConnection;
import com.cosmo.data.DataSource;
import com.cosmo.security.User;
import com.cosmo.security.UserAlreadyExistsException;
import com.cosmo.security.UserNotFoundException;
import com.cosmo.security.auth.Authentication;
import com.cosmo.security.auth.AuthenticationException;
import com.cosmo.security.auth.AuthenticationFactory;
import com.cosmo.structures.PluginProperties;
import com.cosmo.util.CryptoUtils;

/**
 * Implementa el proveedor de autenticación nativo de Cosmo (versión para JDBC/PostgreSQL).
 * <br /><br />
 * Esta implementación incorpora adicionalmente la API de administración de autenticación RDBMS.
 * 
 * @version 1.0.0
 * @author Gerard Llort
 */
public class PostgreSqlAuthenticationImpl implements Authentication
{
   private static String PARAM_LOCKCONTROL = "lockControl";
   private static String PARAM_ATTEMPTS = "lockAttempts";
   private static String PARAM_TIMEOUT = "lockTimeout";
   
   private static String TABLE_NAME = "cosmo_users";
   private static String TABLE_LOCKS = "cosmo_auth_locks";

   private Workspace workspace;
   private PluginProperties agent;
   
   
   //==============================================
   // Constructors
   //==============================================
   
   /**
    * Constructor de la clase. 
    * 
    * @param workspace Una instancia de {@link Workspace} que representa el workspace al que está conectado actualmente.
    */
   public PostgreSqlAuthenticationImpl(Workspace workspace)
   {
      this.workspace = workspace;
      this.agent = this.workspace.getProperties().getAuthenticationAgent();
   }
   
   
   //==============================================
   // Properties
   //==============================================
   
   /**
    * Devuelve un {@code hash} que contiene los parámetros de configuración del agente de seguridad.
    */
   public HashMap<String, String> getParameters()
   {
      return agent.getParams();
   }
   
   
   //==============================================
   // Methods (interface Authentication)
   //==============================================
   
   /**
    * Verifica las credenciales de un usuario.
    * 
    * @param login Login del usuario.
    * @param password Contraseña (sin encriptar) del usuario.
    * @return Una instancia de {@link User} que representa el usuario al que corresponden las credenciales proporcionadas.
    * 
    * @throws UserNotFoundException
    * @throws AuthenticationException 
    */
   @Override
   public User login(String login, String password) throws UserNotFoundException, AuthenticationException
   {
      String sql;
      User user = null;
      DataSource ds;
      DataConnection conn = null;
      
      // Comprobación de cuenta bloqueada
      if (agent.getParamBoolean(PARAM_LOCKCONTROL, false))
      {
         // Obtiene la configuración del agente
         int attemps = agent.getParamInteger(PARAM_ATTEMPTS, 5);
         int timeout = agent.getParamInteger(PARAM_TIMEOUT, 30);
         
         // Determina si la cuenta del usuario está o no bloqueada
         if (isLocked(login, attemps, timeout))
         {
            throw new AuthenticationException("La cuenta " + login + " está bloqueada.");
         }
      }
      
      try 
      {
         sql = "SELECT * " +
               "FROM  " + TABLE_NAME + " " +
               "WHERE Lower(usrlogin) = '" + login.trim().toLowerCase() + "' And " +
               "      usrpwd = '" + CryptoUtils.encrypt(password) + "'";

         ds = this.workspace.getProperties().getServerDataSource();
         conn = new DataConnection(ds);
         conn.connect();
         ResultSet rs = conn.executeSql(sql);
         if (rs.next())
         {
            user = new User();
            user.setLogin(rs.getString("usrlogin"));
            user.setMail(rs.getString("usrmail"));
            user.setName(rs.getString("usrname"));
            user.setCreated(rs.getDate("usrcreated"));
            user.setLastLogin(rs.getDate("usrlastlogin"));
            user.setLogonCount(rs.getInt("usrlogoncount"));
         }
         else
         {
            // Si tiene el control de bloqueo activado, actualiza la información de bloqueo
            if (agent.getParamBoolean(PARAM_LOCKCONTROL, false))
            {
               loginFail(login);
            }
            
            throw new UserNotFoundException();
         }
         
         // Actualiza los datos estadÃ­sticos y de control del usuario
         sql = "UPDATE " + TABLE_NAME + " " +
               "SET usrlastlogin  = current_timestamp, " +
               "    usrlogoncount = usrlogoncount + 1 " +
               "WHERE Lower(usrlogin) = '" + login.trim().toLowerCase() + "'";
         conn.execute(sql);
         
         // Confirma los cambios en la bbdd
         if (!conn.isAutoCommit()) conn.commit();
      } 
      catch (SQLException ex) 
      {
         throw new AuthenticationException(ex.getMessage(), ex);
      }
      catch (GeneralSecurityException ex)
      {
         throw new AuthenticationException(ex.getMessage(), ex);
      }
      catch (UserNotFoundException ex)
      {
         throw ex;
      }
      catch (Exception ex)
      {
         throw new AuthenticationException(ex.getMessage(), ex);
      }
      finally
      {
         conn.disconnect();
      }              

      return user;
   }
   
   /**
    * Elimina la información de autenticación actual.
    */
   @Override
   public void logout()
   {
      // Este driver no informa al SGBD de la salida del usuario
      return;
   }
   
   /**
    * Revalida la sesión de usuario.
    */
   @Override
   public void validate() 
   {
   // Este driver no revalida la sesión
      return;   
   };
   
   /**
    * Indica si el servicio usa un gateway para la autenticación de usuarios.
    */
   public boolean isLoginGatewayRequired()
   {
      return false;
   }
   
   /**
    * Indica si una respuesta corresponde al retorno de la acción de login.
    * 
    * @param request Una instancia de {@link HttpServletRequest} que cotniene el contexto de la llamada.
    * 
    * @return {@code true} si la petición corresponde al retorno de la pantalla de login o {@code false} en cualquier otro caso.
    */
   public boolean isLoginGatewayResponse(HttpServletRequest request)
   {
      return false;
   }
   
   /**
    * Detecta si una autenticación delegada (Login Gateway) ha sido exitosa.<br />
    * Las clases que extiendan a {@link AuthenticationFactory} serán responsables de obtener los datos del usuario 
    * autenticado en el sistema externo, ya sea mediante servicios REST u otros mecanismos.
    * 
    * @param request Una instancia de {@link HttpServletRequest} que cotniene el contexto de la llamada.
    * 
    * @return Una instancia de {@link User} que contiene las propiedades del usuario autenticado o {@code null} en cualquier otro caso. 
    */
   public User getLoginGatewayUser(HttpServletRequest request)
   {
      return null;
   }
   
   /**
    * Devuelve la URL usada para la autenticación de usuarios.
    */
   public String getLoginGatewayUrl()
   {
      return null;
   }
   
   
   //==============================================
   // Methods
   //==============================================
   
   /**
    * Comprueba si un determinado login existe.
    * 
    * @param login Una cadena que contiene el login a comprobar.
    * 
    * @return {@code true} si el login corresponde a una cuenta de usuario o {@code false} en cualquier otro caso.
    * 
    * @throws AuthenticationException
    */
   public boolean loginExist(String login) throws AuthenticationException
   {
      String sSQL;
      DataSource ds;
      DataConnection conn = null;
      
      try 
      {
         // Comprueba si existe el usuario
         sSQL = "SELECT Count(*) " +
                "FROM " + TABLE_NAME + " " +
                "WHERE Lower(usrlogin) = '" + login.trim().toLowerCase() + "'";
         
         ds = this.workspace.getProperties().getServerDataSource();
         conn = new DataConnection(ds);
         conn.connect();
         
         return (conn.executeScalar(sSQL) > 0);
      }
      catch (Exception ex)
      {
         throw new AuthenticationException(ex.getMessage(), ex);
      }
      finally
      {
         conn.disconnect();
      }
   }
   
   /**
    * Crea una nueva cuenta de usuario.
    * 
    * @param user Una instancia de {@link User} que representa el nuevo usuario.
    *     
    * @throws UserAlreadyExistsException
    * @throws AuthenticationException
    */
   public void add(User user, String password) throws UserAlreadyExistsException, AuthenticationException
   {
      String sSQL;
      ResultSet rs;
      DataSource ds;
      DataConnection conn = null;
      
      try 
      {
         // Comprueba si existe algún usuario con mismo LOGIN o CORREO
         sSQL = "SELECT Count(*) " +
                "FROM " + TABLE_NAME + " " +
                "WHERE Lower(usrlogin) = '" + user.getLogin().trim().toLowerCase() + "' Or " +
                "      Lower(usrmail)  = '" + user.getMail().trim().toLowerCase()  + "'";
         
         ds = this.workspace.getProperties().getServerDataSource();
         conn = new DataConnection(ds);
         conn.connect();
         rs = conn.executeSql(sSQL);
         if (rs.next() && rs.getInt(1) > 0)
         {
            throw new UserAlreadyExistsException();
         }
         
         sSQL = "INSERT INTO " + TABLE_NAME + " (usrlogin, usrmail, usrpwd, usrname, usrcreated, usrlastlogin, usrlogoncount) " +
                "VALUES ('" + user.getLogin() + "', " +
                "        '" + user.getMail() + "', " +
                "        '" + CryptoUtils.encrypt(password) + "', " +
                "        '" + user.getName() + "', " +
                "             current_timestamp, " +
                "             null, " +
                "         " + 0 + ")";

         conn.execute(sSQL);
         
         // Confirma los cambios en la bbdd
         if (!conn.isAutoCommit()) conn.commit();
      } 
      catch (Exception ex)
      {
         throw new AuthenticationException(ex.getMessage(), ex);
      }
      finally
      {
         conn.disconnect();
      }
   }
   
   /**
    * Actualiza los datos de un usuario (el login no se puede modificar y la contraseña no se actualiza mediante este método).
    * 
    * @param user Una instancia de {@link User} que representa el nuevo usuario.
    *     
    * @throws UserAlreadyExistsException
    * @throws AuthenticationException
    */
   public void update(User user) throws UserNotFoundException, AuthenticationException
   {
      String sSQL;
      DataSource ds;
      DataConnection conn = null;
      
      // Comprueba si existe el usuario especificado
      if (!loginExist(user.getLogin()))
      {
         throw new UserNotFoundException();
      }
      
      try 
      {
         sSQL = "UPDATE " + TABLE_NAME + " " +
                "SET   usrmail = '" + DataConnection.sqlFormatTextValue(user.getMail()) + "', " +
                "      usrname = '" + DataConnection.sqlFormatTextValue(user.getName()) + "' " +
                "WHERE Lower(usrlogin) = '" + user.getLogin().trim().toLowerCase() + "'";

         ds = this.workspace.getProperties().getServerDataSource();
         conn = new DataConnection(ds);
         conn.connect();
         conn.execute(sSQL);

         // Confirma los cambios en la bbdd
         if (!conn.isAutoCommit()) conn.commit();
      } 
      catch (Exception ex)
      {
         throw new AuthenticationException(ex.getMessage(), ex);
      }
      finally
      {
         conn.disconnect();
      }
   }
   
   /**
    * Elimina una cuenta de usuario.
    * 
    * @param login Una cadena que representa el login del usuario.
    *     
    * @throws UserNotFoundException
    * @throws AuthenticationException
    */
   public void delete(String login) throws UserNotFoundException, AuthenticationException
   {
      String sSQL;
      DataSource ds;
      DataConnection conn = null;
      
      // Comprueba si existe el usuario especificado
      if (!loginExist(login))
      {
         throw new UserNotFoundException();
      }
      
      try 
      {
         // Elimina el usuario
         sSQL = "DELETE FROM " + TABLE_NAME + " " +
                "WHERE Lower(usrlogin) = '" + login.trim().toLowerCase() + "'";

         ds = this.workspace.getProperties().getServerDataSource();
         conn = new DataConnection(ds);
         conn.connect();
         conn.execute(sSQL);

         // Confirma los cambios en la bbdd
         if (!conn.isAutoCommit()) conn.commit();
      } 
      catch (Exception ex)
      {
         throw new AuthenticationException(ex.getMessage(), ex);
      }
      finally
      {
         conn.disconnect();
      }
   }
   
   /**
    * Actualiza el password de un determinado usuario.
    * 
    * @param login Login del usuario.
    * @param oldPassword Contraseña actual.
    * @param newPassword Nueva contraseña.
    * 
    * @throws UserNotFoundException
    * @throws AuthenticationException
    */
   public void setUserPassword(String login, String oldPassword, String newPassword) throws UserNotFoundException, AuthenticationException
   {
      String sSQL;
      DataSource ds;
      DataConnection conn = null;
      
      try 
      {
         // Comprueba que exista el usuario y que el password actual sea el correcto
         sSQL = "SELECT Count(*) " +
                "FROM  " + TABLE_NAME + " " +
                "WHERE Lower(usrlogin) = '" + login.trim().toLowerCase() + "' And " +
                "      usrpwd = '" + CryptoUtils.encrypt(oldPassword) + "'";

         ds = this.workspace.getProperties().getServerDataSource();
         conn = new DataConnection(ds);
         conn.connect();
         if (conn.executeScalar(sSQL) <= 0)
         {
            throw new UserNotFoundException();
         }
         
         // Actualiza la contraseña del usuario
         sSQL = "UPDATE " + TABLE_NAME + " " +
                "SET   usrpwd = '" + CryptoUtils.encrypt(newPassword) + "' " +
                "WHERE Lower(usrlogin) = '" + login.trim().toLowerCase() + "'";
         
         conn.execute(sSQL);
         
         // Confirma los cambios en la bbdd
         if (!conn.isAutoCommit()) conn.commit();
      } 
      catch (Exception ex)
      {
         throw new AuthenticationException(ex.getMessage(), ex);
      }
      finally
      {
         conn.disconnect();
      }
   }
   
   /**
    * Obtiene un listado con todos los usuarios.
    * 
    * @return Una instancia de {@link ResultSet} que contiene los datos del listado.
    * 
    * @throws AuthenticationException
    */
   public ArrayList<User> getUsers() throws AuthenticationException
   {
      String sSQL;
      DataSource ds;
      DataConnection conn = null;
      ResultSet rs;

      ArrayList<User> users = new ArrayList<User>();
      
      try 
      {
         sSQL = "SELECT   * " +
                "FROM     " + TABLE_NAME + " " +
                "ORDER BY usrlogin Asc";
         
         ds = this.workspace.getProperties().getServerDataSource();
         conn = new DataConnection(ds);
         conn.connect();

         rs = conn.executeSql(sSQL);
         while (rs.next())
         {
            users.add(readUser(rs));
         }
         
         return users;
      }
      catch (Exception ex)
      {
         throw new AuthenticationException(ex.getMessage(), ex);
      }
      finally
      {
         conn.disconnect();
      }
   }
   
   /**
    * Obtiene una lista de usuario cuyo perfil contiene determinado texto.<br />
    * Los campos dónde se efectúa la búsqueda son: {@code usrlogin}, {@code usrmail} y {@code usrname}.
    * 
    * @param filter La cadena que debe contener al menos uno de los campos (como fragmento o cadena completa). 
    * 
    * @return Una lista de instancias {@link User} que coinciden con la búsqueda.
    * 
    * @throws AuthenticationException
    */
   public ArrayList<User> findUsers(String filter) throws AuthenticationException
   {
      String sSQL;
      DataSource ds;
      DataConnection conn = null;
      ResultSet rs;

      ArrayList<User> users = new ArrayList<User>();
      
      try 
      {
         sSQL = "SELECT   * " +
                "FROM     " + TABLE_NAME + " " +
                "WHERE usrlogin LIKE '%" + DataConnection.sqlFormatTextValue(filter) + "%' Or " +
                "      usrname  LIKE '%" + DataConnection.sqlFormatTextValue(filter) + "%' Or " +
                "      usrmail  LIKE '%" + DataConnection.sqlFormatTextValue(filter) + "%' " +
                "ORDER BY usrlogin Asc";
         
         ds = this.workspace.getProperties().getServerDataSource();
         conn = new DataConnection(ds);
         conn.connect();

         rs = conn.executeSql(sSQL);
         while (rs.next())
         {
            users.add(readUser(rs));
         }
         
         return users;
      }
      catch (Exception ex)
      {
         throw new AuthenticationException(ex.getMessage(), ex);
      }
      finally
      {
         conn.disconnect();
      }
   }
   
   /**
    * Obtiene un listado con todos los usuarios.
    * 
    * @return Una instancia de {@link ResultSet} que contiene los datos del listado.
    * 
    * @throws AuthenticationException
    */
   public ResultSet getUsersList() throws AuthenticationException
   {
      String sSQL;
      DataSource ds;
      DataConnection conn = null;
      
      try 
      {
         sSQL = "SELECT   usrlogin   As Login, " +
                "         usrname    As Nom, " +
                "         usrmail    As Mail, " +
                "         usrcreated As Creat " +
                "FROM     cosmo_users " +
                "ORDER BY cosmo_users";
         
         ds = this.workspace.getProperties().getServerDataSource();
         conn = new DataConnection(ds);
         conn.connect();
         
         return conn.executeSql(sSQL);
      }
      catch (Exception ex)
      {
         throw new AuthenticationException(ex.getMessage(), ex);
      }
      finally
      {
         conn.disconnect();
      }
   }
   
   
   //==============================================
   // Private members
   //==============================================
   
   /**
    * Lee un usuario de un registro en una instancia de {@link ResultSet}.
    * 
    * @param rs La instancia de {@link ResultSet} con el cursor debidamente posicionado.
    * 
    * @return Una instancia de {@link User} que representa al usuari leido.
    * 
    * @throws SQLException
    */
   private User readUser(ResultSet rs) throws SQLException
   {
      User user = new User();
      user.setLogin(rs.getString("usrlogin"));
      user.setMail(rs.getString("usrmail"));
      user.setName(rs.getString("usrname"));
      user.setCreated(rs.getDate("usrcreated"));
      user.setLastLogin(rs.getDate("usrlastlogin"));
      user.setLogonCount(rs.getInt("usrlogoncount"));
      
      return user;
   }
   
   /**
    * Determina si la cuenta de usuario se encuentra bloqueada (por exceso de intentos fallidos de login).
    * <br /><br />
    * La llamada a este método también limpia la tabla de los registros cuyo bloqueo a caducado, de esta manera no se 
    * precisa mantenimiento de esta tabla ya que se actualiza a cada login que realiza cualquier usuario.
    * 
    * @param login Una cadena que contiene el login para el que se desea realizar la comprobación.
    * @param attempts Un valor entero que representa el número de intentos fallidos necesarios para bloquear una cuenta.
    * @param timeout Número de minutos que se mantendrá la cuenta bloqueada.
    * 
    * @return {@code false} si la cuenta está habilitada o {@code true} si la cuenta se encuentra bloqueada (o en cualquier otro caso).
    * 
    * @throws AuthenticationException 
    */
   private boolean isLocked(String login, int attempts, int timeout) throws AuthenticationException
   {
      String sql;
      DataSource ds;
      DataConnection conn = null;
      
      try 
      {
         // Obtiene y abre la conexión a BBDD
         ds = this.workspace.getProperties().getServerDataSource();
         conn = new DataConnection(ds);
         conn.connect();
         
         // Limpia bloqueos caducados (de más de [timeout] minutos)
         sql = "DELETE FROM " + TABLE_LOCKS + " " +
               "WHERE ((DATE_PART('day', CURRENT_TIMESTAMP - lastattempt) * 24 + " +
               "        DATE_PART('hour', CURRENT_TIMESTAMP - lastattempt)) * 60 + " +
               "        DATE_PART('minute', CURRENT_TIMESTAMP - lastattempt) >= " + timeout + ")";
         conn.execute(sql);
         
         // Consulta si el usuario dispone de un registro bloqueado:
         // Dispone de N intentos (o más) y el último intento hace menos de M minutos que se produjo
         sql = "SELECT Count(*) " +
               "FROM " + TABLE_LOCKS + " " +
               "WHERE ((DATE_PART('day', CURRENT_TIMESTAMP - lastattempt) * 24 + " +
               "        DATE_PART('hour', CURRENT_TIMESTAMP - lastattempt)) * 60 + " +
               "        DATE_PART('minute', CURRENT_TIMESTAMP - lastattempt) < " + timeout + ") And " +
               "        lower(login) = '" + DataConnection.sqlFormatTextValue(login) + "' And " +
               "        fails >= " + attempts;
         int nregs = conn.executeScalar(sql);
         
         return (nregs > 0);
      } 
      catch (SQLException ex) 
      {
         throw new AuthenticationException(ex.getMessage(), ex);
      }
      catch (GeneralSecurityException ex)
      {
         throw new AuthenticationException(ex.getMessage(), ex);
      }
      catch (Exception ex)
      {
         throw new AuthenticationException(ex.getMessage(), ex);
      }
      finally
      {
         conn.disconnect();
      }
   }
   
   /**
    * Registra un fallo de login, actualizando los datos de registro para evitar intentos de acceso mediante fuerza bruta.
    * 
    * @param login Una cadena que contiene el login para el que se desea registrar el fallo de autenticación.
    *  
    * @throws AuthenticationException
    */
   private void loginFail(String login) throws AuthenticationException
   {
      String sql;
      DataSource ds;
      DataConnection conn = null;
      
      try 
      {
         // Obtiene y abre la conexión a BBDD
         ds = this.workspace.getProperties().getServerDataSource();
         conn = new DataConnection(ds);
         conn.connect();
         
         // Consulta si el login tiene un registro asociado
         sql = "SELECT Count(*) " +
               "FROM  " + TABLE_LOCKS + " " +
               "WHERE lower(login) = '" + DataConnection.sqlFormatTextValue(login) + "'";
         int nregs = conn.executeScalar(sql);
         
         if (nregs > 0)
         {
            sql = "UPDATE " + TABLE_LOCKS + " " +
                  "SET lastattempt = CURRENT_TIMESTAMP, fails = fails + 1 " +
                  "WHERE lower(login) = '" + DataConnection.sqlFormatTextValue(login) + "'";
            conn.execute(sql);
         }
         else if (nregs <= 0)
         {
            if (loginExist(login))
            {
               sql = "INSERT INTO " + TABLE_LOCKS + " (login, fails, lastattempt, ipaddress) " +
                     "VALUES ('" + DataConnection.sqlFormatTextValue(login) + "', 1, CURRENT_TIMESTAMP, '" + workspace.getServerRequest().getRemoteAddr() + "')";
               conn.execute(sql);
            }
         }
         
         // Confirma los cambios en la bbdd
         if (!conn.isAutoCommit()) conn.commit();
      } 
      catch (SQLException ex) 
      {
         throw new AuthenticationException(ex.getMessage(), ex);
      }
      catch (GeneralSecurityException ex)
      {
         throw new AuthenticationException(ex.getMessage(), ex);
      }
      catch (Exception ex)
      {
         throw new AuthenticationException(ex.getMessage(), ex);
      }
      finally
      {
         conn.disconnect();
      }
   }
   
}
