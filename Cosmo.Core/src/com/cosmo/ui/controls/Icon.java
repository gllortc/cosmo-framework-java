package com.cosmo.ui.controls;

/**
 * Proporciona herramientas para representar iconos.
 * 
 * @see http://fontawesome.io/icons/ 
 *
 * @author Gerard Llort
 */
public class Icon 
{
   // Medidas
   public static final String ICON_SIZE_XLARGE = "icon-4x";
   public static final String ICON_SIZE_LARGE = "icon-3x";
   public static final String ICON_SIZE_MEDIUM = "icon-2x";
   public static final String ICON_SIZE_SMALL = "icon-large";
   public static final String ICON_SIZE_DEFAULT = "";

   // Colores
   public static final String ICON_COLOR_GRAY = "gray";
   public static final String ICON_COLOR_DARKGRAY = "darkgray";
   public static final String ICON_COLOR_BLACK = "black";
   public static final String ICON_COLOR_WHITE = "white";
   public static final String ICON_COLOR_RED = "red";
   public static final String ICON_COLOR_GREEN = "green";
   public static final String ICON_COLOR_BLUE = "blue";
   public static final String ICON_COLOR_PINK = "pink";
   public static final String ICON_COLOR_DEFAULT = "";
   
	public static final String ICON_IMAGE_GLASS = "icon-glass";
	public static final String ICON_IMAGE_MUSIC = "icon-music";
	public static final String ICON_IMAGE_SEARCH = "icon-search";               
	public static final String ICON_IMAGE_ENVELOPE = "icon-envelope";             
	public static final String ICON_IMAGE_HEART = "icon-heart";                
	public static final String ICON_IMAGE_STAR = "icon-star";                 
	public static final String ICON_IMAGE_STAR_EMPTY = "icon-star-empty";           
	public static final String ICON_IMAGE_USER = "icon-user";                 
	public static final String ICON_IMAGE_FILM = "icon-film";                 
	public static final String ICON_IMAGE_TH_LARGE = "icon-th-large";             
	public static final String ICON_IMAGE_TH = "icon-th";                   
	public static final String ICON_IMAGE_TH_LIST = "icon-th-list";              
	public static final String ICON_IMAGE_OK = "icon-ok";                   
	public static final String ICON_IMAGE_REMOVE = "icon-remove";              
	public static final String ICON_IMAGE_ZOOM_IN = "icon-zoom-in";             

	public static final String ICON_IMAGE_ZOOM_OUT = "icon-zoom-out";
	public static final String ICON_IMAGE_OFF = "icon-off";     
	public static final String ICON_IMAGE_SIGNAL = "icon-signal";   
	public static final String ICON_IMAGE_COG = "icon-cog";        
	public static final String ICON_IMAGE_TRASH = "icon-trash";   
	public static final String ICON_IMAGE_HOME = "icon-home";  
	public static final String ICON_IMAGE_FILE = "icon-file"; 
	public static final String ICON_IMAGE_TIME = "icon-time"; 
	public static final String ICON_IMAGE_ROAD = "icon-road"; 
	public static final String ICON_IMAGE_DOWNLOAD_ALT = "icon-download-alt";
	public static final String ICON_IMAGE_DOWNLOAD = "icon-download"; 
	public static final String ICON_IMAGE_UPLOAD = "icon-upload";  
	public static final String ICON_IMAGE_INBOX = "icon-inbox";       
	public static final String ICON_IMAGE_PLAY_CIRCLE = "icon-play-circle";
	public static final String ICON_IMAGE_REPEAT = "icon-repeat";

