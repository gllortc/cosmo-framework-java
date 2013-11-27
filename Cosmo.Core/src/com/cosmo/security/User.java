package com.cosmo.security;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Representa un usuario de Cosmo.
 * 
 * @author Gerard Llort
 */
public class User implements java.security.Principal
{
   private String login;
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

   /**
    * Devuelve el número de veces que el usuario se ha autenticado.
    */
   public int getLogonCount() 
   {
      return logonCount;
   }

   /**
    * Establece el número de veces que el usuario se ha autenticado.
    */
   public void setLogonCount(int logonCount) 
   {
      this.logonCount = logonCount;
   }


   //==============================================
   // Methods
   //==============================================

   /**
    * Transforma la información de la instancia en una cadena con información comprensible. 
    */
   @Override
   public String toString()
   {
      SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
      
      return "[login       : " + this.login + "\n" +
             " mail        : " + this.mail + "\n" +
             " name        : " + this.name + "\n" +
             " created     : " + sdf.format(this.created) + "\n" +
             " last logon  : " + sdf.format(this.lastLogon) + "\n" +
             " logon count : " + this.logonCount + "]";
   }


   //==============================================
   // Private members
   //==============================================

   /**
    * Inicializa la instancia.
    */
   private void initialize()
   {
      // this.id = -1;
      this.login = "";
      this.mail = "";
      this.name = "";
      this.created = new Date();
      this.lastLogon = null;
      this.logonCount = 0;
   }
}
