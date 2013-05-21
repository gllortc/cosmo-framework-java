package com.cosmo.security;

import java.util.Date;

/**
 * Representa un usuario de Cosmo.
 * 
 * @author gerard llort
 */
public class User implements java.security.Principal
{
   private int id;
   private String login;
   // private String pwd;
   private String mail;
   private String name;
   private Date created;
   private Date lastLogon;
   private int logonCount;

   
   //==============================================
   // Constructors
   //==============================================
   
   /**
    * Constructor de la clase.
    */
   public User()
   {
      initialize();
   }
   
   /**
    * Constructor de la clase.
    * 
    * @param login Login del usuario.
    * @param mail Cuenta de correo electrónico del usuario.
    * @param name Nombre completo del usuario.
    */
   public User(String login, String mail, String name)
   {
      initialize();
      
      this.login = login;
      this.mail = mail;
      this.name = name;
   }
   
   //==============================================
   // Properties
   //==============================================
   
   public int getId() 
   {
      return id;
   }

   public void setId(int id) 
   {
      this.id = id;
   }

   public String getLogin() 
   {
      return login;
   }

   public void setLogin(String login) 
   {
      this.login = login;
   }

   public String getMail() 
   {
      return mail;
   }

   public void setMail(String mail) 
   {
      this.mail = mail;
   }

   public String getName() 
   {
      return name;
   }

   public void setName(String name) 
   {
      this.name = name;
   }

   public Date getCreated() 
   {
      return created;
   }

   public void setCreated(Date created) 
   {
      this.created = created;
   }

   public Date getLastLogin() 
   {
      return lastLogon;
   }

   public void setLastLogin(Date lastLogon) 
   {
      this.lastLogon = lastLogon;
   }

   public int getLogonCount() 
   {
      return logonCount;
   }

   public void setLogonCount(int logonCount) 
   {
      this.logonCount = logonCount;
   }


   //==============================================
   // Private members
   //==============================================
   
   /**
    * Inicializa la instancia.
    */
   private void initialize()
   {
      this.id = -1;
      this.login = "";
      this.mail = "";
      this.name = "";
      this.created = new Date();
      this.lastLogon = null;
      this.logonCount = 0;
   }
}
