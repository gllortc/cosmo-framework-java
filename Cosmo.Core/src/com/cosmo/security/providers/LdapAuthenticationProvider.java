package com.cosmo.security.providers;

import netscape.ldap.LDAPConnection;
import netscape.ldap.LDAPEntry;
import netscape.ldap.LDAPException;
import netscape.ldap.LDAPSearchConstraints;
import netscape.ldap.LDAPSearchResults;
import netscape.ldap.LDAPv2;

import com.cosmo.Workspace;
import com.cosmo.security.Agent;
import com.cosmo.security.User;
import com.cosmo.security.UserNotFoundException;
import com.cosmo.util.StringUtils;

/**
 * Proveedor de seguridad nativo de Cosmo.<br />
 * Este proveedor requiere conexión a BBDD y tener las tablas de usuarios de Cosmo.
 * 
 * @author Gerard Llort
 */
public class LdapAuthenticationProvider extends AuthenticationProvider 
{
   private Workspace workspace;
   private Agent agent;

   private LDAPConnection connection;
   private LDAPSearchConstraints searchCons;
   private int option;
   private Object optionValue;
   private boolean connected;
   private String searchBase;
   
   // private static String PARAM_CONTEXTFACTORY = "context-factory";
   // private static String PARAM_PROVIDERURL = "provider-url";
   private static String PARAM_HOSTURL = "host-url";
   private static String PARAM_HOSTPORT = "host-port";
   private static String PARAM_SEARCHBASE = "search-base";
   
   private static int MAX_QUERY_RESULTS = 100;
   
   //==============================================
   // Constructors
   //==============================================
   
   /**
    * Constructor de la clase. 
    * 
    * @param workspace Una instancia de {@link Workspace} que representa el workspace al que está conectado actualmente.
    */
   public LdapAuthenticationProvider(Workspace workspace)
   {
      connected = false;
      option = -1;
      optionValue = null;
      
      this.workspace = workspace;
      this.agent = workspace.getProperties().getAuthenticationAgent();

      this.searchBase = agent.getParam(PARAM_SEARCHBASE);
   }
   
   
   //==============================================
   // Methods
   //==============================================
   
   /**
    * Verifica las credenciales de un usuario.
    * 
    * @param login Login del usuario.
    * @param password Contraseña (sin encriptar) del usuario.
    * @return Una instancia de {@link User} que representa el usuario al que corresponden las credenciales proporcionadas.
    * 
    * @throws UserNotFoundException
    * @throws AuthenticationProviderException 
    */
   @Override
   public User login(String login, String password) throws UserNotFoundException, AuthenticationProviderException
   {
      User user = null;
      
      try
      {
         connect();
         autheticate(login, password);
         disconnect();
      }
      catch (LDAPException ex)
      {
         throw new AuthenticationProviderException(ex.getMessage(), ex);
      }
      
      return user;
   }
   
   /**
    * Elimina la información de autenticación actual.
    */
   @Override
   public void logout()
   {
      return;
   }
   
   
   //==============================================
   // Private members
   //==============================================
   
   /**
    * Ens conecta al directori.
    *
    * @exception netscape.ldap.LDAPException Si no es pot establir la connexió.
    */
   private void connect() throws LDAPException
   {
      int hostPort;
      String hostUrl;
      
      if (connected)
      {
         return;
      }
      
      // Obtiene la configuración de conexión
      hostUrl = workspace.getProperties().getAuthenticationAgent().getParam(PARAM_HOSTURL);
      hostPort = Integer.parseInt(workspace.getProperties().getAuthenticationAgent().getParam(PARAM_HOSTPORT));

      connection = new LDAPConnection();
      connection.connect(hostUrl, hostPort);
      searchCons = connection.getSearchConstraints();
      if (optionValue != null)
      {
         connection.setOption(option, optionValue);
      }

      searchCons.setMaxResults(MAX_QUERY_RESULTS);
      // this.mSearchCons.setServerTimeLimit(this.mTimeLimit); ?? no va pq??
      // searchCons.setBatchSize(1); // hauria de ser 0 o 1? o no posar res??
   }
   
   /**
    * Ens desconecta del directori.
    *
    * @exception netscape.ldap.LDAPException Si no es pot desconectar.
    */
   private void disconnect() throws LDAPException
   {
      if (connected)
      {
         connection.disconnect();
      }

      connected = false;
      connection = null;
   }
   
