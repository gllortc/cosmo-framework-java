package com.cosmo.security.providers;

import java.security.GeneralSecurityException;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.cosmo.Workspace;
import com.cosmo.data.DataConnection;
import com.cosmo.data.DataSource;
import com.cosmo.security.User;
import com.cosmo.security.User.UserStates;
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
   
   private static String TABLE_NAME = "cosmo_users";
   
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
    * Crea una nueva cuenta de usuario.
    * 
    * @param user Una instancia de {@link User} que representa el nuevo usuario.
    *     
    * @throws UserAlreadyExistsException
    * @throws AuthenticationProviderException
    */
   @Override
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
}
