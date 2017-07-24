package com.zfy.sbgl.collection.util;

import java.util.Random;

/**
 * @author xiangzy
 * @date 2015-9-7
 *
 */
public class IdUtil {
	private static Random r = new Random(System.currentTimeMillis());
	
	public static Long getId(){
		return Math.abs(r.nextLong()%1000000000000L);
	}
}