   /**
    * Ens diu si ja existeix un usuari amb un uid especificat al directori.
    *
    * @param uid Identificador d'usuari que es busca.
    * @return True si existeix l'usuari al directori, False en cas contrari
    * @exception netscape.ldap.LDAPException Si no es pot establir la connexió.
    */
   public boolean existsUid(String uid) throws LDAPException
   {
      LDAPSearchResults res = null;

      if (StringUtils.isNullOrEmptyTrim(uid))
      {
         return false;
      }

      String my_filter = "(uid=" + uid + ")";
      res = connection.search(searchBase, LDAPv2.SCOPE_SUB, my_filter, null, false, searchCons);

      if (res.hasMoreElements())
      {
         return true;
      }

      return false;
   }
   
   /**
    * Ens diu si la cadena té la forma d'un DN (Distinguished Name).
    *
    * @param usr String que representa el Distinguished Name que volem comprovar.
    * @return True si el format del String passat com a paràmetre té la forma d'un DN (Distinguished Name), False en cas contrari
    */
   public boolean isDN(String usr)
   {
      if (StringUtils.isNullOrEmptyTrim(usr))
      {
         return false;
      }

      if (usr.indexOf("=") != -1)
      {
         return true;
      }

      return false;
   }
   
   /**
    * Recupera el DN (Distinguished Name) d'un usuari amb un uid especificat.
    *
    * @param uid Identificador d'usuari únic (uid) del que volem obtenir el Distinguished Name
    * 
    * @throws netscape.ldap.LDAPException Si no es troba el DN, si no ens podem autenticar, si no es pot modificar la entrada, ...
    * @throws AuthenticationProviderException 
    */
   public String getDN(String uid) throws LDAPException, AuthenticationProviderException
   {
      String dn = null;
      LDAPEntry findEntry = null;

      if (StringUtils.isNullOrEmptyTrim(uid))
      {
         throw new AuthenticationProviderException("Cosmo LDAP Authentication provider: Can't get DN. No UID provided.");
      }
      
      String my_filter = "(uid=" + uid + ")";
      LDAPSearchResults res = connection.search(searchBase, LDAPv2.SCOPE_SUB, my_filter, null, false, searchCons);
      if (res.hasMoreElements())
      {
         findEntry = res.next();
         dn = findEntry.getDN();
      }
      
      return dn;
   }

   /**
    * Recupera el DN (Distinguished Name) d'un usuari amb un uid especificat. La búsqueda es realitza a partir de la base especificada.
    *
    * @param uid Uid de l'usuari del que volem recuperar el DN
    * @param searchbase Nivell del directori a partil del qual començarà a buscar
    * @exception netscape.ldap.LDAPException Si no es troba el DN, si no ens podem autenticar, si no es pot modificar la entrada, ...
    */
   private String getDN(String uid, String searchbase) throws LDAPException
   {
      String dn = null;
      LDAPEntry findEntry = null;

      if (!connected)
      {
         connect();
      }
      
      String my_filter = "(uid=" + uid + ")";
      LDAPSearchResults res = connection.search(searchbase, LDAPv2.SCOPE_SUB, my_filter, null, false, searchCons);
      if (res.hasMoreElements())
      {
         findEntry = res.next();
         dn = findEntry.getDN();
      }

      disconnect();

      return dn;
   }
   
   /**
    * Autentica al directori fent servir un usuari i un password especificats .
    *
    * @param dn Distinguished Name que volem autenticar.
    * @param passwd Password corresponent a aquella entrada (dn) que volem autenticar.
    * @exception netscape.ldap.LDAPException Si no es troba el DN, si no ens podem autenticar, si no es pot modificar la entrada, ...
    * @throws AuthenticationProviderException 
    */
   private void autheticate(String dn, String passwd) throws LDAPException, AuthenticationProviderException
   {
      if (StringUtils.isNullOrEmptyTrim(dn))
      {
         throw new AuthenticationProviderException("Cosmo LDAP Authentication provider: Can't authenticate. No DN provided.");
      }

      if (!isDN(dn))
      {
         dn = getDN(dn);
      }

      if (!connected)
      {
         connect();
      }
      
      connection.authenticate(dn, passwd);

      disconnect();
   }
}
