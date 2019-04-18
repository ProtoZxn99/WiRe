/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stts.coba;

import android.util.Base64;

import java.nio.charset.StandardCharsets;
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
            ivspec = new IvParameterSpec(iv.getBytes(StandardCharsets.UTF_8));

            keyspec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");

            try {
                    cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            } catch (NoSuchAlgorithmException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
            } catch (NoSuchPaddingException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
            }
    }
    
    public String encrypt(String text) throws Exception{
        return Base64.encodeToString(this.byteencrypt(text),Base64.DEFAULT);
    }
    
    public byte[] byteencrypt(String text) throws Exception
    {
            if(text == null || text.length() == 0)
                    throw new Exception("Empty string");

            byte[] encrypted = null;

            try {
                    cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);

                encrypted = cipher.doFinal(text.getBytes(StandardCharsets.UTF_8));
                //encrypted = cipher.doFinal(padString(text).getBytes(StandardCharsets.UTF_8));
            } catch (Exception e)
            {
                throw new Exception("[encrypt] " + e.getMessage());
            }

            return encrypted;
    }

    public String decrypt(String text) throws Exception{
        return new String(this.bytedecrypt(Base64.decode(text, Base64.DEFAULT)));
        //return new String(this.bytedecrypt(Base64.decode(text.getBytes(StandardCharsets.UTF_8), Base64.DEFAULT)),StandardCharsets.UTF_8);
    }
    
    public byte[] bytedecrypt(byte [] code) throws Exception
    {
            if(code == null)
                    throw new Exception("Empty string");

            byte[] decrypted = null;

            try {
                    cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);

                    decrypted = cipher.doFinal(code);

                    //decrypted = trim(decrypted);

            } catch (Exception e)
            {
                    throw new Exception("[decrypt] " + e.getMessage());
            }
            return decrypted;
    }

//    public byte[] trim(byte [] bytes){
//        if( bytes.length > 0)
//        {
//            int trim = 0;
//            for( int i = bytes.length - 1; i >= 0; i-- ) if( bytes[i] == 0 ) trim++;
//
//            if( trim > 0 )
//            {
//                byte[] newArray = new byte[bytes.length - trim];
//                System.arraycopy(bytes, 0, newArray, 0, bytes.length - trim);
//                bytes = newArray;
//            }
//        }
//        return bytes;
//    }
//
//
//    private String padString(String source)
//    {
//      char paddingChar = '0';
//      int size = 16;
//      int x = source.length() % size;
//      int padLength = size - x;
//
//      for (int i = 0; i < padLength; i++)
//      {
//              source = paddingChar + source;
//      }
//
//      return source;
//    }
  }
