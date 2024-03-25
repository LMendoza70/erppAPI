package com.gisnet.erpp.util;

import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;

public class SHACheckSum
{
    public static String getDigestion(File file)throws Exception
    {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        FileInputStream fis = new FileInputStream(file);

        byte[] dataBytes = new byte[1024];

        int nread = 0;
        while ((nread = fis.read(dataBytes)) != -1) {
          md.update(dataBytes, 0, nread);
        };
        byte[] mdbytes = md.digest();

        //convert the byte to hex format method 1
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < mdbytes.length; i++) {
          sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
        }

        System.out.println("Hex format : " + sb.toString());

        return   sb.toString();
    }
    
    
    public static String getHashSHA256(String cadena) throws Exception{
    	
    	  	  MessageDigest md = MessageDigest.getInstance("SHA-256");
          md.update(cadena.getBytes());

          byte byteData[] = md.digest();

          //convert the byte to hex format method 1
          StringBuffer sb = new StringBuffer();
          for (int i = 0; i < byteData.length; i++) {
           sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
          }

          System.out.println("Hex format : " + sb.toString());

          return sb.toString();
    	
    }
    
}