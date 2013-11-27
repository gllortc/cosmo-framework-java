package com.cosmo.comm;

import com.cosmo.util.PluginProperties;

/**
 * Interface que deben implementar todos los agentes de comunicaciones de Cosmo.
 * 
 * @author Gerard Llort
 */
public abstract class CommAgent
{
   // Declaración de variables globales
   private PluginProperties properties;


   //==============================================
   // Constructors
   //==============================================

   /**
    * Constructor de la clase {@link CommAgent}.
    * 
    * @param properties Una instancia de {@link PluginProperties} que contiene todos los parámetros de configuración.
    */
   public CommAgent(PluginProperties properties)
   {
      this.properties = properties;
   }


   //==============================================
   // Methods
   //==============================================

   /**
    * Envia un mensaje a través del servidor que representa la instancia actual.
    * 
    * @param message Una instancia de {@link CommMessage} que contiene los datos del mensaje a enviar.
    */
   public abstract void sendMessage(Message message) throws Exception;

   /**
    * Obtiene las propiedades de configuración del servidor.
    * 
    * @return Una instancia de {@link PluginProperties} que contiene todos los parámetros de configuración del servidor.
    */
   public PluginProperties getProperties()
   {
      return this.properties;
   }
}
