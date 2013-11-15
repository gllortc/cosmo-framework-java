package com.cosmo.comm;

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
   public void sendMessage(CommMessage message);

   /**
    * Obtiene una propiedad de configuración del servidor.
    * 
    * @param key Clave asociada al valor que se desea obtener.
    * 
    * @return Una cadena que contiene el valor asociado a la clave proporcionada.
    */
   public String getProperty(String key);
}
