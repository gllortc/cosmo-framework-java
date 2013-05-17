package com.cosmo.security.providers;

import java.sql.ResultSet;
import java.util.ArrayList;

import com.cosmo.Workspace;
import com.cosmo.data.DataConnection;
import com.cosmo.data.DataSource;
import com.cosmo.security.Activity;
import com.cosmo.security.Permission;
import com.cosmo.security.Role;
import com.cosmo.security.UserSession.SecurityInfo;

/**
 * Implementa el proveedor de autorización nativo de Cosmo.<br />
 * Implementa la API de administración de autorización.
 * 
 * Base de datos: JDBC / PostgreSQL
 * 
 * @author Gerard Llort
 *
 */
public class PostgreSqlAuthorizationImpl implements Authorization
{
   private Workspace workspace;
   
   private static String TABLE_ROLES = "cosmo_auth_roles";
   private static String TABLE_ACTIVITIES = "cosmo_auth_activity";
   private static String TABLE_ROLE_ACTIVITIES = "cosmo_auth_role_activity";
   private static String TABLE_USER_ROLES = "cosmo_auth_user_role";
   
   //==============================================
   // Constructors
   //==============================================
   
   /**
    * Constructor de la clase. 
    * 
    * @param workspace Una instancia de {@link Workspace} que representa el workspace al que está conectado actualmente.
    */
   public PostgreSqlAuthorizationImpl(Workspace workspace)
   {
      this.workspace = workspace;
   }
   
   
   //================================================================
   // Interface implementation: Authorization
   //================================================================

   /**
    * Carga la información de autorización de un usuario determinado.
    * 
    * @param login Una cadena que contiene el <em>login</em> del usuario.
    * 
    * @return 
    * 
    * @throws AuthorizationProviderException 
    */
   @Override
   public SecurityInfo loadAuthorizationData(String login, SecurityInfo si) throws AuthorizationProviderException
   {
      // SecurityInfo si = new SecurityInfo();
      
      // Carga los roles del usuario
      si.addRoles(getRolesByUser(login));
      
      // Carga todas las actividades sobre las que el usuario tiene permisos
      si.addPermissions(getActivitiesByUser(login));
      
      return null;
   }
   
   /**
    * Determina si un usuario tiene un rol especifico.
    * 
    * @param login Una cadena que contiene el <em>login</em> del usuario.
    * @param role Una cadena que contiene el nombre (ID) del rol.
    * 
    * @return {@code true} si el usuario tiene asignado el rol o {@code false} en cualquier otro caso.
    */
   @Override
   public boolean isUserInRole(String login, String role) throws AuthorizationProviderException
   {
      ArrayList<String> roles = new ArrayList<String>();
      roles.add(role);
      
      return isUserInRole(login, roles);
   }
   
   /**
    * Determina si un usuario tiene un rol especifico entre una lista de roles.
    * 
    * @param login Una cadena que contiene el <em>login</em> del usuario.
    * @param roles Un array con los identificadores de rol.
    * 
    * @return {@code true} si el usuario tiene asignado al menos un rol de los contenidos en la lista o {@code false} en cualquier otro caso.
    * 
    * @throws AuthorizationProviderException
    */
   @Override
   public boolean isUserInRole(String login, ArrayList<String> roles) throws AuthorizationProviderException
   {
      ArrayList<Role> roleList = null;
      
      // Obtiene los roles del usuario
      roleList = getRolesByUser(login);
      
      // Para cada rol del usuario comprueba la lista proporcionada 
      for (String roleId : roles)
      {
         for (Role role : roleList)
         {
            // Si los IDs coinciden, el usuario tiene uno de los roles
            if (role.getId().equals(roleId))
            {
               return true;
            }
         }
      }
      
      // No de ha hallado ninguna coincidnecia
      return false;
   }

   /**
    * Determina si un usuario tiene permiso para ejecutar determinada actividad.
    * 
    * @param login Una cadena que contiene el <em>login</em> del usuario.
    * @param activityId Una cadena que contiene el nombre (ID) de la actividad.
    * 
    * @return @return {@code true} si el usuario tiene permiso para ejecutar la actividad o {@code false} en cualquier otro caso.
    */
   @Override
   public boolean isActivityGranted(String login, String activityId) 
   {
      return false;
   }

