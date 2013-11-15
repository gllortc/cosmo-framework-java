package com.cosmo.comm;

import com.cosmo.structures.PluginProperties;

/**
 * Interface que deben implementar todos los servidores de comunicaciones de Cosmo.
 * 
 * @author Gerard Llort
 */
public interface CommServer
{
   /**
    * Envia un mensaje a través del servidor que representa la instancia actual.
    * 
    * @param message Una instancia de {@link CommMessage} que contiene los datos del mensaje a enviar.
    */
   public void sendMessage(Message message) throws Exception;

   /**
    * Obtiene las propiedades de configuración del servidor.
    * 
    * @return Una instancia de {@link PluginProperties} que contiene todos los parámetros de configuración del servidor.
    */
   public PluginProperties getProperties();
}
