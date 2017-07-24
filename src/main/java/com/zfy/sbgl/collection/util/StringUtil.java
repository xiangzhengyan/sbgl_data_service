package com.zfy.sbgl.collection.util;

/**
 * @author xiangzy
 * @date 2015-9-12
 *
 */
public class StringUtil {
	public static String toXor(String str) {
//		byte[] bytes = str.getBytes();
//
//		byte b = bytes[1];
//		for (int i = 2; i < bytes.length; i++) {
//			 b ^= bytes[i];
//		}
//	
//		String hex =  Integer.toHexString((int)b);
//		if(hex.length()==1){
//			hex = "0"+hex;
//		}
//		return hex;
		return "x";
	}
	
	public static void main(String[] args) {
		System.out.println(toXor("login,x-2015,pass"));
		System.out.println( Integer.toHexString(0));
	}
	

}
