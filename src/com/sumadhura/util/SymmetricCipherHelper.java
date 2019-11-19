package com.sumadhura.util;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class SymmetricCipherHelper
{
  private static final String mHexKey = "29304E8758327892";

  private static int getKeyLength(String cipherName)
    throws Exception
  {
    if (cipherName.equalsIgnoreCase("DES"))
      return 64;
    if (cipherName.equalsIgnoreCase("AES"))
      return 128;
    if (cipherName.equalsIgnoreCase("DESede")) {
      return 192;
    }

    throw new Exception(
      "SymmetricCipher.getKeyLength() error - Symmetric cipher algorithm name " + 
      cipherName + 
      " is not recognized. Thus can not determine key length.");
  }

  private static byte[] getKey(String randomId, String cipherName)
    throws Exception
  {
    int keyBytes = getKeyLength(cipherName) / 8;
    int len = randomId.length();

    String key = randomId;
    if (len < keyBytes) {
      int repeatFactor = keyBytes / len;
      for (int i = 0; i < repeatFactor; i++) {
        key = key + randomId;
      }
    }

    return key.substring(0, keyBytes).getBytes();
  }

  public static String decrypt(String inputStr, String randomId, String symCipherAlgo)
    throws Exception
  {
    byte[] Message = Base64EncoderDecoder.decode(inputStr);
    byte[] resultByt = (byte[])null;
    try {
      byte[] randomByt = getKey(randomId, symCipherAlgo);
      SecretKeySpec skeySpec = new SecretKeySpec(randomByt, symCipherAlgo);
      Cipher cipher = Cipher.getInstance(symCipherAlgo);
      cipher.init(2, skeySpec);
      resultByt = cipher.doFinal(Message);
    }
    catch (NoSuchAlgorithmException e) {
      throw new Exception("Invalid symmetric cipher algorithm Name");
    }
    catch (InvalidKeyException e) {
      throw new Exception(
        "Error in hex key");
    }
    catch (IllegalBlockSizeException e) {
      throw new Exception(
        "Error while decrypting the string");
    }
    catch (NoSuchPaddingException e) {
      throw new Exception(
        "Error while decrypting the string");
    }
    catch (BadPaddingException e) {
      throw new Exception(
        "Error while decrypting the string");
    }

    return new String(resultByt);
  }

  public static String encrypt(String inputStr, String randomId, String symCipherAlgo)
    throws Exception
  {
    byte[] Message = inputStr.getBytes();
    byte[] resultByt = (byte[])null;
    String B64EncryptedStr = null;
    try {
      byte[] randomByt = getKey(randomId, symCipherAlgo);
      SecretKeySpec skeySpec = new SecretKeySpec(randomByt, symCipherAlgo);
      Cipher cipher = Cipher.getInstance(symCipherAlgo);
      cipher.init(1, skeySpec);
      resultByt = cipher.doFinal(Message);
      B64EncryptedStr = Base64EncoderDecoder.encode(resultByt);
    }
    catch (NoSuchAlgorithmException e) {
      throw new Exception(
        "Invalid symmetric cipher algorithm Name");
    }
    catch (InvalidKeyException e) {
      throw new Exception(
        "Error in hex key");
    }
    catch (IllegalBlockSizeException e) {
      throw new Exception(
        "Error while encrypting the string");
    }
    catch (NoSuchPaddingException e) {
      throw new Exception(
        "Error while encrypting the string");
    }
    catch (BadPaddingException e) {
      throw new Exception(
        "Error while encrypting the string");
    }

    return B64EncryptedStr;
  }

  public static String decrypt(String inputStr, String symCipherAlgo)
    throws Exception
  {
    byte[] Message = Base64EncoderDecoder.decode(inputStr);

    byte[] randomByt = getKey("29304E8758327892", symCipherAlgo);
    byte[] resultByt = (byte[])null;
    try
    {
      SecretKeySpec skeySpec = new SecretKeySpec(randomByt, symCipherAlgo);
      Cipher cipher = Cipher.getInstance(symCipherAlgo);
      cipher.init(2, skeySpec);
      resultByt = cipher.doFinal(Message);
    }
    catch (NoSuchAlgorithmException e) {
      throw new Exception(
        "Invalid symmetric cipher algorithm Name");
    }
    catch (InvalidKeyException e) {
      throw new Exception(
        "Error in hex key");
    }
    catch (IllegalBlockSizeException e) {
      throw new Exception(
        "Error while decrypting the string");
    }
    catch (NoSuchPaddingException e) {
      throw new Exception(
        "Error while decrypting the string");
    }
    catch (BadPaddingException e) {
      throw new Exception(
        "Error while decrypting the string");
    }

    return new String(resultByt);
  }

  public static String encrypt(String inputStr, String symCipherAlgo)
    throws Exception
  {
    byte[] Message = inputStr.getBytes();

    byte[] randomByt = getKey("29304E8758327892", symCipherAlgo);
    byte[] resultByt = (byte[])null;
    String B64EncryptedStr = null;
    try
    {
      SecretKeySpec skeySpec = new SecretKeySpec(randomByt, symCipherAlgo);
      Cipher cipher = Cipher.getInstance(symCipherAlgo);
      cipher.init(1, skeySpec);
      resultByt = cipher.doFinal(Message);
      B64EncryptedStr = Base64EncoderDecoder.encode(resultByt);
    }
    catch (NoSuchAlgorithmException e)
    {
      throw new Exception(
        "Invalid symmetric cipher algorithm Name");
    }
    catch (InvalidKeyException e) {
      throw new Exception(
        "Error in hex key");
    }
    catch (IllegalBlockSizeException e) {
      throw new Exception(
        "Error while encrypting the string");
    }
    catch (NoSuchPaddingException e) {
      throw new Exception(
        "Error while encrypting the string");
    }
    catch (BadPaddingException e) {
      throw new Exception(
        "Error while encrypting the string");
    }

    return B64EncryptedStr;
  }
}