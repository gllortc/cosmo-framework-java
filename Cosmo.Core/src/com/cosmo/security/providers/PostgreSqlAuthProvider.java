package com.cosmo.security.providers;

import java.sql.ResultSet;
import java.util.ArrayList;

import com.cosmo.Workspace;
import com.cosmo.data.DataConnection;
import com.cosmo.data.DataSource;
import com.cosmo.security.Activity;
import com.cosmo.security.Role;

public class PostgreSqlAuthProvider implements AuthProviderInterface
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
   public PostgreSqlAuthProvider(Workspace workspace)
   {
      this.workspace = workspace;
   }
   
   //================================================================
   // Interface implementation: AuthProviderInterface
   //================================================================

   @Override
   public boolean isAutheticated() 
   {
      return false;
   }
   
   @Override
   public void loadAuthenticationData(String login)
   {
      return;
   }
   
   @Override
   public boolean isUserInRole(String login, String role) 
   {
      return false;
   }

   @Override
   public boolean isActivityGranted(String login, String activityId) 
   {
      return false;
   }

   @Override
   public String[] getUserRoles(String login) 
   {
      return null;
   }
   
   //==============================================
   // Methods
   //==============================================
   
   /**
    * Devuelve una lista completa de roles definidos en el esquema.
    * 
    * @return Una instancia de {@link ArrayList} que contiene instancias de {@link Role}.
    * 
    * @throws Exception
    */
   public ArrayList<Role> getRoles() throws Exception
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
         
         ds = this.workspace.getProperties().getDataSource(DataConnection.CONNECTION_SERVER);
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
         throw ex;
      }
      
      return roles;
   }
   
   /**
    * Devuelve la lista de roles a los que pertenece un determinado usuario.
    * 
    * @param login Una cadena que contiene el login del usuario.
    * 
    * @return Una instancia de {@link ArrayList} que contiene las instancias de {@link Role} que corresponden a los roles a los que pertenece un determinado usuario.
    * 
    * @throws Exception
    */
   public ArrayList<Role> getRolesByUser(String login) throws Exception
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
               "ORDER BY roleid";

         ds = this.workspace.getProperties().getDataSource(DataConnection.CONNECTION_SERVER);
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
         throw ex;
      }
      
      return roles;
   }
   
   /**
    * Devuelve una lista completa de actividades definidas en el esquema.
    * 
    * @return Una instancia de {@link ArrayList} que contiene instancias de {@link Activity}.
    * 
    * @throws Exception
    */
   public ArrayList<Activity> getActivities() throws Exception
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
         
         ds = this.workspace.getProperties().getDataSource(DataConnection.CONNECTION_SERVER);
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
         throw ex;
      }
      
      return activities;
   }
   
   /**
    * Devuelve una lista completa de actividades definidas en el esquema.
    * 
    * @return Una instancia de {@link ArrayList} que contiene instancias de {@link Activity}.
    * 
    * @throws Exception
    */
   public ArrayList<Activity> getActivitiesByRole(String roleId) throws Exception
   {
      String sql;
      DataSource ds;
      DataConnection conn = null;
      
      ArrayList<Activity> activities = new ArrayList<Activity>();
      
      try
      {
         sql = "SELECT " + TABLE_ACTIVITIES + ".* " +
               "FROM " + TABLE_ROLES + " Inner Join " + TABLE_ROLE_ACTIVITIES + " On (" + TABLE_ROLES + ".roleid=" + TABLE_ROLE_ACTIVITIES + "roleid) " +
                                       " Inner Join " + TABLE_ACTIVITIES + " On (" + TABLE_ROLE_ACTIVITIES + ".actid=" + TABLE_ACTIVITIES + ".actid) " +
               "WHERE " + TABLE_ROLES + ".roleid = '" + DataConnection.sqlFormatTextValue(roleId) + "' " +
               "ORDER BY actid";
         
         ds = this.workspace.getProperties().getDataSource(DataConnection.CONNECTION_SERVER);
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
         throw ex;
      }
      
      return activities;
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
