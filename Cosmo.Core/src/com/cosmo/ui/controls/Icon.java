package com.cosmo.ui.controls;

/**
 *
 * @author Gerard Llort
 */
public class Icon 
{
   // Medidas
   public static final String ICON_SIZE_XLARGE = "x-large";
   public static final String ICON_SIZE_LARGE = "large";
   public static final String ICON_SIZE_MEDIUM = "medium";
   public static final String ICON_SIZE_SMALL = "small";
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
   
   // Iconos
   public static final String ICON_IMAGE_WARNING = "!";
   public static final String ICON_IMAGE_COLOR = "#";
   public static final String ICON_IMAGE_CART = "$";
   public static final String ICON_IMAGE_FLAG = "%";
   public static final String ICON_IMAGE_LOCATION = "&";
   public static final String ICON_IMAGE_COMMENT = "\"";
	public static final String ICON_IMAGE_COMMENT2 = "'";
	public static final String ICON_IMAGE_DOWNLOAD = "(";
	public static final String ICON_IMAGE_UPLOAD = ")";
	public static final String ICON_IMAGE_STARTBUST = "*";
	public static final String ICON_IMAGE_FOLDER = ",";
	public static final String ICON_IMAGE_PAPER = ".";
	public static final String ICON_IMAGE_PLUS = "+";
	public static final String ICON_IMAGE_MINIMIZE = "-";
   public static final String ICON_IMAGE_BAG = "/";
	public static final String ICON_IMAGE_PICTURE = "0";
	public static final String ICON_IMAGE_PHOTO = "1";
	public static final String ICON_IMAGE_VIDEO = "2";
	public static final String ICON_IMAGE_MUSIC = "3";
	public static final String ICON_IMAGE_MONITOR = "4";
	public static final String ICON_IMAGE_CAMERA = "5";
	public static final String ICON_IMAGE_CALENDAR = "6";
	public static final String ICON_IMAGE_PENCIL = "7";
	public static final String ICON_IMAGE_PHONE = "8";
	public static final String ICON_IMAGE_MICROPHONE = "9";
	public static final String ICON_IMAGE_UNDO = ":";
	public static final String ICON_IMAGE_REDO = ";";
	public static final String ICON_IMAGE_ZOOMIN = "&lt;";
	public static final String ICON_IMAGE_EQUALIZER = "=";
	public static final String ICON_IMAGE_ZOOMOUT = "&gt;";
	public static final String ICON_IMAGE_QUESTION = "?";
	public static final String ICON_IMAGE_MAIL = "@";
	public static final String ICON_IMAGE_PAPERPLANE = "A";
	public static final String ICON_IMAGE_TAG = "B";
	public static final String ICON_IMAGE_CHECKMARK = "C";
	public static final String ICON_IMAGE_DATABASE = "D";
	public static final String ICON_IMAGE_BUSY = "E";
	public static final String ICON_IMAGE_FIRE = "F";
	public static final String ICON_IMAGE_COG = "G";
	public static final String ICON_IMAGE_HEART_EMPTY = "H";
	public static final String ICON_IMAGE_HOME = "I";
	public static final String ICON_IMAGE_NEUTRAL = "J";
	public static final String ICON_IMAGE_HAPPY = "K";
	public static final String ICON_IMAGE_SAD = "L";
	public static final String ICON_IMAGE_CUP = "M";
	public static final String ICON_IMAGE_METER = "N";
	public static final String ICON_IMAGE_KEY = "O";
	public static final String ICON_IMAGE_PRINTER = "P";
	public static final String ICON_IMAGE_SWITCH = "Q";
	public static final String ICON_IMAGE_CUBE = "R";
	public static final String ICON_IMAGE_PRODUCT = "S";
	public static final String ICON_IMAGE_TRASH = "T";
	public static final String ICON_IMAGE_USERS = "U";
	public static final String ICON_IMAGE_POWER = "V";
	public static final String ICON_IMAGE_SHIELD = "W";
	public static final String ICON_IMAGE_EERROR = "X";
	public static final String ICON_IMAGE_GRID = "Y";
	public static final String ICON_IMAGE_WRENCH = "Z";
	public static final String ICON_IMAGE_UPLOAD_2 = "[";
	public static final String ICON_IMAGE_CLOUD = "\\";
	public static final String ICON_IMAGE_DOWNLOAD_2 = "]";
	public static final String ICON_IMAGE_MOVE = "^";
	public static final String ICON_IMAGE_LINK = "_";
	public static final String ICON_IMAGE_SUNNY = "`";
	public static final String ICON_IMAGE_EYE = "a";
	public static final String ICON_IMAGE_BOOKMARK = "b";
	public static final String ICON_IMAGE_CHECKMARK_2 = "c";
	public static final String ICON_IMAGE_STAR_EMPTY = "d";
	public static final String ICON_IMAGE_STAR_HALF = "e";
	public static final String ICON_IMAGE_STAR = "f";
	public static final String ICON_IMAGE_COG2 = "g";
	public static final String ICON_IMAGE_Heart = "h";
	public static final String ICON_IMAGE_Info = "i";
	public static final String ICON_IMAGE_Stats = "j";
	public static final String ICON_IMAGE_PIECHART = "k";
	public static final String ICON_IMAGE_LOCK = "l";
	public static final String ICON_IMAGE_REMOVE = "m";
	public static final String ICON_IMAGE_SUPPORT = "n";
	public static final String ICON_IMAGE_TARGET = "o";
	public static final String ICON_IMAGE_ADD = "p";
	public static final String ICON_IMAGE_THUMBS_UP = "q";
	public static final String ICON_IMAGE_THUMBS_DOWN = "r";
	public static final String ICON_IMAGE_MAGNIFIER = "s";
	public static final String ICON_IMAGE_CLOCK = "t";
	public static final String ICON_IMAGE_USER = "u";
	public static final String ICON_IMAGE_CLIPBOARD = "v";
	public static final String ICON_IMAGE_BOOK = "w";
	public static final String ICON_IMAGE_CLOSE = "x";
	public static final String ICON_IMAGE_MAP = "y";
	public static final String ICON_IMAGE_TOOLS = "z";
	public static final String ICON_IMAGE_TRI_LEF = "{";
	public static final String ICON_IMAGE_TRI_DOWN = "|";
	public static final String ICON_IMAGE_TRI_RIGHT = "}";
	public static final String ICON_IMAGE_TRI_UP = "~";
	public static final String ICON_IMAGE_PLAY = "&nbsp;";
   
   public static String render(String image, String size, String color)
   {
      if (image.equals(""))
      {
         return "";
      }
      else
      {
         return "<span class=\"icon " + size + " " + color + "\" data-icon=\"" + image + "\"></span> ";
      }
   }
   
   public static String render(String image, String size)
   {
      if (image.equals(""))
      {
         return "";
      }
      else
      {
         return "<span class=\"icon " + size + "\" data-icon=\"" + image + "\"></span> ";
      }
   }
}
