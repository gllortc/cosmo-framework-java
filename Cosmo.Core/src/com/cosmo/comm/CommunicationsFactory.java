package com.cosmo.comm;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import com.cosmo.Workspace;
import com.cosmo.structures.PluginProperties;
import com.cosmo.util.StringUtils;

/**
 * Implementa una clase que permite obtener la instancia del agente de autenticación configurado en el workspace.
 * <br /><br />
 * Esta clase está diseñada según el patrón <em>Singleton</em> para asegurar que sólo se instancia una vez
 * la implementación del agente de autenticación.
 * 
 * @author Gerard Llort
 */
public abstract class CommunicationsFactory 
{
   // Mapa con las instancias únicas de los agentes de comunicaciones
   private static HashMap<String, CommAgent> agents = null;


   //==============================================
   // Static members
   //==============================================

   /**
    * Envia un mensaje usando un determinado agente de comunicaciones.
    * 
    * @param workspace Una instancia de {@link Workspace} que representa el workspace actual.
    * @param agentId Identificador del agente de comunicaciones a usar.
    * @param message Una instancia de {@link Message} que contiene los detalles del mensaje.
    * 
    * @throws Exception
    */
   public static void sendMessage(Workspace workspace, String agentId, Message message) throws Exception
   {
      CommAgent server = loadProvider(workspace, agentId);

      server.sendMessage(message);
   }

   /**
    * Envia un mensaje usando el agente de comunicaciones por defecto.
    * 
    * @param workspace Una instancia de {@link Workspace} que representa el workspace actual.
    * @param message Una instancia de {@link Message} que contiene los detalles del mensaje.
    * 
    * @throws Exception
    */
   public static void sendMessage(Workspace workspace, Message message) throws Exception
   {
      sendMessage(workspace, null, message);
   }

   /**
    * Devuelve una instancia de {@link CommAgent} convenientemente instanciada.
    * 
    * @param workspace Una instancia de {@link Workspace} que representa el workspace actual.
    * @param agentId Identificador del agente de comunicaciones a usar.
    * 
    * @return Una instancia única de {@link CommAgent} (sigleton).
    * 
    * @throws CommunicationsException
    */
   public static CommAgent getInstance(Workspace workspace, String agentId) throws CommunicationsException
   {
      return loadProvider(workspace, agentId);
   }

   /**
    * Devuelve una instancia de {@link CommAgent} convenientemente instanciada.
    * 
    * @param workspace Una instancia de {@link Workspace} que representa el workspace actual.
    * 
    * @return Una instancia única de {@link CommAgent} (sigleton).
    * 
    * @throws CommunicationsException
    */
   public static CommAgent getInstance(Workspace workspace) throws CommunicationsException
   {
      return getInstance(workspace, null);
   }


   //==============================================
   // Private members
   //==============================================

   /**
    * Carga el controlador de usuarios.
    *
    * @throws CommunicationsException
    */
   private static CommAgent loadProvider(Workspace workspace, String agentId) throws CommunicationsException
   {
      String className;

      // Inicializa el diccionario de agentes de comunicación
      if (agents == null)
      {
         agents = new HashMap<String, CommAgent>();
      }

      // Si no se proporciona un identificador de agente, se usa el especificado por defecto en la configuración.
      if (StringUtils.isNullOrEmptyTrim(agentId))
      {
         agentId = workspace.getProperties().getDefaultCommunicationsAgentsId();
      }

      if (agents.containsKey(agentId))
      {
         return agents.get(agentId);
      }
      else
      {
         PluginProperties agent = workspace.getProperties().getCommunicationAgent(agentId);

         className = agent.getModuleClass();
         if (StringUtils.isNullOrEmptyTrim(className))
         {
            throw new CommunicationsException("Communications Configuration Exception: No communications driver found");
         }

         try
         {
            Class<?> cls = Class.forName(className);

            // Genera la instancia del agente solicitado
            Class<?>[] types = { PluginProperties.class };
            Object[] arguments = { agent };
            CommAgent server = (CommAgent) cls.getDeclaredConstructor(types).newInstance(arguments);

            // Almacena la instancia en la lista de instancias de agentes (Singleton)
            agents.put(agentId, server);

            return server;
         }
         catch (NoSuchMethodException ex)
         {
            throw new CommunicationsException("NoSuchMethodException: " + className, ex);
         }
         catch (InvocationTargetException ex)
         {
            throw new CommunicationsException("InvocationTargetException: " + className, ex);
         }
         catch (ClassNotFoundException ex)
         {
            throw new CommunicationsException("ClassNotFoundException: " + className, ex);
         }
         catch (InstantiationException ex)
         {
            throw new CommunicationsException("InstantiationException: " + className, ex);
         }
         catch (IllegalAccessException ex)
         {
            throw new CommunicationsException("IllegalAccessException: " + className, ex);
         }
      }
   }
}
