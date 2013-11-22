package com.cosmo;

/**
 * Implementa un interface que contiene los descriptores del producto.
 */
public interface Cosmo 
{

   //==============================================
   // Cosmo Framework Descriptors
   //==============================================

   public static final String COSMO_VERSION = "1.0beta";
   public static final String COSMO_NAME = "Cosmo Framework for Java";
   public static final String COSMO_LONGNAME = "Cosmo Framework " + Cosmo.COSMO_VERSION;


   //==============================================
   // Charsets
   //==============================================

   public static final String CHARSET_US_ASCII = "US-ASCII";
   public static final String CHARSET_ISO_8859_1 = "ISO-8859-1";
   public static final String CHARSET_UTF_8 = "UTF-8";
   public static final String CHARSET_UTF_16BE = "UTF-16BE";
   public static final String CHARSET_UTF_16LE = "UTF-16LE";
   public static final String CHARSET_UTF_16 = "UTF-16";


   //==============================================
   // Standard workspace properties
   //==============================================

   public static final String PROPERTY_WORKSPACE_TITLE = "ws.title";
   public static final String PROPERTY_WORKSPACE_URL = "ws.url";
   public static final String PROPERTY_WORKSPACE_MAIL = "ws.mail";
   public static final String PROPERTY_WORKSPACE_UI_CHARSET = "ws.ui.charset";
   public static final String PROPERTY_WORKSPACE_UI_LANGUAGE = "ws.ui.lang";
   public static final String PROPERTY_WORKSPACE_UI_RENDER_PROVIDER = "ws.ui.render.provider";
   public static final String PROPERTY_WORKSPACE_SECURITY_PROVIDER = "ws.security.provider";
   public static final String PROPERTY_WORKSPACE_SECURITY_LOGINPAGE = "ws.security.loginPage";
   public static final String PROPERTY_WORKSPACE_MENU_PROVIDER = "ws.menu.provider";
   public static final String PROPERTY_WORKSPACE_LOGGER_PROVIDER = "ws.logger.provider";
   // public static final String PROPERTY_WORKSPACE_DATA_SERVERID = "ws.data.serverId";
   public static final String PROPERTY_WORKSPACE_DATA_ORM_DRIVER = "ws.data.orm.driver";

   /*public static final String PROPERTY_WORKSPACE_COMM_MAIL_TRANSPORT = "ws.comm.mail.transport.protocol";
   public static final String PROPERTY_WORKSPACE_COMM_SMTP_HOST = "ws.comm.mail.smtp.host";
   public static final String PROPERTY_WORKSPACE_COMM_SMTP_AUTH = "ws.comm.mail.smtp.auth";
   public static final String PROPERTY_WORKSPACE_COMM_SMTP_STARTTLS = "ws.comm.mail.smtp.starttls.enable";
   public static final String PROPERTY_WORKSPACE_COMM_SMTP_LOGIN = "ws.comm.mail.smtp.login";
   public static final String PROPERTY_WORKSPACE_COMM_SMTP_PASSWORD = "ws.comm.mail.smtp.password";
   public static final String PROPERTY_WORKSPACE_COMM_SMTP_FROMNAME = "ws.comm.mail.smtp.from.name";
   public static final String PROPERTY_WORKSPACE_COMM_SMTP_FROMADD = "ws.comm.mail.smtp.from.address";*/ 


   //==============================================
   // Standard URL parameters
   //==============================================

   public static final String URL_PARAM_OBJECTID = "oid";
   public static final String URL_PARAM_TOURL = "tourl";


   //==============================================
   // Tokens
   //==============================================

   /** Clave adjunta al valor de detección de envio de un formulario Cosmo */
   public static final String KEY_UI_FORM_ACTION = "cosmo.ui.form.action";
   /** Clave adjunta al valor de detección de envio de un formulario Cosmo */
   public static final String KEY_UI_FORM_CAPTCHA = "cosmo.ui.form.captcha";

   /** Token de detección de envio de un formulario Cosmo */
   public static final String TOKEN_UI_FORM_POSTBACK = "com.cosmo.tokens.form-postback";


   //==============================================
   // Tokens de caché
   //==============================================

   /** Clave de Caché para almacenar el workspace (en sesión) */
   public static final String KEY_CACHE_SESSION_WORKSPACE = "cosmo.cache.session.workspace";
   /** Clave de Caché para almacenar la última excepción capturada (en sesión) */
   public static final String KEY_CACHE_SESSION_LASTEXCEPTION = "cosmo.cache.session.lastException";
   /** Clave de Caché para almacenar datos de formularios (se almacenan en sesión) */
   public static final String KEY_CACHE_SESSION_CTRLDATA = "cosmo.cache.ctrldata.";
}