	/* \f020 doesn't work in Safari. all shifted one down */
	public static final String ICON_IMAGE_REFRESH = "icon-refresh";
	public static final String ICON_IMAGE_LIST_ALT = "icon-list-alt"; 
	public static final String ICON_IMAGE_LOCK = "icon-lock";    
	public static final String ICON_IMAGE_FLAG = "icon-flag";       
	public static final String ICON_IMAGE_HEADPHONES = "icon-headphones";  
	public static final String ICON_IMAGE_VOLUME_OFF = "icon-volume-off";  
	public static final String ICON_IMAGE_VOLUME_DOWN = "icon-volume-down";
	public static final String ICON_IMAGE_VOLUME_UP = "icon-volume-up";
	public static final String ICON_IMAGE_QRCODE = "icon-qrcode";
	public static final String ICON_IMAGE_BARCODE = "icon-barcode";
	public static final String ICON_IMAGE_TAG = "icon-tag"; 
	public static final String ICON_IMAGE_TAGS = "icon-tags";     
	public static final String ICON_IMAGE_BOOK = "icon-book"; 
	public static final String ICON_IMAGE_BOOKMARK = "icon-bookmark"; 
	public static final String ICON_IMAGE_PRINT = "icon-print"; 

	public static final String ICON_IMAGE_CAMERA = "icon-camera"; 
	public static final String ICON_IMAGE_FONT = "icon-font";
	public static final String ICON_IMAGE_BOLD = "icon-bold";
	public static final String ICON_IMAGE_ITALIC = "icon-italic";
	public static final String ICON_IMAGE_TEXT_HEIGHT = "icon-text-height";
	public static final String ICON_IMAGE_TEXT_WIDTH = "icon-text-width";
	public static final String ICON_IMAGE_ALIGN_LEFT = "icon-align-left";
	public static final String ICON_IMAGE_ALIGN_CENTER = "icon-align-center";
	public static final String ICON_IMAGE_ALIGN_RIGHT = "icon-align-right"; 
	public static final String ICON_IMAGE_ALIGN_JUSTIFY = "icon-align-justify";
	public static final String ICON_IMAGE_LIST = "icon-list";
	public static final String ICON_IMAGE_INDENT_LEFT = "icon-indent-left";
	public static final String ICON_IMAGE_INDENT_RIGHT = "icon-indent-right";
	public static final String ICON_IMAGE_FACETIME_VIDEO = "icon-facetime-video";
	public static final String ICON_IMAGE_PICTURE = "icon-picture";

	public static final String ICON_IMAGE_PENCIL = "icon-pencil"; 
	public static final String ICON_IMAGE_MAP_MARKER = "icon-map-marker";
	public static final String ICON_IMAGE_ADJUST = "icon-adjust";             
	public static final String ICON_IMAGE_TINT = "icon-tint";                 
	public static final String ICON_IMAGE_EDIT = "icon-edit";             
	public static final String ICON_IMAGE_SHARE = "icon-share";             
	public static final String ICON_IMAGE_CHECK = "icon-check";            
	public static final String ICON_IMAGE_MOVE = "icon-move";              
	public static final String ICON_IMAGE_STEP_BACKWARD = "icon-step-backward";       
	public static final String ICON_IMAGE_FAST_BACKWARD = "icon-fast-backward";       
	public static final String ICON_IMAGE_BACKWARD = "icon-backward";            
	public static final String ICON_IMAGE_PLAY = "icon-play";                
	public static final String ICON_IMAGE_PAUSE = "icon-pause";               
	public static final String ICON_IMAGE_STOP = "icon-stop";                
	public static final String ICON_IMAGE_FORWARD = "icon-forward";             

	public static final String ICON_IMAGE_FAST_FORWARD = "icon-fast-forward";        
	public static final String ICON_IMAGE_STEP_FORWARD = "icon-step-forward";        
	public static final String ICON_IMAGE_EJECT = "icon-eject";               
	public static final String ICON_IMAGE_CHEVRON_LEFT = "icon-chevron-left";        
	public static final String ICON_IMAGE_CHEVRON_RIGHT = "icon-chevron-right";       
	public static final String ICON_IMAGE_PLUS_SIGN = "icon-plus-sign";           
	public static final String ICON_IMAGE_MINUS_SIGN = "icon-minus-sign";           
	public static final String ICON_IMAGE_REMOVE_SIGN = "icon-remove-sign";          
	public static final String ICON_IMAGE_OK_SIGN = "icon-ok-sign";              
	public static final String ICON_IMAGE_QUESTION_SIGN = "icon-question-sign";        
	public static final String ICON_IMAGE_INFO_SIGN = "icon-info-sign";            
	public static final String ICON_IMAGE_SCREENSHOT = "icon-screenshot";           
	public static final String ICON_IMAGE_REMOVE_CIRCLE = "icon-remove-circle";        
	public static final String ICON_IMAGE_OK_CIRCLE = "icon-ok-circle";            
	public static final String ICON_IMAGE_BAR_CIRCLE = "icon-ban-circle";           

