package com.cosmo.data.adapter;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

import com.cosmo.Cosmo;
import com.cosmo.Workspace;
import com.cosmo.data.DataConnection;
import com.cosmo.data.DataException;
import com.cosmo.security.providers.UserProvider;
import com.cosmo.security.providers.UserProviderException;

/**
 * Implementa un acceso uniforme a los drivers CORM.<br />
 * Esta clase se encarga de instanciar el driver adecuado (según la configuración) y de manejar las llamadas a métodos.
 * 
 * @author Gerard Llort
 */
public class OrmProvider extends OrmDriver
{

   public OrmProvider(DataConnection connection) 
   {
      super(connection);
   }

   @Override
   public String getProviderName() 
   {
      return null;
   }

   @Override
   public void add(Object data) throws InvalidMappingException, SQLException, DataException, Exception 
   {
      
   }
   
 //==============================================
   // Private members
   //==============================================
   
   /**
    * Carga el controlador de usuarios.
    * 
    * @throws UserProviderException 
    */
   private static UserProvider loadDriver(Workspace workspace) 
   {
      String className = "-- no CORM driver defined in proprties --";
      UserProvider provider;
      
      try 
      {
         className = workspace.getProperties().getWorkspaceProperty(Cosmo.PROPERTY_WORKSPACE_DATA_ORM_DRIVER);
         
         Class<?> cls = Class.forName(className);
         Constructor<?> cons = cls.getConstructor(Workspace.class);
         provider = (UserProvider) cons.newInstance(workspace);
         
         return provider;
      }
      catch (NoSuchMethodException ex) 
      {
         throw new UserProviderException("NoSuchMethodException: " + className, ex);
      }
      catch (InvocationTargetException ex) 
      {
         throw new UserProviderException("InvocationTargetException: " + className, ex);
      }
      catch (ClassNotFoundException ex) 
      {
         throw new UserProviderException("ClassNotFoundException: " + className, ex);
      }
      catch (InstantiationException ex)
      {
         throw new UserProviderException("InstantiationException: " + className, ex);
      }
      catch (IllegalAccessException ex)
      {
         throw new UserProviderException("IllegalAccessException: " + className, ex);
      }
   }
}
