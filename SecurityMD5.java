package modules;


import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Patrick
 */

//Generate MD5 hash
public class SecurityMD5 {
    public String hash(String message) throws NoSuchAlgorithmException{
        MessageDigest m=MessageDigest.getInstance("MD5");
        m.update(message.getBytes(),0,message.length());
        return new BigInteger(1,m.digest()).toString(16);
    }
    public String hash(byte [] b) throws NoSuchAlgorithmException{
        MessageDigest m=MessageDigest.getInstance("MD5");
        m.update(b,0,b.length);
        return new BigInteger(1,m.digest()).toString(16);
    }
}
