package com.gisnet.erpp.util;


import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
 
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 
public class EncryptAESUtil {
	private static final Logger log = LoggerFactory.getLogger(EncryptAESUtil.class);
	private static String KEY="nndfdaqq34adañmnasdfa·$%&fwerawqñlasdfa,sdmfa231231!·$%/$";
    private static SecretKeySpec secretKey;
    private static byte[] key;
    
   /* public static void main(String[] args){
    	System.out.println("Encriptanndo pruebaRPPC04=" + encrypt("pruebaRPPC04"));
    }*/
 
    public static void setKey(String myKey) 
    {
    	if (secretKey==null){
	        MessageDigest sha = null;
	        try {
	            key = myKey.getBytes("UTF-8");
	            sha = MessageDigest.getInstance("SHA-1");
	            key = sha.digest(key);
	            key = Arrays.copyOf(key, 16); 
	            secretKey = new SecretKeySpec(key, "AES");
	        } 
	        catch (NoSuchAlgorithmException e) {
	            e.printStackTrace();
	        } 
	        catch (UnsupportedEncodingException e) {
	            e.printStackTrace();
	        }
    	}
    }
    
    public static String encrypt(String strToEncrypt){
    	return encrypt(strToEncrypt, KEY);
    }
    
    public static String decrypt(String strToDecrypt){
    	return decrypt(strToDecrypt, KEY);
    }    
 
    public static String encrypt(String strToEncrypt, String secret) 
    {
        try
        {
            setKey(secret);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
        } 
        catch (Exception e) 
        {
            log.error("Error while encrypting: " + e.toString());
        }
        return null;
    }
 
    public static String decrypt(String strToDecrypt, String secret) 
    {
        try
        {
            setKey(secret);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
        } 
        catch (Exception e) 
        {
        	log.error("Error while decrypting: " + e.toString());
        }
        return null;
    }
    
    
}