   /**
    * Devuelve la lista de roles a los que pertenece un determinado usuario.
    * 
    * @param login Una cadena que contiene el <em>login</em> del usuario.
    * 
    * @return Una instancia de {@link ArrayList} que contiene las instancias de {@link Role} que corresponden a los roles a los que pertenece un determinado usuario.
    * 
    * @throws AuthorizationProviderException
    */
   @Override
   public ArrayList<Role> getRolesByUser(String login) throws AuthorizationProviderException
   {
      String sql;
      DataSource ds;
      DataConnection conn = null;
      
      ArrayList<Role> roles = new ArrayList<Role>();
      
      try
      {
         sql = "SELECT * " +
               "FROM " + TABLE_ROLES + " Inner Join " + TABLE_USER_ROLES + " On (" + TABLE_ROLES + ".roleid=" + TABLE_USER_ROLES + ".roleid) " +
               "WHERE " + TABLE_USER_ROLES + ".usrlogin='" + DataConnection.sqlFormatTextValue(login) + "' " + 
               "ORDER BY " + TABLE_ROLES + ".roleid";

         ds = this.workspace.getProperties().getServerDataSource();
         conn = new DataConnection(ds);
         conn.connect();
         ResultSet rs = conn.executeSql(sql);
         while (rs.next())
         {
            roles.add(readRole(rs));
         }
      }
      catch (Exception ex) 
      {
         throw new AuthorizationProviderException(ex.getMessage(), ex);
      }
      
      return roles;
   }
   
   
   //==============================================
   // Methods
   //==============================================
   
   /**
    * Devuelve una lista completa de roles definidos en el esquema.
    * 
    * @return Una instancia de {@link ArrayList} que contiene instancias de {@link Role}.
    * 
    * @throws AuthorizationProviderException
    */
   public ArrayList<Role> getRoles() throws AuthorizationProviderException 
   {
      String sql;
      DataSource ds;
      DataConnection conn = null;
      
      ArrayList<Role> roles = new ArrayList<Role>();
      
      try
      {
         sql = "SELECT * " +
               "FROM " + TABLE_ROLES + " " +
               "ORDER BY roleid";
         
         ds = this.workspace.getProperties().getServerDataSource();
         conn = new DataConnection(ds);
         conn.connect();
         ResultSet rs = conn.executeSql(sql);
         while (rs.next())
         {
            roles.add(readRole(rs));
         }
      }
      catch (Exception ex) 
      {
         throw new AuthorizationProviderException(ex.getMessage(), ex);
      }
      
      return roles;
   }
   
   
   
   
   
   /**
    * Devuelve una lista completa de actividades definidas en el esquema.
    * 
    * @return Una instancia de {@link ArrayList} que contiene instancias de {@link Activity}.
    * 
    * @throws AuthorizationProviderException
    */
   public ArrayList<Activity> getActivities() throws AuthorizationProviderException
   {
      String sql;
      DataSource ds;
      DataConnection conn = null;
      
      ArrayList<Activity> activities = new ArrayList<Activity>();
      
      try
      {
         sql = "SELECT * " +
               "FROM " + TABLE_ACTIVITIES + " " +
               "ORDER BY actid";
         
         ds = this.workspace.getProperties().getServerDataSource();
         conn = new DataConnection(ds);
         conn.connect();
         ResultSet rs = conn.executeSql(sql);
         while (rs.next())
         {
            activities.add(readActivity(rs));
         }
      }
      catch (Exception ex) 
      {
         throw new AuthorizationProviderException(ex.getMessage(), ex);
      }
      
      return activities;
   }
   
