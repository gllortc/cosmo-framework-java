package com.cosmo.security;

import java.util.Date;

/**
 * Representa un usuario de Cosmo.
 * @author gllort
 */
public class User implements java.security.Principal
{
   private int id;
   private String login;
   private String pwd;
   private String mail;
   private String name;
   private String city;
   private UserStates status;
   private Date created;
   private Date lastLogon;
   private int logonCount;
   
   public enum UserStates 
   {
      Disabled(0), 
      Active(1),
      NotConfirmed(2), 
      Bloqued(3);

      @SuppressWarnings("unused")
      private int value;

      private UserStates(int value) 
      {
         this.value = value;
      }
      
      public static UserStates valueOf(int value)
      {
         switch (value)
         {
            case 1:  return UserStates.Active;
            case 2:  return UserStates.NotConfirmed;
            case 3:  return UserStates.Bloqued;
            default: return UserStates.Disabled;
         }
      }
   };   
   
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
    * @param pwd Contraseña del usuario.
    */
   public User(String login, String pwd)
   {
      initialize();
      
      this.login = login;
      this.pwd = pwd;
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

   public String getPwd() 
   {
      return pwd;
   }

   public void setPwd(String pwd) 
   {
      this.pwd = pwd;
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

   public String getCity() 
   {
      return city;
   }

   public void setCity(String city)
   {
      this.city = city;
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

   public UserStates getStatus() 
   {
      return status;
   }

   public void setStatus(UserStates status) 
   {
      this.status = status;
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
      this.pwd = "";
      this.mail = "";
      this.name = "";
      this.city = "";
      this.created = new Date();
      this.lastLogon = null;
      this.logonCount = 0;
      this.status = UserStates.Disabled;
   }
}
