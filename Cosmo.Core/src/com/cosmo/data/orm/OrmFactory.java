package com.cosmo.data.orm;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import com.cosmo.Workspace;
import com.cosmo.data.DataConnection;
import com.cosmo.data.DataException;
import com.cosmo.data.DataSource;
import com.cosmo.data.orm.annotations.CormObjectField;
import com.cosmo.data.orm.annotations.CormFieldSetter;
import com.cosmo.data.orm.annotations.CormObject;
import com.cosmo.data.orm.annotations.CormObjectField.FieldSortType;
import com.cosmo.net.HttpRequestUtils;

/**
 * Implementa un acceso uniforme a los drivers CORM.<br />
 * Esta clase se encarga de instanciar el driver adecuado (según la configuración) y de manejar las llamadas a métodos.
 * 
 * @author Gerard Llort
 */
public class OrmFactory 
{
   OrmDriver driver;
   
   //==============================================
   // Constructors
   //==============================================
   
   /**
    * Constructor de la clase.
    * 
    * @param dataSourceId Una cadena que contiene el identificador del DataSource definido en la configuración.
    * @param workspace Una instancia de {@link Workspace} que representa el contexto actual de la aplicación.
    */
   public OrmFactory(String dataSourceId, Workspace workspace) throws OrmDriverException
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
    * Obtiene el resultado de una consulta.
    * 
    * @param ormObject Una referencia a un objeto CORM (clase POJO que está anotada con anotaciones CORM).
    * 
    * @return Una instancia de {@link ResultSet} que contiene los datos obtenidos en la consulta.
    * 
    * @throws InvalidMappingException
    * @throws SQLException
    * @throws DataException
    * @throws Exception
    */
   public ResultSet select(Class<?> ormObject) throws InvalidMappingException, SQLException, DataException, Exception
   {
      return driver.select(ormObject);
   }
   
   /**
    * Obtiene el resultado de una consulta.
    * 
    * @param ormObject Una referencia a un objeto CORM (clase POJO que está anotada con anotaciones CORM).
    * 
    * @return Una instancia de {@link ResultSet} que contiene los datos obtenidos en la consulta.
    * 
    * @throws InvalidMappingException
    * @throws SQLException
    * @throws DataException
    * @throws Exception
    */
   public ResultSet select(Class<?> ormObject, boolean showAllColumns) throws InvalidMappingException, SQLException, DataException, Exception
   {
      return driver.select(ormObject, showAllColumns);
   }
   
   /**
    * Genera una senténcia SELECT a partir de una instancia de un objeto CORM.
    * 
    * @param ormObject Una referencia a un objeto CORM (clase POJO que está anotada con anotaciones CORM).
    * 
    * @return Una instancia de {@link ResultSet} que contiene los datos obtenidos en la consulta.
    * 
    * @throws InvalidMappingException
    * @throws ClassNotFoundException 
    * @throws DataException 
    * @throws SQLException 
    * @throws Exception 
    */
   public Object get(Object data) throws InvalidMappingException, SQLException, DataException, Exception
   {
      return driver.get(data);
   }
   
   /**
    * Genera una senténcia INSERT INTO a partir de una instancian de clase.
    * 
    * @param data Clase que contiene los datos a insertar.
    * 
    * @throws InvalidMappingException
    * @throws DataException 
    * @throws SQLException 
    * @throws Exception 
    */
   public void insert(Object data) throws InvalidMappingException, SQLException, DataException, Exception 
   {
      driver.insert(data);
   }
   
   /**
    * Actualiza la información del registro de la tabla de datos que indica el valor asociado a/los campo/s identificador/es.
    * 
    * @param ormClass Una referencia a un objeto CORM (clase POJO que está anotada con anotaciones CORM).
    * @param request Una instancia de {@link HttpServletRequest} que contiene los datos (parámetros) usados para ejecutar la senténcia UPDATE.
    * 
    * @throws Exception 
    * @throws DataException 
    * @throws SQLException 
    * @throws InvalidMappingException 
    */
   public void update(Object data) throws InvalidMappingException, SQLException, DataException, Exception
   {
      driver.update(data);
   }
   
   /**
    * Elimina el registro de la tabla de datos que indica el valor asociado a/los campo/s identificador/es.
    * 
    * @param data Una instancia de un objeto CORM que contiene los datos para el borrado (todos los campos de la clave principal establecidos).
    * 
    * @throws Exception 
    * @throws DataException 
    * @throws SQLException 
    * @throws InvalidMappingException 
    */
   public void delete(Object data) throws InvalidMappingException, SQLException, DataException, Exception
   {
      driver.delete(data);
   }
   
   
   //==============================================
   // Static members
   //==============================================
   
