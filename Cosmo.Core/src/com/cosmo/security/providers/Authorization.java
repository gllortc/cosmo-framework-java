package com.cosmo.security.providers;

import java.util.ArrayList;

import com.cosmo.security.Role;
import com.cosmo.security.UserSession.SecurityInfo;

public interface Authorization 
{
   /**
    * Carga la información de autorización de un usuario determinado.
    * 
    * @param login Una cadena que contiene el <em>login</em> del usuario.
    * 
    * @throws AuthorizationException
    */
   public SecurityInfo loadAuthorizationData(String login, SecurityInfo si) throws AuthorizationException;
   
   /**
    * Determina si un usuario tiene un rol especifico.
    * 
    * @param login Una cadena que contiene el <em>login</em> del usuario.
    * @param role Una cadena que contiene el nombre (ID) del rol.
    * 
    * @return {@code true} si el usuario tiene asignado el rol o {@code false} en cualquier otro caso.
    */
   public boolean isUserInRole(String login, String role) throws AuthorizationException;
   
   /**
    * Determina si un usuario tiene un rol especifico entre una lista de roles.
    * 
    * @param login Una cadena que contiene el <em>login</em> del usuario.
    * @param roles Un array con los identificadores de rol.
    * 
    * @return {@code true} si el usuario tiene asignado al menos un rol de los contenidos en la lista o {@code false} en cualquier otro caso.
    * 
    * @throws AuthorizationException
    */
   public boolean isUserInRole(String login, ArrayList<String> roles) throws AuthorizationException;

   /**
    * Determina si un usuario tiene permiso para ejecutar determinada actividad.
    * 
    * @param login Una cadena que contiene el <em>login</em> del usuario.
    * @param activityId Una cadena que contiene el nombre (ID) de la actividad.
    * 
    * @return @return {@code true} si el usuario tiene permiso para ejecutar la actividad o {@code false} en cualquier otro caso.
    */
   public boolean isActivityGranted(String login, String activityId);

   /**
    * Obtiene la lista de roles asignados al usuario.
    * 
    * @param login Una cadena que contiene el <em>login</em> del usuario.
    * 
    * @return Un array con los nombres (IDs) de los roles asignados al usuario.
    */
   public ArrayList<Role> getRolesByUser(String login) throws AuthorizationException;
}
