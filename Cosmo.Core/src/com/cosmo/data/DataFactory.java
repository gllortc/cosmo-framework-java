package com.cosmo.data;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import com.cosmo.Workspace;
import com.cosmo.util.PluginProperties;
import com.cosmo.util.StringUtils;

public class DataFactory
{
   // Mapa con las instancias únicas de los agentes de comunicaciones
   private static HashMap<String, DataAgent> agents = null;


   //==============================================
   // Static members
   //==============================================

   /**
    * Devuelve una instancia de {@link DataAgent} convenientemente instanciada.
    * 
    * @param workspace Una instancia de {@link Workspace} que representa el workspace actual.
    * @param agentId Identificador del agente de comunicaciones a usar.
    * 
    * @return Una instancia única de {@link DataAgent} (sigleton).
    * 
    * @throws DataException
    */
   public static DataAgent getInstance(Workspace workspace, String agentId) throws DataException
   {
      return loadProvider(workspace, agentId);
   }

   /**
    * Devuelve una instancia de {@link DataAgent} convenientemente instanciada.
    * 
    * @param workspace Una instancia de {@link Workspace} que representa el workspace actual.
    * 
    * @return Una instancia única de {@link DataAgent} (sigleton).
    * 
    * @throws DataException
    */
   public static DataAgent getInstance(Workspace workspace) throws DataException
   {
      return getInstance(workspace, null);
   }


   //==============================================
   // Private members
   //==============================================

   /**
    * Carga el agente de conexión a datos.
    *
    * @throws DataException
    */
   private static DataAgent loadProvider(Workspace workspace, String agentId) throws DataException
   {
      String className;

      // Inicializa el diccionario de agentes de comunicación
      if (agents == null)
      {
         agents = new HashMap<String, DataAgent>();
      }

      // Si no se proporciona un identificador de agente, se usa el especificado por defecto en la configuración.
      if (StringUtils.isNullOrEmptyTrim(agentId))
      {
         agentId = workspace.getProperties().getCommProperties().getDefaultCommunicationsAgentId();
      }

      if (agents.containsKey(agentId))
      {
         return agents.get(agentId);
      }
      else
      {
         PluginProperties agent = workspace.getProperties().getCommProperties().getCommunicationAgent(agentId);

         className = agent.getModuleClass();
         if (StringUtils.isNullOrEmptyTrim(className))
         {
            throw new DataException("Data Services Configuration Exception: No data driver found");
         }

         try
         {
            Class<?> cls = Class.forName(className);

            // Genera la instancia del agente solicitado
            Class<?>[] types = { PluginProperties.class };
            Object[] arguments = { agent };
            DataAgent server = (DataAgent) cls.getDeclaredConstructor(types).newInstance(arguments);

            // Almacena la instancia en la lista de instancias de agentes (Singleton)
            agents.put(agentId, server);

            return server;
         }
         catch (NoSuchMethodException ex)
         {
            throw new DataException("NoSuchMethodException: " + className, ex);
         }
         catch (InvocationTargetException ex)
         {
            throw new DataException("InvocationTargetException: " + className, ex);
         }
         catch (ClassNotFoundException ex)
         {
            throw new DataException("ClassNotFoundException: " + className, ex);
         }
         catch (InstantiationException ex)
         {
            throw new DataException("InstantiationException: " + className, ex);
         }
         catch (IllegalAccessException ex)
         {
            throw new DataException("IllegalAccessException: " + className, ex);
         }
      }
   }
}