   /**
    * Recupera una instancia desde la URL.
    * 
    * @param ormClass Una referencia a un objeto CORM (clase POJO que está anotada con anotaciones CORM).
    * @param request Una instancia de {@link HttpServletRequest} que contiene los datos de la instancia.
    * 
    * @return
    * 
    * @throws InvalidMappingException 
    * @throws OrmException 
    * @throws InvocationTargetException 
    * @throws IllegalAccessException 
    * @throws InstantiationException 
    * @throws IllegalArgumentException 
    */
   public static Object getObjectFromRequest(Class<?> ormClass, HttpServletRequest request) throws InvalidMappingException, OrmException
   {
      Object instance = null;
      CormFieldSetter cfs;
      Class<?> paramType;
      
      // Comprueba si el objeto proporcionado es un objeto CORM válido
      if (!OrmFactory.isValidCormObject(ormClass))
      {
         throw new InvalidMappingException(ormClass.getName() + " is not a CORM object.");
      }

      // Genera la instancia de la clase
      try
      {
         Constructor<?>[] ctors = ormClass.getDeclaredConstructors();
         for (Constructor<?> ctor : ctors)
         {
            if (ctor.getGenericParameterTypes().length == 0)
            {
               ctor.setAccessible(true);
               instance = ctor.newInstance();
            }
         }
      }
      catch (Exception ex)
      {
         throw new OrmException(ex.getMessage(), ex);
      }
      
      // Establece los valores de las propiedades con el contenido de los parámetros contenidos en el Request
      try
      {
         for (Method method : ormClass.getMethods())
         {
            if (method.isAnnotationPresent(CormFieldSetter.class))
            {               
               method.setAccessible(true);
               cfs = method.getAnnotation(CormFieldSetter.class);
               paramType = method.getParameterTypes()[0];
   
               // Establece el valor según el tipo de datos
               if ((paramType == String.class) || (paramType == char.class))
               {
                  method.invoke(instance, HttpRequestUtils.getValue(request, cfs.dbTableColumn(), ""));
               }
               else if ((paramType == Integer.class) || (paramType == int.class) || 
                        (paramType == Long.class) || (paramType == long.class) || 
                        (paramType == Short.class) || (paramType == short.class) || 
                        (paramType == Byte.class) || (paramType == byte.class))
               {
                  method.invoke(instance, HttpRequestUtils.getInt(request, cfs.dbTableColumn()));
               }
               else if ((paramType == Float.class) || (paramType == float.class) || 
                        (paramType == Double.class) || (paramType == double.class))
               {
                  method.invoke(instance, HttpRequestUtils.getDouble(request, cfs.dbTableColumn()));
               }
               else if (paramType == Date.class)
               {
                  method.invoke(instance, HttpRequestUtils.getDate(request, cfs.dbTableColumn()));
               }
            }
         }
      }
      catch (Exception ex)
      {
         throw new OrmException(ex.getMessage(), ex);
      }
      
      return instance;
   }
   
   /**
    * Determina si una clase es un objeto CORM (Cosmo ORM).
    * <br /><br />
    * Básicamente comprueba que disponga de la anotación {@link CormObject} y que al menos tenga una anotación {@link CormObjectField} 
    * (es decir, que tenga al menos un atributo). 
    * 
    * @param ormClass Clase a comprobar.
    * 
    * @return Devuelve {@code true} si la clase está definida como objeto CORM o {@code false} en cualquier otro caso.
    */
   public static boolean isValidCormObject(Class<?> ormClass)
   {
      if (!ormClass.isAnnotationPresent(CormObject.class))
      {
         return false;
      }

      boolean isValid = false;
      for (Method method : ormClass.getMethods())
      {
         isValid = isValid || method.isAnnotationPresent(CormObjectField.class);
      }
      
      return isValid;
   }
   
   /**
    * Obtiene el nombre de la tabla de base de datos de un objeto CORM (Cosmo ORM).
    * 
    * @param ormClass Clase a comprobar.
    * 
    * @return Una cadena que contiene el nombre de la tabla o {@code null} en cualquier otro caso.
    * 
    * @throws InvalidMappingException 
    */
   public static String getDbTableName(Class<?> ormClass) throws InvalidMappingException
   {
      CormObject co = ormClass.getAnnotation(CormObject.class);
      if (co != null)
      {
         return co.dbTable();
      }

      throw new InvalidMappingException(ormClass.getName() + " is not a CORM object.");
   }
   
   /**
    * Determina si un objeto CORM (Cosmo ORM) tiene clave/s primaria/s definida/s.
    * 
    * @param ormClass Clase a comprobar.
    * 
    * @return Devuelve {@code true} si el objeto dispone de clave/s primaria/s o {@code false} en cualquier otro caso.
    */
   public static boolean havePrimaryKey(Class<?> ormClass)
   {
      if (!ormClass.isAnnotationPresent(CormObject.class))
      {
         return false;
      }

      boolean isValid = false;
      for (Method method : ormClass.getMethods())
      {
         if (method.isAnnotationPresent(CormObjectField.class))
         {
            isValid = isValid || method.getAnnotation(CormObjectField.class).isPrimaryKey();
         }
      }

      return isValid;
   }
   
   /**
    * Determina si una instancia de un objeto CORM dispone de campos ordenados.
    * 
    * @param ormClass Clase a comprobar.
    * 
    * @return Devuelve {@code true} si la clase dispone de propiedades ordenadas o {@code false} en cualquier otro caso.
    */
   public static boolean haveSortedFields(Class<?> ormClass)
   {
      CormObjectField cfg;
      
      for (Method method : ormClass.getMethods())
      {
         cfg = method.getAnnotation(CormObjectField.class);
         
         if (cfg != null)
         {
            if (cfg.sort() != FieldSortType.None)
            {
               return true;
            }
         }
      }
      
      return false;
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
      DataConnection conn;
      
      try 
      {
         // Genera la instancia
         ds = workspace.getProperties().getDataSource(dataSourceId);
         if (ds == null)
         {
            throw new OrmDriverException("Datasource " + dataSourceId + " not found");
         }
         
         // Genera la conexión
         conn = new DataConnection(ds);
         
         // Invoca el constructor del driver
         Class<?> cls = Class.forName(ds.getCormDriver());
         Constructor<?> cons = cls.getConstructor(DataConnection.class);
         provider = (OrmDriver) cons.newInstance(conn);
         
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