	public static final String ICON_IMAGE_ARROW_LEFT = "icon-arrow-left";           
	public static final String ICON_IMAGE_ARROW_RIGHT = "icon-arrow-right";          
	public static final String ICON_IMAGE_ARROW_UP = "icon-arrow-up";             
	public static final String ICON_IMAGE_ARROW_DOWN = "icon-arrow-down";           
	public static final String ICON_IMAGE_SHARE_ALT = "icon-share-alt";            
	public static final String ICON_IMAGE_RESIZE_FULL = "icon-resize-full";          
	public static final String ICON_IMAGE_RESIZE_SMALL = "icon-resize-small";         
	public static final String ICON_IMAGE_PLUS = "icon-plus";                 
	public static final String ICON_IMAGE_MINUS = "icon-minus";                
	public static final String ICON_IMAGE_ASTERISK = "icon-asterisk";             
	public static final String ICON_IMAGE_EXCLAMATION_SIGN = "icon-exclamation-sign";     
	public static final String ICON_IMAGE_GIFT = "icon-gift";                 
	public static final String ICON_IMAGE_LEAF = "icon-leaf";                 
	public static final String ICON_IMAGE_FIRE = "icon-fire";                 
	public static final String ICON_IMAGE_EYE_OPEN = "icon-eye-open";             

	public static final String ICON_IMAGE_EYE_CLOSE = "icon-eye-close";            
	public static final String ICON_IMAGE_WARNING_SIGN = "icon-warning-sign";         
	public static final String ICON_IMAGE_PLANE = "icon-plane";                
	public static final String ICON_IMAGE_CALENDAR = "icon-calendar";             
	public static final String ICON_IMAGE_RANDOM = "icon-random";               
	public static final String ICON_IMAGE_COMMENT = "icon-comment";              
	public static final String ICON_IMAGE_MAGNET = "icon-magnet";               
	public static final String ICON_IMAGE_CHEVRON_UP = "icon-chevron-up";           
	public static final String ICON_IMAGE_CHEVRON_DOWN = "icon-chevron-down";         
	public static final String ICON_IMAGE_RETWEET = "icon-retweet";              
	public static final String ICON_IMAGE_CHOPPING_CART = "icon-shopping-cart";        
	public static final String ICON_IMAGE_FOLDER_CLOSE = "icon-folder-close";         
	public static final String ICON_IMAGE_FOLDER_OPEN = "icon-folder-open";          
	public static final String ICON_IMAGE_RESIZE_VERTICAL = "icon-resize-vertical";      
	public static final String ICON_IMAGE_RESIZE_HORIZONTAL = "icon-resize-horizontal";    

	public static final String ICON_IMAGE_BAR_CHART = "icon-bar-chart";            
	public static final String ICON_IMAGE_TWITTER_SIGN = "icon-twitter-sign";         
	public static final String ICON_IMAGE_FACEBOOK_SIGN = "icon-facebook-sign";        
	public static final String ICON_IMAGE_CAMERA_RETRO = "icon-camera-retro";         
	public static final String ICON_IMAGE_KEY = "icon-key";                  
	public static final String ICON_IMAGE_COGS = "icon-cogs";                 
	public static final String ICON_IMAGE_COMMENTS = "icon-comments";             
	public static final String ICON_IMAGE_THUMBS_UP = "icon-thumbs-up";            
	public static final String ICON_IMAGE_THUMBS_DOWN = "icon-thumbs-down";          
	public static final String ICON_IMAGE_STAR_HALF = "icon-star-half";            
	public static final String ICON_IMAGE_HEART_EMPTY = "icon-heart-empty";          
	public static final String ICON_IMAGE_HEART_SIGNOUT = "icon-signout";              
	public static final String ICON_IMAGE_LINKEDIN_SIGN = "icon-linkedin-sign";        
	public static final String ICON_IMAGE_PUSHPIN = "icon-pushpin";              
	public static final String ICON_IMAGE_EXTERNAL_LINK = "icon-external-link";        

