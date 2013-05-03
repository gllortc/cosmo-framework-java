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
import com.cosmo.security.User.UserStates;
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
    * @param login Una cadena que contiene el login del usuario.
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
    * Autentica un usuario y obtiene sus propiedades.
    * 
    * @param login Una cadena que contiene el login del usuario.
    * @param password Una cadena que contiene la contraseña del usuario.
    * 
    * @return Una instancia de {@link User} que contiene la información del usuario autenticado o {@code null} si la autenticación a fallado.
    * 
    * @throws UserNotFoundException
    * @throws AuthenticationProviderException
    */
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
         {
            user = new User();
            user.setLogin(login);
            user.setStatus(UserStates.Active);
            
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
   
   /**
    * Obtiene el patrón de login y la formatea con el login proporcionado.
    */
   private String getFormattedLogin(String login)
   {
      return this.loginPattern.replace("%login%", login);
   }
}
