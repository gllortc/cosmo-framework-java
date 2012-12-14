package com.cosmo.data.orm;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

import com.cosmo.Workspace;
import com.cosmo.data.DataException;
import com.cosmo.data.DataSource;

/**
 * Implementa un acceso uniforme a los drivers CORM.<br />
 * Esta clase se encarga de instanciar el driver adecuado (según la configuración) y de manejar las llamadas a métodos.
 * 
 * @author Gerard Llort
 */
public class OrmProvider 
{
   OrmDriver driver;
   
   //==============================================
   // Constructors
   //==============================================
   
   /**
    * Constructor de la clase.
    * 
    * @param connection Un objeto de conexión a datos.
    */
   public OrmProvider(String dataSourceId, Workspace workspace) throws OrmDriverException
   {
      this.driver = loadDriver(dataSourceId, workspace);
   }

   //==============================================
   // Properties
   //==============================================
   
   /**
    * Devuelve el nombre del proveedor.<br />
    * Debe especificar claramente para qué gestor de BBDD está implementado (nombre del producto).
    */
   public String getProviderName() 
   {
      return driver.getProviderName();
   }

   /**
    * Devuelve el nombre cualificado de la clase que actúa de driver para la conexión JDBC.
    */
   public String getJdbcDriver() 
   {
      return driver.getJdbcDriver();
   }
   
   /**
    * Devuelve la última senténcia SQL generada y ejecutada.
    */
   public void add(Object data) throws InvalidMappingException, SQLException, DataException, Exception 
   {
      driver.add(data);
   }
   
   //==============================================
   // Private members
   //==============================================
   
   /**
    * Carga el controlador de usuarios.
    * 
    * @throws OrmDriverException 
    */
   private static OrmDriver loadDriver(String dataSourceId, Workspace workspace) throws OrmDriverException
   {
      OrmDriver provider;
      DataSource ds = null;
      
      try 
      {
         ds = workspace.getProperties().getDataSource(dataSourceId);
         if (ds == null)
         {
            throw new OrmDriverException("Datasource " + dataSourceId + " not found");
         }
         
         Class<?> cls = Class.forName(ds.getCormDriver());
         Constructor<?> cons = cls.getConstructor(Workspace.class);
         provider = (OrmDriver) cons.newInstance(workspace);
         
         return provider;
      }
      catch (NoSuchMethodException ex)
      {
         throw new OrmDriverException("CORM driver loader: NoSuchMethodException: " + ds.getCormDriver(), ex);
      }
      catch (InvocationTargetException ex) 
      {
         throw new OrmDriverException("CORM driver loader: InvocationTargetException: " + ds.getCormDriver(), ex);
      }
      catch (ClassNotFoundException ex) 
      {
         throw new OrmDriverException("CORM driver loader: ClassNotFoundException: " + ds.getCormDriver(), ex);
      }
      catch (InstantiationException ex)
      {
         throw new OrmDriverException("CORM driver loader: InstantiationException: " + ds.getCormDriver(), ex);
      }
      catch (IllegalAccessException ex)
      {
         throw new OrmDriverException("CORM driver loader: IllegalAccessException: " + ds.getCormDriver(), ex);
      }
   }
}
