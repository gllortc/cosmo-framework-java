package com.cosmo.util;

import java.io.IOException;
import java.security.GeneralSecurityException;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.xml.bind.DatatypeConverter;

/**
 * Clase helper que implementa utilidades de criptografia.
 */
public class CryptoUtils 
{
   private static final String PASSWORD = "enfldsgbnlsngdlksdsgm";
   private static final byte[] SALT = { (byte) 0xde, (byte) 0x33, (byte) 0x10, (byte) 0x12,
                                        (byte) 0xde, (byte) 0x33, (byte) 0x10, (byte) 0x12 };

   private static final String SECRET_KEY_FACTORY = "PBEWithMD5AndDES";
   
   //==============================================
   // Static members
   //==============================================
   
   /**
    * Encripta una cadena de texto.
    * Como clave secreta se usará una clave interna.
    * 
    * @param text Cadena de texto a encriptar.
    * @return Cadena de texto encriptada.
    * 
    * @throws GeneralSecurityException
    */
   public static String encrypt(String text) throws GeneralSecurityException 
   {
      return encrypt(text, CryptoUtils.PASSWORD);
   }
   
   /**
    * Encripta una cadena de texto.
    * 
    * @param text Cadena de texto a encriptar.
    * @param secretKey Clave secreta que se usará para encriptar.
    * @return Cadena de texto encriptada.
    * 
    * @throws GeneralSecurityException
    */
   public static String encrypt(String text, String secretKey) throws GeneralSecurityException 
   {
      SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(CryptoUtils.SECRET_KEY_FACTORY);
      SecretKey key = keyFactory.generateSecret(new PBEKeySpec(secretKey.toCharArray()));
      Cipher pbeCipher = Cipher.getInstance(CryptoUtils.SECRET_KEY_FACTORY);
      pbeCipher.init(Cipher.ENCRYPT_MODE, key, new PBEParameterSpec(SALT, 20));
      
      // return base64Encode(pbeCipher.doFinal(text.getBytes()));
      return DatatypeConverter.printBase64Binary(pbeCipher.doFinal(text.getBytes()));
   }

   /**
    * Desencripta una cadena de texto.
    * Como clave secreta se usará una clave interna.
    * 
    * @param text Cadena de texto encriptada.
    * @return Cadena de texto desencriptada.
    * 
    * @throws GeneralSecurityException
    * @throws IOException
    */
   public static String decrypt(String text) throws GeneralSecurityException, IOException 
   {
      return decrypt(text, CryptoUtils.PASSWORD);
   }
   
   /**
    * Desencripta una cadena de texto.
    * 
    * @param text Cadena de texto encriptada.
    * @param secretKey Clave secreta usada para la encriptación.
    * @return Cadena de texto desencriptada.
    * 
    * @throws GeneralSecurityException
    * @throws IOException
    */
   public static String decrypt(String text, String secretKey) throws GeneralSecurityException, IOException 
   {
      SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(CryptoUtils.SECRET_KEY_FACTORY);
      SecretKey key = keyFactory.generateSecret(new PBEKeySpec(secretKey.toCharArray()));
      Cipher pbeCipher = Cipher.getInstance(CryptoUtils.SECRET_KEY_FACTORY);
      pbeCipher.init(Cipher.DECRYPT_MODE, key, new PBEParameterSpec(SALT, 20));
      
      // return new String(pbeCipher.doFinal(base64Decode(text)));
      return new String(pbeCipher.doFinal(DatatypeConverter.parseBase64Binary(text)));
   }
   
   //==============================================
   // Private members
   //==============================================
   
   /*private static String base64Encode(byte[] bytes) 
   {
      return new BASE64Encoder().encode(bytes);
   }
   
   private static byte[] base64Decode(String text) throws IOException 
   {
      return new BASE64Decoder().decodeBuffer(text);
   }*/
}
