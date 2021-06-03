/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;


import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import org.mindrot.jbcrypt.BCrypt;

/**
 *
 * @author USER
 */
public class Hash {

    /* Retorna un hash a partir de un tipo y un texto */
    private static String getHash(String txt, String hashType) {
        try {

            java.security.MessageDigest md = java.security.MessageDigest.getInstance(hashType);
            byte[] array = md.digest(txt.getBytes("UTF-8"));
            return Base64.getEncoder().encodeToString(array);
            /*  StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
            }
            return sb.toString();*/

            // juan -MD5-> s4as4as4as5
            /// juan -> MD5-> hassh as juanhash == BDHASH   <-BDhash
        } catch (java.security.NoSuchAlgorithmException e) {
            System.out.println(e.getMessage());
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Hash.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    // return 
    

    /* Retorna un hash MD5 a partir de un texto */
    public static String md5(String txt) {
        return Hash.getHash(txt, "MD5");
    }

    /* Retorna un hash SHA1 a partir de un texto */
    public static String sha1(String txt) {
        return Hash.getHash(txt, "SHA1");
    }

    public static String encript(String password) {
        String  paswordEncript = BCrypt.hashpw(password, BCrypt.gensalt());
        return paswordEncript;  
    }
    
    public static boolean compareHash(String password, String hashPassword) {
        return  BCrypt.checkpw(password, hashPassword);
    }
    
   
 

}
