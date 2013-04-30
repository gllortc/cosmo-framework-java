package com.cosmo.security.providers;

/**
 * Interface que deben implementar los proveedores de autorización de usuario.
 * 
 * @author Gerard Llort
 */
public interface AuthorizationProviderInterface 
{
   
   //==============================================
   // Methods
   //==============================================
   
   /**
    * Carga la información de autenticación en memória.
    * 
    * @param login Una cadena que contiene el login del usuario.
    */
   public void loadAuthorizationData(String login);

   /**
    * Verifica si un determinado usuario pertenece a un determinado rol.
    * 
    * @param login Una cadena que contiene el login del usuario.
    * @param role Una cadena que contiene el identificador del rol.
    * 
    * @return {@code true} si el usuario pertenece al rol o {@code false} en cualquier otro caso.
    */
   public boolean isUserInRole(String login, String role);

   /**
    * Verifica si un usuario tiene permiso para ejecutar determinada actividad.
    * 
    * @param login Una cadena que contiene el login del usuario.
    * @param activityId Una cadena que contiene el identificador de la actividad.
    * 
    * @return {@code true} si el usuario tiene permiso de ejecución sobre la actividad o {@code false} en cualquier otro caso.
    */
   public boolean isActivityGranted(String login, String activityId);
   
   /**
    * Devuelve un array con todos los roles asociados a un usuario.
    * 
    * @param login Una cadena que contiene el login del usuario.
    * 
    * @return Un array de {@link String} con todos los identificadores de rol asociados al usuario.
    */
   public String[] getUserRoles(String login);

}
