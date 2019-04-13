/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modules;

import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class SecurityAES128CBC {

private IvParameterSpec ivspec;
private SecretKeySpec keyspec;
private Cipher cipher;

    public SecurityAES128CBC(String key, String iv)
    {
            ivspec = new IvParameterSpec(iv.getBytes());

            keyspec = new SecretKeySpec(key.getBytes(), "AES");

            try {
                    cipher = Cipher.getInstance("AES/CBC/NoPadding");
            } catch (NoSuchAlgorithmException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
            } catch (NoSuchPaddingException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
            }
    }
    
    public String encrypt(String text) throws Exception{
        return this.bytesToHex( this.byteencrypt(text));
    }
    
    public byte[] byteencrypt(String text) throws Exception
    {
            if(text == null || text.length() == 0)
                    throw new Exception("Empty string");

            byte[] encrypted = null;

            try {
                    cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);

                    encrypted = cipher.doFinal(padString(text).getBytes());
            } catch (Exception e)
            {			
                    throw new Exception("[encrypt] " + e.getMessage());
            }

            return encrypted;
    }

    public String decrypt(String text) throws Exception{
        return new String(this.bytedecrypt(text));
    }
    
    public byte[] bytedecrypt(String code) throws Exception
    {
            if(code == null || code.length() == 0)
                    throw new Exception("Empty string");

            byte[] decrypted = null;

            try {
                    cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);

                    decrypted = cipher.doFinal(hexToBytes(code));
            } catch (Exception e)
            {
                    throw new Exception("[decrypt] " + e.getMessage());
            }
            return decrypted;
    }



    public static String bytesToHex(byte[] data)
    {
            if (data==null)
            {
                    return null;
            }

            int len = data.length;
            String str = "";
            for (int i=0; i<len; i++) {
                    if ((data[i]&0xFF)<16)
                            str = str + "0" + java.lang.Integer.toHexString(data[i]&0xFF);
                    else
                            str = str + java.lang.Integer.toHexString(data[i]&0xFF);
            }
            return str;
    }


    public static byte[] hexToBytes(String str) {
            if (str==null) {
                    return null;
            } else if (str.length() < 2) {
                    return null;
            } else {
                    int len = str.length() / 2;
                    byte[] buffer = new byte[len];
                    for (int i=0; i<len; i++) {
                            buffer[i] = (byte) Integer.parseInt(str.substring(i*2,i*2+2),16);
                    }
                    return buffer;
            }
    }



    private static String padString(String source)
    {
      char paddingChar = 0;
      int size = 16;
      int x = source.length() % size;
      int padLength = size - x;

      for (int i = 0; i < padLength; i++)
      {
              source += paddingChar;
      }

      return source;
    }
  }