   /**
    * Devuelve una lista completa de actividades definidas en el esquema.
    * 
    * @return Una instancia de {@link ArrayList} que contiene instancias de {@link Activity}.
    * 
    * @throws AuthorizationProviderException
    */
   public ArrayList<Activity> getActivitiesByRole(String roleId) throws AuthorizationProviderException
   {
      String sql;
      DataSource ds;
      DataConnection conn = null;
      
      ArrayList<Activity> activities = new ArrayList<Activity>();
      
      try
      {
         sql = "SELECT " + TABLE_ACTIVITIES + ".* " +
               "FROM " + TABLE_ROLES + " Inner Join " + TABLE_ROLE_ACTIVITIES + " On (" + TABLE_ROLES + ".roleid=" + TABLE_ROLE_ACTIVITIES + ".roleid) " +
                                       " Inner Join " + TABLE_ACTIVITIES + " On (" + TABLE_ROLE_ACTIVITIES + ".actid=" + TABLE_ACTIVITIES + ".actid) " +
               "WHERE " + TABLE_ROLES + ".roleid = '" + DataConnection.sqlFormatTextValue(roleId) + "' " +
               "ORDER BY actid";
         
         ds = this.workspace.getProperties().getServerDataSource();
         conn = new DataConnection(ds);
         conn.connect();
         ResultSet rs = conn.executeSql(sql);
         while (rs.next())
         {
            activities.add(readActivity(rs));
         }
      }
      catch (Exception ex) 
      {
         throw new AuthorizationProviderException(ex.getMessage(), ex);
      }
      
      return activities;
   }
   
   /**
    * Obtiene todas las actividades con permisos otorgados (o denegados) asociadas a los roles asociados al usuario.
    * 
    * @param login Una cadena que contiene el <em>login</em> del usuario.
    * 
    * @return Un array de instancias de {@link Permission} que contiene los permisos efectivos del usuario.
    * 
    * @throws AuthorizationProviderException
    */
   public ArrayList<Permission> getActivitiesByUser(String login) throws AuthorizationProviderException
   {
      String sql;
      DataSource ds;
      DataConnection conn = null;
      
      ArrayList<Permission> permissions = new ArrayList<Permission>();
      
      try
      {
         sql = "SELECT cosmo_auth_activity.*, cosmo_auth_role_activity.isgranted " +
               "FROM cosmo_auth_user_role Inner Join cosmo_auth_roles On (cosmo_auth_user_role.roleid = cosmo_auth_roles.roleid) " +
               "                          Inner Join cosmo_auth_role_activity On (cosmo_auth_roles.roleid = cosmo_auth_role_activity.roleid) " +
               "                          Inner Join cosmo_auth_activity On (cosmo_auth_role_activity.actid = cosmo_auth_activity.actid) " +
               "WHERE cosmo_auth_user_role.usrlogin = '" + DataConnection.sqlFormatTextValue(login) + "' " +
               "ORDER BY cosmo_auth_role_activity.actid";
         
         ds = this.workspace.getProperties().getServerDataSource();
         conn = new DataConnection(ds);
         conn.connect();
         ResultSet rs = conn.executeSql(sql);
         while (rs.next())
         {
            permissions.add(new Permission(readActivity(rs), true));
         }
      }
      catch (Exception ex) 
      {
         throw new AuthorizationProviderException(ex.getMessage(), ex);
      }
      
      return permissions;
   }
   
   
   //==============================================
   // Private members
   //==============================================
   
   /**
    * Lee un rol desde una consulta (ResultSet) que contenga todos los campos de ROLES.
    */
   private Role readRole(ResultSet rs) throws Exception
   {
      Role role = new Role();
      
      try
      {
         role.setId(rs.getString("roleid"));
         role.setDescription(rs.getString("roledescription"));
         role.setApplicationId(rs.getString("roleappid"));
      }
      catch (Exception ex) 
      {
         throw ex;
      }
      
      return role;
   }
   
   /**
    * Lee una actividad desde una consulta (ResultSet) que contenga todos los campos de ACTIVITIES.
    */
   private Activity readActivity(ResultSet rs) throws Exception
   {
      Activity activity = new Activity();
      
      try
      {
         activity.setId(rs.getString("actid"));
         activity.setDescription(rs.getString("actdescription"));
         activity.setGrantedByDefault(rs.getBoolean("actdefaultgrant"));
         activity.setEnabled(rs.getBoolean("actenabled"));
      }
      catch (Exception ex) 
      {
         throw ex;
      }
      
      return activity;
   }
}
