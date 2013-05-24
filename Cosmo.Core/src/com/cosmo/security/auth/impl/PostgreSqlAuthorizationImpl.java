package com.cosmo.security.auth.impl;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

import com.cosmo.Workspace;
import com.cosmo.data.DataConnection;
import com.cosmo.data.DataSource;
import com.cosmo.security.Activity;
import com.cosmo.security.Permission;
import com.cosmo.security.Role;
import com.cosmo.security.UserSecurityPolicy;
import com.cosmo.security.auth.Authorization;
import com.cosmo.security.auth.AuthorizationException;
import com.cosmo.structures.PluginProperties;

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
   private static String TABLE_ROLES = "cosmo_auth_roles";
   private static String TABLE_ACTIVITIES = "cosmo_auth_activity";
   private static String TABLE_ROLE_ACTIVITIES = "cosmo_auth_role_activity";
   private static String TABLE_USER_ROLES = "cosmo_auth_user_role";
   
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
   public PostgreSqlAuthorizationImpl(Workspace workspace)
   {
      this.workspace = workspace;
      this.agent = this.workspace.getProperties().getAuthorizationAgent();
   }
   
   
   //================================================================
   // Properties
   //================================================================
   
   /**
    * Devuelve un {@code hash} que contiene los parámetros de configuración del agente de seguridad.
    */
   public HashMap<String, String> getParameters()
   {
      return agent.getParams();
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
    * @throws AuthorizationException 
    */
   @Override
   public UserSecurityPolicy getAuthorizationData(String login) throws AuthorizationException
   {
      UserSecurityPolicy usp = new UserSecurityPolicy();

      // Carga los roles del usuario
      usp.addRoles(getRolesByUser(login));

      // Carga todas las actividades sobre las que el usuario tiene permisos
      usp.addPermissions(getActivitiesByUser(login));

      return usp;
   }
   
   
   //==============================================
   // Methods
   //==============================================
   
   /**
    * Devuelve una lista completa de roles definidos en el esquema.
    * 
    * @return Una instancia de {@link ArrayList} que contiene instancias de {@link Role}.
    * 
    * @throws AuthorizationException
    */
   public ArrayList<Role> getRoles() throws AuthorizationException 
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
         throw new AuthorizationException(ex.getMessage(), ex);
      }
      
      return roles;
   }
   
   /**
    * Devuelve la lista de roles a los que pertenece un determinado usuario.
    * 
    * @param login Una cadena que contiene el <em>login</em> del usuario.
    * 
    * @return Una instancia de {@link ArrayList} que contiene las instancias de {@link Role} que corresponden a los roles a los que pertenece un determinado usuario.
    * 
    * @throws AuthorizationException
    */
   public ArrayList<Role> getRolesByUser(String login) throws AuthorizationException
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
         throw new AuthorizationException(ex.getMessage(), ex);
      }
      
      return roles;
   }   
   
   /**
    * Devuelve una lista completa de actividades definidas en el esquema.
    * 
    * @return Una instancia de {@link ArrayList} que contiene instancias de {@link Activity}.
    * 
    * @throws AuthorizationException
    */
   public ArrayList<Activity> getActivities() throws AuthorizationException
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
         throw new AuthorizationException(ex.getMessage(), ex);
      }
      
      return activities;
   }
   
   /**
    * Devuelve una lista completa de actividades definidas en el esquema.
    * 
    * @return Una instancia de {@link ArrayList} que contiene instancias de {@link Activity}.
    * 
    * @throws AuthorizationException
    */
   public ArrayList<Activity> getActivitiesByRole(String roleId) throws AuthorizationException
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
         throw new AuthorizationException(ex.getMessage(), ex);
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
    * @throws AuthorizationException
    */
   public ArrayList<Permission> getActivitiesByUser(String login) throws AuthorizationException
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
         throw new AuthorizationException(ex.getMessage(), ex);
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
         role.setSuperUser(rs.getBoolean("rolesu"));
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
