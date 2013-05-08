package com.cosmo.security.providers;

import java.security.GeneralSecurityException;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

import com.cosmo.Workspace;
import com.cosmo.data.DataConnection;
import com.cosmo.data.DataSource;
import com.cosmo.security.User;
import com.cosmo.security.User.UserStates;
import com.cosmo.security.Agent;
import com.cosmo.security.UserAlreadyExistsException;
import com.cosmo.security.UserNotFoundException;
import com.cosmo.util.CryptoUtils;

/**
 * Proveedor de seguridad nativo de Cosmo.<br />
 * Este proveedor requiere conexión a BBDD y tener las tablas de usuarios de Cosmo.
 * 
 * @author Gerard Llort
 */
public class PostgreSqlAuthenticationProvider extends AuthenticationProvider 
{
   private Workspace workspace;
   private Agent agent;
   
   private static String PARAM_LOCKCONTROL = "lockControl";
   private static String PARAM_ATTEMPTS = "lockAttempts";
   private static String PARAM_TIMEOUT = "lockTimeout";
   
   private static String TABLE_NAME = "cosmo_users";
   private static String TABLE_LOCKS = "cosmo_auth_locks";
   
   //==============================================
   // Constructors
   //==============================================
   
   /**
    * Constructor de la clase. 
    * 
    * @param workspace Una instancia de {@link Workspace} que representa el workspace al que está conectado actualmente.
    */
   public PostgreSqlAuthenticationProvider(Workspace workspace)
   {
      this.workspace = workspace;
      this.agent = this.workspace.getProperties().getAuthenticationAgent();
   }
   
   
   //==============================================
   // Methods
   //==============================================
   
   /**
    * Verifica las credenciales de un usuario.
    * 
    * @param login Login del usuario.
    * @param password Contraseña (sin encriptar) del usuario.
    * @return Una instancia de {@link User} que representa el usuario al que corresponden las credenciales proporcionadas.
    * 
    * @throws UserNotFoundException
    * @throws AuthenticationProviderException 
    */
   @Override
   public User login(String login, String password) throws UserNotFoundException, AuthenticationProviderException
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
            throw new AuthenticationProviderException("La cuenta " + login + " está bloqueada.");
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
            user.setId(rs.getInt("usrid"));
            user.setLogin(rs.getString("usrlogin"));
            user.setPwd(password);
            user.setMail(rs.getString("usrmail"));
            user.setName(rs.getString("usrname"));
            user.setStatus(UserStates.valueOf(rs.getInt("usrstatus")));
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
               "SET usrlastlogin = current_timestamp, " +
               "    usrlogoncount = usrlogoncount + 1 " +
               "WHERE usrid = " + user.getId();
         conn.execute(sql);
         
         // Confirma los cambios en la bbdd
         if (!conn.isAutoCommit()) conn.commit();
      } 
      catch (SQLException ex) 
      {
         throw new AuthenticationProviderException(ex.getMessage(), ex);
      }
      catch (GeneralSecurityException ex)
      {
         throw new AuthenticationProviderException(ex.getMessage(), ex);
      }
      catch (UserNotFoundException ex)
      {
         throw ex;
      }
      catch (Exception ex)
      {
         throw new AuthenticationProviderException(ex.getMessage(), ex);
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
      
   }
   
   /**
    * Revalida la sesión de usuario.
    */
   @Override
   public void validate() 
   {
      return;   
   };
   
   /**
    * Indica si el servicio usa un gateway para la autenticación de usuarios.
    */
   public boolean isLoginGatewayRequired()
   {
      return false;
   }
   
   public boolean isLoginGatewayValidated(HttpServletRequest request)
   {
      return false;
   }
   
   /**
    * Devuelve la URL usada para la autenticación de usuarios.
    */
   public String getLoginGatewayUrl()
   {
      return null;
   }
   
   /**
    * Crea una nueva cuenta de usuario.
    * 
    * @param user Una instancia de {@link User} que representa el nuevo usuario.
    *     
    * @throws UserAlreadyExistsException
    * @throws AuthenticationProviderException
    */
   public void add(User user) throws UserAlreadyExistsException, AuthenticationProviderException
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
         
         // Agrega la petición
         sSQL = "INSERT INTO " + TABLE_NAME + " (usrlogin, usrmail, usrpwd, usrname, usrcity, usroptions, usrstatus, usrcreated, usrlastlogin, usrlogoncount) " +
                "VALUES " +
                "('" + user.getLogin() + "', " +
                " '" + user.getMail() + "', " +
                " '" + CryptoUtils.encrypt(user.getPwd()) + "', " +
                " '" + user.getName() + "', " +
                " '" + user.getCity() + "', " +
                "  " + 0 + ", " +
                "  " + AuthenticationProvider.statusToNumber(user.getStatus()) + ", " +
                "  current_timestamp, " +
                "  null, " +
                "  " + 0 + ")";
         conn.execute(sSQL);
         
         // Confirma los cambios en la bbdd
         if (!conn.isAutoCommit()) conn.commit();
      } 
      catch (SQLException ex) 
      {
         throw new AuthenticationProviderException(ex.getMessage(), ex);
      }
      catch (GeneralSecurityException ex)
      {
         throw new AuthenticationProviderException(ex.getMessage(), ex);
      }
      catch (UserAlreadyExistsException ex)
      {
         throw ex;
      }
      catch (Exception ex)
      {
         throw new AuthenticationProviderException(ex.getMessage(), ex);
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
    * @throws AuthenticationProviderException 
    */
   private boolean isLocked(String login, int attempts, int timeout) throws AuthenticationProviderException
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
         
         // Limpia bloqueos caducados
         sql = "DELETE FROM " + TABLE_LOCKS + " " +
               "WHERE ((DATE_PART('day', CURRENT_TIMESTAMP - lastattempt) * 24 + " +
               "        DATE_PART('hour', CURRENT_TIMESTAMP - lastattempt)) * 60 + " +
               "        DATE_PART('minute', CURRENT_TIMESTAMP - lastattempt) >= " + timeout + ") And " +
               "        lower(login) = '" + DataConnection.sqlFormatTextValue(login) + "' And " +
               "        fails >= " + attempts;
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
         throw new AuthenticationProviderException(ex.getMessage(), ex);
      }
      catch (GeneralSecurityException ex)
      {
         throw new AuthenticationProviderException(ex.getMessage(), ex);
      }
      catch (Exception ex)
      {
         throw new AuthenticationProviderException(ex.getMessage(), ex);
      }
      finally
      {
         conn.disconnect();
      }
   }
   
   private void loginFail(String login) throws AuthenticationProviderException
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
            sql = "INSERT INTO " + TABLE_LOCKS + " (login, fails, lastattempt, ipaddress) " +
                  "VALUES ('" + DataConnection.sqlFormatTextValue(login) + "', 1, CURRENT_TIMESTAMP, '" + workspace.getServerRequest().getRemoteAddr() + "')";
            conn.execute(sql);
         }
         
         // Confirma los cambios en la bbdd
         if (!conn.isAutoCommit()) conn.commit();
      } 
      catch (SQLException ex) 
      {
         throw new AuthenticationProviderException(ex.getMessage(), ex);
      }
      catch (GeneralSecurityException ex)
      {
         throw new AuthenticationProviderException(ex.getMessage(), ex);
      }
      catch (Exception ex)
      {
         throw new AuthenticationProviderException(ex.getMessage(), ex);
      }
      finally
      {
         conn.disconnect();
      }
   }
   
}
