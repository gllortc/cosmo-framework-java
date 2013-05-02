package com.cosmo.security.providers;

import java.util.Enumeration;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import com.cosmo.Workspace;
import com.cosmo.security.Agent;
import com.cosmo.security.User;
import com.cosmo.security.UserNotFoundException;

/**
 * Proveedor de autenticación LDAP para Cosmo.
 * 
 * @author Gerard Llort
 */
public class LdapAuthenticationProvider extends AuthenticationProvider 
{
   private Workspace workspace;
   private Agent agent;

   private String loginPattern;
   
   private static String PARAM_HOSTURL = "host-url";
   private static String PARAM_HOSTPORT = "host-port";
   private static String PARAM_SEARCHBASE = "search-base";
   private static String PARAM_LOGINPATTERN = "login-pattern";
   private static String PARAM_CONTEXTFACTORY = "context-factory";
   private static String PARAM_ATTR_MAIL = "ldap-attrib-mail";
   private static String PARAM_ATTR_NAME = "ldap-attrib-name";
   private static String PARAM_ATTR_SURNAME = "ldap-attrib-surname";
   
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
      this.workspace = workspace;
      this.agent = this.workspace.getProperties().getAuthenticationAgent();

      agent.getParam(PARAM_SEARCHBASE);
      this.loginPattern = agent.getParam(PARAM_LOGINPATTERN);
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
      return authenticate(login, password);
   }
   
   /**
    * Elimina la información de autenticación actual.
    */
   @Override
   public void logout()
   {
      return;
   }
   
   /**
    * Revalida la sesión de usuario.
    */
   @Override
   public void validate() 
   {
      return;   
   };
   
   
   //==============================================
   // Private members
   //==============================================
   
   /**
    * Ens conecta al directori.
    *
    * @exception netscape.ldap.LDAPException Si no es pot establir la connexió.
    */
   /*private void connect() throws LDAPException
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
   }*/
   
   /**
    * Ens desconecta del directori.
    *
    * @exception netscape.ldap.LDAPException Si no es pot desconectar.
    */
   /*private void disconnect() throws LDAPException
   {
      if (connected)
      {
         connection.disconnect();
      }

      connected = false;
      connection = null;
   }*/
   
   /**
    * Ens diu si ja existeix un usuari amb un uid especificat al directori.
    *
    * @param uid Identificador d'usuari que es busca.
    * @return True si existeix l'usuari al directori, False en cas contrari
    * @exception netscape.ldap.LDAPException Si no es pot establir la connexió.
    */
   /*public boolean existsUid(String uid) throws LDAPException
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
   }*/
   
   /**
    * Ens diu si la cadena té la forma d'un DN (Distinguished Name).
    *
    * @param usr String que representa el Distinguished Name que volem comprovar.
    * @return True si el format del String passat com a paràmetre té la forma d'un DN (Distinguished Name), False en cas contrari
    */
   /*public boolean isDN(String usr)
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
   }*/
   
   /**
    * Recupera el DN (Distinguished Name) d'un usuari amb un uid especificat.
    *
    * @param uid Identificador d'usuari únic (uid) del que volem obtenir el Distinguished Name
    * 
    * @throws netscape.ldap.LDAPException Si no es troba el DN, si no ens podem autenticar, si no es pot modificar la entrada, ...
    * @throws AuthenticationProviderException 
    */
   /*public String getDN(String uid) throws LDAPException, AuthenticationProviderException
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
   }*/

   /**
    * Autentica al directori fent servir un usuari i un password especificats .
    *
    * @param dn Distinguished Name que volem autenticar.
    * @param passwd Password corresponent a aquella entrada (dn) que volem autenticar.
    * @exception netscape.ldap.LDAPException Si no es troba el DN, si no ens podem autenticar, si no es pot modificar la entrada, ...
    * @throws AuthenticationProviderException 
    */
   /*private void autheticate(String dn, String passwd) throws LDAPException, AuthenticationProviderException
   {
      if (StringUtils.isNullOrEmptyTrim(dn))
      {
         throw new AuthenticationProviderException("Cosmo LDAP Authentication provider: Can't authenticate. No DN provided.");
      }

      / *if (!isDN(dn))
      {
         dn = getDN(getFormattedLogin(dn));
      }* /

      connect();
      connection.authenticate(getFormattedLogin(dn), passwd);
      disconnect();
   }*/
   
   private String getFormattedLogin(String login)
   {
      return this.loginPattern.replace("%login%", login);
   }
   
   
   /*public static String INITCTX = "com.sun.jndi.ldap.LdapCtxFactory";
   public static String MY_HOST = "ldap://yoda:391";
   public static String MY_SEARCHBASE = "dc=isdintegration,dc=com";
   public static String MY_FILTER = "jabberID=test1@yoda";
   public static String MGR_DN = "cn=stuart";
   public static String MGR_PW = "stuart";*/
   
   private User authenticate(String login, String password) throws UserNotFoundException, AuthenticationProviderException
   {
      String attrValue;
      User user = null;
      
      try
      {
         Hashtable<String, String> env = new Hashtable<String, String>();
         env.put(Context.INITIAL_CONTEXT_FACTORY, agent.getParam(PARAM_CONTEXTFACTORY));
         env.put(Context.PROVIDER_URL, "ldap://" + agent.getParam(PARAM_HOSTURL) + ":" + agent.getParam(PARAM_HOSTPORT));
         env.put(Context.SECURITY_AUTHENTICATION, "simple");
         env.put(Context.SECURITY_PRINCIPAL, getFormattedLogin(login));
         env.put(Context.SECURITY_CREDENTIALS, password);
         
         DirContext ctx = new InitialDirContext(env);
         SearchControls constraints = new SearchControls();
         constraints.setSearchScope(SearchControls.SUBTREE_SCOPE);
         
         // Realiza la búsqueda del usuario en el directorio y se autentica
         NamingEnumeration<?> results = ctx.search(agent.getParam(PARAM_SEARCHBASE), "cn=" + login, constraints);
         
         // Verifica si se ha encontrado la entrada en el directorio
         if (results == null || !results.hasMore())
         {
            throw new UserNotFoundException();
         }
         else
         // while (results.hasMore())
         {
            user = new User();
            user.setLogin(login);
            
            SearchResult sr = (SearchResult) results.next();
            Attributes attrs = sr.getAttributes();

            for (NamingEnumeration<?> nenum = attrs.getAll(); nenum.hasMoreElements();)
            {
               Attribute attrib = (Attribute) nenum.next();
               
               if (attrib.getID().trim().equalsIgnoreCase(agent.getParam(PARAM_ATTR_MAIL)))
               {
                  attrValue = "";
                  for (Enumeration<?> vals = attrib.getAll(); vals.hasMoreElements();)  
                  {
                     attrValue += vals.nextElement();
                  }
                  user.setMail(attrValue);
               }
               else if (attrib.getID().trim().equalsIgnoreCase(agent.getParam(PARAM_ATTR_NAME)))
               {
                  attrValue = "";
                  for (Enumeration<?> vals = attrib.getAll(); vals.hasMoreElements();)  
                  {
                     attrValue += vals.nextElement();
                  }
                  user.setName(attrValue);
               }
               else if (attrib.getID().trim().equalsIgnoreCase(agent.getParam(PARAM_ATTR_SURNAME)))
               {
                  attrValue = "";
                  for (Enumeration<?> vals = attrib.getAll(); vals.hasMoreElements();)  
                  {
                     attrValue += vals.nextElement();
                  }
                  user.setName(user.getName() + " " + attrValue);
               }
            }
         }
      }
      catch (NamingException ex)
      {
         throw new AuthenticationProviderException(ex.getMessage());
      }
      
      return user;
   }
}
