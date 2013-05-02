package com.cosmo.security.providers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import javax.net.ssl.HttpsURLConnection;

import com.cosmo.Workspace;
import com.cosmo.security.Agent;
import com.cosmo.security.User;
import com.cosmo.security.UserNotFoundException;

/**
 * Proveedor de seguridad nativo de Cosmo.<br />
 * Este proveedor requiere conexión a BBDD y tener las tablas de usuarios de Cosmo.
 * 
 * @author Gerard Llort
 */
public class CasAuthenticationProvider extends AuthenticationProvider 
{
   private Workspace workspace;
   private Agent agent;

   private static String PARAM_CASSERVICE = "cas-service";
   
   private String grantingTicket;
   
   //==============================================
   // Constructors
   //==============================================
   
   /**
    * Constructor de la clase. 
    * 
    * @param workspace Una instancia de {@link Workspace} que representa el workspace al que está conectado actualmente.
    */
   public CasAuthenticationProvider(Workspace workspace)
   {
      grantingTicket = "";
      
      this.workspace = workspace;
      this.agent = this.workspace.getProperties().getAuthenticationAgent();
   }
   
   
   //==============================================
   // Properties
   //==============================================
   
   /**
    * Devuelve el Ticket Granting Ticket de CAS.
    * 
    * @return Una cadena que contiene el Ticket Granting Ticket de CAS.
    */
   public String getGrantingTicket()
   {
      return this.grantingTicket;
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
         validateFromCAS(login, password, "service");
      }
      catch (Exception ex)
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
   
   public boolean validateFromCAS(String username, String password, String service) throws Exception
   {
      String s;
      String url = agent.getParam(PARAM_CASSERVICE);
      
      try
      {
         com.cosmo.util.URL qurl = new com.cosmo.util.URL(url);
         qurl.addParameter("username", username);
         qurl.addParameter("password", password);
         
         HttpsURLConnection httpsConnection = (HttpsURLConnection)openConn(url);
         s = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8");
         s += "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");
    
         System.out.println(s);
         OutputStreamWriter out = new OutputStreamWriter(httpsConnection.getOutputStream());
         BufferedWriter bwr = new BufferedWriter(out);
         bwr.write(s);
         bwr.flush();
         bwr.close();
         out.close();
    
         this.grantingTicket = httpsConnection.getHeaderField("location");
         System.out.println( httpsConnection.getResponseCode());
         if (this.grantingTicket != null && httpsConnection.getResponseCode() == 201)
         {
            System.out.println(this.grantingTicket);
            System.out.println("Tgt is : " + this.grantingTicket.substring(this.grantingTicket.lastIndexOf("/") + 1));
            this.grantingTicket = this.grantingTicket.substring(this.grantingTicket.lastIndexOf("/") + 1);
            bwr.close();
            closeConn(httpsConnection);

            String serviceURL = "https://myserver.com/testApplication";
            String encodedServiceURL = URLEncoder.encode("service", "utf-8") + "=" + URLEncoder.encode(serviceURL, "utf-8");
            System.out.println("Service url is : " + encodedServiceURL);

            String myURL = url + "/" + this.grantingTicket;
            System.out.println(myURL);
            httpsConnection = (HttpsURLConnection)openConn(myURL);
            out = new OutputStreamWriter(httpsConnection.getOutputStream());
            bwr = new BufferedWriter(out);
            bwr.write(encodedServiceURL);
            bwr.flush();
            bwr.close();
            out.close();
  
            System.out.println("Response code is: " + httpsConnection.getResponseCode());
    
            BufferedReader isr = new BufferedReader(new InputStreamReader(httpsConnection.getInputStream()));
            String line;
            System.out.println(httpsConnection.getResponseCode());
            
            while ((line = isr.readLine()) != null) 
            {
               System.out.println( line);
            }

            isr.close();
            httpsConnection.disconnect();

            return true;
         }
         else
         {
            return false;
         }
      }
      catch(MalformedURLException mue)
      {
         mue.printStackTrace();
         throw mue;
      }
      catch(IOException ioe)
      {
         ioe.printStackTrace();
         throw ioe;
      }
   }

   static URLConnection openConn(String urlk) throws MalformedURLException, IOException
   {
      URL url = new URL(urlk);
      HttpsURLConnection hsu = (HttpsURLConnection) url.openConnection();

      hsu.setDoInput(true);
      hsu.setDoOutput(true);
      hsu.setRequestMethod("POST");

      return hsu;
   }

   static void closeConn(HttpsURLConnection c)
   {
      c.disconnect();
   }
}
