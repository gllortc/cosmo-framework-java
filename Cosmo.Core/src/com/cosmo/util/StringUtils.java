package com.cosmo.util;

/**
 * Utilidades para cadenas de texto (String).
 * 
 * @author Gerard Llort
 */
public class StringUtils 
{
   /**
	 * Permet esbrinar si una cadena está vacía o nula. 
	 * 
	 * @param text Cadena de carácteres a evaluar.
	 * 
	 * @return {@code true} si la cadena está vacía o nula o {@code false} en qualsevol otro caso.
	 */
	public static boolean isNullOrEmpty(String text)
	{
		if (text == null) 
      {
         return true;
      }
		
		return (text.isEmpty());
	}
	
	/**
	 * Indica si una cadena está vacía o es nula. 
	 * 
	 * @param text Cadena de carácteres a evaluar.
	 * 
	 * @return {@code true} si la cadena está vacía o es nula o {@code false} en cualquier otro caso.
	 */
	public static boolean isNullOrEmptyTrim(String text)
	{
		if (text == null) 
      {
         return true;
      }
		
		text = text.trim();
		
		return (text.isEmpty());
	}
	
	/**
	 * Determina si una cadena contiene sólo números.
	 * 
	 * @param str Una cadena de texto a comprobar.
	 * 
	 * @return {@code true} si la cadena sólo contiene números o {@code false} en cualquier otro caso.
	 */
	public static boolean isNumeric(String str)
	{
	    for (char c : str.toCharArray())
	    {
	        if (!Character.isDigit(c)) return false;
	    }

	    return true;
	}
}
