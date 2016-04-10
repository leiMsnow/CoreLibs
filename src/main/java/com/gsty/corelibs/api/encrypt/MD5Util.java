package com.gsty.corelibs.api.encrypt;

import java.security.MessageDigest;

public class MD5Util {

     public final static String MD5(String str) {
    	 try {
 			MessageDigest md = MessageDigest.getInstance("MD5");
 			md.update(str.getBytes("UTF-8"));
 			byte b[] = md.digest();

 			int i;

 			StringBuilder sb = new StringBuilder();
 			for (int offset = 0; offset < b.length; offset++) {
 				i = b[offset];
 				if (i < 0)
 					i += 256;
 				if (i < 16)
 					sb.append("0");
 				sb.append(Integer.toHexString(i).toUpperCase());
 			}
 			str = sb.toString();
 		} catch (Exception e) {
 			e.printStackTrace();
 		}
 		return str;
     }
//     // only for test
//     public static void main(String[] args) {
//         System.out.println(MD5Util.MD5("phoneNo=18513504759&timestamp="+System.currentTimeMillis()).concat("3325"));
//         System.out.println();
//     }
}
