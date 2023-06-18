package com.aboutdk.note.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Utils {

   public static String MD5(String md5) {
      try {
         MessageDigest md = MessageDigest.getInstance("MD5");
         byte[] array = md.digest(md5.getBytes());
         StringBuffer sb = new StringBuffer();
         for (int i = 0; i < array.length; ++i) {
            sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
         }
         return sb.toString();
      } catch (NoSuchAlgorithmException e) {
      }
      return null;
   }
}