	public static final String ICON_IMAGE_SIGNIN = "icon-signin";               
	public static final String ICON_IMAGE_TROPHY = "icon-trophy";               
	public static final String ICON_IMAGE_GITHUB_SIGN = "icon-github-sign";          
	public static final String ICON_IMAGE_UPLOAD_ALT = "icon-upload-alt";           
	public static final String ICON_IMAGE_LEMON = "icon-lemon";                
	public static final String ICON_IMAGE_PHONE = "icon-phone";                
	public static final String ICON_IMAGE_CHECK_EMPTY = "icon-check-empty";          
	public static final String ICON_IMAGE_BOOKMARK_EMPTY = "icon-bookmark-empty";       
	public static final String ICON_IMAGE_PHONE_SIGN = "icon-phone-sign";           
	public static final String ICON_IMAGE_TWITTER = "icon-twitter";              
	public static final String ICON_IMAGE_FACEBOOK = "icon-facebook";             
	public static final String ICON_IMAGE_GITHUB = "icon-github";               
	public static final String ICON_IMAGE_UNLOCK = "icon-unlock";               
	public static final String ICON_IMAGE_CREDIT_CARD = "icon-credit-card";          
	public static final String ICON_IMAGE_RSS = "icon-rss";                  

	public static final String ICON_IMAGE_HDD = "icon-hdd";                  
	public static final String ICON_IMAGE_BULLHORN = "icon-bullhorn";             
	public static final String ICON_IMAGE_BELL = "icon-bell";                 
	public static final String ICON_IMAGE_CERTIFICATE = "icon-certificate";          
	public static final String ICON_IMAGE_HAND_RIGHT = "icon-hand-right";           
	public static final String ICON_IMAGE_HAND_LEFT = "icon-hand-left";            
	public static final String ICON_IMAGE_HAND_UP = "icon-hand-up";              
	public static final String ICON_IMAGE_HAND_DOWN = "icon-hand-down";            
	public static final String ICON_IMAGE_CIRCLE_ARROW_LEFT = "icon-circle-arrow-left";    
	public static final String ICON_IMAGE_CIRCLE_ARROW_RIGHT = "icon-circle-arrow-right";   
	public static final String ICON_IMAGE_CIRCLE_ARROW_UP = "icon-circle-arrow-up";      
	public static final String ICON_IMAGE_CIRCLE_ARROW_DOWN = "icon-circle-arrow-down";    
	public static final String ICON_IMAGE_GLOBE = "icon-globe";                
	public static final String ICON_IMAGE_WRENCH = "icon-wrench";               
	public static final String ICON_IMAGE_TASKS = "icon-tasks";                

	public static final String ICON_IMAGE_FILTER = "icon-filter";               
	public static final String ICON_IMAGE_BRIEFCASE = "icon-briefcase";            
	public static final String ICON_IMAGE_FULLSCREEN = "icon-fullscreen";           

	public static final String ICON_IMAGE_GROUP = "icon-group";                
	public static final String ICON_IMAGE_LINK = "icon-link";                 
	public static final String ICON_IMAGE_CLOUD = "icon-cloud";                
	public static final String ICON_IMAGE_BREAKER = "icon-beaker";               
	public static final String ICON_IMAGE_CUT = "icon-cut";                  
	public static final String ICON_IMAGE_COPY = "icon-copy";                 
	public static final String ICON_IMAGE_PAPER_CLIP = "icon-paper-clip";           
	public static final String ICON_IMAGE_SAVE = "icon-save";                 
	public static final String ICON_IMAGE_SIGN_BLANK = "icon-sign-blank";           
	public static final String ICON_IMAGE_REORDER = "icon-reorder";              
	public static final String ICON_IMAGE_LIST_UL = "icon-list-ul";              
	public static final String ICON_IMAGE_LIST_OL = "icon-list-ol";              
	public static final String ICON_IMAGE_STRIKETHROUGH = "icon-strikethrough";        
	public static final String ICON_IMAGE_UNDERLINE = "icon-underline";            
	public static final String ICON_IMAGE_TABLE = "icon-table";                

