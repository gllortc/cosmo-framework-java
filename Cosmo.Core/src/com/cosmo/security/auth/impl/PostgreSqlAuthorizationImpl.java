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
 * Implementa el proveedor de autorización nativo de Cosmo (versión para JDBC/PostgreSQL).
 * <br /><br />
 * Esta implementación incorpora adicionalmente la API de administración de autorización RDBMS.
 * 
 * @author Gerard Llort
 * @version 1.0.0
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
      this.agent = this.workspace.getProperties().getSecurityProperties().getAuthorizationAgent();
   }
   
   
   //================================================================
   // Interface implementation: Authorization
   //================================================================

   /**
    * Devuelve un {@code hash} que contiene los parámetros de configuración del agente de seguridad.
    */
   public HashMap<String, String> getParameters()
   {
      return agent.getParams();
   }
   
   /**
    * Carga la información de autorización de un usuario determinado.
    * 
    * @param login Una cadena que contiene el <em>login</em> del usuario.
    * 
    * @return Una instancia de {@link UserSecurityPolicy} que contiene las políticas de autorización del usuario.
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
   
   
   //---------------------
   // ROLE MANAGEMENT
   //---------------------
   
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
         // Obtiene la conexión
         ds = this.workspace.getProperties().getServerDataSource();
         conn = new DataConnection(ds);
         conn.connect();

         // Obtiene la lista de roles
         sql = "SELECT * " +
               "FROM " + TABLE_ROLES + " " +
               "ORDER BY roleid";
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
         // Obtiene la conexión
         ds = this.workspace.getProperties().getServerDataSource();
         conn = new DataConnection(ds);
         conn.connect();
         
         // Obtiene la lista de roles del usuario
         sql = "SELECT * " +
               "FROM     " + TABLE_ROLES + " Inner Join " + TABLE_USER_ROLES + " On (" + TABLE_ROLES + ".roleid=" + TABLE_USER_ROLES + ".roleid) " +
               "WHERE    " + TABLE_USER_ROLES + ".usrlogin='" + DataConnection.sqlFormatTextValue(login) + "' " + 
               "ORDER BY " + TABLE_ROLES + ".roleid";
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
    * Agrega un nuevo rol.
    * 
    * @param role Una instancia de {@link Role} que contiene la información del nuevo rol.
    * 
    * @throws AuthorizationException 
    */
   public void addRole(Role role) throws AuthorizationException
   {
      String sql;
      DataSource ds;
      DataConnection conn = null;
      
      try
      {
         // Obtiene la conexión
         ds = this.workspace.getProperties().getServerDataSource();
         conn = new DataConnection(ds);
         conn.connect();
         
         // Agrega el nuevo rol
         sql = "INSERT INTO " + TABLE_ROLES + " (roleid, roledescription, roleappid, rolesu) " +
               "VALUES ('" + DataConnection.sqlFormatTextValue(role.getId()) + "', " +
                       "'" + DataConnection.sqlFormatTextValue(role.getDescription()) + "', " +
                       "'" + DataConnection.sqlFormatTextValue(role.getApplicationId()) + "', " +
                             (role.isSuperUser() ? "true" : "false") + ")";
         conn.execute(sql);
      }
      catch (Exception ex) 
      {
         throw new AuthorizationException(ex.getMessage(), ex);
      }
      finally
      {
         conn.disconnect();
      }
   }
   
   /**
    * Elimina un rol y todas sus asociaciones (usuarios asignados y acitivades asignadas).
    * 
    * @param roleId Una cadena que contiene el identificador del rol.
    * 
    * @throws AuthorizationException 
    */
   public void deleteRole(String roleId) throws AuthorizationException
   {
      String sql;
      DataSource ds;
      DataConnection conn = null;
      
      try
      {
         // Obtiene la conexión
         ds = this.workspace.getProperties().getServerDataSource();
         conn = new DataConnection(ds);
         conn.connect();
         
         // Elimina las asociaciones con actividades 
         sql = "DELETE FROM " + TABLE_ROLE_ACTIVITIES + " " +
               "WHERE roleid='" + DataConnection.sqlFormatTextValue(roleId) + "'";
         conn.execute(sql);
         
         // Elimina las asociaciones con usuarios
         sql = "DELETE FROM " + TABLE_USER_ROLES + " " +
               "WHERE roleid='" + DataConnection.sqlFormatTextValue(roleId) + "'";
         conn.execute(sql);
         
         // Elimina el rol
         sql = "DELETE FROM " + TABLE_ROLES + " " +
               "WHERE roleid='" + DataConnection.sqlFormatTextValue(roleId) + "'";
         conn.execute(sql);
      }
      catch (Exception ex) 
      {
         throw new AuthorizationException(ex.getMessage(), ex);
      }
      finally
      {
         conn.disconnect();
      }
   }
   
   /**
    * Actualiza los datos de un rol.<br />
    * La modificación del identificador no está permitido.
    * 
    * @param role Una instancia de {@link Role} que contiene la información actualizada del rol.
    * 
    * @throws AuthorizationException 
    */
   public void updateRole(Role role) throws AuthorizationException
   {
      String sql;
      DataSource ds;
      DataConnection conn = null;
      
      try
      {
         // Obtiene la conexión
         ds = this.workspace.getProperties().getServerDataSource();
         conn = new DataConnection(ds);
         conn.connect();
         
         // Agrega el nuevo rol
         sql = "UPDATE " + TABLE_ROLES + " " +
               "SET roledescription = '" + DataConnection.sqlFormatTextValue(role.getDescription()) + "', " +
               "    roleappid       = '" + DataConnection.sqlFormatTextValue(role.getApplicationId()) + "', " +
               "    rolesu          =  " + (role.isSuperUser() ? "true" : "false") + " " +
               "WHERE roleid = '" + DataConnection.sqlFormatTextValue(role.getId()) + "'";
         conn.execute(sql);
      }
      catch (Exception ex) 
      {
         throw new AuthorizationException(ex.getMessage(), ex);
      }
      finally
      {
         conn.disconnect();
      }
   }
   
   
   //---------------------
   // ACTIVITY MANAGEMENT
   //---------------------
   
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
         // Obtiene la conexión
         ds = this.workspace.getProperties().getServerDataSource();
         conn = new DataConnection(ds);
         conn.connect();

         // Obtiene la lista de actividades
         sql = "SELECT * " +
               "FROM " + TABLE_ACTIVITIES + " " +
               "ORDER BY actid";
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
         // Obtiene la conexión
         ds = this.workspace.getProperties().getServerDataSource();
         conn = new DataConnection(ds);
         conn.connect();
         
         // Obtiene la lista de actividades del rol especificado
         sql = "SELECT   " + TABLE_ACTIVITIES + ".* " +
               "FROM     " + TABLE_ROLES + " Inner Join " + TABLE_ROLE_ACTIVITIES + " On (" + TABLE_ROLES + ".roleid=" + TABLE_ROLE_ACTIVITIES + ".roleid) " +
               "                             Inner Join " + TABLE_ACTIVITIES + " On (" + TABLE_ROLE_ACTIVITIES + ".actid=" + TABLE_ACTIVITIES + ".actid) " +
               "WHERE    " + TABLE_ROLES + ".roleid = '" + DataConnection.sqlFormatTextValue(roleId) + "' " +
               "ORDER BY " + TABLE_ACTIVITIES + ".actid";
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
         // Obtiene la conexión
         ds = this.workspace.getProperties().getServerDataSource();
         conn = new DataConnection(ds);
         conn.connect();

         // Obtiene la lista de actividades del usuario
         sql = "SELECT   " + TABLE_ACTIVITIES + ".*, " + TABLE_ROLE_ACTIVITIES + ".isgranted " +
               "FROM     " + TABLE_USER_ROLES + " Inner Join " + TABLE_ROLES + "           On (" + TABLE_ROLES + ".roleid = " + TABLE_USER_ROLES + ".roleid) " +
               "                                  Inner Join " + TABLE_ROLE_ACTIVITIES + " On (" + TABLE_ROLES + ".roleid = " + TABLE_ROLE_ACTIVITIES + ".roleid) " +
               "                                  Inner Join " + TABLE_ACTIVITIES + "      On (" + TABLE_ROLE_ACTIVITIES + ".actid = " + TABLE_ACTIVITIES + ".actid) " +
               "WHERE    " + TABLE_USER_ROLES + ".usrlogin = '" + DataConnection.sqlFormatTextValue(login) + "' " +
               "ORDER BY " + TABLE_ROLE_ACTIVITIES + ".actid";
         
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

   /**
    * Agrega una nueva actividad.
    * 
    * @param activity Una instancia de {@link Activity} que contiene la información de la nueva actividad.
    * 
    * @throws AuthorizationException 
    */
   public void addActivity(Activity activity) throws AuthorizationException
   {
      String sql;
      DataSource ds;
      DataConnection conn = null;

      try
      {
         // Obtiene la conexión
         ds = this.workspace.getProperties().getServerDataSource();
         conn = new DataConnection(ds);
         conn.connect();

         // Agrega la nueva actividad
         sql = "INSERT INTO " + TABLE_ACTIVITIES + " (actid, actdescription, actdefaultgrant, actenabled) " +
               "VALUES ('" + DataConnection.sqlFormatTextValue(activity.getId()) + "', " +
                       "'" + DataConnection.sqlFormatTextValue(activity.getDescription()) + "', " +
                             (activity.isGrantedByDefault() ? "true" : "false") + ", " +
                             (activity.isEnabled() ? "true" : "false") + ")";
         conn.execute(sql);
      }
      catch (Exception ex) 
      {
         throw new AuthorizationException(ex.getMessage(), ex);
      }
      finally
      {
         conn.disconnect();
      }
   }

   /**
    * Elimina una actividad.
    * 
    * @param activityId Una cadena que contiene el identificador de la actividad a eliminar.
    * 
    * @throws AuthorizationException 
    */
   public void deleteActivity(String activityId) throws AuthorizationException
   {
      String sql;
      DataSource ds;
      DataConnection conn = null;

      try
      {
         // Obtiene la conexión
         ds = this.workspace.getProperties().getServerDataSource();
         conn = new DataConnection(ds);
         conn.connect();

         // Elimina las asociaciones con roles
         sql = "DELETE FROM " + TABLE_ROLE_ACTIVITIES + " " +
               "WHERE actid='" + DataConnection.sqlFormatTextValue(activityId) + "'";
         conn.execute(sql);

         // Elimina el rol
         sql = "DELETE FROM " + TABLE_ACTIVITIES + " " +
               "WHERE actid='" + DataConnection.sqlFormatTextValue(activityId) + "'";
         conn.execute(sql);
      }
      catch (Exception ex) 
      {
         throw new AuthorizationException(ex.getMessage(), ex);
      }
      finally
      {
         conn.disconnect();
      }
   }

   /**
    * Actualiza los datos de un rol.<br />
    * La modificación del identificador no está permitido.
    * 
    * @param activity Una instancia de {@link Role} que contiene la información actualizada del rol.
    * 
    * @throws AuthorizationException 
    */
   public void updateActivity(Activity activity) throws AuthorizationException
   {
      String sql;
      DataSource ds;
      DataConnection conn = null;
      
      try
      {
         // Obtiene la conexión
         ds = this.workspace.getProperties().getServerDataSource();
         conn = new DataConnection(ds);
         conn.connect();
         
         // Agrega el nuevo rol
         sql = "UPDATE " + TABLE_ACTIVITIES + " " +
               "SET actdescription  = '" + DataConnection.sqlFormatTextValue(activity.getDescription()) + "', " +
               "    actdefaultgrant =  " + (activity.isGrantedByDefault() ? "true" : "false") + ", " +
               "    actenabled      =  " + (activity.isEnabled() ? "true" : "false") + " " +
               "WHERE actid = '" + DataConnection.sqlFormatTextValue(activity.getId()) + "'";
         conn.execute(sql);
      }
      catch (Exception ex) 
      {
         throw new AuthorizationException(ex.getMessage(), ex);
      }
      finally
      {
         conn.disconnect();
      }
   }
   
   /**
    * Asocia una actividad a un rol.
    * 
    * @param activityId Identificador de la actividad.
    * @param roleId     Identificador del rol.
    * @param isGranted  {@code true} si la actividad está permitida para los usuarios del rol o {@code false} si la 
    *                   actividad no está permitida a dichos usuarios.
    * 
    * @throws AuthorizationException
    */
   public void assignActivityToRole(String activityId, String roleId, boolean isGranted) throws AuthorizationException
   {
      String sql;
      DataSource ds;
      DataConnection conn = null;
      
      // Si existe esta asociación, la elimina
      unassignActivityFromRole(activityId, roleId);
      
      try
      {
         // Obtiene la conexión
         ds = this.workspace.getProperties().getServerDataSource();
         conn = new DataConnection(ds);
         conn.connect();
         
         // Agrega la nueva asociación
         sql = "INSERT INTO " + TABLE_ROLE_ACTIVITIES + " (actid, roleid, isgranted) " +
               "VALUES ('" + DataConnection.sqlFormatTextValue(activityId) + "', " +
                       "'" + DataConnection.sqlFormatTextValue(roleId) + "', " +
                             (isGranted ? "true" : "false") + ")";
         conn.execute(sql);
      }
      catch (Exception ex) 
      {
         throw new AuthorizationException(ex.getMessage(), ex);
      }
      finally
      {
         conn.disconnect();
      }
   }
   
   /**
    * Rompe una asociación entre una actividad y un rol.
    * 
    * @param activityId Identificador de la actividad.
    * @param roleId Identificador del rol.
    * 
    * @throws AuthorizationException
    */
   public void unassignActivityFromRole(String activityId, String roleId) throws AuthorizationException
   {
      String sql;
      DataSource ds;
      DataConnection conn = null;
      
      try
      {
         // Obtiene la conexión
         ds = this.workspace.getProperties().getServerDataSource();
         conn = new DataConnection(ds);
         conn.connect();
         
         // Si existe esta asociación, la elimina
         sql = "DELETE FROM " + TABLE_ROLE_ACTIVITIES + " " +
               "WHERE  actid  = '" + DataConnection.sqlFormatTextValue(activityId) + "' And " +
               "       roleid = '" + DataConnection.sqlFormatTextValue(roleId) + "'";
         conn.execute(sql);
      }
      catch (Exception ex) 
      {
         throw new AuthorizationException(ex.getMessage(), ex);
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
