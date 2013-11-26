package com.cosmo.security.auth;

import java.util.HashMap;

import com.cosmo.security.UserSecurityPolicy;

/**
 * Interface que deben implementar los agentes de autorización.
 * 
 * @author Gerard Llort
 */
public interface Authorization 
{

   //------------------------------------------
   // Properties
   //------------------------------------------

   /**
    * Devuelve un {@code hash} que contiene los parámetros de configuración del agente de seguridad.
    */
   public HashMap<String, String> getParameters();


   //------------------------------------------
   // Methods
   //------------------------------------------

   /**
    * Carga la información de autorización de un usuario determinado.
    * 
    * @param login Una cadena que contiene el <em>login</em> del usuario.
    * 
    * @throws AuthorizationException
    */
   public UserSecurityPolicy getAuthorizationData(String login) throws AuthorizationException;

}