	public static final String ICON_IMAGE_MAGIC = "icon-magic";                
	public static final String ICON_IMAGE_TRUCK = "icon-truck";                
	public static final String ICON_IMAGE_PINTEREST = "icon-pinterest";            
	public static final String ICON_IMAGE_PINTEREST_SIGN = "icon-pinterest-sign";       
	public static final String ICON_IMAGE_GOOGLE_PLUS_SIGN = "icon-google-plus-sign";     
	public static final String ICON_IMAGE_GOOGLE_PLUS = "icon-google-plus";          
	public static final String ICON_IMAGE_MONEY = "icon-money";                
	public static final String ICON_IMAGE_CARET_DOWN = "icon-caret-down";           
	public static final String ICON_IMAGE_CARET_UP = "icon-caret-up";             
	public static final String ICON_IMAGE_CARET_LEFT = "icon-caret-left";           
	public static final String ICON_IMAGE_CARET_RIGHT = "icon-caret-right";          
	public static final String ICON_IMAGE_COLUMNS = "icon-columns";              
	public static final String ICON_IMAGE_SORT = "icon-sort";                 
	public static final String ICON_IMAGE_SORT_DOWN = "icon-sort-down";            
	public static final String ICON_IMAGE_SORT_UP = "icon-sort-up";              

	public static final String ICON_IMAGE_ENVELOPE_ALT = "icon-envelope-alt";         
	public static final String ICON_IMAGE_LINKEDIN = "icon-linkedin";             
	public static final String ICON_IMAGE_UNDO = "icon-undo";                 
	public static final String ICON_IMAGE_LEGAL = "icon-legal";                
	public static final String ICON_IMAGE_DASHBOARD = "icon-dashboard";            
	public static final String ICON_IMAGE_COMMENT_ALT = "icon-comment-alt";          
	public static final String ICON_IMAGE_COMMENTS_ALT = "icon-comments-alt";         
	public static final String ICON_IMAGE_BOLT = "icon-bolt";                 
	public static final String ICON_IMAGE_SITEMAP = "icon-sitemap";              
	public static final String ICON_IMAGE_UMBRELLA = "icon-umbrella";             
	public static final String ICON_IMAGE_PASTE = "icon-paste";                
	public static final String ICON_IMAGE_LIGHTBULB = "icon-lightbulb";            
	public static final String ICON_IMAGE_EXCHANGE = "icon-exchange";             
	public static final String ICON_IMAGE_CLOUD_DOWNLOAD = "icon-cloud-download";       
	public static final String ICON_IMAGE_CLOUD_UPLOAD = "icon-cloud-upload";         

	public static final String ICON_IMAGE_USER_MD = "icon-user-md";              
	public static final String ICON_IMAGE_STETHOSCOPE = "icon-stethoscope";          
	public static final String ICON_IMAGE_SUITCASE = "icon-suitcase";             
	public static final String ICON_IMAGE_BELL_ALT = "icon-bell-alt";             
	public static final String ICON_IMAGE_COFFEE = "icon-coffee";               
	public static final String ICON_IMAGE_FOOD = "icon-food";                 
	public static final String ICON_IMAGE_FILE_ALT = "icon-file-alt";             
	public static final String ICON_IMAGE_BUILDING = "icon-building";             
	public static final String ICON_IMAGE_HOSPITAL = "icon-hospital";             
	public static final String ICON_IMAGE_AMBULANCE = "icon-ambulance";            
	public static final String ICON_IMAGE_MEDKIT = "icon-medkit";               
	public static final String ICON_IMAGE_FIGHTER_JET = "icon-fighter-jet";          
	public static final String ICON_IMAGE_BEER = "icon-beer";                 
	public static final String ICON_IMAGE_H_SIGN = "icon-h-sign";               
	public static final String ICON_IMAGE_PLUS_SIGN_ALT = "icon-plus-sign-alt";        

	public static final String ICON_IMAGE_DOUBLE_ANGLE_LEFT = "icon-double-angle-left";    
	public static final String ICON_IMAGE_DOUBLE_ANGLE_RIGHT = "icon-double-angle-right";   
	public static final String ICON_IMAGE_DOUBLE_ANGLE_UP = "icon-double-angle-up";      
	public static final String ICON_IMAGE_DOUBLE_ANGLE_DOWN = "icon-double-angle-down";    
	public static final String ICON_IMAGE_ANGLE_LEFT = "icon-angle-left";           
	public static final String ICON_IMAGE_ANGLE_RIGHT = "icon-angle-right";          
	public static final String ICON_IMAGE_ANGLE_UP = "icon-angle-up";             
	public static final String ICON_IMAGE_ANGLE_DOWN = "icon-angle-down";           
	public static final String ICON_IMAGE_DESKTOP = "icon-desktop";              
	public static final String ICON_IMAGE_LAPTOP = "icon-laptop";               
	public static final String ICON_IMAGE_TABLET = "icon-tablet";               
	public static final String ICON_IMAGE_MOBILE_PHONE = "icon-mobile-phone";         
	public static final String ICON_IMAGE_CIRCLE_BLANK = "icon-circle-blank";         
	public static final String ICON_IMAGE_QUOTE_LEFT = "icon-quote-left";           
	public static final String ICON_IMAGE_QUOTE_RIGHT = "icon-quote-right";          

	public static final String ICON_IMAGE_ICON_SPINNER = "icon-spinner";              
	public static final String ICON_IMAGE_CIRCLE = "icon-circle";               
	public static final String ICON_IMAGE_REPLY = "icon-reply";                
	public static final String ICON_IMAGE_GITHUB_ALT = "icon-github-alt";           
	public static final String ICON_IMAGE_FOLDER_CLOSE_ALT = "icon-folder-close-alt";     
	public static final String ICON_IMAGE_FOLDER_OPEN_ALT = "icon-folder-open-alt";
	
	/**
	 * Renderiza un icono.
	 * 
	 * @param image Una cadena que contiene el identificador de la imagen (constante {@code ICON_IMAGE_XXXX}).
	 * @param size Una cadena que contiene el identificador del tamaño (constante {@code ICON_SIZE_XXXX}).
	 * @param color Una cadena que contiene el identificador del color (constante {@code ICON_COLOR_XXXX}).
	 * 
	 * @return Una cadena en format XHTML que genera el icono especificado.
	 */
   public static String render(String image, String size, String color)
   {
      if (image.equals(""))
      {
         return "";
      }
      else
      {
         return "<i class=\"" + image + " " + size + " " + color +  "\"></i>";
      }
   }
   
   /**
    * Renderiza un icono.
    * 
    * @param image Una cadena que contiene el identificador de la imagen (constante {@code ICON_IMAGE_XXXX}).
    * @param size Una cadena que contiene el identificador del tamaño (constante {@code ICON_SIZE_XXXX}).
    * 
    * @return Una cadena en format XHTML que genera el icono especificado.
    */
   public static String render(String image, String size)
   {
      if (image.equals(""))
      {
         return "";
      }
      else
      {
         return "<i class=\"" + image + " " + size + "\"></i>";
      }
   }
   
   /**
    * Renderiza un icono.
    * 
    * @param image Una cadena que contiene el identificador de la imagen (constante {@code ICON_IMAGE_XXXX}).
    * 
    * @return Una cadena en format XHTML que genera el icono especificado.
    */
   public static String render(String image)
   {
      if (image.equals(""))
      {
         return "";
      }
      else
      {
         return "<i class=\"" + image + "\"></i>";
      }
   }
}
